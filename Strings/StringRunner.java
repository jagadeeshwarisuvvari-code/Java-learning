
public class StringRunner {
    public static void main(String[] args) {

        String[] weekdays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String dayWithMostChars = "";
        for(String day:weekdays){
                if(day.length()>dayWithMostChars.length())
                    dayWithMostChars = day;
        }
        System.out.println(dayWithMostChars);
        System.out.println("Reverse the weekdays");
        for(int i= weekdays.length-1;i>=0;i--){
            System.out.println(weekdays[i]);
        }
    }
}
