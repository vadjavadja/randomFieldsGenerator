/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import randomfields.Field;
import worker.QueueFiller;

/**
 *
 * @author Vlad
 */
public class GetNextImg extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
               
        BlockingQueue<Field> fieldQueue;
        if (session.getAttribute("queue") == null) {
            fieldQueue = new LinkedBlockingDeque<>();
            session.setAttribute("queue", fieldQueue);
            QueueFiller filler = new QueueFiller(fieldQueue,request);
            new Thread(filler).start();
        } else {
            fieldQueue = (BlockingQueue<Field>) session.getAttribute("queue");
        }
        if (session.getAttribute("counter") == null) {
            session.setAttribute("counter", 0);
        }

        int counter = (int) session.getAttribute("counter");

        try (PrintWriter out = response.getWriter()) {
//            String path = request.getContextPath() + "/pic.bmp";
            Field field = fieldQueue.take();
//            field.saveToFile(path);
//            out.format("%s%n", field);
            out.write(field.toJSON());
//            for (Field f : fieldQueue) {
//                out.format("h %d %n", f.getHeight());
//            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        session.setAttribute("counter", ++counter);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
