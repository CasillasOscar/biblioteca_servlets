package com.biblioteca.bibliotecaservlets.modelos;

import jakarta.persistence.TypedQuery;

public class DAOLibros extends DAO_Generico<Libro,String>{

    public DAOLibros() {
        super(Libro.class,String.class);
    }

    public Libro getByTitulo(String titulo){
        TypedQuery<Libro> query = em.createQuery("SELECT u FROM Libro u WHERE u.titulo=:titulo", Libro.class);
        query.setParameter("titulo", titulo);

        return query.getSingleResultOrNull();
    }

    public Libro getByISBN(String isbn){
        TypedQuery<Libro> query = em.createQuery("SELECT u FROM Libro u WHERE u.isbn=:isbn", Libro.class);
        query.setParameter("isbn", isbn);

        return query.getSingleResultOrNull();
    }


}
