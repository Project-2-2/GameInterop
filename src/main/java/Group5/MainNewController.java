package Group5;

import Group9.gui2.Gui;
import javafx.application.Application;

public class MainNewController {

    /**
     * Use this class to call the new GUI
     * The agents try to walk out of the map, so if you see no movemnt this is no bug
     * The agents only walk in one certain direction, actually for inspiration look at the randomAgent class from group 9 it is actually already a decent implementation
     * @param args
     */
    public static void main(String[] args) {
        new Thread(() -> Application.launch(Gui.class)).start();
    }
}
