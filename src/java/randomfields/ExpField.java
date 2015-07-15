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
public class ExpField extends BesselSuperpositionField {

    public ExpField() {
        super();
    }

    public ExpField(int width, int height) {
        super(width, height);
    }

    public ExpField(int width, int height, double ro) {
        super(width, height, ro);
    }

    public ExpField(int width, int height, double ro, int paramN, int paramM) {
        super(width, height, ro, paramN, paramM);
    }

    @Override
    public double[] calculateMu(double[] gammaUniform) {
        int len = gammaUniform.length;
        double[] mu = new double[len];
        for (int i = 0; i < len; i++) {
            mu[i] = ro * Math.sqrt(1 / gammaUniform[i] - 1);
        }
        return mu;
    }
}
