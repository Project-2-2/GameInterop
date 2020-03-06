package Group9.map.parser;

import Group9.map.Map;
import Group9.map.area.ShadedArea;
import Group9.map.area.TargetArea;
import Group9.map.area.TeleportArea;
import Group9.map.objects.Door;
import Group9.map.objects.SentryTower;
import Group9.map.objects.Wall;
import Group9.map.objects.Window;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Scenario.GameMode;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Parser {

    public static Map parseFile(String path)
    {
        Map.Builder builder = new Map.Builder();

        try {
            Files.lines(Paths.get(path)).forEachOrdered(line -> {

                String trimmed = line.trim();

                if(trimmed.isEmpty())
                {
                    return;
                }

                if(trimmed.startsWith("//"))
                {

                }
                else
                {
                    System.out.println(trimmed);
                    String[] split = trimmed.split("=");
                    String type = split[0].trim();
                    String[] data = split[1].trim().split(",");

                    switch (type.toLowerCase())
                    {
                        case "wall": {
                            builder.object(new Wall(quadrilateralFromData(data)));
                        } break;

                        case "targetarea": {
                            builder.effect(new TargetArea(quadrilateralFromData(data)));
                        } break;

                        case "teleport": {
                            builder.effect(new TeleportArea(quadrilateralFromData(data)));
                        } break;

                        case "shaded": {
                            builder.effect(new ShadedArea(quadrilateralFromData(data)));
                        } break;

                        case "door": {
                            builder.object(new Door(quadrilateralFromData(data)));
                        } break;

                        case "window": {
                            builder.object(new Window(quadrilateralFromData(data)));
                        } break;

                        case "sentry": {
                            builder.object(new SentryTower(quadrilateralFromData(data)));
                        } break;

                        case "gamemode": {
                            //TODO
                            builder.gameMode(Integer.parseInt(data[0]) == 0 ? GameMode.CaptureAllIntruders : GameMode.CaptureOneIntruder);
                        } break;

                        case "height": {
                            builder.height(Integer.parseInt(data[0]));
                        } break;

                        case "width": {
                            builder.width(Integer.parseInt(data[0]));
                        } break;

                        case "numguards": {
                            builder.numGuards(Integer.parseInt(data[0]));
                        } break;

                        case "numintruders": {
                            builder.numIntruders(Integer.parseInt(data[0]));
                        } break;

                        case "capturedistance": {
                            builder.captureDistance(Double.parseDouble(data[0]));
                        } break;

                        case "winconditionintruderrounds": {
                            builder.winConditionIntruderRounds(Integer.parseInt(data[0]));
                        } break;

                        case "maxrotationangle": {
                            builder.maxRotationAngle(Double.parseDouble(data[0]));
                        } break;

                        case "maxmovedistanceintruder": {
                            builder.intruderMaxMoveDistance(Double.parseDouble(data[0]));
                        } break;

                        case "maxsprintdistanceintruder": {
                            builder.intruderMaxSprintDistance(Double.parseDouble(data[0]));
                        } break;

                        case "maxmovedistanceguard": {
                            builder.guardMaxMoveDistance(Double.parseDouble(data[0]));
                        } break;

                        case "sprintcooldown": {
                            builder.sprintCooldown(Integer.parseInt(data[0]));
                        } break;

                        case "pheremoncooldown": {
                            builder.pheromoneCooldown(Integer.parseInt(data[0]));
                        } break;

                        case "radiuspheromone": {
                            builder.pheromoneRadius(Double.parseDouble(data[0]));
                        } break;

                        case "slowdownmodifierwindow": {
                            builder.windowSlowdownModifier(Double.parseDouble(data[0]));
                        } break;

                        case "slowdownmodifierdoor": {
                            builder.doorSlowdownModifier(Double.parseDouble(data[0]));
                        } break;

                        case "slowdownmodifiersentrytower": {
                            builder.sentrySlowdownModifier(Double.parseDouble(data[0]));
                        } break;

                        case "viewangle": {
                            builder.viewAngle(Double.parseDouble(data[0]));
                        } break;

                        case "viewrays": {
                            //TODO you can calculate this... you know: math
                        } break;

                        case "viewrangeintrudernormal": {
                            builder.intruderViewRangeNormal(Double.parseDouble(data[0]));
                        } break;

                        case "viewrangeintrudershaded": {
                            builder.intruderViewRangeShaded(Double.parseDouble(data[0]));
                        } break;

                        case "viewrangeguardnormal": {
                            builder.guardViewRangeNormal(Double.parseDouble(data[0]));
                        } break;

                        case "viewrangeguardshaded": {
                            builder.guardViewRangeShaded(Double.parseDouble(data[0]));
                        } break;

                        case "viewrangesentry": {
                            builder.sentryViewRange(Double.parseDouble(data[0]),Double.parseDouble(data[1]));
                        } break;

                        case "yellsoundradius": {
                            builder.yellSoundRadius(Double.parseDouble(data[0]));
                        } break;

                        case "maxmovesoundradius": {
                            builder.moveMaxSoundRadius(Double.parseDouble(data[0]));
                        } break;

                        case "windowsoundradius": {
                            builder.windowSoundRadius(Double.parseDouble(data[0]));
                        } break;

                        case "doorsoundradius": {
                            builder.doorSoundRadius(Double.parseDouble(data[0]));
                        } break;
                    }
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();

    }

    private static PointContainer.Quadrilateral quadrilateralFromData(String[] data)
    {
        return new PointContainer.Quadrilateral(
                new Vector2(Double.parseDouble(data[0]), Double.parseDouble(data[1])),
                new Vector2(Double.parseDouble(data[2]), Double.parseDouble(data[3])),
                new Vector2(Double.parseDouble(data[4]), Double.parseDouble(data[5])),
                new Vector2(Double.parseDouble(data[6]), Double.parseDouble(data[7]))
        );
    }

    public static enum Types {



    }

    public static interface Type {

        String getName();

        String getKey();

    }

}
