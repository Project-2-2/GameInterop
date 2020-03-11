package Group9.gui;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Agent 
{
	final double x;
	final double y;
	final double r;
	final static double speed = 1.4;
	final Duration Timestep = Duration.seconds(0.1);
	
	public Agent(double x,double y,double r)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		agents(x,y,r);
		
	}
	public static Circle agents(double x, double y, double radius)
	{
		Circle agent = new Circle(x,y,radius);
		
		return agent;
				
	}
	
	/*public static void move(Scene scene, Circle agent) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode()== KeyCode.UP)
				{agent.setCenterY(agent.getCenterY()-speed);}
				else if(event.getCode()== KeyCode.DOWN)
				{agent.setCenterY(agent.getCenterY()+speed);}
				else if(event.getCode()== KeyCode.RIGHT)
				{agent.setCenterX(agent.getCenterX()+speed);}
				else if(event.getCode()== KeyCode.LEFT)
				{agent.setCenterX(agent.getCenterX()-speed);}
				
			}
			
		});
	}*/
	
	
	}