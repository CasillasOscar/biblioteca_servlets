package com.biblioteca.bibliotecaservlets.controladores.ejemplar;


import com.biblioteca.bibliotecaservlets.modelos.DAOLibros;
import com.biblioteca.bibliotecaservlets.modelos.DAO_Generico;
import com.biblioteca.bibliotecaservlets.modelos.Ejemplar;
import com.biblioteca.bibliotecaservlets.modelos.Libro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ejemplarDelete", value = "/ejemplar-delete")
public class Ejemplar_Delete extends HttpServlet {

        DAO_Generico daoEjemplar;


    public void init(){

        daoEjemplar = new DAO_Generico<>(Ejemplar.class, Integer.class);
    }

    //Añadir un ejemplar
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        // Comprobamos isbn empty
        if (request.getParameter("id").isEmpty()) {

            impresora.println(conversorJson.writeValueAsString("No se ha introducido el ejemplar con ese id"));

        } else{

            try{

                Integer id = Integer.parseInt(request.getParameter("id"));

                Ejemplar ejemplar = (Ejemplar) daoEjemplar.getById(id);

                if(ejemplar == null){

                    impresora.println(conversorJson.writeValueAsString("No se ha encontrado ningún ejemplar con dicho id"));

                } else {

                    //Borramos el ejemplar

                    System.out.println(ejemplar); //Java

                    String json_response = conversorJson.writeValueAsString(ejemplar);
                    impresora.println(json_response);

                    daoEjemplar.delete(ejemplar);

                }



            } catch (java.lang.NumberFormatException e) {

            impresora.println(conversorJson.writeValueAsString("El id debe ser un numero entero"));

        }

        }
    }


    public void destroy(){

    }
}
