package net.bunselmeyer.tsd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(SchemaBuilder.class);

    public enum TSType {
        BOOLEAN("boolean"),
        NUMBER("number"),
        STRING("string"),
        ANY("any");

        private final String notation;

        TSType(String notation) {
            this.notation = notation;
        }

        public String getNotation() {
            return notation;
        }
    }

    private final Set<Class<?>> types;
    private final ObjectMapper objectMapper;

    public SchemaBuilder(Set<Class<?>> types) {
        this(types, configureObjectMapper(new ObjectMapper()));
    }

    public SchemaBuilder(Set<Class<?>> types, ObjectMapper objectMapper) {
        this.types = types;
        this.objectMapper = objectMapper;
    }

    public static ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JodaModule());
        // Allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // Write java.util.Date, Calendar as number (timestamp)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        // Prevent exception when encountering unknown property
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Coerce JSON empty String ("") to null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // Coerce unknown enum to null
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

        // Force escaping of non-ASCII characters
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        return objectMapper;
    }

    public List<Schema> build() {
        List<String> classNames = types.stream().map(Class::getName).collect(Collectors.toList());
        return types.stream()
                .map((type) -> build(type, classNames))
                .filter(Objects::nonNull)
                .distinct()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
    }

    private Schema build(Class<?> type, List<String> classNames) {
        JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper);
        JsonSchema jsonSchema = null;
        try {
            jsonSchema = generator.generateSchema(type);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
        ObjectSchema objectSchema = jsonSchema.asObjectSchema();
        if (objectSchema == null) {
            LOGGER.warn("Failed to generate schema for `{}`", type.getName());
            return null;
        }

        Map<String, JsonSchema> properties = objectSchema.getProperties();
        List<JsonSchema> jsonSchemas = properties.values().stream().filter((s) -> s.getId() != null).collect(Collectors.toList());
        ImmutableMap<String, JsonSchema> jsonSchemaIndex = Maps.uniqueIndex(jsonSchemas, JsonSchema::getId);

        return properties.keySet().stream()
                .sorted()
                .reduce(new Schema(type.getSimpleName()), (schema, name) -> {
                    JsonSchema propertySchema = properties.get(name);
                    String typeLabel = buildTypeNotation(propertySchema, jsonSchemaIndex, classNames);

                    if ("any".equals(typeLabel)) {
                        LOGGER.warn("Cannot resolve type for `{}#{}`", type.getName(), name);
                    }

                    schema.addProperty(name, typeLabel);
                    return schema;
                }, (schema, __) -> schema);
    }

    private String buildTypeNotation(JsonSchema propertySchema, ImmutableMap<String, JsonSchema> jsonSchemaIndex, List<String> classNames) {
        String $ref = propertySchema.get$ref();
        if (StringUtils.isNotBlank($ref)) {
            String classPath = convertIdToClassPath($ref);
            if (jsonSchemaIndex.containsKey($ref)) {
                propertySchema = jsonSchemaIndex.get($ref);
            } else if (classNames.contains(classPath)) {
                return getSimpleName(classPath);
            }
        }
        if (propertySchema.isStringSchema()) {
            return TSType.STRING.getNotation();
        } else if (propertySchema.isBooleanSchema()) {
            return TSType.BOOLEAN.getNotation();
        } else if (propertySchema.isNumberSchema()) {
            return TSType.NUMBER.getNotation();
        } else if (propertySchema.isObjectSchema()) {
            return buildObjectTypeNotation(propertySchema.asObjectSchema(), jsonSchemaIndex, classNames);
        } else if (propertySchema.isArraySchema()) {
            return buildArrayTypeNotation(propertySchema.asArraySchema(), jsonSchemaIndex, classNames);
        }

        return TSType.ANY.getNotation();
    }

    private String buildArrayTypeNotation(ArraySchema propertySchema, ImmutableMap<String, JsonSchema> jsonSchemaIndex, List<String> classNames) {
        ArraySchema.Items items = propertySchema.getItems();
        if (items.isSingleItems()) {
            return buildTypeNotation(items.asSingleItems().getSchema(), jsonSchemaIndex, classNames) + "[]";
        } else {
            return TSType.ANY.getNotation() + "[]";
        }
    }

    private String buildObjectTypeNotation(ObjectSchema propertySchema, ImmutableMap<String, JsonSchema> jsonSchemaIndex, List<String> classNames) {
        // urn:jsonschema:net:bunselmeyer:tsd:models:Address
        String id = propertySchema.getId();
        if (StringUtils.isBlank(id)) {
            return buildMapTypeNotation(propertySchema, jsonSchemaIndex, classNames);
        }
        String path = convertIdToClassPath(id);
        if (classNames.contains(path)) {
            return getSimpleName(path);
        }
        return TSType.ANY.getNotation();
    }

    private String buildMapTypeNotation(ObjectSchema propertySchema, ImmutableMap<String, JsonSchema> jsonSchemaIndex, List<String> classNames) {
        ObjectSchema.AdditionalProperties additionalProperties = propertySchema.getAdditionalProperties();
        if (!(additionalProperties instanceof ObjectSchema.SchemaAdditionalProperties)) {
            return "{[key:string]:any}";
        }
        JsonSchema jsonSchema = ((ObjectSchema.SchemaAdditionalProperties) additionalProperties).getJsonSchema();
        String tsType = buildTypeNotation(jsonSchema, jsonSchemaIndex, classNames);
        // JSON any supports strings as keys
        return "{[key:string]:" + tsType + "}";
    }

    private static String getSimpleName(String classPath) {
        int i = StringUtils.lastIndexOf(classPath, ".");
        return StringUtils.right(classPath, classPath.length() - i - 1);
    }

    private static String convertIdToClassPath(String id) {
        return id.replace("urn:jsonschema:", "").replace(":", ".");
    }

}
