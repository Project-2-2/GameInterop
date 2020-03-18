package Group9.gui;
import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.paint.Color; 
import javafx.stage.Stage; 
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


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
            	   Map x = new Map();
            	   try {
					x.start(primaryStage);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
                }
                if (event.getSource() == quit) {
                    System.exit(0);
                }
            }

        };
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
        r.getChildren().addAll(title,start,quit);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi-agents Surveillance");
        primaryStage.setResizable(false);
        primaryStage.show();
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
