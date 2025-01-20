package com.biblioteca.bibliotecaservlets.controladores.prestamo;

// DEVOLUCION DE UN EJEMPLAR

import com.biblioteca.bibliotecaservlets.modelos.DAOPrestamo;
import com.biblioteca.bibliotecaservlets.modelos.Prestamo;
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

@WebServlet(name = "prestamoDelete", value = "/prestamo-delete")
public class Prestamo_Delete extends HttpServlet {

        DAOPrestamo daoPrestamo;


    public void init(){

        daoPrestamo = new DAOPrestamo();

    }

    // Borrar todos los de más de 30 días con entregado OK
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());
        String json_response;

        List<Prestamo> listaPrestamos = daoPrestamo.getAfter30days();

        if(listaPrestamos.size() < 1){

            impresora.println("No hay prestamos con fecha de devolucion anterior al: " + LocalDate.now().plusDays(-30));

        } else {

            for(Prestamo p: listaPrestamos){

                if(p.getEntregado().equalsIgnoreCase("OK")){

                    json_response = conversorJson.writeValueAsString(p);
                    impresora.println(json_response);

                    daoPrestamo.delete(p);
                }
            }
        }
    }


    public void destroy(){

    }
}
