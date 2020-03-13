package Group9.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MapWindow extends Application {
	
	 double x1;
	 double x2;
	 double x3;
	double x4;
	double y1;
	double y2;
	double y3;
	double y4;
	public static double scale = 5;

	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		double scale = 5;
		VBox vBox = new VBox();
	    vBox.setPrefWidth(200);
	    vBox.setPrefHeight(50);
	    //Map
	    Rectangle map = new Rectangle(0,0,80*scale,120*scale);
	    map.setFill(Color.TRANSPARENT);
	    map.setStroke(Color.BLACK);
	    map.setStrokeWidth(2);
	   
	    
	    
	    Polygon spawnareaGuards = draw(57.0,13.0,57.0,17.0,59.0,17.0,59.0,13.0); 
	    Polygon targetArea = draw(89.0,38.0,94.0,38.0,94.0,40.0,89.0,40.0);
	    Polygon spawnAreaIntruders = draw(57.0,67.0,57.0,77.0,59.0,77.0,59.0,67.0);
	    Polygon wall1 = draw(0.0,0.0,0.0,1,120,1,120,0);
	    Polygon wall2 = draw(0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0);
	    Polygon wall3 = draw(0,0,0,1,1,1,1,0);
	    Polygon wall4 = draw(1.1,0,1.1,1,2,1,2,0);
	    Polygon wall5 = draw(1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0);
	    Polygon wall6 = draw(30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0);
	    Polygon wall7 = draw(55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0);
	    Polygon wall8 = draw(30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0);
	    Polygon wall9 =draw(31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0);
	    Polygon wall10 =draw(55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0);
	    Polygon wall11 = draw(56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0);
	    Polygon wall12 = draw(60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0);
	    Polygon wall13 = draw(61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0);
	    Polygon wall14 = draw(60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0);
	    Polygon wall15 = draw(56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0);
	    Polygon wall16 =draw(61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0);
	    Polygon wall17 = draw(95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0);
	    
	    
	
	    //Floors 
	    Button Floor1 = new Button("Floor1");
	    Floor1.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
	    Floor1.setTranslateX(750);
	    Floor1.setTranslateY(329);
	    Floor1.setTextFill(Color.WHITE);      
	    Floor1.setMinWidth(vBox.getPrefWidth());
	    Floor1.setMinHeight(vBox.getPrefHeight());
	    Floor1.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
	    Floor1.setOnMousePressed(event -> {
	    	Floor1.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
	     });
	    Floor1.setOnMouseEntered(event -> {
	    	Floor1.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

	     });
	    Floor1.setOnMouseExited(event -> {
	    	Floor1.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

	     });


	    Button Floor3 = new Button("Floor3");
	    Floor3.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
	    Floor3.setTranslateX(750);
	    Floor3.setTranslateY(450);
	    Floor3.setTextFill(Color.WHITE);      
	    Floor3.setMinWidth(vBox.getPrefWidth());
	    Floor3.setMinHeight(vBox.getPrefHeight());
	    Floor3.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
	    Floor3.setOnMousePressed(event -> {
	    	Floor3.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
	     });
	    Floor3.setOnMouseEntered(event -> {
	    	Floor3.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

	     });
	    Floor3.setOnMouseExited(event -> {
	    	Floor3.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

	     });

	    
	    Button Floor2 = new Button("Floor2");
	    Floor2.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
	    Floor2.setTranslateX(750);
	    Floor2.setTranslateY(390);
	    Floor2.setTextFill(Color.WHITE); 
	     
	    Floor2.setMinWidth(vBox.getPrefWidth());
	    Floor2.setMinHeight(vBox.getPrefHeight());
	    Floor2.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
	    Floor2.setOnMousePressed(event -> {
	    	Floor2.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
	     });
	    Floor2.setOnMouseEntered(event -> {
	    	Floor2.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

	     });
	    Floor2.setOnMouseExited(event -> {
	    	Floor2.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

	     });

	    
	 /*  VBox vBox = new VBox();
	      vBox.setPrefWidth(10);
	      vBox.setPrefHeight(10);
	      Button left = new Button("L");
	  left.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
	  left.setTranslateX(30);
	  left.setTranslateY(90);  
	  left.setTextFill(Color.WHITE);
	  Button right = new Button("R");
	  right.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
	  right.setTranslateX(80);
	  right.setTranslateY(90);  
	  right.setTextFill(Color.WHITE);
	  Button front = new Button("F");
	  front.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
	  front.setTranslateX(60);
	  front.setTranslateY(50);  
	  front.setTextFill(Color.WHITE);
	  Button back = new Button("B");
	  back.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
	  back.setTranslateX(60);
	  back.setTranslateY(130);  
	  back.setTextFill(Color.WHITE);
	 
		Line l = new Line(70,60,70,140);
		Line l2 = new Line(40,100,90,100);*/
		

		  Group root = new Group();
		  
	      Scene scene = new Scene(root, 970, 630,Color.BURLYWOOD);
	      
	     root.getChildren().addAll(map,wall1,wall2,wall3,wall4,wall5,wall6,wall7,wall8,wall9,wall10,wall11,wall12,wall13,wall14,wall15,wall16,wall17,spawnareaGuards,targetArea,spawnAreaIntruders);
		s.setScene(scene);
	    s.setTitle("Map ");
	  s.setResizable(false);
	    s.show();
		
	}

	public Polygon draw(double x1,double y1,double x2,double y2, double x3, double y3 , double x4,double y4) {
		Polygon map = new Polygon(x1*scale,y1*scale,x2*scale,y2*scale,x3*scale,y3*scale,x4*scale,y4*scale);
		map.setStroke(Color.BLACK);
		map.setFill(Color.TRANSPARENT);
		
		return map;}
	
	
	

}
