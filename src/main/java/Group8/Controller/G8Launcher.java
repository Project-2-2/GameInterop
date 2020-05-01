package Group8.Controller;


import Group8.Controller.Utils.Scenario;

public class G8Launcher {
    public static Scenario Scenario = new Scenario("res/testMap.txt");
    public static void main(String[] args){
            Controller.start(true);
    }
}
