package net.bunselmeyer.tsmodels;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import net.bunselmeyer.tsmodels.models.User;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class RunnerTest {

    @Test
    public void testApp() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);
        JsonSchema jsonSchema = generator.generateSchema(User.class);


        ObjectSchema objectSchema = jsonSchema.asObjectSchema();


        Map<String, JsonSchema> properties = objectSchema.getProperties();

        assertTrue(true);
    }
}
