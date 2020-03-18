package Group9.gui;

import Group9.math.Vector2;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class AgentGui extends Group implements GameObject
{
	double x;
	double y;
	final double r;
	final static double speed = 1.4;//for both guard and intruder
	final static double sprint = 4;//maxSprintDistanceIntruder 
	final Duration Timestep = Duration.seconds(0.1);
	static boolean guard = false;
	final Circle head;
	final VisionConeGui vision;

	
	public AgentGui(double x, double y, double r, Vector2 direction, double range, boolean guard)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		head = new Circle();
		head.setCenterX(x);
		head.setCenterY(y);
		head.setRadius(r);
		if(guard) {head.setFill(Color.BLUE);head.setStroke(Color.BLACK);}
		else {head.setFill(Color.RED);}
		vision = new VisionConeGui(direction, x, y, range);
		getChildren().addAll(vision, head);
		
		
	}
	
	
	@Override
	public void updateScale() {
		// TODO Auto-generated method stub
		double scale = Scale.scale;
       head.setCenterX(x*scale);
       head.setCenterY(y*scale);
       head.setRadius(r*scale);
       vision.updateScale();
       
	}
	public Circle getHead()
	{
		return head;
	}
	public static void move(Scene scene, AgentGui x) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				if(event.getCode()== KeyCode.UP)
				{x.getHead().setCenterY(x.getHead().getCenterY()-speed);}
				else if(event.getCode()== KeyCode.DOWN)
				{x.getHead().setCenterY(x.getHead().getCenterY()+speed);}
				else if(event.getCode()== KeyCode.RIGHT)
				{x.getHead().setCenterX(x.getHead().getCenterX()+speed);}
				else if(event.getCode()== KeyCode.LEFT)
				{x.getHead().setCenterX(x.getHead().getCenterX()-speed);}
				
			}
			
		});
		
		
	}
	
	
	}