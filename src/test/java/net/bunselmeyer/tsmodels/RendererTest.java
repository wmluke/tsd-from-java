package net.bunselmeyer.tsmodels;

import org.apache.commons.lang3.StringUtils;
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

        List<Schema> schemas = new ArrayList<>();
        schemas.add(fooSchema);

        StringWriter stringWriter = new StringWriter();

        Renderer renderer = new Renderer("App.Model", schemas, stringWriter);

        renderer.render();

        String output = stringWriter.toString();

        assertEquals(true, StringUtils.isNotBlank(output));

    }
}