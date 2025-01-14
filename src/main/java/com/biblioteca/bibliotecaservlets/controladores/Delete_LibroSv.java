package com.biblioteca.bibliotecaservlets.controladores;


import com.biblioteca.bibliotecaservlets.modelos.DAOLibros;
import com.biblioteca.bibliotecaservlets.modelos.Libro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "librosDelete", value = "/delete-libro")
public class Delete_LibroSv extends HttpServlet {

    DAOLibros daoLibro;

    public void init() {

        daoLibro = new DAOLibros();
    }

// Se necesita el titulo o isbn para borrar un libro

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        //Si tanto isbn como titulo esta vacio se manda aviso
        if (request.getParameter("isbn").isEmpty() && request.getParameter("titulo").isEmpty()) {

            impresora.println(conversorJson.writeValueAsString("No se puede borrar un libro sin el isbn o titulo"));

        } else{

            //Si isbn esta vacio se busca el libro por titulo
            if(request.getParameter("isbn").isEmpty()){

                String titulo = request.getParameter("titulo");

                Libro libro = daoLibro.getByTitulo(titulo);

                //Se comprueba si se encuentra el libro por titulo
                if(libro == null){

                    impresora.println(conversorJson.writeValueAsString("No se ha encontrado un libro con dicho titulo"));

                } else {

                    System.out.println("En java" + libro);

                    String json_response = conversorJson.writeValueAsString(libro);
                    impresora.println(json_response);

                    daoLibro.deleteUsuario(libro);

                }

                // Si el isbn esta relleno se busca por este mismo
            } else {

                String isbn = request.getParameter("isbn");
                Libro libro = daoLibro.getByISBN(isbn);

                //Se comprueba si se encuentra el libro por isbn
                if(libro == null){

                    impresora.println(conversorJson.writeValueAsString("No se ha encontrado un libro con dicho isbn"));

                } else {

                    System.out.println("En java" + libro);

                    String json_response = conversorJson.writeValueAsString(libro);
                    impresora.println(json_response);

                    daoLibro.deleteUsuario(libro);
                }
            }
        }
    }
    

    public void destroy(){

    }
}
