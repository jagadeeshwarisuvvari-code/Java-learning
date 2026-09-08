import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer {
    private String id;
    private String name;

    private List<Order> orderList = new ArrayList<>();
    private Set<Product> wishlist = new HashSet<>();
    public Customer(){

    }
    public void addOrder(Order order){
        orderList.add(order);
    }

}
