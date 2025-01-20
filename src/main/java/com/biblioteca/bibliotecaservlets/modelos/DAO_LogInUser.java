package com.biblioteca.bibliotecaservlets.modelos;

import jakarta.persistence.*;

import java.util.List;

public class DAO_LogInUser extends DAO_Generico<Usuario,String>{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    public DAO_LogInUser(){
        super(Usuario.class, String.class);

    }


    public Usuario getByDNI2(Usuario user){
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.dni = :dni", Usuario.class);
        query.setParameter("dni", user.getDni());

        return query.getSingleResultOrNull();
    }

    public Usuario getByEmail(Usuario user){
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", user.getEmail());

        return query.getSingleResultOrNull();
    }


    }

