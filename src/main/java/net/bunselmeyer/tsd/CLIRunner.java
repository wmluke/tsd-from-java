package net.bunselmeyer.tsd;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class CLIRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(CLIRunner.class);

    public static void main(String[] args) throws IOException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("tsd-from-java")
                .defaultHelp(true)
                .description("Generate Typescript type definitions from Java");


        parser.addArgument("-p", "--packages")
                .required(true)
                .help("Comma separated list of java packages");

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
        String packages = ns.getString("packages");
        String outputFile = ns.getString("output");
        String jarFile = ns.<String>getList("jar").get(0);

        File file = new File(jarFile.trim());
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + jarFile);
        }

        ClassPathHack.addURL(file.toURI().toURL());

        List<String> packageList = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(packages));

        generate(module, outputFile, packageList);


    }

    private static void generate(String moduleName, String outputFile, List<String> packages) throws IOException {
        Set<Class<?>> classes = new Scanner(packages).scan();

        LOGGER.info("Found " + classes.size() + " classes");

        List<Schema> schemas = new SchemaBuilder(classes).build();

        LOGGER.info("Generated " + schemas.size() + " schemas");

        new Renderer(moduleName, schemas, new PrintWriter(outputFile, "UTF-8")).render();

        LOGGER.info("Saved typescript definitions to `" + outputFile + "`");
    }


}
