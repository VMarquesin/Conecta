package br.com.conecta.dto;

import br.com.conecta.entity.Avaliacao;
import java.time.LocalDateTime;

public class AvaliacaoResponseDTO {

    private Integer id;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private String nomeCliente;

    // Construtor que converte a Entidade para o DTO
    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataAvaliacao = avaliacao.getDataAvaliacao();
        // Acessamos o nome do cliente aqui, enquanto a sessão está ativa
        this.nomeCliente = avaliacao.getCliente().getNomeCompleto();
    }

    // Getters
    public Integer getId() { return id; }
    public int getNota() { return nota; }
    public String getComentario() { return comentario; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public String getNomeCliente() { return nomeCliente; }
}