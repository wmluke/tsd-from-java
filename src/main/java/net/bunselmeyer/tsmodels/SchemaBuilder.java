package net.bunselmeyer.tsmodels;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaBuilder {

    public enum TSType {
        BOOLEAN("boolean"),
        NUMBER("number"),
        STRING("string"),
        ANY("any");

        private final String typeLabel;

        TSType(String typeLabel) {
            this.typeLabel = typeLabel;
        }

        public String getTypeLabel() {
            return typeLabel;
        }
    }

    private final Set<Class<?>> types;
    private final ObjectMapper mapper = new ObjectMapper();

    public SchemaBuilder(Set<Class<?>> types) {
        this.types = types;
        mapper.registerModule(new JodaModule());
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
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);
        JsonSchema jsonSchema = null;
        try {
            jsonSchema = generator.generateSchema(type);
        } catch (JsonMappingException e) {
            return null;
        }
        ObjectSchema objectSchema = jsonSchema.asObjectSchema();
        if (objectSchema == null) {
            return null;
        }
        Schema schema = new Schema(type.getSimpleName());
        Map<String, JsonSchema> properties = objectSchema.getProperties();
        for (Map.Entry<String, JsonSchema> entry : properties.entrySet()) {
            JsonSchema propertySchema = entry.getValue();

            String typeLabel = getTSType(propertySchema, classNames);

            schema.addProperty(entry.getKey(), typeLabel);

        }

        return schema;

    }

    private String getTSType(JsonSchema propertySchema, List<String> classNames) {

        if (propertySchema.isStringSchema()) {
            return TSType.STRING.getTypeLabel();
        } else if (propertySchema.isBooleanSchema()) {
            return TSType.BOOLEAN.getTypeLabel();
        } else if (propertySchema.isNumberSchema()) {
            return TSType.NUMBER.getTypeLabel();
        } else if (propertySchema.isObjectSchema()) {
            return getObjectType(propertySchema, classNames);
        } else if (propertySchema.isArraySchema()) {
            ArraySchema.Items items = propertySchema.asArraySchema().getItems();
            if (items.isSingleItems()) {
                return getTSType(items.asSingleItems().getSchema(), classNames) + "[]";
            } else {
                return TSType.ANY.getTypeLabel() + "[]";
            }
        }

        return TSType.ANY.getTypeLabel();
    }

    private String getObjectType(JsonSchema propertySchema, List<String> classNames) {
        // urn:jsonschema:net:bunselmeyer:tsmodels:models:Address
        String id = propertySchema.getId();
        if (StringUtils.isBlank(id)) {
            return TSType.ANY.getTypeLabel();
        }
        String path = id.replace("urn:jsonschema:", "").replace(":", ".");
        if (classNames.contains(path)) {
            int i = StringUtils.lastIndexOf(path, ".");
            return StringUtils.right(path, path.length() - i - 1);
        } else {
            return TSType.ANY.getTypeLabel();
        }
    }

}
