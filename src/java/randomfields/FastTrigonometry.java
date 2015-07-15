/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomfields;

/**
 *
 * @author Vlad
 */
public class FastTrigonometry {

    private static double[] cos = new double[361];
    private static double[] sin = new double[361];

    static {
        for (int i = 0; i <= 360; i++) {
            cos[i] = Math.cos(Math.toRadians(i));
            sin[i] = Math.sin(Math.toRadians(i));
        }
    }

    public static double getCos(double angle) {
        angle = ((angle % Math.PI) + Math.PI) % Math.PI;
        int deg = (int) Math.round(Math.toDegrees(angle));
        double retVal = -1;
        
        try {
            retVal = cos[deg];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error, angle = "+angle+", deg = "+deg);
        }
        return retVal;
    }
}
