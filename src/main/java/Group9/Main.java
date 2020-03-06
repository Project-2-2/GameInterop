package Group9;

import Group9.map.Map;
import Group9.map.parser.Parser;

public class Main {

    public static void main(String[] args) {

        // How to read in a map from a file
        Map map = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");

    }


}
