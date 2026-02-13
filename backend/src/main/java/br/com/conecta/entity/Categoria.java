package br.com.conecta.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(unique = true)
    private String slug; // ex: eletricista-residencial

    @Column(name = "icone_url")
    private String iconeUrl;

    @ManyToMany(mappedBy = "categorias")
    private Set<Prestador> prestadores = new HashSet<>();

    // --- Getters e Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIconeUrl() {
        return iconeUrl;
    }

    public void setIconeUrl(String iconeUrl) {
        this.iconeUrl = iconeUrl;
    }

    public Set<Prestador> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(Set<Prestador> prestadores) {
        this.prestadores = prestadores;
    }
}