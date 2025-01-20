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

@WebServlet(name = "ejemplarAdd", value = "/ejemplar-add")
public class Ejemplar_Add extends HttpServlet {

        DAO_Generico daoEjemplar;
        DAOLibros daoLibro;

    public void init(){

        daoEjemplar = new DAO_Generico<>(Ejemplar.class, Integer.class);
        daoLibro = new DAOLibros();
    }

    //Añadir un ejemplar
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        // Comprobamos isbn empty
        if (request.getParameter("isbn").isEmpty()) {

            impresora.println(conversorJson.writeValueAsString("No se ha introducido el isbn"));

        } else{

            // Comprobamos que el isbn exista
            String isbn = request.getParameter("isbn");
            Libro libro = daoLibro.getByISBN(isbn);

            if(libro == null){

                impresora.println(conversorJson.writeValueAsString("No se ha encontrado ningún libro con dicho isbn, hay que crear antes el libro"));

            } else {

                //Creamos el ejemplar y lo añadimos
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setId(null);
                ejemplar.setIsbn(libro);
                ejemplar.setEstado("Disponible");

                daoEjemplar.add(ejemplar);

                System.out.println(ejemplar); //Java

                String json_response = conversorJson.writeValueAsString(ejemplar);
                impresora.println(json_response);

            }


        }
    }


    public void destroy(){

    }
}
