package br.com.conecta.dto;

import jakarta.validation.constraints.NotBlank;

// Este DTO contém apenas os campos que podem ser editados no perfil
public class PerfilUpdateDTO {

    @NotBlank(message = "O nome completo não pode estar em branco.")
    private String nomeCompleto;
    
    private String nomeFantasia;
    private String bio;

    // Getters e Setters
    public String getNomeCompleto() {
        return nomeCompleto;
    }
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    public String getNomeFantasia() {
        return nomeFantasia;
    }
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
}