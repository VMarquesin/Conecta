package br.com.conecta.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio_items") // Nome mais profissional no banco
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "imagem_url", nullable = false)
    private String imagemUrl;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    // Quem postou?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id", nullable = false)
    private Prestador prestador;

    // --- CAMPOS PREPARADOS PARA IA (FETEPS) ---
    @Column(name = "tags_ia", columnDefinition = "TEXT")
    private String tagsIa; 

    @Column(name = "analise_ia_json", columnDefinition = "TEXT")
    private String analiseIaJson; 

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Prestador getPrestador() {
        return prestador;
    }

    public void setPrestador(Prestador prestador) {
        this.prestador = prestador;
    }

    public String getTagsIa() {
        return tagsIa;
    }

    public void setTagsIa(String tagsIa) {
        this.tagsIa = tagsIa;
    }

    public String getAnaliseIaJson() {
        return analiseIaJson;
    }

    public void setAnaliseIaJson(String analiseIaJson) {
        this.analiseIaJson = analiseIaJson;
    }
}