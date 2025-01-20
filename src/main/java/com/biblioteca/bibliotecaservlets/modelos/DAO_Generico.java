package com.biblioteca.bibliotecaservlets.modelos;


import jakarta.persistence.*;

import java.util.List;

public class DAO_Generico<T,ID> {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Class<T> clase;
        Class<ID> claseID;

        public DAO_Generico(Class<T> clase, Class<ID> claseID){
            this.clase=clase;
            this.claseID=claseID;
        }

        //INSERT
        public void add(T objeto){
            tx.begin();
            em.persist(objeto);
            tx.commit();
        }

        //SELECT WHERE ID
        public T getById(ID id){
            return em.find(clase, id);
        }


        //SELECT *
        public List<T> getAll(){
            return em.createQuery("SELECT c from "+ clase.getName()+" c").getResultList();
        }


        //UPDATE
        public T update(T objeto){
            tx.begin();
            objeto = em.merge(objeto);
            tx.commit();
            return objeto;
        }
        //DELETE WHERE objeto.id
        public void delete(T objeto){
            tx.begin();
            objeto = em.merge(objeto);
            em.remove(objeto);
            tx.commit();
        }

        @Override
        public String toString() {
            return "DAOGenerico{" +
                    "clase=" + clase.getSimpleName() +
                    "clase=" + clase.getName() +
                    ", claseID=" + claseID +
                    '}';
        }

    }


