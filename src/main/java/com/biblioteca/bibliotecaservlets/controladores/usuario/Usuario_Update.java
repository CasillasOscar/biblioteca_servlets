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

@WebServlet(name = "usuarioUpdate", value = "/usuario-update")
public class Usuario_Update extends HttpServlet {

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

       String campo = request.getParameter("campo");

       switch (campo){
           case "nombre":
               if(request.getParameter("nombre").isEmpty()){

                   impresora.println("El campo nombre debe estar completo");

               } else {

                    String nombre = request.getParameter("nombre");
                    if(nombre_valido(nombre)){

                        //Cogeriamos el nombre de la variable de session activa
                        Usuario usuario = (Usuario)session.getAttribute("usuario");
                        usuario.setNombre(nombre);

                        daoLog.add(usuario);
                        session.setAttribute("usuario", usuario);

                        String json_response = conversorJson.writeValueAsString(usuario);
                        impresora.println(json_response);

                    } else {
                        impresora.println("El campo nombre no es válido");
                    }
               }
               break;

           case "email":
               if(request.getParameter("email").isEmpty()){

                   impresora.println("El campo email debe estar completo");

               } else {

                   String email = request.getParameter("email");
                   if(email_valido(email)){

                       //Cogeriamos el nombre de la variable de session activa
                       Usuario usuario = (Usuario)session.getAttribute("usuario");

                       Usuario usuario_validado = new Usuario();
                       usuario_validado.setEmail(email);

                       usuario_validado = daoLog.getByEmail(usuario_validado);

                       if(usuario_validado == null){

                           usuario.setEmail(email);

                           daoLog.add(usuario);
                           session.setAttribute("usuario", usuario);

                           String json_response = conversorJson.writeValueAsString(usuario);
                           impresora.println(json_response);

                       } else{

                           impresora.println("El email ya existe");

                       }

                   } else {
                       impresora.println("El campo nombre no es válido");
                   }

               }
               break;

           case "password":
               if(request.getParameter("password").isEmpty()){

                   impresora.println("El campo password debe estar completo");

               } else {

                   String pwd = request.getParameter("pwd");
                   if(password_valido(pwd)){

                       //Cogeriamos el nombre de la variable de session activa
                       Usuario usuario = (Usuario)session.getAttribute("usuario");
                       usuario.setPassword(pwd);

                       daoLog.add(usuario);
                       session.setAttribute("usuario", usuario);

                       String json_response = conversorJson.writeValueAsString(usuario);
                       impresora.println(json_response);

                   } else {
                       impresora.println("El campo password no es válido");
                   }

               }
               break;

           default:

               impresora.println("No es un campo válido a modificar");

               break;

       }

    }

    public static boolean nombre_valido(String nombre){
        return nombre.matches("[a-zA-Z]+");
    }

    public static boolean email_valido(String email){
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }

    public static boolean password_valido(String password){
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$");
    }

    public void destroy(){

    }
}
