/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker;

import java.util.concurrent.BlockingQueue;
import javax.servlet.http.HttpServletRequest;
import randomfields.BesselField;
import randomfields.ExpField;
import randomfields.Field;
import randomfields.SinField;
import randomfields.WhiteNoiseField;

/**
 *
 * @author Vlad
 */
public class QueueFiller implements Runnable {

    BlockingQueue<Field> fieldQueue;
    private final HttpServletRequest request;

    public QueueFiller(BlockingQueue<Field> queue, HttpServletRequest request) {
        this.fieldQueue = queue;
        this.request = request;
    }

    public Field makeSpecifiedField() {
        int size = Integer.valueOf(request.getParameter("size"));
        double paramP = 0;
        int paramM = 0, paramN = 0;
        if (request.getParameter("paramP") != null) {
            paramP = Double.valueOf(request.getParameter("paramP"));
        }
        if (request.getParameter("paramM") != null) {
            paramM = Integer.valueOf(request.getParameter("paramM"));
        }
        if (request.getParameter("paramN") != null) {
            paramN = Integer.valueOf(request.getParameter("paramN"));
        }
        switch (request.getParameter("corType")) {
            case "0":
                return new BesselField(size, size, paramP, paramM);
            case "1":
                return new ExpField(size, size, paramP, paramM, paramN);
            case "2":
                return new SinField(size, size, paramP, paramM, paramN);
            case "3":
                return new WhiteNoiseField(size, size);
            default:
                return null;
        }

    }

    @Override
    public void run() {
        int quantity = Integer.valueOf(request.getParameter("quantity"));
        for (int i = 0; i < quantity; i++) {
            System.out.println("run " + i);
            try {
                Field field = makeSpecifiedField();
                field.build();
                fieldQueue.put(field);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
