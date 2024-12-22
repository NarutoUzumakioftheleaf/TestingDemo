import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService() {
        this.employeePayrollList = new ArrayList<>();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);

        try (Scanner consoleInputReader = new Scanner(System.in)) {
            employeePayrollService.readEmployeePayrollData(consoleInputReader);
            employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);

            System.out.println("Reading data from file:");
            employeePayrollService.printData(IOService.FILE_IO);

            System.out.println("Total entries in file: " + employeePayrollService.countEntries(IOService.FILE_IO));
        }
    }

    private void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee Id:");
        int id = consoleInputReader.nextInt();
        consoleInputReader.nextLine(); // Consume newline left-over
        System.out.println("Enter Employee Name:");
        String name = consoleInputReader.nextLine();
        System.out.println("Enter Employee Salary:");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO)) {
            System.out.println("\nWriting Employee Payroll Roster to Console:\n" + employeePayrollList);
        } else if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
    }

    public long readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) {
            return new EmployeePayrollFileIOService().readData().size();
        }
        return 0;
    }

    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().printData();
        }
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) {
            return new EmployeePayrollFileIOService().countEntries();
        }
        return 0;
    }
}
