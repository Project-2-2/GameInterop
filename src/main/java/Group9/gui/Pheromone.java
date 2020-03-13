package Group9.gui;

import javafx.scene.shape.Polygon; 
         
public class Pheromone extends Polygon implements GameObject{ 
	
	final double x1;
	final double y1;
	final double x2;
	final double y2;
	final double x3;
	final double x4;
	final double x5;
	final double y3;
	final double y4;
	final double y5;
	
	public Pheromone(double x1,double y1,double x2, double y2, double x3, double y3, double x4,double y4, double x5,double y5)
	{
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.x3 = x3;
		this.x4 = x4;
		this.x5 = x5;
		this.y3 = y3;
		this.y4 = y4;
		this.y5 = y5;
		getPoints().addAll(x1,y1,x2,y2,x3,y3,x4,y4,x5,y5);
	}


	@Override
	public void updateScale() {
		// TODO Auto-generated method stub
		
	} 
} 