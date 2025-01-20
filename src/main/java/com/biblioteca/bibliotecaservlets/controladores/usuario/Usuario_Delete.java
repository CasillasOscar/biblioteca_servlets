package com.biblioteca.bibliotecaservlets.controladores.usuario;


import com.biblioteca.bibliotecaservlets.modelos.DAO_LogInUser;
import com.biblioteca.bibliotecaservlets.modelos.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "usuarioDelete", value = "/usuario-delete")
public class Usuario_Delete extends HttpServlet {

        DAO_LogInUser daoLog;

    public void init(){

        daoLog = new DAO_LogInUser();

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();


        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

       Usuario usuario = (Usuario) session.getAttribute("usuario");

       if(request.getParameter("eliminar").equalsIgnoreCase("yes")){

           daoLog.delete(usuario);
           impresora.println("Usuario eliminado");
       }

    }


    public void destroy(){

    }
}
