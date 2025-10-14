package br.com.conecta.dto;

public class TelefoneDTO {
    private String numero;
    private boolean isWhatsapp;

    // Getters e Setters
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public boolean isWhatsapp() { return isWhatsapp; }
    public void setWhatsapp(boolean whatsapp) { isWhatsapp = whatsapp; }
}