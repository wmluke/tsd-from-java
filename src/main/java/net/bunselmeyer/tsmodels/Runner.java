package net.bunselmeyer.tsmodels;

import com.google.common.collect.Lists;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class Runner {

    public static void main(String[] args) throws IOException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("tsmodels")
                .defaultHelp(true)
                .description("Generate Typescript type definitions from Java POJOS");


        parser.addArgument("-p", "--package")
                .required(true)
                .help("Java package to scan");

        parser.addArgument("-m", "--module")
                .required(true)
                .help("Typescript module");

        parser.addArgument("-o", "--output")
                .required(true)
                .help("Location of output file");

        parser.addArgument("jar")
                .nargs(1)
                .type(String.class)
                .required(true)
                .help("jar to scan");


        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        String module = ns.getString("module");
        String packageName = ns.getString("package");
        String outputFile = ns.getString("output");
        String jarFile = ns.<String>getList("jar").get(0);

        File file = new File(jarFile.trim());
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + jarFile);
        }

        ClassPathHack.addURL(file.toURI().toURL());

        Set<Class<?>> classes = new Scanner(Lists.newArrayList(packageName)).scan();

        List<Schema> schemas = new SchemaBuilder(classes).build();

        Renderer renderer = new Renderer(module, schemas, new PrintWriter(outputFile, "UTF-8"));
        renderer.render();


    }


}
