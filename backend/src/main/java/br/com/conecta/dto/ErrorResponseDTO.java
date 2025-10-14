package br.com.conecta.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    // Construtor e Getters
    public ErrorResponseDTO(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }
}