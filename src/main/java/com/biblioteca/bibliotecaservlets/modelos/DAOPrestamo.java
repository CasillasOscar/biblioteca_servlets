package com.biblioteca.bibliotecaservlets.modelos;

import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class DAOPrestamo extends DAO_Generico<Prestamo,Integer>{

    public DAOPrestamo() {
        super(Prestamo.class,Integer.class);
    }

    public Prestamo getByid_id(Integer usuario_id, Integer id_ejemplar){
        TypedQuery<Prestamo> query = em.createQuery("SELECT u FROM Prestamo u WHERE u.usuario.id=:usuario_id and u.ejemplar.id=:id_ejemplar and u.entregado='KO'", Prestamo.class);

        query.setParameter("usuario_id", usuario_id);
        query.setParameter("id_ejemplar", id_ejemplar);

        return query.getSingleResultOrNull();
    }

    public List<Prestamo> getAllOK(){
        return em.createQuery("SELECT u FROM Prestamo u WHERE u.entregado='OK'", Prestamo.class).getResultList();

    }

    public List<Prestamo> getAllKO(){
        return em.createQuery("SELECT u FROM Prestamo u WHERE u.entregado='KO'", Prestamo.class).getResultList();

    }


    public List<Prestamo> getForIdUsuario(Integer id){
        TypedQuery<Prestamo> query = em.createQuery("SELECT u FROM Prestamo u WHERE u.usuario.id=:id", Prestamo.class);

        query.setParameter("id", id);
        return query.getResultList();

    }

    public List<Prestamo> getForIdEjemplar(Integer id){
        TypedQuery<Prestamo> query = em.createQuery("SELECT u FROM Prestamo u WHERE u.ejemplar.id=:id", Prestamo.class);

        query.setParameter("id", id);
        return query.getResultList();

    }

    public List<Prestamo> getAfter30days(){
        TypedQuery<Prestamo> query = em.createQuery("SELECT u FROM Prestamo u WHERE u.fechaDevolucion < :fechaLimite", Prestamo.class);

        query.setParameter("fechaLimite", LocalDate.now().plusDays(-30));
        return query.getResultList();

    }

}
