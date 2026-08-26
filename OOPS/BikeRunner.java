public class BikeRunner {
   public static void main(String args[]) {
            System.out.println("This is the encapsulation using getters and setters");
            Bike honda = new Bike();
            Bike suzuki = new Bike(17);
       System.out.println(suzuki.getSpeed());
            honda.start();
            System.out.println("Initial speed "+honda.speed);
            // error becoz we can't access private variable here directly
          //System.out.println(honda.private_speed);
            System.out.println("Private speed initially "+honda.getSpeed());
            honda.setSpeed(45);
            System.out.println("After setting speed of private "+honda.getSpeed());
            honda.setSpeed(-100);
            System.out.println("setting speed of -ve "+honda.getSpeed());
            honda.increaseSpeed(100);
            System.out.println("After increasing speed "+honda.getSpeed());
            honda.decreaseSpeed(30);
            System.out.println("After decreasing speed "+honda.getSpeed());
            honda.decreaseSpeed(200);
            System.out.println("After decreasing speed "+honda.getSpeed());



   }
}