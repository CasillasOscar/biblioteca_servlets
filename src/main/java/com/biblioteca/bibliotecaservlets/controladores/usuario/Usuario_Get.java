package com.biblioteca.bibliotecaservlets.controladores.usuario;


import com.biblioteca.bibliotecaservlets.modelos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "usuarioGet", value = "/usuario-get")
public class Usuario_Get extends HttpServlet {

        DAO_LogInUser daoLog;

    public void init(){

        daoLog = new DAO_LogInUser();

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        if(request.getParameter("dni").isEmpty() || request.getParameter("pwd").isEmpty()){

            impresora.println("Tiene que introducir el dni y password!");

        } else {

            String dni = request.getParameter("dni");
            String pwd = request.getParameter("pwd");

            if(dni_valido(dni)){

                Usuario usuario = new Usuario();
                usuario.setDni(dni);

                usuario = daoLog.getByDNI2(usuario);

                if(usuario == null){

                    impresora.println("El usuario no existe");

                } else {

                    if(usuario.getPassword().equals(pwd)){

                        String json_response = conversorJson.writeValueAsString(usuario);
                        impresora.println(json_response);
                        //Iniciar variable session
                        HttpSession session = request.getSession(true);
                        session.setAttribute("usuario", usuario);


                    } else {

                        impresora.println("La contraseña no es correcta");

                    }

                }

            } else {
                impresora.println("El dni tiene que tener el siguiente formato 00000000X");
            }
        }

    }

    public static boolean dni_valido(String dni){
        return dni.matches("^\\d{8}[TRWAGMYFPDXBNJZSQVHLCKE]$");
    }

    public void destroy(){

    }
}
