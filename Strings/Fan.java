public class Fan {
    String make;
    double radius;
    String color;
    boolean isOn;
    byte speed; // 0 to 5
    Fan(String make,double radius,String color){
        this.make = make;
        this.radius = radius;
        this.color = color;
    }
    public void switchOn(){
        this.isOn = true;
    }
    public void switchOff(){
        this.isOn = false;
    }
    public void setSpeed(byte speed){
        this.speed = speed;
    }
        public String toString(){
        return String.format("make - %s,radius - %f,color-%s,isOn - %b,speed - %d",make,radius,color,isOn,speed);
    }
    static void main() {
        Fan f = new Fan("Manufacture",12,"brown");
        System.out.println(f);
        f.switchOn();
        System.out.println(f);
        f.switchOff();
        f.setSpeed((byte)5);
        System.out.println(f);

    }
}
