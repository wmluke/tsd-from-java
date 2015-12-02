package net.bunselmeyer.tsd;

import net.bunselmeyer.tsd.models.Address;
import net.bunselmeyer.tsd.models.Model;
import net.bunselmeyer.tsd.models.User;
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
        assertEquals(true, classes.contains(Model.class));
        assertEquals(true, classes.contains(Address.class));
        assertEquals(true, classes.contains(User.class));

    }
}