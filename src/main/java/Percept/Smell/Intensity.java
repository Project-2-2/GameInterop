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
        Require.realNumber(intensity, "The smell intensity must be real number!");
        Require.notNegative(intensity, "The intensity can not be negative!");
        this.intensity = intensity;
    }

    public double getValue() {
        return intensity;
    }

}
