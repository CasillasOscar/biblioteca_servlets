package com.biblioteca.bibliotecaservlets.controladores.libro;


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

@WebServlet(name = "librosAdd", value = "/add-libro")
public class Add_LibroSv extends HttpServlet {

        DAOLibros daoLibro;

    public void init(){

        daoLibro = new DAOLibros();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        //Se comprueba que todos los input esten rellenados
        if(request.getParameter("titulo").isEmpty() || request.getParameter("isbn").isEmpty() || request.getParameter("autor").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("No se puede crear un libro con alguno de los campos vacíos"));

        }else {

            String titulo = request.getParameter("titulo");
            String isbn = request.getParameter("isbn");
            String autor = request.getParameter("autor");


            Libro libro = new Libro();

            libro.setTitulo(titulo);
            libro.setIsbn(isbn);
            libro.setAutor(autor);

            Libro libro2 = daoLibro.getByISBN(libro.getIsbn());

            if(libro2 == null){

                daoLibro.add(libro);

                System.out.println("En java" + libro);

                String json_response = conversorJson.writeValueAsString(libro);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);

            } else {

                impresora.println(conversorJson.writeValueAsString("El isbn ya existe"));
            }


        }

    }
    

    public void destroy(){

    }
}
