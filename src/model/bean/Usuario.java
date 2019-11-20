package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	private String apelido;
	private String nome;
	private String email;
	private String senha;
	private Foto foto;
	private List<String> telefones;
	private List<Projeto> projetos;

	public Usuario(String apelido, String nome, String email, String senha) {
		this(apelido, nome, email, senha, null, new ArrayList<>(), new ArrayList<>());
	}

	public Usuario(
		String apelido,
		String nome,
		String email,
		String senha,
		Foto foto,
		List<String> telefones
	) {
		this(apelido, nome, email, senha, foto, telefones, new ArrayList<>());
	}

	public Usuario(
		String apelido,
		String nome,
		String email,
		String senha,
		Foto foto,
		List<String> telefones,
		List<Projeto> projetos
	) {
		this.apelido = apelido;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.foto = foto;
		this.telefones = telefones;
		this.projetos = projetos;
	}

	public String getApelido() {
		return apelido;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public List<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<String> telefones) {
		this.telefones = telefones;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
}