package Group9;

import Group9.map.Map;
import Group9.map.parser.Parser;

public class Main {

    public static void main(String[] args) {

        Map map = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        map.toString();

        //TODO limit tree depth
        //TODO check whether or not the order of the points matter

    }


}
