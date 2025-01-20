package com.biblioteca.bibliotecaservlets.controladores.prestamo;

// DEVOLUCION DE UN EJEMPLAR
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

@WebServlet(name = "prestamoUpdate", value = "/prestamo-update")
public class Prestamo_Update extends HttpServlet {

        DAOPrestamo daoPrestamo;
        DAO_LogInUser daoLogInUser;
        DAO_Generico daoEjemplar;

    public void init(){

        daoPrestamo = new DAOPrestamo();
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

            impresora.println(conversorJson.writeValueAsString("Tienen que enviarse tanto el dni del usuario como el del ejemplar que va a devolver"));

        } else{

            // Si es correcto: confirmamos es digito
            if(isDigit(request.getParameter("id_ejemplar"))){

                Integer id_ejemplar = Integer.parseInt(request.getParameter("id_ejemplar"));

                //Si es correcto: buscamos al usuario
                String dni = request.getParameter("dni");
                Usuario usuario = new Usuario();
                usuario.setDni(dni);

                usuario = daoLogInUser.getByDNI2(usuario);

                if(usuario == null){

                    impresora.println(conversorJson.writeValueAsString("El dni no pertenece a ningun usuario registrado"));

                } else {
                    //Si es correcto: buscamos el ejemplar
                   Ejemplar ejemplar = (Ejemplar) daoEjemplar.getById(id_ejemplar);

                   if(ejemplar == null){

                       impresora.println(conversorJson.writeValueAsString("El id no pertenece a ningun ejemplar registrado"));

                   } else {

                       //Si es correcto: buscamos el prestamos por id de usuario y ejemplar
                       Prestamo prestamo = new Prestamo();
                       prestamo.setUsuario(usuario);
                       prestamo.setEjemplar(ejemplar);

                       prestamo = daoPrestamo.getByid_id(usuario.getId(), ejemplar.getId());

                       if(prestamo == null){

                           impresora.println(conversorJson.writeValueAsString("No existe ningun prestamo activo para este usuario y ejemplar"));

                       } else {

                        //Si es correcto: comprobamos la fecha actual y la de devolucion y se actua segun requerimientos
                           if(LocalDate.now().isAfter(prestamo.getFechaDevolucion())){
                                //Penalizacion
                               usuario.setPenalizacionHasta(LocalDate.now().plusDays(15));

                               daoLogInUser.update(usuario);

                               prestamo.setFechaDevolucion(LocalDate.now());
                               prestamo.setEntregado("OK");

                               daoPrestamo.update(prestamo);

                               //Cambiar ejemplar a Disponible
                               ejemplar.setEstado("Disponible");
                               daoEjemplar.update(ejemplar);

                               String json_respose = conversorJson.writeValueAsString(prestamo);
                               String json_respose2 = conversorJson.writeValueAsString(usuario);

                               impresora.println(prestamo);
                               impresora.println(usuario);

                           } else {

                               prestamo.setFechaDevolucion(LocalDate.now());
                               prestamo.setEntregado("OK");

                               daoPrestamo.update(prestamo);

                               //Cambiar ejemplar a Disponible
                               ejemplar.setEstado("Disponible");
                               daoEjemplar.update(ejemplar);

                               String json_respose = conversorJson.writeValueAsString(prestamo);
                               impresora.println(prestamo);

                           }
                       }
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
