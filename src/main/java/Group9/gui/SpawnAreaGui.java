package Group9.gui;

import javafx.scene.paint.Color;

public  class SpawnAreaGui {

	public SpawnAreaGui()
	{
		
	}
	
	public static class SpawnAreaGuardGui extends InternalWallGui{

		public SpawnAreaGuardGui(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
				double y4) {
			super(x1, y1, x2, y2, x3, y3, x4, y4);
			setFill(Color.LINEN);
			setStroke(Color.BLACK);
		}
		
	}
	public static class SpawnAreaIntruderGui extends InternalWallGui
	{

		public SpawnAreaIntruderGui(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
				double y4) {
			super(x1, y1, x2, y2, x3, y3, x4, y4);
			setFill(Color.BISQUE);
			setStroke(Color.BLACK);
			// TODO Auto-generated constructor stub
		}
		
	}
	
}