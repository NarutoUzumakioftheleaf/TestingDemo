import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 1000000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 3000000.0)
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);

        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        long entries = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException {
        String homeDir = System.getProperty("user.home");
        Path dir = Paths.get(homeDir, "PLAY_WITH_NIO");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
        Java8WatchServiceExample watchService = new Java8WatchServiceExample(dir);
        watchService.processEvents();
    }
}
