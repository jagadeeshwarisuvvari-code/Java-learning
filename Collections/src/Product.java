public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    public Product(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
    public void reduceStock(int howMany){
        this.stock -= howMany;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
