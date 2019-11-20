package model.dao;

import model.bean.Tarefa;
import model.bean.Tarefa.Prioridade;
import model.bean.Tarefa.Estado;
import model.bean.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TarefaDAOImpl implements TarefaDAO {

    private static final String INSERT     = "INSERT INTO tarefa (titulo, descricao, prioridade, " +
                                             "estado, ordem, projeto_id, proprietario) " +
                                             "VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE     = "UPDATE tarefa SET titulo = ?, descricao = ?, prioridade = ?, " +
                                             "estado = ?, ordem = ? " +
                                             "WHERE id = ? AND projeto_id = ? AND proprietario = ?";
    private static final String DELETE     = "DELETE FROM tarefa WHERE id = ? AND projeto_id = ?";
    private static final String DELETE_ALL = "DELETE FROM tarefa WHERE projeto_id = ?";
    private static final String SELECT     = "SELECT * FROM tarefa WHERE projeto_id = ?";

    private Connection conn;

    public TarefaDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(int projeto, Tarefa tarefa) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT, RETURN_GENERATED_KEYS);

        // Insere os dados da tarefa
        stmt.setString(1, tarefa.getTitulo());
        stmt.setString(2, tarefa.getDescricao());
        stmt.setInt(3, tarefa.getPrioridade().toInt());
        stmt.setInt(4, tarefa.getEstado().toInt());
        stmt.setInt(5, tarefa.getOrdem());
        stmt.setInt(6, projeto);
        stmt.setString(7, tarefa.getProprietario().getApelido());

        // Faz a inserção no banco
        stmt.execute();

        // Encerra o stmt
        stmt.close();
    }

    @Override
    public void saveList(int projeto, List<Tarefa> tarefas) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT);

        // Itera através da lista de tarefas
        for (Tarefa tarefa : tarefas) {
            // Insere os dados de cada tarefa
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getPrioridade().toInt());
            stmt.setInt(4, tarefa.getEstado().toInt());
            stmt.setInt(5, tarefa.getOrdem());
            stmt.setInt(6, projeto);
            stmt.setString(7, tarefa.getProprietario().getApelido());

            // E salva um por um no batch
            stmt.addBatch();
        }

        // Por fim, executa o batch e salva tudo de uma vez no banco
        stmt.executeBatch();
        stmt.close();
    }

    @Override
    public void update(int projeto, Tarefa tarefa) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(UPDATE);

        // Atualiza os dados da tarefa
        stmt.setString(1, tarefa.getTitulo());
        stmt.setString(2, tarefa.getDescricao());
        stmt.setInt(3, tarefa.getPrioridade().toInt());
        stmt.setInt(4, tarefa.getEstado().toInt());
        stmt.setInt(5, tarefa.getOrdem());

        // Define o id da tarefa, o id do projeto que a contém, e o apelido do proprietário.
        stmt.setInt(6, tarefa.getId());
        stmt.setInt(7, projeto);
        stmt.setString(8, tarefa.getProprietario().getApelido());

        // Atualiza os dados no banco
        stmt.executeUpdate();
        stmt.close();
    }

    @Override
    public void updateList(int projeto, List<Tarefa> tarefas) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(UPDATE);

        // Itera pela lista de tarefas e, atualiza uma a uma
        for (Tarefa tarefa : tarefas) {
            // Atualiza os dados da tarefa
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getPrioridade().toInt());
            stmt.setInt(4, tarefa.getEstado().toInt());
            stmt.setInt(5, tarefa.getOrdem());

            // Define o id da tarefa, o id do projeto que a contém, e o apelido do proprietário.
            stmt.setInt(6, tarefa.getId());
            stmt.setInt(7, projeto);
            stmt.setString(8, tarefa.getProprietario().getApelido());

            // Adiciona o batch
            stmt.addBatch();
        }

        // Atualiza todos os dados, de uma vez
        stmt.executeBatch();
        stmt.close();
    }

    @Override
    public void delete(int projeto, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE);

        // Define o id da tarefa a ser excluída
        stmt.setInt(1, id);
        // Define o id do projeto ao qual a tarefa pertence
        stmt.setInt(2, projeto);

        // Deleta do banco
        stmt.execute();
        stmt.close();
    }

    @Override
    public void deleteAll(int projeto) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_ALL);

        // Define o id do projeto ao qual as tarefas pertencem
        stmt.setInt(1, projeto);

        // Deleta do banco
        stmt.execute();
        stmt.close();
    }

    @Override
    public List<Tarefa> get(int projeto) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SELECT);

        // Define o id do projeto
        stmt.setInt(1, projeto);

        // Pega todas as tarefas pertencentes ao projeto
        ResultSet rs = stmt.executeQuery();

        // Itera por todas as tarefas retornadas
        List<Tarefa> tarefas = new ArrayList<>();
        while (rs.next()) {
            // Pega as informações da tarefa
            int id = rs.getInt("id");
            String titulo = rs.getString("titulo");
            String descricao = rs.getString("descricao");
            Prioridade prioridade = Prioridade.fromInt(rs.getInt("prioridade"));
            Estado estado = Estado.fromInt(rs.getInt("estado"));
            int ordem = rs.getInt("ordem");
            String apelido = rs.getString("proprietario");

            // Pega o proprietário da tarefa
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario proprietario = dao.get(apelido);

            // Adiciona a tarefa à lista
            tarefas.add(new Tarefa(id, titulo, descricao, prioridade, estado, ordem, proprietario));
        }

        rs.close();
        stmt.close();

        // Retorna a lista
        return tarefas;
    }
}
