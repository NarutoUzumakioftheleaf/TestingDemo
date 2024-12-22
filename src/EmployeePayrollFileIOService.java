import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {

    public static final String PAYROLL_FILE_NAME = "payroll-file.txt";

    public void writeData(List<EmployeePayrollData> employeePayrollList) {
        StringBuilder empBuffer = new StringBuilder();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });
        try {
            Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public List<EmployeePayrollData> readData() {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(line -> {
                String[] data = line.split(", ");
                int id = Integer.parseInt(data[0].split("=")[1]);
                String name = data[1].split("=")[1];
                double salary = Double.parseDouble(data[2].split("=")[1].replace("}", ""));
                employeePayrollList.add(new EmployeePayrollData(id, name, salary));
            });
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return employeePayrollList;
    }

    public void printData() {
        try {
            Files.lines(Paths.get(PAYROLL_FILE_NAME)).forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Error printing data from file: " + e.getMessage());
        }
    }

    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(Paths.get(PAYROLL_FILE_NAME)).count();
        } catch (IOException e) {
            System.err.println("Error counting entries in file: " + e.getMessage());
        }
        return entries;
    }
}
