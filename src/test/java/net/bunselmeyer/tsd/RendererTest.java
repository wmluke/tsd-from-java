package net.bunselmeyer.tsd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RendererTest {

    @Test
    public void testRender() throws Exception {
        Schema fooSchema = new Schema("Foo");
        fooSchema.addProperty("aaa", "string");
        fooSchema.addProperty("bbb", "number");

        Schema barSchema = new Schema("Bar");
        barSchema.addProperty("ccc", "boolean");
        barSchema.addProperty("ddd", "Foo");

        List<Schema> schemas = new ArrayList<>();
        schemas.add(fooSchema);
        schemas.add(barSchema);

        StringWriter stringWriter = new StringWriter();

        Renderer renderer = new Renderer("App.Model", schemas, stringWriter);

        renderer.render();

        String output = stringWriter.toString();

        String expected = IOUtils.toString(getClass().getResourceAsStream("RendererTest-output.d.ts.txt"));


        assertEquals(expected, output);

    }
}