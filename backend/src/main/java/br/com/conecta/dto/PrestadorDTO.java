package br.com.conecta.dto;
import java.util.List;

// Esta classe não tem anotações do JPA. É uma classe Java simples.
public class PrestadorDTO {

    private String nomeCompleto;
    private String nomeFantasia;
    private String email;
    private String senha; // Recebemos a senha em texto plano, vamos criptografar no service
    private String bio;
    private String fotoPerfilUrl;
    private String cpf;
    private List<Integer> categoriaIds;

    // Gere os Getters e Setters para todos os campos
    // (Botão direito -> Source Action -> Generate Getters and Setters)

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public List<Integer> getCategoriaIds() {
        return categoriaIds;
    }

    public void setCategoriaIds(List<Integer> categoriaIds) {
        this.categoriaIds = categoriaIds;
    }
}