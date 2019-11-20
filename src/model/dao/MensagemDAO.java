package model.dao;

import model.bean.Mensagem;

import java.sql.SQLException;
import java.util.List;

public interface MensagemDAO {
    /**
     * Salva uma mensagem no banco.
     * @param mensagem Mensagem a ser salva.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void save(Mensagem mensagem) throws SQLException;

    /**
     * Altera uma mensagem.
     * @param mensagem Mensagem a ser alterada, já com os dados alterados.
     * @throws SQLException Exceção lançada caso haja algum erro na alteração.
     */
    void update(Mensagem mensagem) throws SQLException;

    /**
     * Deleta uma mensagem do banco.
     * @param mensagemId Id da mensagem a ser deletada.
     * @throws SQLException Exceção lançada caso haja algum erro na exclusão.
     */
    void delete(int mensagemId) throws SQLException;

    /**
     * Retorna todos os dados da mensagem com id especificado.
     * @param mensagemId Id da mensagem que deverá ser retornada.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    Mensagem get(int mensagemId) throws SQLException;

    /**
     * Retorna todas as mensagens de um usuário.
     * @param apelido Apelido do usuário que deverá ter suas mensagens retornadas.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Mensagem> getAll(String apelido) throws SQLException;

    /**
     * Retorna APENAS as mensagens que o usuário enviou.
     * @param apelido Apelido do usuário que deverá ter suas mensagens retornadas.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Mensagem> getAllSent(String apelido) throws SQLException;

    /**
     * Retorna APENAS as mensagens que o usuário recebeu.
     * @param apelido Apelido do usuário que deverá ter suas mensagens retornadas.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Mensagem> getAllReceived(String apelido) throws SQLException;
}