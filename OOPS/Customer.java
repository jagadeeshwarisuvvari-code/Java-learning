public class Customer {
    private String name;
    private Address homeaddress;
    // It is the composition -- object inside another object...
    private Address workaddress;
    public Customer(String name,Address homeaddress){
        this.name = name;
        this.homeaddress = homeaddress;
    }

    public String getName() {
        return name;
    }

    public Address getHomeaddress() {
        return homeaddress;
    }

    public Address getWorkaddress() {
        return workaddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHomeaddress(Address homeaddress) {
        this.homeaddress = homeaddress;
    }

    public void setWorkaddress(Address workaddress) {
        this.workaddress = workaddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", homeaddress=" + homeaddress +
                ", workaddress=" + workaddress +
                '}';
    }
}
