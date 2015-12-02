package net.bunselmeyer.tsd;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Scanner {

    private final List<String> packages;

    public Scanner(List<String> packages) {
        this.packages = packages;
    }

    public Set<Class<?>> scan() {

        Set<URL> urls = new LinkedHashSet<>();

        FilterBuilder filterBuilder = new FilterBuilder();
        for (String packageName : packages) {
            urls.addAll(ClasspathHelper.forPackage(packageName));
            filterBuilder.includePackage(packageName);
        }

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(urls)
                        .setScanners(new SubTypesScanner(false))
                        .filterInputsBy(filterBuilder)
        );

        return reflections.getSubTypesOf(Object.class);
    }
}
