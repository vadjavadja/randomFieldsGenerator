/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomfields;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Vlad
 */
public abstract class Field {

    final public int width, height;
    public double[][] realField;
    public byte[][] byteField;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);

        for (double[] row : realField) {
            for (double val : row) {
                formatter.format("%.2f ", val);
            }
            formatter.format("%n");
        }

        return sb.toString();
    }

    public String toJSON() {
        byte[][] byteField = doubleToByteArray(realField);
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("[");
        for (int i = 0; i < byteField.length; i++) {
            formatter.format("[");
            for (int j = 0; j < byteField[0].length; j++) {
                formatter.format("%d", byteField[i][j]+128);
                if (j < byteField[0].length - 2) {
                    formatter.format(",");
                }
            }
            formatter.format("]");
            if (i < byteField.length - 1) {
                formatter.format(",");
            }
            formatter.format("]");

        }
        return sb.toString();
    }

    public void saveToFile(String fileName) {
        byte[][] arr = doubleToByteArray(realField);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = arr[i][j] & 0xFF;
                int val = (grey << 16) | (grey << 8) | grey;
                img.setRGB(i, j, val);
            }
        }
        try {
            File imgFile = new File(fileName);
            ImageIO.write(img, "bmp", imgFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Field() {
        this(300, 300);
    }

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        realField = new double[height][width];
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    public abstract void build();

    public double[] buildArrayUniform(int length) {
        double[] arr = new double[length];
        fillArrayUniform(arr);
        return arr;
    }

    public double[][] buildArrayUniform(int rows, int cols) {
        double[][] arr = new double[rows][cols];
        for (double[] row : arr) {
            fillArrayUniform(row);
        }
        return arr;
    }

    public void fillArrayUniform(double[] arr) {
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextDouble();
        }
    }

    public void normalize(double[][] arr) {
        double maxValue = maxValue(arr);
        double minValue = minValue(arr);
        double peakToPeak = maxValue - minValue;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = (arr[i][j] - minValue) / peakToPeak;
            }
        }

    }

    public double maxValue(double[][] arr) {
        double maxValue = arr[0][0];
        for (double[] row : arr) {
            for (double value : row) {
                maxValue = Math.max(maxValue, value);
            }
        }
        return maxValue;
    }

    public double minValue(double[][] arr) {
        double minValue = arr[0][0];
        for (double[] row : arr) {
            for (double value : row) {
                minValue = Math.min(minValue, value);
            }
        }
        return minValue;
    }

    public byte[][] doubleToByteArray(double[][] arr) {
        byte intArr[][] = new byte[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                intArr[i][j] = (byte) (255 * arr[i][j]);
            }
        }
        return intArr;
    }
}
