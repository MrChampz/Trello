package model.dao;

import conn.ConnectionFactory;
import model.bean.Projeto;
import model.bean.Tarefa;
import model.bean.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOImpl implements ProjetoDAO {

    private static final String INSERT     = "INSERT INTO projeto (nome, proprietario) " +
                                             "VALUES (?,?)";
    private static final String UPDATE     = "UPDATE projeto SET nome = ? " +
                                             "WHERE id = ? AND proprietario = ?";
    private static final String DELETE     = "DELETE FROM projeto WHERE id = ?";
    private static final String SELECT     = "SELECT * FROM projeto WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM projeto WHERE id IN (" +
                                             "SELECT projeto_id FROM projeto_usuario WHERE usuario_apelido = ?)";

    private static final String INSERT_USUARIO  = "INSERT INTO projeto_usuario (projeto_id, usuario_apelido) " +
                                                  "VALUES (?,?)";
    private static final String DELETE_USUARIO  = "DELETE FROM projeto_usuario " +
                                                  "WHERE projeto_id = ? AND usuario_apelido = ?";
    private static final String DELETE_USUARIOS = "DELETE FROM projeto_usuario WHERE projeto_id = ?";
    private static final String SELECT_USUARIOS = "SELECT usuario_apelido FROM projeto_usuario WHERE projeto_id = ?";

    @Override
    public void save(Projeto projeto) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        try {
            // Salva o projeto
            int id = saveProjeto(conn, projeto);

            // Insere o proprietário como usuário do projeto
            saveUsuarioIntoProjeto(id, projeto.getProprietario());

            // Por fim, insere as tarefas desse projeto
            TarefaDAO dao = new TarefaDAOImpl();
            dao.saveList(id, projeto.getTarefas());
        } finally {
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void update(Projeto projeto) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            // Atualiza o nome
            stmt.setString(1, projeto.getNome());

            // Define o id do projeto e o apelido do proprietário
            stmt.setInt(2, projeto.getId());
            stmt.setString(3, projeto.getProprietario().getApelido());

            // Atualiza os dados no banco
            stmt.executeUpdate();

            // Atualiza os usuários participantes
            updateUsuarios(conn, projeto);

            // Fim do bloco de transação
            conn.commit();

            // Por fim, atualiza as tarefas do projeto
            TarefaDAO dao = new TarefaDAOImpl();
            dao.updateList(projeto.getId(), projeto.getTarefas());
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void delete(int projetoId) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        // É necessário remover os usuários participantes antes
        deleteUsuarios(conn, projetoId);

        // Também, é necessário excluir todas as tarefas do projeto antes
        TarefaDAO dao = new TarefaDAOImpl();
        dao.deleteAll(projetoId);

        // Assim, é possível excluir o projeto do banco
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            // Define o id do projeto que deverá ser deletado
            stmt.setInt(1, projetoId);

            // Deleta do banco
            stmt.execute();

            // Fim do bloco de transação
            conn.commit();
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Projeto get(int projetoId) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(SELECT)) {
            // Define o id do projeto a ser retornado
            stmt.setInt(1, projetoId);

            // Tenta pegar o projeto no banco
            ResultSet rs = stmt.executeQuery();

            // Se houver algum resultado
            if (rs.next()) {
                // Pega as informações do projeto
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String prop = rs.getString("proprietario");

                // Pega os usuários participantes do projeto
                List<Usuario> participantes = getUsuarios(conn, id);

                // Pega o usuário proprietário
                Usuario proprietario = getProprietario(prop, participantes);

                // Fim do bloco de transação
                conn.commit();

                // Encerra o ResultSet
                rs.close();

                // Pega as tarefas do projeto
                TarefaDAO dao = new TarefaDAOImpl();
                List<Tarefa> tarefas = dao.get(id);

                // Retorna o projeto
                return new Projeto(id, nome, proprietario, participantes, tarefas);
            }

            // Se o fluxo de execução chegar a esse ponto, nenhum projeto foi encontrado
            throw new SQLException("Projeto não encontrado!");
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Projeto> getAll(String apelido) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL)) {
            // Define o apelido do usuário que deverá ter seus projetos retornados
            stmt.setString(1, apelido);

            // Pega os projetos do usuário
            ResultSet rs = stmt.executeQuery();

            // Itera por tudo que foi retornado
            List<Projeto> projetos = new ArrayList<>();
            while (rs.next()) {
                // Pega as informações do projeto
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String prop = rs.getString("proprietario");

                // Pega os usuários participantes do projeto
                List<Usuario> participantes = getUsuarios(conn, id);

                // Pega o usuário proprietário
                Usuario proprietario = getProprietario(prop, participantes);

                // Pega as tarefas do projeto
                TarefaDAO dao = new TarefaDAOImpl();
                List<Tarefa> tarefas = dao.get(id);

                // Adiciona à lista de projetos
                projetos.add(new Projeto(id, nome, proprietario, participantes, tarefas));
            }

            // Fim do bloco de transação
            conn.commit();

            // Encerra o ResultSet
            rs.close();

            // Retorna a lista
            return projetos;
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void saveUsuarioIntoProjeto(int projetoId, Usuario usuario) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_USUARIO)) {

            // Insere o id do projeto e o apelido do usuário participante
            stmt.setInt(1, projetoId);
            stmt.setString(2, usuario.getApelido());

            // Salva no banco
            stmt.execute();
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void deleteUsuarioFromProjeto(int projetoId, String apelido) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_USUARIO)) {

            // Insere o id do projeto e o apelido do usuário que deve ser retirado
            stmt.setInt(1, projetoId);
            stmt.setString(2, apelido);

            // Retira o usuário
            stmt.execute();
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    private int saveProjeto(Connection conn, Projeto projeto) throws SQLException {
        // O segundo parâmetro faz com que seja retornado o id gerado
        PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

        // Insere os dados do projeto
        stmt.setString(1, projeto.getNome());
        stmt.setString(2, projeto.getProprietario().getApelido());

        // Faz a inserção no banco
        stmt.execute();

        // Salva o id gerado
        int id = Projeto.ID_DESCONHECIDO;
        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) id =  keys.getInt(1);

        // Encerra tudo
        keys.close();
        stmt.close();

        // Retorna o id gerado
        return id;
    }

    private void saveUsuarios(Connection conn, Projeto projeto) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT_USUARIO);

        for (Usuario usuario : projeto.getUsuarios()) {
            // Insere o id do projeto e o apelido do usuário participante
            stmt.setInt(1, projeto.getId());
            stmt.setString(2, usuario.getApelido());
            stmt.addBatch();
        }

        // Salva no banco
        stmt.executeBatch();
        stmt.close();
    }

    private void updateUsuarios(Connection conn, Projeto projeto) throws SQLException {
        // Deleta todos os usuários do projeto
        deleteUsuarios(conn, projeto.getId());
        // Adiciona a lista de usuários atualizada
        saveUsuarios(conn, projeto);
    }

    private void deleteUsuarios(Connection conn, int projetoId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_USUARIOS);

        // Insere o id do projeto do qual os usuários devem ser deletados
        stmt.setInt(1, projetoId);

        // Retira os usuários
        stmt.execute();
        stmt.close();
    }

    private List<Usuario> getUsuarios(Connection conn, int projetoId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SELECT_USUARIOS);

        // Insere o id do projeto do qual os usuários devem ser obtidos
        stmt.setInt(1, projetoId);

        // Pega os usuários
        ResultSet rs = stmt.executeQuery();

        // Itera por tudo que foi retornado
        List<Usuario> usuarios = new ArrayList<>();
        while (rs.next()) {
            // Pega o apelido de cada usuário retornado
            String apelido = rs.getString("usuario_apelido");

            // Busca os dados desse usuário no banco
            UsuarioDAO dao = new UsuarioDAOImpl();
            Usuario usuario = dao.get(apelido);

            // Adiciona o usuário à lista
            usuarios.add(usuario);
        }

        // Encerra tudo
        rs.close();
        stmt.close();

        // Retorna os usuários obtidos
        return usuarios;
    }

    private Usuario getProprietario(
        String proprietario,
        List<Usuario> participantes
    ) throws RuntimeException {
        // Itera por todos os usuários participantes
        for (Usuario usuario : participantes) {
            // Se o apelido do usuário for igual ao apelido do proprietário
            if (usuario.getApelido().equals(proprietario)) {
                // Retorna uma referência para o usuário
                return usuario;
            }
        }
        // Caso o fluxo de execução alcance esse ponto, o usuário proprietário não foi encontrado
        throw new RuntimeException("Proprietário não encontrado!");
    }
}
