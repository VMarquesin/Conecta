package br.com.conecta.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prestadores")
public class Prestador {

    // A chave primária é compartilhada com Usuario (Herança de ID)
    @Id
    private Long usuarioId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Isso diz: "Meu ID é igual ao ID do Usuario vinculado"
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String nomeFantasia; 

    // O antigo "cpf" agora é genérico para aceitar CNPJ também
    @Column(nullable = false, length = 20)
    private String documento; 

    @Column(name = "foto_documento_url")
    private String fotoDocumentoUrl; // Para validação do Admin

    // Substituímos a tabela extra de telefones por um campo direto (Regra de Negócio: WhatsApp é o que importa)
    @Column(name = "telefone_whatsapp", nullable = false, length = 20)
    private String telefoneWhatsapp;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    // --- Campos de Gestão (Novos) ---
    @Column(name = "plano_assinatura", length = 20)
    private String planoAssinatura = "GRATIS"; 

    private Boolean verificado = false;

    @Column(name = "status_disponibilidade", length = 20)
    private String statusDisponibilidade = "DISPONIVEL";

    // --- Relacionamentos Mantidos ---

    // Mantivemos suas Publicações
    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Publicacao> publicacoes = new HashSet<>();

    // Mantivemos suas Categorias
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "prestador_categorias",
        joinColumns = @JoinColumn(name = "prestador_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    // --- Getters e Setters ---

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getFotoDocumentoUrl() {
        return fotoDocumentoUrl;
    }

    public void setFotoDocumentoUrl(String fotoDocumentoUrl) {
        this.fotoDocumentoUrl = fotoDocumentoUrl;
    }

    public String getTelefoneWhatsapp() {
        return telefoneWhatsapp;
    }

    public void setTelefoneWhatsapp(String telefoneWhatsapp) {
        this.telefoneWhatsapp = telefoneWhatsapp;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getPlanoAssinatura() {
        return planoAssinatura;
    }

    public void setPlanoAssinatura(String planoAssinatura) {
        this.planoAssinatura = planoAssinatura;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public String getStatusDisponibilidade() {
        return statusDisponibilidade;
    }

    public void setStatusDisponibilidade(String statusDisponibilidade) {
        this.statusDisponibilidade = statusDisponibilidade;
    }

    public Set<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(Set<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }
    
    // Método auxiliar para adicionar categoria
    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getPrestadores().add(this);
    }
}