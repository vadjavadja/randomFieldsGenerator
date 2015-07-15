/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomfields;

import java.util.Random;

/**
 *
 * @author Vlad
 */
public class WhiteNoiseField extends Field {

    public WhiteNoiseField() {
        super();
    }

    public WhiteNoiseField(int width, int height) {
        super(width, height);
    }

    @Override
    public void build() {
        Random random = new Random();
        for (int i = 0; i < realField.length; i++) {
            for (int j = 0; j < realField[0].length; j++) {
                realField[i][j] = random.nextGaussian();
            }
        }
    }

}
