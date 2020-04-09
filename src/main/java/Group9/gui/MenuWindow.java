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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

//import java.awt.Polygon;


public class MenuWindow extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    String selectedFile;// = "./src/main/java/Group9/map/maps/test_2.map";

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
            	   Map x = new Map();
            	   try {
					x.start(primaryStage, selectedFile);
				} catch (Exception e) {

					e.printStackTrace();
				}
                }
                if (event.getSource() == quit) {
                    System.exit(0);
                }
            }

        };

        //filechooser
        Button chooseMap = new Button("Choose map");
        chooseMap.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        chooseMap.setTextFill(Color.WHITE);
        chooseMap.setTranslateX(600);
        chooseMap.setTranslateY(400);
        chooseMap.setMinWidth(vBox.getPrefWidth());
        chooseMap.setMinHeight(vBox.getPrefHeight());
        chooseMap.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:BLACK");
        chooseMap.setOnMousePressed(event -> {
            chooseMap.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:DARKSLATEGREY");
        });
        chooseMap.setOnMouseEntered(event -> {
            chooseMap.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:SIENNA");

        });
        chooseMap.setOnMouseExited(event -> {
            chooseMap.setStyle("-fx-border-color: white;-fx-border-width:3px;-fx-background-color:BLACK");

        });


        chooseMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser f = new FileChooser();
                String path = Paths.get("src/main/java/Group9/map/maps").toAbsolutePath().toString();
                f.setInitialDirectory(new File(path));
                selectedFile = f.showOpenDialog(primaryStage).toString();
            }
        });


        //Background image.
        /*ImageView background = new ImageView("menuImg.jpg");
        background.setFitHeight(650);
        background.setFitWidth(1000);*/

        Title title = new Title ("Multi-agents Surveillance");
		title.setTranslateX(120);
		title.setTranslateY(25);

       start.setOnMouseClicked(handler);
       quit.setOnMouseClicked(handler);


       	Group r = new Group();
       	Scene scene = new Scene(r, 970, 630);
        //r.getChildren().add(background);
        r.getChildren().addAll(title,start,quit,chooseMap);

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
                /*MapWindow map = new MapWindow();
                try {
					map.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

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
     //  primaryStage.setResizable(false);
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
	double scale = 5;
	VBox vBox = new VBox();
    vBox.setPrefWidth(200);
    vBox.setPrefHeight(50);
    //Map
    Rectangle map = new Rectangle(0,0,80,120);
    map.setFill(Color.TRANSPARENT);
    map.setStroke(Color.BLACK);
    map.setStrokeWidth(2);


    //Adding coordinates to the polygon
    double spawnareaGuards[] = {57.0,13.0,57.0,17.0,59.0,17.0,59.0,13.0};
    double targetArea[] = {89.0,38.0,94.0,38.0,94.0,40.0,89.0,40.0};
    double spawnAreaIntruders[] = {57.0,67.0,57.0,77.0,59.0,77.0,59.0,67.0};
    double wall1[] =  {0.0,0.0,0.0,1.0,120.0,1.0,120.0,0.0};

    // wall1.updateScale();
   double wall2[] = {0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0};
//    wall2.updateScale();
   double wall3[] = {0,0,0,1,1,1,1,0};
 //   wall3.updateScale();
   double wall4[] = {1.1,0,1.1,1,2,1,2,0};
   // wall4.updateScale();
    double wall5[] = { 1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0};
//    wall5.updateScale();
    double wall6[] = {30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0};
   // wall6.updateScale();
    double wall7[] = {55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0};
    //wall7.updateScale();
    double wall8[] = { 30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0};
  //  wall8.updateScale();
  double wall9[] = {31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0};
  //  wall9.updateScale();
   double wall10[] = {55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0};
   // wall10.updateScale();
   double wall11[] = {56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0};
   // wall11.updateScale();
    double wall12[] = {60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0};
   // wall12.updateScale();
    double wall13[] = {61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0};
  //  wall13.updateScale();
   double wall14[] = {60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0};
    //wall14.updateScale();
   double wall15[] = {56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0};
 //   wall15.updateScale();
    double wall16[] = {61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0};
   // wall16.updateScale();
    double wall17[] = {95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0};
    //wall17.updateScale();
    /*Objects pol1 = new Objects();
    pol1.draw(0.0,0.0,0.0,1,120,1,120,0);
    pol1.updateScale();
   Objects pol2 = new Objects();
    pol2.draw(0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0);
    pol2.updateScale();
    Objects pol3 = new Objects();
   pol3.draw(0,0,0,1,1,1,1,0);
    Polygon pol4 = Objects.draw(1.1,0,1.1,1,2,1,2,0);
    Polygon pol5 = Objects.draw(1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0);
    Polygon pol6 = Objects.draw(30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0);
    Polygon pol7 = Objects.draw(55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0);
    Polygon pol8 = Objects.draw(30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0);
    Polygon pol9 = Objects.draw(31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0);
    Polygon pol10 = Objects.draw(55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0);
    Polygon pol11 = Objects.draw(56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0);
    Polygon pol12 = Objects.draw(60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0);
    Polygon pol13 = Objects.draw(61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0);
    Polygon pol14 = Objects.draw(60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0);
    Polygon pol15 = Objects.draw(56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0);
    Polygon pol16 = Objects.draw(61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0);
    Polygon pol17 = Objects.draw(95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0);*/


 /*   Objects polygon1 = new Objects(0.0,0.0,0.0,1,120,1,120,0);
   polygon1.setStroke(Color.BLACK);
   // polygon1.updateScale();
   Objects polygon2 = new Objects(0.0,1.0,1.0,1.0,1.0,121.0,0.0,121.0);
    polygon2.setStroke(Color.BLACK);
   // polygon2.updateScale();
    Objects polygon3= new Objects(0,0,0,1,1,1,1,0);
    polygon3.setStroke(Color.BLACK);
   // polygon3.updateScale();
    Objects polygon4 = new Objects(1.1,0,1.1,1,2,1,2,0);
    polygon4.setStroke(Color.BLACK);
   // polygon4.updateScale();
    Objects polygon5 = new Objects(1.0,121.0,119.0,121.0,119.0,120.0,1.0,120.0);
    polygon5.setStroke(Color.BLACK);
   // polygon5.updateScale();
    Objects polygon6 = new Objects(30.0,37.0,55.0,37.0,55.0,36.0,30.0,36.0);
    polygon6.setStroke(Color.BLACK);
   // polygon6.updateScale();
    Objects polygon7 = new Objects(55.0,36.0,55.0,12.0,56.0,12.0,56.0,36.0);
    polygon7.setStroke(Color.BLACK);
    //polygon7.updateScale();
    Objects polygon8 = new Objects(30.0,37.0,30.0,42.0,31.0,42.0,31.0,37.0);
    polygon8.setStroke(Color.BLACK);
 //   polygon8.updateScale();
    Objects polygon9 = new Objects(31.0,42.0,31.0,41.0,55.0,41.0,55.0,42.0);
    polygon9.setStroke(Color.BLACK);
 //   polygon9.updateScale();
    Objects polygon10 = new Objects(55.0,41.0,55.0,87.0,56.0,87.0,56.0,41.0);
    polygon10.setStroke(Color.BLACK);
  //  polygon10.updateScale();
    Objects polygon11 = new Objects(56.0,12.0,60.0,12.0,60.0,11.0,56.0,11.0);
    polygon11.setStroke(Color.BLACK);
  //  polygon11.updateScale();
    Objects polygon12 = new Objects(60.0,12.0,60.0,36.0,61.0,36.0,61.0,12.0);
    polygon12.setStroke(Color.BLACK);
//    polygon12.updateScale();
    Objects polygon13 = new Objects(61.0,36.0,61.0,37.0,95.0,37.0,95.0,36.0);
    polygon13.setStroke(Color.BLACK);
   // polygon13.updateScale();
    Objects polygon14 = new Objects(60.0,41.0,60.0,87.0,61.0,87.0,61.0,41.0);
    polygon14.setStroke(Color.BLACK);
    //polygon14.updateScale();
    Objects polygon15 = new Objects(56.0,87.0,56.0,88.0,60.0,88.0,60.0,87.0);
    polygon15.setStroke(Color.BLACK);
    //polygon15.updateScale();
    Objects polygon16 = new Objects(61.0,41.0,61.0,42.0,95.0,42.0,95.0,41.0);
    polygon16.setStroke(Color.BLACK);
   // polygon16.updateScale();
    Objects polygon17 = new Objects(95.0,36.0,95.0,42.0,96.0,42.0,96.0,36.0);
    polygon17.setStroke(Color.BLACK);
  //  polygon17.updateScale();
    Polygon polygon18 = new Polygon(57.0,13.0,57.0,17.0,59.0,17.0,59.0,13.0);
    polygon18.setStroke(Color.BLACK);
 //   polygon18.updateScale();
    Polygon polygon19 = new Polygon(89.0,38.0,94.0,38.0,94.0,40.0,89.0,40.0);
    polygon19.setStroke(Color.BLACK);
  //  polygon19.updateScale();
    Polygon polygon20 = new Polygon(57.0,67.0,57.0,77.0,59.0,77.0,59.0,67.0);
    polygon20.setStroke(Color.BLACK);
  //  polygon20.updateScale();
  */
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

   // root.getChildren().addAll(map,line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line11,line12,line13,line14,line15,line16,line17,line18,line19,line20,line21,line22,line23,line24,line25,line26,line27,,line29,line30,line31,line32,line33,line34,line35,line36,line37,line38,line39,line40,line41,line42,line43,line44,line45,line46,line47,line48,line49,line50,line51,line52,line53,line54,line55,line56,line57,line58,line59,line60,line61,line62,line63,line64,line65,line66,line67,line68);
  // root.getChildren().addAll(map,polygon1,polygon2,polygon3,polygon4,polygon5,polygon6,polygon7,polygon8,polygon9,polygon10,polygon11,polygon12,,polygon14,polygon15,polygon16,polygon17,polygon18,polygon19,polygon20);
     //root.getChildren().addAll(map,pol1,pol2,pol3,pol4,pol5,pol6,pol7,pol8,pol9,pol10,pol11,pol12,pol13,pol14,pol15,pol16,pol17);
	s.setScene(scene);
    s.setTitle("Map ");
  //  s.setResizable(false);
    s.show();
}

}

class Title extends StackPane{
	public Title(String name) {
		Text text = new Text(name);
		text.setFill(Color.BLACK);
		text.setFont(Font.font("Chiller",FontWeight.EXTRA_BOLD, 70));
	//	setAlignment(Pos.CENTER);
		getChildren().addAll(text);
	}

}
