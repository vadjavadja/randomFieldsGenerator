/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomfields;

import java.util.Arrays;

/**
 *
 * @author Vlad
 */
public class BesselField extends Field {

    private int paramM;
    private double ro;

    public BesselField(int width, int height, double ro, int paramM) {
        super(width, height);
        this.ro = ro;
        this.paramM = paramM;
    }

    public BesselField(int width, int height, double ro) {
        this(width, height, ro, 20);
    }

    public BesselField(int width, int height) {
        this(width, height, 0.5, 20);
    }

    public BesselField() {
        this(300, 300, 0.5, 20);
    }

    @Override
    public void build() {
        long t = System.currentTimeMillis();
        System.out.format("%.2fs Starting...%n", (System.currentTimeMillis() - t) / 1000d);

        double[] alphaUniform = buildArrayUniform(paramM);
        double[] betaUniform = buildArrayUniform(paramM);
        double[] alphaPrime = buildArrayUniform(paramM);
        System.out.format("%.2fs all uniforms done...%n", (System.currentTimeMillis() - t) / 1000d);

        double[] omega = new double[paramM];
        double PiOverM = Math.PI / paramM;
        for (int i = 0; i < omega.length; i++) {
            omega[i] = PiOverM * (i - alphaPrime[i]);
        }

        double[] sqrtLogA = new double[paramM];
        double[] roCosW = new double[paramM];
        double[] roSinW = new double[paramM];
        double[] TwoPiB = new double[paramM];
        for (int i = 0; i < paramM; i++) {
            sqrtLogA[i] = Math.sqrt((-2) * Math.log(alphaUniform[i]));
            roCosW[i] = ro * Math.cos(omega[i]);
            roSinW[i] = ro * Math.sin(omega[i]);
            TwoPiB[i] = 2 * Math.PI * betaUniform[i];
        }
        double oneOverSqrtM = 1 / Math.sqrt(paramM);

        System.out.format("%.2fs all initialized...%n", (System.currentTimeMillis() - t) / 1000d);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double curCellValue = 0;
                for (int m = 0; m < paramM; m++) {
                    curCellValue += sqrtLogA[m] * Math.cos(i * roCosW[m] + j * roSinW[m] + TwoPiB[m]);

                }
                realField[i][j] = oneOverSqrtM * curCellValue;
            }
        }

        System.out.format("%.2fs field built...%n", (System.currentTimeMillis() - t) / 1000d);

        normalize(realField);

        System.out.format("%.2fs and normalized%nwe are done.%n", (System.currentTimeMillis() - t) / 1000d);
    }

}
