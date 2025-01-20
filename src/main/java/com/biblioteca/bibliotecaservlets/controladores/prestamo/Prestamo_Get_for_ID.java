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
import java.util.List;

@WebServlet(name = "prestamoGetForId", value = "/prestamo-get-for-id")
public class Prestamo_Get_for_ID extends HttpServlet {

        DAOPrestamo daoPrestamo;


    public void init(){

        daoPrestamo = new DAOPrestamo();

    }


    // Podemos elegir entre consultar todos los prestamos por los diferentes id's
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());
        String json_response;

        if(request.getParameter("tipo_id").isEmpty() || request.getParameter("id").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("Ambos campos deben estar completos"));

        } else {

            if (isDigit(request.getParameter("id"))) {

                Integer id = Integer.parseInt(request.getParameter("id"));
                String tipo_id = request.getParameter("tipo_id").toUpperCase();
                List <Prestamo> listaPrestamos;

                switch(tipo_id){

                    case "USUARIO":

                        listaPrestamos = daoPrestamo.getForIdUsuario(id);

                        if(listaPrestamos.size() < 1){

                            impresora.println(conversorJson.writeValueAsString("No hay prestamos para ese usuario"));

                        } else {

                            for (Prestamo p: listaPrestamos){

                                json_response = conversorJson.writeValueAsString(p);
                                impresora.println(json_response);
                            }

                        }

                        break;

                    case "EJEMPLAR":

                        listaPrestamos = daoPrestamo.getForIdEjemplar(id);

                        if(listaPrestamos.size() < 1){

                            impresora.println(conversorJson.writeValueAsString("No hay prestamos para ese ejemplar"));

                        } else {

                            for (Prestamo p: listaPrestamos){

                                json_response = conversorJson.writeValueAsString(p);
                                impresora.println(json_response);
                            }

                        }

                        break;

                    case "PRESTAMO":

                        Prestamo prestamo = daoPrestamo.getById(id);

                        if(prestamo == null){

                            impresora.println(conversorJson.writeValueAsString("No hay ningún préstamo con ese id"));

                        } else {

                            json_response = conversorJson.writeValueAsString(prestamo);
                            impresora.println(json_response);

                        }

                        break;

                    default:

                        impresora.println(conversorJson.writeValueAsString("El tipo no es válido"));

                        break;
                }

            } else {

                impresora.println(conversorJson.writeValueAsString("El id tiene que ser de tipo numérico"));
            }


        }

    }

    public boolean isDigit(String number){
        if(number.matches("^\\d+$")){
            return true;
        }
        return false;
    }

    public void destroy(){

    }
}
