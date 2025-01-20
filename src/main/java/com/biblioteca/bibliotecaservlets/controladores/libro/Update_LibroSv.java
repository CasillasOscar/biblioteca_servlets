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

@WebServlet(name = "librosUpdate", value = "/update-libro")
public class Update_LibroSv extends HttpServlet {

    DAOLibros daoLibro;

    public void init(){

        daoLibro = new DAOLibros();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        // Sin el isbn no se puede actualizar
        if(request.getParameter("isbn").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("No ha introducido el isbn del libro a actualizar"));

        }else{

            // Si los campos que se pueden actualizar no se rellenan se avisa
            if(request.getParameter("titulo").isEmpty() && request.getParameter("autor").isEmpty()){

                impresora.println(conversorJson.writeValueAsString("No se ha introducido ningun campo a actualizar"));

            }else {

                //Si autor esta vacio solo se actualiza el titulo
                if(request.getParameter("autor").isEmpty()){

                    String isbn = request.getParameter("isbn");
                    String titulo = request.getParameter("titulo");

                    Libro libro = daoLibro.getByISBN(isbn);

                    if(libro == null){

                        impresora.println(conversorJson.writeValueAsString("No se ha encontrado el isbn"));

                    } else {

                        libro.setTitulo(titulo);

                        daoLibro.update(libro);

                        System.out.println("En java" + libro);

                        String json_response = conversorJson.writeValueAsString(libro);
                        System.out.println("En java json" + json_response);
                        impresora.println(json_response);

                    }

                    //Si titulo esta vacio solo se actualiza el autor
                } else if(request.getParameter("titulo").isEmpty()){

                    String isbn = request.getParameter("isbn");
                    String autor = request.getParameter("autor");

                    Libro libro = daoLibro.getByISBN(isbn);

                    if(libro == null){

                        impresora.println(conversorJson.writeValueAsString("No se ha encontrado el isbn"));

                    } else {

                        libro.setAutor(autor);

                        daoLibro.update(libro);

                        System.out.println("En java" + libro);

                        String json_response = conversorJson.writeValueAsString(libro);
                        System.out.println("En java json" + json_response);
                        impresora.println(json_response);

                    }

                    // Se actualizan tanto titulo como autor
                } else {

                    String isbn = request.getParameter("isbn");
                    String autor = request.getParameter("autor");
                    String titulo = request.getParameter("titulo");


                    Libro libro = daoLibro.getByISBN(isbn);

                    if (libro == null){

                        impresora.println(conversorJson.writeValueAsString("No se ha encontrado el isbn"));

                    } else {

                        libro.setAutor(autor);
                        libro.setTitulo(titulo);

                        daoLibro.update(libro);

                        System.out.println("En java" + libro);

                        String json_response = conversorJson.writeValueAsString(libro);
                        System.out.println("En java json" + json_response);
                        impresora.println(json_response);

                    }
                }
            }
        }
    }


    public void destroy(){

    }
}
