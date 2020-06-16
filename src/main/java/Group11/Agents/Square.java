package Group11.Agents;

import Group11.Elements.EnvironmentElement;
import Group11.Elements.EnvironmentType;

/***

 * this class defines the information about each square

 */


public class Square {

    private int[] coord;
    private double val;
    private EnvironmentElement element;

    public Square(int[] coord, double val, EnvironmentElement element){
        this.coord=coord;
        this.val=val;
        this.element= element;
    }

    public void setValue(double value){
        this.val=value;
    }

    public double getValue(){
        return this.val;
    }

    public EnvironmentElement getElement() {
        return element;
    }

    public void setElement(EnvironmentElement type) {
        this.element = type;
    }

    public int[] getCoord() {
        return coord;
    }
}
