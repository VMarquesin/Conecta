package br.com.conecta.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PrestadorDTO {

    @NotBlank(message = "O nome completo não pode estar em branco.")
    private String nomeCompleto;
    
    private String nomeFantasia;

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "O formato do email é inválido.")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha; 
    private String bio;
    private String fotoPerfilUrl;
    @NotBlank(message = "O CPF não pode estar em branco.")
    private String cpf;
    private List<Integer> categoriaIds;

    private String telefoneNumero;
    private Boolean isWhatsapp;

    // Getters e Setters 
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

    public String getTelefoneNumero() {
        return telefoneNumero;
    }
    public void setTelefoneNumero(String telefoneNumero) {
        this.telefoneNumero = telefoneNumero;
    }
    public Boolean getIsWhatsapp() {
        return isWhatsapp;
    }
    public void setIsWhatsapp(Boolean isWhatsapp) {
        this.isWhatsapp = isWhatsapp;
    }
}