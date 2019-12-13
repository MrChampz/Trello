package model.dao;

import model.bean.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {

    /**
     * Tenta fazer login, se houver sucesso, retorna o usuário logado.
     * @param email Email do usuário.
     * @param password Senha do usuário.
     * @return O usuário logado em caso de sucesso, caso contrário, null.
     * @throws SQLException Exceção lançada caso haja algum erro na consulta.
     */
    Usuario login(String email, String password) throws SQLException;

    /**
     * Salva um usuário no banco.
     * @param usuario Usuário a ser salvo.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void save(Usuario usuario) throws SQLException;

    /**
     * Altera um usuário, os únicos atributos que podem ser alterados são:
     * nome, senha, foto e telefones.
     * @param usuario Usuário a ser alterado, já com os dados alterados.
     * @throws SQLException Exceção lançada caso haja algum erro na alteração.
     */
    void update(Usuario usuario) throws SQLException;

    /**
     * Deleta um usuário do banco.
     * @param apelido Apelido do usuário a ser deletado.
     * @throws SQLException Exceção lançada caso haja algum erro na exclusão.
     */
    void delete(String apelido) throws SQLException;

    /**
     * Retorna um usuário que esteja salvo no banco.
     * @param apelido Apelido do usuário a ser retornado.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    Usuario get(String apelido) throws SQLException;

    /**
     * TODO: É necessário listar todos os usuários?
     * Retorna todos os usuários salvos no banco.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Usuario> getAll() throws SQLException;
}
