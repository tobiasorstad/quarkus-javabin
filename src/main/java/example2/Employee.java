package example2;

public class Employee{

    String name;
    String employeeNumber;
    String dateCreated;

    public Employee(String name, String employeeNumber, String dateCreated){
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.dateCreated = dateCreated;
    }

    public Employee(String name, String employeeNumber){
        this.name = name;
        this.employeeNumber = employeeNumber;
    }
}
