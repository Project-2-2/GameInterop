package Group9.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Objects  {
	public class Door extends InternalWall{

		public Door(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
			super(x1, y1, x2, y2, x3, y3, x4, y4);
			setFill(Color.DARKGREEN);
		}
		
		public class Windows extends InternalWall{

			public Windows(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
				super(x1, y1, x2, y2, x3, y3, x4, y4);
				setFill(Color.DARKBLUE);
			}
			
		}
		public class ShadedArea extends InternalWall{

			public ShadedArea(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
				super(x1, y1, x2, y2, x3, y3, x4, y4);
				setFill(Color.BLACK);
			}
			
		}
		public class Sentry extends InternalWall{

			public Sentry(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
				super(x1, y1, x2, y2, x3, y3, x4, y4);
				setFill(Color.AQUA);
			}
			
		}
		
		public class targetArea extends InternalWall{

			public targetArea(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
				super(x1, y1, x2, y2, x3, y3, x4, y4);
				setFill(Color.DARKCYAN);
			}
		
		}	
	}
}
