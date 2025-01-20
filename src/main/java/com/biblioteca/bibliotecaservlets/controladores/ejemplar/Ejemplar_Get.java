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
import java.util.List;

@WebServlet(name = "ejemplarGet", value = "/ejemplar-get")
public class Ejemplar_Get extends HttpServlet {

        DAO_Generico daoEjemplar;

    public void init(){

        daoEjemplar = new DAO_Generico<>(Ejemplar.class, Integer.class);
    }

//Todos los ejemplares de un libro
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());

            List<Ejemplar> listaEjemplares  = daoEjemplar.getAll();
            System.out.println("En java" + listaEjemplares);

            String json_response = conversorJson.writeValueAsString(listaEjemplares);
            System.out.println("En java json" + json_response);
            impresora.println(json_response);

    }

    //Consultar un ejemplar en especifico id
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());


        // Si el id esta vacio devuelve alerta
        if (request.getParameter("id").isEmpty()) {

            impresora.println(conversorJson.writeValueAsString("No se puede consultar un libro sin el id"));

        } else{

            try{

                Integer id = Integer.parseInt(request.getParameter("id"));

                Ejemplar ejemplar = (Ejemplar) daoEjemplar.getById(id);

                //Se comprueba si se encuentra el Ejemplar por id
                if(ejemplar == null){

                    impresora.println(conversorJson.writeValueAsString("No se ha encontrado un ejemplar con dicho id"));

                } else {

                    System.out.println("En java" + ejemplar);

                    String json_response = conversorJson.writeValueAsString(ejemplar);
                    impresora.println(json_response);

                }

            } catch (java.lang.NumberFormatException e) {

                impresora.println(conversorJson.writeValueAsString("El id debe ser un numero entero"));

            }

        }
    }


    public void destroy(){

    }
}
