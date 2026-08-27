public class Basics {
    int id;
    String name;
    double salary;

    Basics(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id +
                ", name= " + name +
                ", salary=" + salary + "}";
    }


    static void main() {
        Basics b = new Basics(1,"Jagadeeswari",4.5);
        System.out.println(b); // className@Hashcode
//       System.out.println(b.toString());

    }
}
