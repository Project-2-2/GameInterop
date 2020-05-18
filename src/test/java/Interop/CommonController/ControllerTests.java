//package Interop.CommonController;
//
//import Group9.Game;
//import Group9.agent.factories.DefaultAgentFactory;
//import Group9.agent.factories.IAgentFactory;
//import Group9.map.parser.Parser;
//import Interop.Action.GuardAction;
//import Interop.Action.IntruderAction;
//import Interop.Action.NoAction;
//import Interop.Action.Rotate;
//import Interop.Agent.Guard;
//import Interop.Agent.Intruder;
//import Interop.Geometry.Angle;
//import Interop.Geometry.Direction;
//import Interop.Percept.GuardPercepts;
//import Interop.Percept.IntruderPercepts;
//import Interop.Percept.Vision.ObjectPerceptType;
//import SimpleUnitTest.SimpleUnitTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ControllerTests extends SimpleUnitTest {
//
//    public static void main(String[] args) {
//
//        System.out.println("\n\nController Tests\n");
//
//        it("agents can't see trough wall", () -> {
//
//            Game game = new Game(
//                Parser.parseFile("./src/test/java/Interop/CommonController/maps/vision_test.map"),
//                new IAgentFactory() {
//                    public List<Intruder> createIntruders(int amount) {
//                        List<Intruder> list = new ArrayList<>();
//                        for (int i = 0; i < amount; i++) {
//                            list.add(new Intruder() {
//                                public IntruderAction getAction(IntruderPercepts percepts) {
//
//                                    int countDoorInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Door)
//                                        .getAll()
//                                        .size();
//
//                                    int countWindowInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Window)
//                                        .getAll()
//                                        .size();
//
//                                    int countGuardsInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Guard)
//                                        .getAll()
//                                        .size();
//
//                                    assertTrue(countWindowInFieldOfView > 0, "Intruder can see window.");
//                                    assertEqual(countGuardsInFieldOfView, 0, "Intruder can't see guard.");
//                                    assertEqual(countDoorInFieldOfView, 0, "Intruder can't see door.");
//
//                                    return new NoAction();
//                                }
//                            });
//                        }
//                        return list;
//                    }
//
//                    public List<Guard> createGuards(int amount) {
//                        List<Guard> list = new ArrayList<>();
//                        for (int i = 0; i < amount; i++) {
//                            list.add(new Guard() {
//                                public GuardAction getAction(GuardPercepts percepts) {
//
//                                    int countDoorInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Door)
//                                        .getAll()
//                                        .size();
//
//                                    int countWindowInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Window)
//                                        .getAll()
//                                        .size();
//
//                                    int countIntrudersInFieldOfView = percepts
//                                        .getVision()
//                                        .getObjects()
//                                        .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder)
//                                        .getAll()
//                                        .size();
//
//                                    assertTrue(countDoorInFieldOfView > 0, "Guard can see doors.");
//                                    assertEqual(countIntrudersInFieldOfView, 0, "Guard can't see intruder.");
//                                    assertEqual(countWindowInFieldOfView, 0, "Guard can't see window.");
//
//                                    return new NoAction();
//                                }
//                            });
//                        }
//                        return list;
//                    }
//                },
//                false,
//                -1,
//                (game1) -> {
//                    game1.getRunningLoop().set(false);
//                }
//            );
//
//            game.run();
//
//        });
//
//        xit("allows intruders to do 360 degree rotation", () -> {
//
//            var counter = new Object() {
//                int rounds = 0;
//            };
//
//            new Game(
//                Parser.parseFile("./src/test/java/Interop/CommonController/maps/simple_test.map"),
//                new IAgentFactory() {
//                    public List<Intruder> createIntruders(int amount) {
//                        List<Intruder> list = new ArrayList<>();
//                        for (int i = 0; i < amount; i++) {
//                            list.add((intruderPercepts) -> {
//                                return new Rotate(intruderPercepts.getTargetDirection());
//                            });
//                        }
//                        return list;
//                    }
//                    public List<Guard> createGuards(int amount) {
//                        List<Guard> list = new ArrayList<>();
//                        for (int i = 0; i < amount; i++) {
//                            list.add((guardPercepts) -> new NoAction());
//                        }
//                        return list;
//                    }
//                },
//                false,
//                -1,
//                (game) -> {
//                    counter.rounds++;
//                    if(counter.rounds > 1000) game.getRunningLoop().set(false);
//                }
//            ).run();
//
//        });
//
//    }
//
//}
