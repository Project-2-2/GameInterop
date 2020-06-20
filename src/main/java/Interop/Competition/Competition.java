package Interop.Competition;

import Group9.Callback;
import Group9.Game;
import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Interop.Competition.Agent.CompetitionIAgentsFactory;
import Interop.Competition.Agent.SittingDuckAgentsFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomasz Darmetko
 */
public class Competition {

    static List<Class<?>> factories = Arrays.asList(
        Group1.AgentsFactory.class,
        Group2.AgentsFactory.class,
        Group3.AgentsFactory.class,
        Group4.AgentsFactory.class,
        Group5.AgentsFactory.class,
        Group6.AgentsFactory.class,
        Group7.AgentsFactory.class,
        Group8.AgentsFactory.class,
        Group9.AgentsFactory.class,
        Group10.AgentsFactory.class,
        Group11.AgentsFactory.class
    );

    static int maxRounds = 5000;

    static int qualificationTries = 10;
    static int qualificationMaxRounds = 5000;

    static List<String> mapsGroup6 = Arrays.asList(
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple.map",
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple2.map",
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple3.map",
        "src/main/java/Interop/Competition/Maps/Group6/mirror.map",
        "src/main/java/Interop/Competition/Maps/Group6/open.map",
        "src/main/java/Interop/Competition/Maps/Group6/rooms.map",
        "src/main/java/Interop/Competition/Maps/Group6/spiral.map",
        "src/main/java/Interop/Competition/Maps/Group6/temple.map"
    );

    static List<String> simpleMaps = Arrays.asList(
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple.map",
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple2.map",
        "src/main/java/Interop/Competition/Maps/Group6/Simple/simple3.map"
    );

    static List<Class<?>> sittingDucksFactory = Collections.singletonList(SittingDuckAgentsFactory.class);

    public static void main(String[] args) {

        Console.disableErrOutput(); // !!! comment out for debugging !!!

        List<Class<?>> intruderFactories = factories;
        List<Class<?>> guardFactories = factories;

        System.out.println("\n\n### Qualifications! Select agents working on 3 simple maps ... ");
        for (String mapPath: simpleMaps) {

            System.out.println("Qualification on: " + getMapName(mapPath));
            intruderFactories = getWorkingIntruders(intruderFactories, mapPath);
            guardFactories = getWorkingGuards(guardFactories, mapPath);

            System.out.print("Qualified intruders of groups: ");
            reportFactories(intruderFactories);

            System.out.print("Qualified guards of groups: ");
            reportFactories(guardFactories);

            System.out.println();

        }

        System.out.println("\n\n### Preliminaries! Groups Agents vs. Sitting Ducks on simple and complex maps:\n\n");

        List<String> maps = mapsGroup6;

        runCompetition(intruderFactories, sittingDucksFactory, maps);
        runCompetition(sittingDucksFactory, guardFactories, maps);

        System.out.println("\n\n### Finals! Group Agents vs. Group Agents on simple and complex maps:\n\n");

        runCompetition(intruderFactories, guardFactories, maps);

    }

    public static void runCompetition(
        List<Class<?>> intruderFactories,
        List<Class<?>> guardFactories,
        List<String> maps
    ) {
        for (Class<?> intruderFactory: intruderFactories) {

            for (Class<?> guardFactory: guardFactories) {

                for(String map: maps) {

                    for (int i = 0; i < 3; i++) {

                        playAndReportGame(intruderFactory, guardFactory, map);

                    }

                }

                System.out.println();

            }

        }
    }

    public static List<Class<?>> getWorkingIntruders(List<Class<?>> factories, String mapPath) {
        return factories.stream().filter(factory -> {
                for (int i = 0; i < qualificationTries; i++) {
                    LimitedRoundsGame game = getGame(
                        qualificationMaxRounds,
                        Parser.parseFile(mapPath),
                        factory,
                        SittingDuckAgentsFactory.class
                    );
                    Console.runSilent(game::play);
                    if(game.getWinner() == Game.Team.INTRUDERS) return true;
                }
                return false;
            }).collect(Collectors.toList());
    }

    public static List<Class<?>> getWorkingGuards(List<Class<?>> factories, String mapPath) {
        return factories.stream().filter(factory -> {
            for (int i = 0; i < qualificationTries; i++) {
                LimitedRoundsGame game = getGame(
                    qualificationMaxRounds,
                    Parser.parseFile(mapPath),
                    SittingDuckAgentsFactory.class,
                    factory
                );
                Console.runSilent(game::play);
                if(game.getWinner() == Game.Team.GUARDS) return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    public static LimitedRoundsGame playAndReportGame(Class<?> intruderFactory, Class<?> guardFactory, String map) {

        LimitedRoundsGame game = getGame(
            maxRounds,
            Parser.parseFile(map),
            intruderFactory,
            guardFactory
        );

        Console.runSilent(game::play);

        System.out.println(
            "Map: " + getMapName(map) + "\t" +
                "Intruders: " + intruderFactory.getPackage().getName() + "\t" +
                "Guards: " + guardFactory.getPackage().getName() + "\t" +
                "Winner: " + game.getWinner() + "\t" +
                "Rounds: " + game.getCurrentRoundsCount()
        );

        return game;

    }

    public static LimitedRoundsGame getGame(
        int maxRounds,
        GameMap gameMap,
        Class<?> intrudersFactoryClass,
        Class<?> guardsFactoryClass
    ) {
        return new LimitedRoundsGame(
            gameMap,
            new CompetitionIAgentsFactory(intrudersFactoryClass, guardsFactoryClass),
            maxRounds
        );
    }

    public static void reportFactories(List<Class<?>> factories) {
        for (Class<?> factory: factories) {
            System.out.print(factory.getPackage().getName() + " ");
        }
        System.out.println();
    }

    public static String getMapName(String mapPath) {
        return mapPath.substring(mapPath.lastIndexOf('/') + 1);
    }

}
