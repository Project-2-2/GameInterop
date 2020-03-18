package Group9.gui;

public class Calculations {
    public static double highest(double[] numbers)
    {
        double highest = Double.NEGATIVE_INFINITY;
        for(int i=0; i<numbers.length; i++)
        {
            if(numbers[i] > highest)
            {
                highest = numbers[i];
            }
        }
        return highest;
    }
    public static double higest(double n1, double n2, double n3, double n4)
    {
        double[] numbers = {n1, n2, n3, n4};
        return highest(numbers);
    }
    public static double lowest(double[] numbers)
    {
        double lowest = Double.POSITIVE_INFINITY;
        for(int i=0; i<numbers.length; i++)
        {
            if(lowest > numbers[i])
            {
                lowest = numbers[i];
            }
        }
        return lowest;
    }
    public static double lowest(double n1, double n2, double n3, double n4)
    {
        double[] numbers = {n1, n2, n3, n4};
        return lowest(numbers);
    }
}
