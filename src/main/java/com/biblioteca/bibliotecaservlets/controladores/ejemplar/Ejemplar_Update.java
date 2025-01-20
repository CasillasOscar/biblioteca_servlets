package com.biblioteca.bibliotecaservlets.controladores.ejemplar;


import com.biblioteca.bibliotecaservlets.modelos.DAO_Generico;
import com.biblioteca.bibliotecaservlets.modelos.Ejemplar;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ejemplarUpdate", value = "/ejemplar-update")
public class Ejemplar_Update extends HttpServlet {

        DAO_Generico daoEjemplar;

    public void init(){

        daoEjemplar = new DAO_Generico<>(Ejemplar.class, Integer.class);
    }



    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        //Introducimos un id, miramos si esta el campo empty
        if(request.getParameter("id").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("No ha introducido el id"));

        } else{

            try{

                Integer id = Integer.parseInt(request.getParameter("id"));
                //Si esta correcto: buscamos ejemplar por id
                Ejemplar ejemplar = (Ejemplar) daoEjemplar.getById(id);

                if(ejemplar == null){

                    impresora.println(conversorJson.writeValueAsString("El id no corresponde a ningún ejemplar existente"));

                } else {

                    if(request.getParameter("estado").isEmpty()){

                        impresora.println(conversorJson.writeValueAsString("El estado introducido esta vacío"));

                    } else {

                        //Si es correcto: comprobamos que sea Disponible, Dañado o Prestado
                        if(request.getParameter("estado").equalsIgnoreCase("Disponible") ||
                                request.getParameter("estado").equalsIgnoreCase("Dañado") ||
                                request.getParameter("estado").equalsIgnoreCase("Prestado")){

                            //Si es correcto hacemos update y devolvemos json
                            String estado = request.getParameter("estado");
                            ejemplar.setEstado(estado);
                            ejemplar = (Ejemplar) daoEjemplar.update(ejemplar);

                            String json_response = conversorJson.writeValueAsString(ejemplar);
                            impresora.println(json_response);

                        } else {
                            impresora.println(conversorJson.writeValueAsString("El estado introducido no es válido"));
                        }

                    }
                }

            } catch (java.lang.NumberFormatException e) {
                impresora.println(conversorJson.writeValueAsString("El id debe ser un numero entero"));
            }
        }
    }


    public void destroy(){

    }
}
