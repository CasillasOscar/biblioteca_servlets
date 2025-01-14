package com.biblioteca.bibliotecaservlets.modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ejemplar")
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "isbn", nullable = false)
    @JsonBackReference
    private com.biblioteca.bibliotecaservlets.modelos.Libro isbn;

    @ColumnDefault("'Disponible'")
    @Lob
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "ejemplar")
    @JsonManagedReference
    private Set<com.biblioteca.bibliotecaservlets.modelos.Prestamo> prestamos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.biblioteca.bibliotecaservlets.modelos.Libro getIsbn() {
        return isbn;
    }

    public void setIsbn(com.biblioteca.bibliotecaservlets.modelos.Libro isbn) {
        this.isbn = isbn;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<com.biblioteca.bibliotecaservlets.modelos.Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(Set<com.biblioteca.bibliotecaservlets.modelos.Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "id=" + id +
                ", isbn=" + isbn.getIsbn() +
                ", estado='" + estado + '\'' +
                ", prestamos=" + prestamos +
                '}' + "\n";
    }
}