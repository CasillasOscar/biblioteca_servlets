package com.biblioteca.bibliotecaservlets.controladores.prestamo;

// DEVOLUCION DE UN EJEMPLAR
import com.biblioteca.bibliotecaservlets.modelos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "prestamoGet", value = "/prestamo-get")
public class Prestamo_Get extends HttpServlet {

        DAOPrestamo daoPrestamo;


    public void init(){

        daoPrestamo = new DAOPrestamo();

    }

    //Get de todos los prestamos sin condiciones
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());
        String json_response;

        List <Prestamo> listaPrestamos = daoPrestamo.getAll();

        if(listaPrestamos.size() < 1){

            impresora.println(conversorJson.writeValueAsString("No hay prestamos"));

        } else {

            for (Prestamo p: listaPrestamos){

                json_response = conversorJson.writeValueAsString(p);
                impresora.println(json_response);
            }

        }

    }

    // Podemos elegir entre consultar todos los prestamos OK y los KO
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());
        String json_response;

        if(request.getParameter("tipo").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("No ha introducido que tipo de prestamos quiere que se muestren"));

        } else {

            List <Prestamo> listaPrestamos;
            String tipo = request.getParameter("tipo").toUpperCase();

            switch (tipo){
                case "OK":

                    listaPrestamos = daoPrestamo.getAllOK();

                    if(listaPrestamos.size() < 1){

                        impresora.println(conversorJson.writeValueAsString("No hay prestamos OK"));

                    } else {

                        for (Prestamo p: listaPrestamos){

                            json_response = conversorJson.writeValueAsString(p);
                            impresora.println(json_response);
                        }

                    }

                    break;

                case "KO":

                    listaPrestamos = daoPrestamo.getAllKO();

                    if(listaPrestamos.size() < 1){

                        impresora.println(conversorJson.writeValueAsString("No hay prestamos KO"));

                    } else {

                        for (Prestamo p: listaPrestamos){

                            json_response = conversorJson.writeValueAsString(p);
                            impresora.println(json_response);
                        }

                    }

                    break;

                default:
                    impresora.println(conversorJson.writeValueAsString("No ha introducido un tipo valido"));
                    break;
            }

        }

    }

    public void destroy(){

    }
}
