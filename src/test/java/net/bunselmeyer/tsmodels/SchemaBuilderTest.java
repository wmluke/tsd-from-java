package net.bunselmeyer.tsmodels;

import net.bunselmeyer.tsmodels.models.Address;
import net.bunselmeyer.tsmodels.models.User;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


public class SchemaBuilderTest {

    @Test
    public void testBuild() throws Exception {

        LinkedHashSet<Class<?>> types = new LinkedHashSet<>();
        types.add(User.class);
        types.add(Address.class);

        SchemaBuilder schemaBuilder = new SchemaBuilder(types);

        List<Schema> Schemas = schemaBuilder.build();

        assertEquals(2, Schemas.size());

    }
}