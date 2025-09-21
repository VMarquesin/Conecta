package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore; // Adicione esta importação
import java.util.HashSet; // Adicione esta importação
import java.util.Set;    // Adicione esta importação

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Usando Integer como no seu modelo (id INT)

    @Column(unique = true, nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT") // Para campos TEXT
    private String descricao;

    @ManyToMany(mappedBy = "categorias") // 'mappedBy' aponta para o nome do campo na classe Prestador
    @JsonIgnore // Evita loops infinitos ao converter para JSON
    private Set<Prestador> prestadores = new HashSet<>();

    // Getters e Setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    // Adicione também o Getter e Setter para 'prestadores'
    public Set<Prestador> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(Set<Prestador> prestadores) {
        this.prestadores = prestadores;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Categoria other = (Categoria) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}