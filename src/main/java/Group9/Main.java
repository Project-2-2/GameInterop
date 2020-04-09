package Group9;

import Group9.math.Vector2;
import Group9.tree.PointContainer;


public class Main {

    public static void main(String[] args) {

        //Line{start=Vector2{x=10.402091197212645, y=109.61228558829279, length=110.10475309070263}, end=Vector2{x=12.698191791403183, y=115.15556278336051, length=115.853561510744}}
        //Vector2{x=11.391114889615475, y=112.0, length=112.57778421353132}

        /*
        0 = {PointContainer$Line@1789} "Line{start=Vector2{x=10.0, y=108.0, length=108.46197490364999}, end=Vector2{x=10.0, y=112.0, length=112.44554237496477}}"
        1 = {PointContainer$Line@1790} "Line{start=Vector2{x=10.0, y=112.0, length=112.44554237496477}, end=Vector2{x=12.0, y=112.0, length=112.64102272262978}}"
        2 = {PointContainer$Line@1791} "Line{start=Vector2{x=12.0, y=112.0, length=112.64102272262978}, end=Vector2{x=12.0, y=108.0, length=108.664621657649}}"
        3 = {PointContainer$Line@1792} "Line{start=Vector2{x=10.0, y=108.0, length=108.46197490364999}, end=Vector2{x=12.0, y=108.0, length=108.664621657649}}"
         */

        PointContainer.Line line = new PointContainer.Line(
                new Vector2(10.402091197212645, 109.61228558829279),
                new Vector2(12.698191791403183, 115.15556278336051)
        );
        PointContainer.Polygon polygon = new PointContainer.Polygon(
                new Vector2(10, 108), new Vector2(10, 112), new Vector2(12, 112), new Vector2(10, 108)
        );


        System.out.println(PointContainer.intersectionPoints(polygon, line));

    }


}
