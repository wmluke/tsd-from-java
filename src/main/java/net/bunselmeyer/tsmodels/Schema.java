package net.bunselmeyer.tsmodels;

import java.util.ArrayList;
import java.util.List;

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

