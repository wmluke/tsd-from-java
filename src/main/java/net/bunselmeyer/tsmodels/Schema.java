package net.bunselmeyer.tsmodels;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Schema {
    private final List<SchemaProperty> properties = new ArrayList<>();
    private final String name;

    public Schema(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Schema addProperty(String name, String type) {
        properties.add(new SchemaProperty(name, type));
        return this;
    }

    public List<SchemaProperty> getProperties() {
        return properties;
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
    }
}

