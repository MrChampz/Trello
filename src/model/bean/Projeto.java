package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Projeto {

	private static final int ID_PADRAO = -1;

	private int id;
	private String nome;
	private Usuario proprietario;
	private List<Tarefa> tarefas;

	public Projeto(String nome, Usuario proprietario) {
		this.id = ID_PADRAO;
		this.nome = nome;
		this.proprietario = proprietario;
		this.tarefas = new ArrayList<>();
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

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}
}