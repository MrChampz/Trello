package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Projeto {

	public static final int ID_DESCONHECIDO = -1;

	private int id;
	private String nome;
	private Usuario proprietario;
	private List<Usuario> usuarios;
	private List<Tarefa> tarefas;

	public Projeto(String nome, Usuario proprietario) {
		this(ID_DESCONHECIDO, nome, proprietario, new ArrayList<>(), new ArrayList<>());
	}

	public Projeto(
		int id,
		String nome,
		Usuario proprietario,
		List<Usuario> usuarios,
		List<Tarefa> tarefas
	) {
		this.id = id;
		this.nome = nome;
		this.proprietario = proprietario;
		this.usuarios = usuarios;
		this.tarefas = tarefas;
	}

	public int getId() {
		return id;
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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}
}