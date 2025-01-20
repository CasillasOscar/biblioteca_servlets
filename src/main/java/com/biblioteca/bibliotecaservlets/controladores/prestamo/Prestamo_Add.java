package com.biblioteca.bibliotecaservlets.controladores.prestamo;

// RESERVA DE UN EJEMPLAR
import com.biblioteca.bibliotecaservlets.modelos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "prestamoAdd", value = "/prestamo-add")
public class Prestamo_Add extends HttpServlet {

        DAO_Generico daoPrestamo;
        DAO_LogInUser daoLogInUser;
        DAO_Generico daoEjemplar;

    public void init(){

        daoPrestamo = new DAO_Generico<>(Prestamo.class, Integer.class);
        daoLogInUser = new DAO_LogInUser();
        daoEjemplar = new DAO_Generico<>(Ejemplar.class, Integer.class);

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter impresora = response.getWriter();
        ObjectMapper conversorJson = new ObjectMapper();
        conversorJson.registerModule(new JavaTimeModule());

       //Comprobamos campos dni // id_ejemplar vacios
        if(request.getParameter("dni").isEmpty() || request.getParameter("id_ejemplar").isEmpty()){

            impresora.println(conversorJson.writeValueAsString("Tienen que enviarse tanto el dni del usuario como el del ejemplar que se lleva"));

        } else{

            // Si es correcto: confirmamos es digito confirmamos ejemplar existe
            if(isDigit(request.getParameter("id_ejemplar"))){

                Integer id_ejemplar = Integer.parseInt(request.getParameter("id_ejemplar"));
                String dni = request.getParameter("dni");

                Usuario usuario = new Usuario();
                usuario.setDni(dni);

                // Si es correcto: confirmamos usuario existe
                usuario = daoLogInUser.getByDNI2(usuario);

                if (usuario == null) {

                    impresora.println(conversorJson.writeValueAsString("No se ha encontrado un usuario con dicho DNI"));

                } else {

                    if(LocalDate.now().isAfter(usuario.getPenalizacionHasta())){

                        Ejemplar ejemplar = (Ejemplar) daoEjemplar.getById(id_ejemplar);

                        if (ejemplar == null) {

                            impresora.println(conversorJson.writeValueAsString("No se ha encontrado ejemplar con dicho DNI"));

                        } else {

                            if(ejemplar.getEstado().equalsIgnoreCase("Disponible")){

                                ejemplar.setEstado("Prestado");

                                // Si es correcto: hacemos add y cambiamos en Ejemplar el estado a Prestado
                                Prestamo prestamo = new Prestamo();
                                prestamo.setEjemplar(ejemplar);
                                prestamo.setEntregado("KO");
                                prestamo.setId(null);
                                prestamo.setUsuario(usuario);
                                prestamo.setFechaInicio(LocalDate.now());
                                prestamo.setFechaDevolucion(LocalDate.now().plusDays(15));

                                daoPrestamo.add(prestamo);
                                daoEjemplar.update(ejemplar);

                                String json_response = conversorJson.writeValueAsString(prestamo);
                                impresora.println(json_response);

                            } else{

                                impresora.println(conversorJson.writeValueAsString("El libro no esta disponible"));

                            }
                        }

                    } else {

                        impresora.println(conversorJson.writeValueAsString("El usuario esta penalizado hasta: " + usuario.getPenalizacionHasta()));

                    }
                }

            } else {

                impresora.println(conversorJson.writeValueAsString("El id del ejemplar o del usuario introducidos no son un numero"));

            }


        }

    }

    public boolean isDigit(String number){
        if(number.matches("^\\d+$")){
            return true;
        }
        return false;
    }

    public void destroy(){

    }
}
