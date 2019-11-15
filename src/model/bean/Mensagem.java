package model.bean;

import java.util.Date;

public class Mensagem {

	public enum Estado { ENVIADA, LIDA }

	private Usuario remetente;
	private Usuario destinatario;
	private String texto;
	private Estado estado;
	private Date tsEnvio;
	private Date tsVisualizacao;

	public Mensagem(Usuario remetente, Usuario destinatario, String texto) {
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.texto = texto;
		this.estado = Estado.ENVIADA;
	}

	public Usuario getRemetente() {
		return remetente;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Date getTsEnvio() {
		return tsEnvio;
	}

	public Date getTsVisualizacao() {
		return tsVisualizacao;
	}

	public void setTsVisualizacao(Date tsVisualizacao) {
		this.tsVisualizacao = tsVisualizacao;
	}
}