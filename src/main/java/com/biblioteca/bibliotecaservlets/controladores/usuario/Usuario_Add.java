package com.biblioteca.bibliotecaservlets.controladores.usuario;


import com.biblioteca.bibliotecaservlets.modelos.DAO_LogInUser;
import com.biblioteca.bibliotecaservlets.modelos.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "usuarioAdd", value = "/usuario-add")
public class Usuario_Add extends HttpServlet {

        DAO_LogInUser daoLog;

    public void init(){

        daoLog = new DAO_LogInUser();

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

        //id - dni - nombre - email - pwd - tipo

        if(request.getParameter("dni").isEmpty() || request.getParameter("nombre").isEmpty() || request.getParameter("email").isEmpty() || request.getParameter("pwd").isEmpty() || request.getParameter("tipo").isEmpty()){

            impresora.println("Todos los campos deben estar rellenados");

        } else {

            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String pwd = request.getParameter("pwd");
            String tipo = request.getParameter("tipo");

            if(dni_valido(dni)){
                if(nombre_valido(nombre)){
                    if (email_valido(email)){
                        if(password_valido(pwd)){
                            if(tipo_valido(tipo)){

                                Usuario usuario_introducido = new Usuario();
                                Usuario usuario_bd = new Usuario();

                                usuario_introducido.setDni(dni);
                                usuario_introducido.setEmail(email);

                                usuario_bd = daoLog.getByDNI2(usuario_introducido);

                                if(usuario_bd == null) {

                                    usuario_bd = daoLog.getByEmail(usuario_introducido);

                                    if(usuario_bd == null) {

                                        usuario_introducido.setId(null);
                                        usuario_introducido.setNombre(nombre);
                                        usuario_introducido.setPassword(pwd);

                                        if(usuario_introducido.getTipo().equals("N")){
                                            usuario_introducido.setTipo("normal");
                                        } else {
                                            usuario_introducido.setTipo("administrador");
                                        }

                                        daoLog.add(usuario_introducido);

                                        String json_response = conversorJson.writeValueAsString(usuario_introducido);
                                        impresora.println(json_response);

                                    } else {

                                        impresora.println("Ya hay un usuario con ese email");

                                    }

                                } else {

                                    impresora.println("Ya hay un usuario con ese dni");

                                }

                            } else {

                                impresora.println("Tipo con formato incorrecto (A/N)");

                            }

                        } else {

                            impresora.println("Password con formato incorrecto (De 8 a 20 caracteres con mayusculas, minusculas y numeros)");

                        }

                    } else {

                        impresora.println("Email con formato incorrecto");

                    }

                } else {

                    impresora.println("Nombre con formato incorrecto (Unicamente letras)");

                }

            } else {

                impresora.println("DNI con formato incorrecto");

            }

        }

    }

    public static boolean nombre_valido(String nombre){
        return nombre.matches("[a-zA-Z]+");
    }

    public static boolean email_valido(String email){
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }

    public static boolean dni_valido(String dni){
        return dni.matches("^\\d{8}[TRWAGMYFPDXBNJZSQVHLCKE]$");
    }

    public static boolean password_valido(String password){
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$");
    }

    public static boolean tipo_valido(String tipo){
        return tipo.matches("^[AN]$");
    }

    public void destroy(){

    }
}
