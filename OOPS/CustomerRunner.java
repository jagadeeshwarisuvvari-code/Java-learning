public class CustomerRunner {
    public static void main(String []args) {
        Address homeaddress = new Address("Korlakota","Amadalavalsa","532185");
        Customer c = new Customer("Jagadeeswari",homeaddress);
        System.out.println(c);
        Address workaddress = new Address("Sails Software Solutions","Vizag","532410");
        c.setWorkaddress(workaddress);
        System.out.println(c);
    }
}
