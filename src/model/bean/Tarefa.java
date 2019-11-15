package model.bean;

public class Tarefa {

	public enum Prioridade { ALTA, MEDIA, BAIXA }
	public enum Estado { NOVA, ANDAMENTO, CONCLUIDA }

	private String titulo;
	private String descricao;
	private Prioridade prioridade;
	private Estado estado;
	private int ordem;
	private Usuario proprietario;

	public Tarefa(
		String titulo, String descricao, Prioridade prioridade, int ordem, Usuario proprietario
	) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.prioridade = prioridade;
		this.ordem = ordem;
		this.proprietario = proprietario;
		this.estado = Estado.NOVA;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public Usuario getProprietario() {
		return proprietario;
	}
}