package com.biblioteca.bibliotecaservlets.controladores;


import com.biblioteca.bibliotecaservlets.modelos.DAOLibros;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.biblioteca.bibliotecaservlets.modelos.DAO_Generico;
import com.biblioteca.bibliotecaservlets.modelos.Libro;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "librosServlet", value = "/libros-servlet")
public class LibrosServlet extends HttpServlet {

        DAOLibros daoLibro;

    public void init(){


        daoLibro = new DAOLibros();
    }

//Todos los libros
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());

            List<Libro> listaLibros  = daoLibro.getAll();
            System.out.println("En java" + listaLibros);

            String json_response = conversorJson.writeValueAsString(listaLibros);
            System.out.println("En java json" + json_response);
            impresora.println(json_response);

    }

    //Consultar libro por titulo o isbn
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());


        //Si tanto isbn como titulo esta vacio se manda aviso
        if (request.getParameter("isbn").isEmpty() && request.getParameter("titulo").isEmpty()) {

            impresora.println(conversorJson.writeValueAsString("No se puede consultar un libro sin el isbn o titulo"));

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

                }
            }
        }
    }


    public void destroy(){

    }
}
