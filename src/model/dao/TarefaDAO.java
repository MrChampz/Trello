package model.dao;

import model.bean.Tarefa;

import java.sql.SQLException;
import java.util.List;

public interface TarefaDAO {
    /**
     * Salva uma tarefa no banco.
     * @param projeto Id do projeto ao qual a tarefa pertence.
     * @param tarefa Tarefa a ser salva.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void save(int projeto, Tarefa tarefa) throws SQLException;

    /**
     * Salva uma lista de tarefas no banco.
     * @param projeto Id do projeto ao qual as tarefas pertencem.
     * @param tarefas Lista de tarefas a ser salva.
     * @throws SQLException Exceção lançada caso haja algum erro na inserção.
     */
    void saveList(int projeto, List<Tarefa> tarefas) throws SQLException;

    /**
     * Altera uma tarefa.
     * @param projeto Id do projeto ao qual a tarefa pertence.
     * @param tarefa Tarefa a ser alterada, já com os dados alterados.
     * @throws SQLException Exceção lançada caso haja algum erro na alteração.
     */
    void update(int projeto, Tarefa tarefa) throws SQLException;

    /**
     * Altera uma lista de tarefas.
     * @param projeto Id do projeto ao qual as tarefas pertencem.
     * @param tarefas Lista de tarefas a ser alterada, já com os dados alterados.
     * @throws SQLException Exceção lançada caso haja algum erro na alteração.
     */
    void updateList(int projeto, List<Tarefa> tarefas) throws SQLException;

    /**
     * Deleta uma tarefa do banco.
     * @param projeto Id do projeto ao qual a tarefa pertence.
     * @param id Id da tarefa a ser deletada.
     * @throws SQLException Exceção lançada caso haja algum erro na exclusão.
     */
    void delete(int projeto, int id) throws SQLException;

    /**
     * Deleta todas as tarefas de um projeto.
     * @param projeto Id do projeto que terá suas tarefas excluídas.
     * @throws SQLException Exceção lançada caso haja algum erro na exclusão.
     */
    void deleteAll(int projeto) throws SQLException;

    /**
     * Retorna todas as tarefas de um projeto.
     * @param projeto Id do projeto, que terá suas tarefas retornadas.
     * @throws SQLException Exceção lançada caso haja algum erro na obtenção.
     */
    List<Tarefa> get(int projeto) throws SQLException;
}
