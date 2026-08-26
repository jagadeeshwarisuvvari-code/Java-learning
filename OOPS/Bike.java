public class Bike {

    int speed=100;
    private int private_speed=89; //member variable

   // Bike(){
        // Default constructor
 //   }
    Bike(){
        System.out.println("This is the constructor");
        this(52);
    }
    Bike(int speed){
        this.private_speed = this.speed;
    }



    void setSpeed(int private_speed){
        // checking validation here
        if(private_speed>0)
            this.private_speed = private_speed;
    }
    int getSpeed(){
             return private_speed;
    }
    public void start(){
        System.out.println("Starting");
    }
    public void increaseSpeed(int howmuch){
       // this.private_speed += howmuch;
        setSpeed(this.private_speed+howmuch);
    }
    public void decreaseSpeed(int howmuch){
       // if(howmuch<=private_speed) // Again checking validation here
        //  this.private_speed -= howmuch;
        setSpeed(this.private_speed-howmuch);

    }
}
