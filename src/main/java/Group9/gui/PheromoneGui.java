package Group9.gui;

import Interop.Percept.Smell.SmellPerceptType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PheromoneGui extends Circle implements GameObject{

	//star representation.
	final double x;
	final double y;

	final double radiusPheromone;

	public PheromoneGui(double x, double y, double radius, SmellPerceptType color)
	{
		super();
		this.x = x;
		this.y = y;
		this.radiusPheromone = radius;
		this.setCenterX(x);
		this.setCenterY(y);
		if(AgentGui.guard)
		{setStroke(Color.BLUE);}
		else {setStroke(Color.RED);}
		switch (color)
		{
			case Pheromone1:
			{
				setFill(Color.rgb(255, 255, 0, 0.2));
				break;
			}
			case Pheromone2:
			{
				setFill(Color.PINK);
				break;
			}
			case Pheromone3:
			{
				setFill(Color.LIGHTBLUE);
				break;
			}
			case Pheromone4:
			{
				setFill(Color.GRAY);
				break;
			}
			case Pheromone5:
			{
				setFill(Color.GREEN);
				break;
			}
			default:
			{
				setFill(Color.BLACK);
				break;
			}
		}
		updateScale();

	}


	@Override
	public void updateScale() {
		double scale = Scale.scale;
		setCenterY(y*scale);
		setCenterX(x*scale);
		setRadius(radiusPheromone*scale);

	}
}
