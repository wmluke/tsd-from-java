package net.bunselmeyer.tsmodels;

import net.bunselmeyer.tsmodels.models.Model;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class ScannerTest {

    @Test
    public void testScan() throws Exception {

        ArrayList<String> packages = new ArrayList<>();
        packages.add(Model.class.getPackage().getName());

        Scanner scanner = new Scanner(packages);

        Set<Class<?>> classes = scanner.scan();

        assertEquals(3, classes.size());

    }
}