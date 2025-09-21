package com.example.demo.dto;

public class AvaliacaoDTO {
    private int nota;
    private String comentario;
    private Integer clienteId;

    // Getters e Setters
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
}