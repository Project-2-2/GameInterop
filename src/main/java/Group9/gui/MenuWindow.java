package Group9.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MenuWindow extends Application {
	
    public static void main(String[] args) {
        launch(args);
      
    }

    public void start(Stage primaryStage) {
	   
	   VBox vBox = new VBox();
       vBox.setPrefWidth(250);
       vBox.setPrefHeight(90);
       
    //Start button   
     Button start = new Button("START");
     start.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
     start.setTranslateX(150);
     start.setTranslateY(200);
     start.setTextFill(Color.WHITE);
     start.setMinWidth(vBox.getPrefWidth());
     start.setMinHeight(vBox.getPrefHeight());
     start.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
     start.setOnMousePressed(event -> {
         start.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
     });
     start.setOnMouseEntered(event -> {
         start.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");
     });
     start.setOnMouseExited(event -> {
         start.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:BLACK");
     });
    
     //Quit button     
     Button quit = new Button("QUIT");
     quit.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
     quit.setTextFill(Color.WHITE);
      quit.setTranslateX(150);
      quit.setTranslateY(400);   
      quit.setMinWidth(vBox.getPrefWidth());
      quit.setMinHeight(vBox.getPrefHeight());
      quit.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:BLACK");
      quit.setOnMousePressed(event -> {
          quit.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:DARKSLATEGREY");
      });
      quit.setOnMouseEntered(event -> {
          quit.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:SIENNA");

      });
      quit.setOnMouseExited(event -> {
          quit.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:BLACK");

      });
      
        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub

               if (event.getSource() == start) {
                  start1(primaryStage);
                }
                if (event.getSource() == quit) {
                    System.exit(0);
                }
            }

        };
        //Background image.
        ImageView background = new ImageView("menuImg.jpg");
        background.setFitHeight(650);
        background.setFitWidth(1000);
        
        Title title = new Title ("Multi-agents Surveillance");
		title.setTranslateX(120);
		title.setTranslateY(25);
		
       start.setOnMouseClicked(handler);
       quit.setOnMouseClicked(handler);
      
       	Group r = new Group();
       	Scene scene = new Scene(r, 970, 630);
        r.getChildren().add(background);
        r.getChildren().addAll(title,start,quit);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi-agents Surveillance");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
   public static void start1(Stage primaryStage) {

       VBox vBox = new VBox();
       vBox.setPrefWidth(250);
       vBox.setPrefHeight(90);
      
      //Game mode settings : 
       //Easy button
    Button easy = new Button("EASY");
   easy.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
    easy.setTranslateX(150);
    easy.setTranslateY(150);
    easy.setTextFill(Color.WHITE);
    easy.setMinWidth(vBox.getPrefWidth());
    easy.setMinHeight(vBox.getPrefHeight());
    easy.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
    easy.setOnMousePressed(event -> {
        easy.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
    });
    easy.setOnMouseEntered(event -> {
        easy.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

    });
    easy.setOnMouseExited(event -> {
        easy.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

    });
    //Medium button
    Button medium = new Button("MEDIUM");
    medium.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
     medium.setTranslateX(150);
     medium.setTranslateY(300);
     medium.setTextFill(Color.WHITE);
     medium.setMinWidth(vBox.getPrefWidth());
     medium.setMinHeight(vBox.getPrefHeight());
     medium.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: 	BLACK");
     medium.setOnMousePressed(event -> {
         medium.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
     });
     medium.setOnMouseEntered(event -> {
         medium.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

     });
     medium.setOnMouseExited(event -> {
         medium.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
});
     //hard button
     Button hard = new Button("HARD");
     hard.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
      hard.setTranslateX(150);
      hard.setTranslateY(450);
      hard.setTextFill(Color.WHITE);
      hard.setMinWidth(vBox.getPrefWidth());
      hard.setMinHeight(vBox.getPrefHeight());
      hard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
      hard.setOnMousePressed(event -> {
          hard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
      });
      hard.setOnMouseEntered(event -> {
          hard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");
});
      hard.setOnMouseExited(event -> {
          hard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
  });     
      //back
      Button back = new Button("BACK");
       back.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
       back.setTranslateX(720);
       back.setTranslateY(540);
       back.setTextFill(Color.WHITE);
       back.setMinWidth(vBox.getPrefWidth());
       back.setMinHeight(vBox.getPrefHeight());
       back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
       back.setOnMousePressed(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
       });
       back.setOnMouseEntered(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

       });
       back.setOnMouseExited(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

       });
     
       EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               // TODO Auto-generated method stub

              if (event.getSource() == easy) {
                start3(primaryStage);
            	 
               }
               if (event.getSource() == medium) {
            	   start3(primaryStage);
               }
               if(event.getSource()==hard)
               {
            	   start3(primaryStage);
               }
               if(event.getSource()==back)
               {
            	   MenuWindow back = new MenuWindow();            	   
            	  back.start(primaryStage);  
               }
           }

       };
       easy.setOnMouseClicked(handler);
       medium.setOnMouseClicked(handler);
       hard.setOnMouseClicked(handler);
       back.setOnMouseClicked(handler);
       ImageView background = new ImageView("menuImg.jpg");
       background.setFitHeight(650);
       background.setFitWidth(1000);
       

       Title title = new Title ("Select game level  ");
    	title.setTranslateX(100);
    	title.setTranslateY(30);
       
      
      
       Group root = new Group();
       Scene scene = new Scene(root, 970, 630);
       root.getChildren().add(background);
       root.getChildren().addAll(title,easy,medium,hard,back);
       
       primaryStage.setScene(scene);
       primaryStage.setTitle("Play mode ");
       primaryStage.setResizable(false);
       primaryStage.show();
   }
   
/*   public static void start2(Stage primaryStage) {

       VBox vBox = new VBox();
       vBox.setPrefWidth(250);
       vBox.setPrefHeight(90);
       
   Button guard = new Button("GUARD");
   guard.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
   guard.setTranslateX(150);
   guard.setTranslateY(200);  
   guard.setTextFill(Color.WHITE);
   guard.setMinWidth(vBox.getPrefWidth());
   guard.setMinHeight(vBox.getPrefHeight());
   guard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
   guard.setOnMousePressed(event -> {
	   guard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
    });
   guard.setOnMouseEntered(event -> {
	   guard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

    });
   guard.setOnMouseExited(event -> {
    	guard.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

    });
    
    
     
     Button intruder = new Button("INTRUDER");
     intruder.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
     intruder.setTranslateX(150);
     intruder.setTranslateY(350);
     intruder.setTextFill(Color.WHITE); 
      
     intruder.setMinWidth(vBox.getPrefWidth());
     intruder.setMinHeight(vBox.getPrefHeight());
      intruder.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
      intruder.setOnMousePressed(event -> {
    intruder.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
      });
      intruder.setOnMouseEntered(event -> {
    	  intruder.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

      });
      intruder.setOnMouseExited(event -> {
    	  intruder.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

      });

      Button back = new Button("BACK");
      back.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
       back.setTranslateX(720);
       back.setTranslateY(540);
       back.setTextFill(Color.WHITE);      
       back.setMinWidth(vBox.getPrefWidth());
       back.setMinHeight(vBox.getPrefHeight());
       back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");
       back.setOnMousePressed(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: DARKSLATEGREY");
       });
       back.setOnMouseEntered(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: SIENNA");

       });
       back.setOnMouseExited(event -> {
           back.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color: BLACK");

       });

       EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               // TODO Auto-generated method stub

              if (event.getSource() == guard) {
                //   ModeSelectionWindow.start(primaryStage);
            	  start1(primaryStage);
               }
               
               if(event.getSource()==intruder)
               {
            	   start1(primaryStage);
               }
               if(event.getSource()==back)
               {
            	  //start(primaryStage);
           	   }
           }

       };
       
       intruder.setOnMouseClicked(handler);
       guard.setOnMouseClicked(handler);
       
       ImageView background = new ImageView("menuImg.jpg");
       background.setFitHeight(650);
       background.setFitWidth(1000);
       
       Title title = new Title ("Select one : ");
		title.setTranslateX(100);
		title.setTranslateY(30);
   
      
		//General window setup
      
       Group root = new Group();
       Scene scene = new Scene(root, 970, 630);
       root.getChildren().add(background);
       root.getChildren().addAll(title,guard,intruder,back);
       
       primaryStage.setScene(scene);
       primaryStage.setTitle("Play mode ");
       primaryStage.setResizable(false);
       primaryStage.show();
   }
  */
	
public static void start3(Stage s)
{
	VBox vBox = new VBox();
    vBox.setPrefWidth(200);
    vBox.setPrefHeight(50);
    //Map
    Rectangle map = new Rectangle(10,10,650,500);
    map.setFill(Color.TRANSPARENT);
    map.setStroke(Color.BLACK);
    map.setStrokeWidth(2);
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
	  
      Scene scene = new Scene(root, 970, 630, Color.BURLYWOOD);
     root.getChildren().addAll(Floor2,Floor3,Floor1,map);
	s.setScene(scene);
    s.setTitle("Map ");
    s.setResizable(false);
    s.show();
}
	
}

class Title extends StackPane {
	public Title(String name) {
		Text text = new Text(name);
		text.setFill(Color.BLACK);
		text.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 70));
	//	setAlignment(Pos.CENTER);
		getChildren().addAll(text);
	}
}
