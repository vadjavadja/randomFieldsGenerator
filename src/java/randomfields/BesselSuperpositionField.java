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
public abstract class BesselSuperpositionField extends Field{
    int paramN, paramM;
    double ro;

    public BesselSuperpositionField() {
        this(300, 300, 0.5, 20, 20);
    }
    
    public BesselSuperpositionField(int width, int height) {
        this(width, height, 0.5, 20, 20);
    }

    public BesselSuperpositionField(int width, int height, double ro) {
        this(width, height, ro, 20, 20);
    }

    public BesselSuperpositionField(int width, int height, double ro, int paramN, int paramM) {
        super(width, height);
        this.ro = ro;
        this.paramN = paramN;
        this.paramM = paramM;
    }

    @Override
    public void build() {
        long t = System.currentTimeMillis();
        System.out.format("%.2fs Starting...%n", (System.currentTimeMillis() - t) / 1000d);

        double[][] alphaUniform = buildArrayUniform(paramM, paramN);
        double[][] betaUniform = buildArrayUniform(paramM, paramN);
        double[] gammaUniform = buildArrayUniform(paramN);
        double[] alphaPrime = buildArrayUniform(paramM);
        System.out.format("%.2fs all uniforms done...%n", (System.currentTimeMillis() - t) / 1000d);

        double[] mu = calculateMu(gammaUniform);
        
        double[] omega = new double[paramM];
        double PiOverM = Math.PI / paramM;
        for (int i = 0; i < omega.length; i++) {
            omega[i] = PiOverM * (i - alphaPrime[i]);
        }

        double[][] sqrtLogA = new double[paramM][paramN];
        double[][] muCosW = new double[paramM][paramN];
        double[][] muSinW = new double[paramM][paramN];
        double[][] TwoPiB = new double[paramM][paramN];
        for (int i = 0; i < paramM; i++) {
            for (int j = 0; j < paramN; j++) {
                sqrtLogA[i][j] = Math.sqrt((-2) * Math.log(alphaUniform[i][j]));
                muCosW[i][j] = mu[i] * Math.cos(omega[j]);
                muSinW[i][j] = mu[i] * Math.sin(omega[j]);
                TwoPiB[i][j] = 2 * Math.PI * betaUniform[i][j];
            }
        }
        double oneOverSqrtM = 1 / Math.sqrt(paramM);

        System.out.format("%.2fs all initialized...%n", (System.currentTimeMillis() - t) / 1000d);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double curCellValue = 0;
                for (int n = 0; n < paramN; n++) {
                    for (int m = 0; m < paramM; m++) {
                        curCellValue += sqrtLogA[n][m] * Math.cos(i * muCosW[n][m] + j * muSinW[n][m] + TwoPiB[n][m]);
                    }
                }
                realField[i][j] = oneOverSqrtM * curCellValue;
            }
        }

        System.out.format("%.2fs field built...%n", (System.currentTimeMillis() - t) / 1000d);

        normalize(realField);

        System.out.format("%.2fs and normalized%nwe are done.%n", (System.currentTimeMillis() - t) / 1000d);
    }

    public abstract double[] calculateMu(double[] gammaUniform);
}
