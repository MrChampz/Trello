package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Projeto {

	public static final int ID_DESCONHECIDO = -1;

	private int id;
	private String nome;
	private Usuario proprietario;
	private List<Usuario> membros;
	private List<Tarefa> tarefas;
	private Color color;

	public Projeto(String nome, Usuario proprietario) {
		this(ID_DESCONHECIDO, nome, proprietario, new ArrayList<>(), new ArrayList<>());
		this.membros.add(proprietario);
	}

	public Projeto(
		int id,
		String nome,
		Usuario proprietario,
		List<Usuario> membros,
		List<Tarefa> tarefas
	) {
		this.id = id;
		this.nome = nome;
		this.proprietario = proprietario;
		this.membros = membros;
		this.tarefas = tarefas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Usuario proprietario) {
		this.proprietario = proprietario;
	}

	public List<Usuario> getMembros() {
		return membros;
	}

	public void setMembros(List<Usuario> membros) {
		this.membros = membros;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}