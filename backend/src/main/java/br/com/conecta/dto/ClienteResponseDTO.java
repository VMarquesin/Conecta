package br.com.conecta.dto;

import br.com.conecta.entity.Cliente;
import java.time.LocalDateTime;

public class ClienteResponseDTO {

    private Integer id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private LocalDateTime dataCadastro;

    // Construtor que converte a Entidade para este DTO
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nomeCompleto = cliente.getNomeCompleto();
        this.email = cliente.getEmail();
        this.cpf = cliente.getCpf();
        this.dataCadastro = cliente.getDataCadastro();
    }

    // Apenas Getters
    public Integer getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
}