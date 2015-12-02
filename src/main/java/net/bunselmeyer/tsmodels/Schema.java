package net.bunselmeyer.tsmodels;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class Schema {
    private final Map<String, SchemaProperty> properties = new LinkedHashMap<>();
    private final String name;

    public Schema(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Schema addProperty(String name, String type) {
        properties.put(name, new SchemaProperty(name, type));
        return this;
    }

    public SchemaProperty getProperty(String name) {
        return properties.get(name);
    }

    public List<SchemaProperty> getProperties() {
        return new ArrayList<>(properties.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schema schema = (Schema) o;
        return new EqualsBuilder()
                .append(name, schema.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class SchemaProperty {

        private final String name;
        private final String type;

        public SchemaProperty(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SchemaProperty that = (SchemaProperty) o;
            return new EqualsBuilder()
                    .append(name, that.name)
                    .append(type, that.type)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(name)
                    .append(type)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return name + ":" + type;
        }
    }
}

