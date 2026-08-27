import org.w3c.dom.css.Rect;

public class Rectangle {
    private int length;
    private int width;
    Rectangle(int length,int width){
        this.length = length;
        this.width = width;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                " length=" + length +
                " Width=" + width +
                " Area=" + Area() +
                ", Perimeter=" + perimeter() +
                '}';
    }

    public int Area(){
        return length*width;
    }
    public int perimeter(){
        return 2*(length+width);
    }
    static void main() {
        Rectangle r1 = new Rectangle(12,13);
        System.out.println(r1.Area());
        System.out.println(r1.perimeter());
        System.out.println(r1);
    }
}
