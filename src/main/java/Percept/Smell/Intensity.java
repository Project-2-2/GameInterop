package Percept.Smell;

import Utils.Require;


/**
 * Represents intensity of a smell.
 *
 * Intensity of a smell is linearly dependent on the distance and the age of smell.
 */
public class Intensity {

    private double intensity;

    public Intensity(double intensity) {
        Require.realNumber(intensity);
        Require.notNegative(intensity);
        this.intensity = intensity;
    }

    public double getValue() {
        return intensity;
    }

}
