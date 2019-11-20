package model.bean;

import java.util.Date;

public class Mensagem {

	public enum Estado {
		ENVIADA, LIDA;
		public int toInt() { return ordinal(); }
		public static Estado fromInt(int e) { return values()[e]; }
	}

	public static final int ID_DESCONHECIDO = -1;

	private int id;
	private Usuario remetente;
	private Usuario destinatario;
	private String texto;
	private Estado estado;
	private Date tsEnvio;
	private Date tsVisualizacao;

	public Mensagem(Usuario remetente, Usuario destinatario, String texto) {
		this(
			ID_DESCONHECIDO,
			remetente,
			destinatario,
			texto,
			Estado.ENVIADA,
			null,
			null
		);
	}

	public Mensagem(
		int id,
		Usuario remetente,
		Usuario destinatario,
		String texto,
		Estado estado,
		Date tsEnvio,
		Date tsVisualizacao
	) {
		this.id = id;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.texto = texto;
		this.estado = estado;
		this.tsEnvio = tsEnvio;
		this.tsVisualizacao = tsVisualizacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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