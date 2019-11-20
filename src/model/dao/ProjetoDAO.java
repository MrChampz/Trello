package model.dao;

import model.bean.Projeto;
import model.bean.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface ProjetoDAO {
    /**
     * Salva um projeto no banco.
     * @param projeto Projeto a ser salvo.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void save(Projeto projeto) throws SQLException;

    /**
     * Altera um projeto.
     * @param projeto Projeto a ser alterado, já com os dados alterados.
     * @throws SQLException Exceção lançada caso haja algum erro na alteração.
     */
    void update(Projeto projeto) throws SQLException;

    /**
     * Deleta um projeto do banco.
     * @param projetoId Id do projeto a ser deletado.
     * @throws SQLException Exceção lançada caso haja algum erro na exclusão.
     */
    void delete(int projetoId) throws SQLException;

    /**
     * Retorna todos os dados do projeto com id especificado.
     * @param projetoId Id do projeto que deverá ser retornado.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    Projeto get(int projetoId) throws SQLException;

    /**
     * Retorna todos os projetos de um usuário.
     * @param apelido Apelido do usuário que deverá ter seus projetos retornados.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Projeto> getAll(String apelido) throws SQLException;

    /**
     * Adiciona um usuário ao projeto de id especificado.
     * @param projetoId Id do projeto ao qual o usuário será adicionado.
     * @param usuario Usuário a ser adicionado.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void saveUsuarioIntoProjeto(int projetoId, Usuario usuario) throws SQLException;

    /**
     * Remove um usuário do projeto especificado.
     * O usuário NÃO pode ser o proprietário do projeto!
     * @param projetoId Id do projeto ao qual o usuário será removido.
     * @param apelido apelido Apelido do usuário a ser removido.
     * @throws SQLException Exceção lançada caso haja algum erro na remoção.
     */
    void deleteUsuarioFromProjeto(int projetoId, String apelido) throws SQLException;
}
