package br.com.conecta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "publicacoes")
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    private String fotoUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataPublicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id", nullable = false)
    @JsonIgnore
    private Prestador prestador;

    @OneToMany(mappedBy = "publicacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Avaliacao> avaliacoes = new HashSet<>();

    // Getters e Setters
    public Set<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
    public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    public Prestador getPrestador() { return prestador; }
    public void setPrestador(Prestador prestador) { this.prestador = prestador; }

    // equals() e hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacao that = (Publicacao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}