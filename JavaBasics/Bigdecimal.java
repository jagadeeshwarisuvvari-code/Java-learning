import java.math.BigDecimal;
public class Bigdecimal {
    static void main(String []args) {
        float f1 = 45.1f;
        float f2 = 23.4567f;
        double db = 345.4;
        System.out.println(f1);
        System.out.println(db);
        System.out.println(f1+f2);
        BigDecimal n1 = new BigDecimal("45.1");
        BigDecimal n2 = new BigDecimal("23.4567");
        BigDecimal result = n1.add(n2);
        System.out.println(result);
    }
}
