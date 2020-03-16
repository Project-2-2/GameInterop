package Group9.gui;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Agent extends Circle implements GameObject
{
	final double x;
	final double y;
	final double r;
	final static double speed = 1.4;//for both guard and intruder
	final static double sprint = 4;//maxSprintDistanceIntruder 
	final Duration Timestep = Duration.seconds(0.1);
	static boolean guard = false;

	
	public Agent(double x,double y,double r, boolean guard)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		setCenterX(x);
		setCenterY(y);
		setRadius(r);
		if(guard == true) {setFill(Color.RED);setStroke(Color.BLACK);}
		else {setFill(Color.BLUE);}
		
		
	}
	
	
	@Override
	public void updateScale() {
		// TODO Auto-generated method stub
		double scale = Scale.scale;
       setCenterX(x*scale);
       setCenterY(y*scale);
       setRadius(r*scale);
       
	}
	
	public static void move(Scene scene, Agent x) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				if(event.getCode()== KeyCode.UP)
				{x.setCenterY(x.getCenterY()-speed);}
				else if(event.getCode()== KeyCode.DOWN)
				{x.setCenterY(x.getCenterY()+speed);}
				else if(event.getCode()== KeyCode.RIGHT)
				{x.setCenterX(x.getCenterX()+speed);}
				else if(event.getCode()== KeyCode.LEFT)
				{x.setCenterX(x.getCenterX()-speed);}
				
			}
			
		});
		
		
	}
	
	
	}