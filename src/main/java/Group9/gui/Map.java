package Group9.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import Group9.gui.SpawnAreaGui.SpawnAreaGuardGui;
import Group9.gui.SpawnAreaGui.SpawnAreaIntruderGui;

public class Map extends Application {

	@Override
	public void start(Stage s) throws Exception {
		VBox vBox = new VBox();
	    vBox.setPrefWidth(200);
	    vBox.setPrefHeight(50);
	    //Map
	    Rectangle map = new Rectangle(0,0,120*Scale.scale,80*Scale.scale);
	    map.setFill(Color.LIGHTGREEN);
	    map.setStroke(Color.BLACK);
	    map.setStrokeWidth(3);

	    //Draw Map
	    InternalWallGui wall1 = new InternalWallGui( 0.0,0.0,0.0,1.0,120.0,1.0,120.0,0.0);
	    wall1.updateScale();
	    InternalWallGui wall2 = new InternalWallGui(0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0);
	    wall2.updateScale();
	    InternalWallGui wall3 = new InternalWallGui(120.0,1.0,120.0,121.0,119.0,121.0,119.0,1.0);
	   wall3.updateScale();
	    InternalWallGui wall4 = new InternalWallGui(120.1,1.0,121.0,121.0,121.0,121.0,121.0,1.0);
	    wall4.updateScale();
	    InternalWallGui wall5 = new InternalWallGui(0,0,0,1,1,1,1,0);
	    wall5.updateScale();
	    InternalWallGui wall6 = new InternalWallGui(1.1,0,1.1,1,2,1,2,0);
	    wall6.updateScale();
	    InternalWallGui wall7 = new InternalWallGui(1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0);
	    wall7.updateScale();
	    InternalWallGui wall8 = new InternalWallGui(30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0);
	    wall8.updateScale();
	    InternalWallGui wall9 = new InternalWallGui(55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0);
	    wall9.updateScale();
	    InternalWallGui wall10 = new InternalWallGui(30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0);
	    wall10.updateScale();
	    InternalWallGui wall11 = new InternalWallGui(31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0);
	    wall11.updateScale();
	    InternalWallGui wall12 = new InternalWallGui(55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0);
	    wall12.updateScale();
	    InternalWallGui wall13 = new InternalWallGui(56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0);
	    wall13.updateScale();
	    InternalWallGui wall14 = new InternalWallGui(60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0);
	    wall14.updateScale();
	    InternalWallGui wall15 = new InternalWallGui(61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0);
	    wall15.updateScale();
	    InternalWallGui wall16 = new InternalWallGui(60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0);
	    wall16.updateScale();
	    InternalWallGui wall17 = new InternalWallGui(56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0);
	    wall17.updateScale();
	    InternalWallGui wall18 = new InternalWallGui(61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0);
	    wall18.updateScale();
	    InternalWallGui wall19 = new InternalWallGui(95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0);
	    wall19.updateScale();
	   
	    SpawnAreaGui.SpawnAreaGuardGui areaGuard = new SpawnAreaGuardGui(57.0,13.0,57.0,17.0,59.0,17.0,59.0,13.0);
	    areaGuard.updateScale();
	    targetAreaGui target = new targetAreaGui(89.0,38.0,94.0,38.0,94.0,40.0,89.0,40.0);
	    target.updateScale();
	    SpawnAreaGui.SpawnAreaIntruderGui areaIntruders = new SpawnAreaIntruderGui(57.0,67.0,57.0,77.0,59.0,77.0,59.0,67.0);
	    areaIntruders.updateScale();
	   
	    AgentGui intruder1 = new AgentGui(58,68,0.5,false);
	    intruder1.updateScale();
	    AgentGui intruder2 = new AgentGui(58,70,0.5,false);
	    intruder2.updateScale();
	    AgentGui guard1 = new AgentGui(58,13.5,0.5,true);
	    guard1.updateScale();
	    AgentGui guard2 = new AgentGui(58,14.7,0.5,true);
	    guard2.updateScale();
	    AgentGui guard3 = new AgentGui(58,15.7,0.5,true);
	    guard3.updateScale();


	    Group root = new Group();

	      Scene scene = new Scene(root, 970, 630,Color.BURLYWOOD);

	     root.getChildren().addAll(map,wall1,wall2,wall3,wall4,wall5,wall6,wall7,wall8,wall9,wall10,wall11,wall12,wall13,wall14,wall15,wall16,wall17,wall18,wall19,target,areaGuard,areaIntruders,guard1,guard2,guard3,intruder1,intruder2);
	      s.setScene(scene);
	    s.setTitle("Map ");
	 s.setResizable(false);
	    s.show();
	}


}
