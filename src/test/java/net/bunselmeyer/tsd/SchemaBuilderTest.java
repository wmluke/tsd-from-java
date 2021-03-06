package net.bunselmeyer.tsd;

import net.bunselmeyer.tsd.models.Address;
import net.bunselmeyer.tsd.models.User;
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

        List<Schema> schemas = schemaBuilder.build();

        assertEquals(2, schemas.size());

        Schema addressSchema = schemas.get(0);
        assertEquals("IAddress", addressSchema.getName());
        assertEquals(5, addressSchema.getProperties().size());
        assertEquals("string", addressSchema.getProperty("streetAddress").getType());
        assertEquals("string", addressSchema.getProperty("city").getType());
        assertEquals("string", addressSchema.getProperty("state").getType());
        assertEquals("string", addressSchema.getProperty("zipCode").getType());
        assertEquals("string", addressSchema.getProperty("country").getType());


        Schema userSchema = schemas.get(1);
        assertEquals("IUser", userSchema.getName());
        assertEquals(9, userSchema.getProperties().size());
        assertEquals("string", userSchema.getProperty("firstName").getType());
        assertEquals("string", userSchema.getProperty("lastName").getType());
        assertEquals("IAddress", userSchema.getProperty("homeAddress").getType());
        assertEquals("IAddress", userSchema.getProperty("workAddress").getType());
        assertEquals("string", userSchema.getProperty("dateOfBirth").getType());
        assertEquals("number", userSchema.getProperty("favoriteNumber").getType());
        assertEquals("{[key: string]: number}", userSchema.getProperty("lookupFoo").getType());
        assertEquals("{[key: string]: string}", userSchema.getProperty("lookupBar").getType());
        assertEquals("IAddress[]", userSchema.getProperty("additionalAddresses").getType());

    }
}
