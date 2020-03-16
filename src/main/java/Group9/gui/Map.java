package Group9.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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
	    InternalWall wall1 = new InternalWall( 0.0,0.0,0.0,1.0,120.0,1.0,120.0,0.0);
	    wall1.updateScale();
	    InternalWall wall2 = new InternalWall(0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0);
	    wall2.updateScale();
	    InternalWall wall3 = new InternalWall(120.0,1.0,120.0,121.0,119.0,121.0,119.0,1.0);
	    wall3.updateScale();
	    InternalWall wall4 = new InternalWall(120.1,1.0,121.0,121.0,121.0,121.0,121.0,1.0);
	    wall4.updateScale();
	    InternalWall wall5 = new InternalWall(0,0,0,1,1,1,1,0);
	    wall5.updateScale();
	    InternalWall wall6 = new InternalWall(1.1,0,1.1,1,2,1,2,0);
	    wall6.updateScale();
	    InternalWall wall7 = new InternalWall(1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0);
	    wall7.updateScale();
	    InternalWall wall8 = new InternalWall(30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0);
	    wall8.updateScale();
	    InternalWall wall9 = new InternalWall(55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0);
	    wall9.updateScale();
	    InternalWall wall10 = new InternalWall(30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0);
	    wall10.updateScale();
	    InternalWall wall11 = new InternalWall(31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0);
	    wall11.updateScale();
	    InternalWall wall12 = new InternalWall(55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0);
	    wall12.updateScale();
	    InternalWall wall13 = new InternalWall(56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0);
	    wall13.updateScale();
	    InternalWall wall14 = new InternalWall(60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0);
	    wall14.updateScale();
	    InternalWall wall15 = new InternalWall(61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0);
	    wall15.updateScale();
	    InternalWall wall16 = new InternalWall(60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0);
	    wall16.updateScale();
	    InternalWall wall17 = new InternalWall(56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0);
	    wall17.updateScale();
	    InternalWall wall18 = new InternalWall(61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0);
	    wall18.updateScale();
	    InternalWall wall19 = new InternalWall(95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0);
	    wall19.updateScale();
	    SpawnAreaGuard areaGuard = new SpawnAreaGuard(57.0,13.0,57.0,17.0,59.0,17.0,59.0,13.0);
	    areaGuard.updateScale();   
	    
	    targetArea target = new targetArea(89.0,38.0,94.0,38.0,94.0,40.0,89.0,40.0);
	    target.updateScale();
	    SpawnAreaIntruders areaIntruders = new SpawnAreaIntruders(57.0,67.0,57.0,77.0,59.0,77.0,59.0,67.0);
	    areaIntruders.updateScale();
	    Agent intruder1 = new Agent(58,68,0.5,false);
	    intruder1.updateScale();
	    Agent intruder2 = new Agent(58,70,0.5,false);
	    intruder2.updateScale();
	    Agent guard1 = new Agent(58,13.5,0.5,true);
	    guard1.updateScale();
	    Agent guard2 = new Agent(58,14.7,0.5,true);
	    guard2.updateScale();
	    Agent guard3 = new Agent(58,15.7,0.5,true);
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
