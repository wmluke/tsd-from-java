package net.bunselmeyer.tsmodels;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

public class Renderer {

    private final List<Schema> schemas;
    private final Writer writer;
    private final String moduleName;

    public Renderer(String moduleName, List<Schema> schemas, Writer writer) {
        this.moduleName = moduleName;
        this.schemas = schemas;
        this.writer = writer;
    }

    public void render() throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/net/bunselmeyer/tsmodels/templates");

        Handlebars handlebars = new Handlebars(loader);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", moduleName);
        data.put("models", schemas);

        Template template = handlebars.compile("module");
        template.apply(data, writer);

        writer.close();
    }

}
