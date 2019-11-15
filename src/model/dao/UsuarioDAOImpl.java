package model.dao;

import conn.ConnectionFactory;
import model.bean.Foto;
import model.bean.Usuario;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String INSERT = "INSERT INTO usuario (apelido, nome, email, senha, foto) " +
                                         "VALUES (?,?,?,?,?)";
    private static final String UPDATE = "UPDATE usuario SET nome = ?, senha = ?, foto = ? " +
                                         "WHERE apelido = ?";
    private static final String DELETE = "DELETE FROM usuario WHERE apelido = ?";
    private static final String SELECT = "SELECT * FROM usuario WHERE apelido = ?";
    private static final String SELECT_ALL = "SELECT * FROM usuario";

    private static final String INSERT_TELEFONES = "INSERT INTO usuario_telefone (usuario_apelido, telefone) " +
                                                   "VALUES (?,?)";
    private static final String DELETE_TELEFONES = "DELETE FROM usuario_telefone " +
                                                   "WHERE usuario_apelido = ?";
    private static final String SELECT_TELEFONES = "SELECT telefone FROM usuario_telefone " +
                                                   "WHERE usuario_apelido = ?";

    @Override
    public void save(Usuario usuario) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            // Insere apelido, nome, email e senha
            stmt.setString(1, usuario.getApelido());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());

            // Insere a foto do usuário, se houver
            if (usuario.getFoto() != null) {
                InputStream stream = new ByteArrayInputStream(usuario.getFoto().getBinary());
                stmt.setBinaryStream(5, stream);
            } else {
                stmt.setBinaryStream(5, null);
            }

            // Faz a inserção no banco
            stmt.execute();

            // Insere os telefones no banco
            createTelefones(conn, usuario);
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            // Atualiza o nome e senha
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());

            // Atualiza a foto do usuário
            if (usuario.getFoto() != null) {
                InputStream stream = new ByteArrayInputStream(usuario.getFoto().getBinary());
                stmt.setBinaryStream(3, stream);
            } else {
                stmt.setBinaryStream(3, null);
            }

            // Define o apelido do usuário que deverá sofrer alteração
            stmt.setString(4, usuario.getApelido());

            // Atualiza os dados no banco
            stmt.executeUpdate();

            // Atualiza os telefones
            updateTelefones(conn, usuario);
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void delete(String apelido) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        // É necessário excluir os telefones antes
        deleteTelefones(conn, apelido);

        // Assim, é possível excluir o usuário da tabela também
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            // Define o apelido do usuário que deverá ser deletado
            stmt.setString(1, apelido);
            // Deleta do banco
            stmt.execute();
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Usuario get(String apelido) throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(SELECT)) {
            // Define o apelido do usuário a ser retornado
            stmt.setString(1, apelido);
            // Tenta pegar o usuário no banco
            ResultSet rs = stmt.executeQuery();

            // Se houver algum resultado
            if (rs.next()) {
                // Pega as informações do usuário
                String apel = rs.getString("apelido");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                // Pega a foto do usuário
                Blob blob = rs.getBlob("foto");
                Foto foto = getFoto(blob);

                // Pega os telefones do usuário
                List<String> telefones = getTelefones(conn, apel);

                // Encerra o ResultSet
                rs.close();

                // Retorna o usuário
                return new Usuario(apel, nome, email, senha, foto, telefones);
            }

            // Se o fluxo de execução chegar a esse ponto, nenhum usuário foi encontrado
            throw new SQLException("Usuário não encontrado!");
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Usuario> getAll() throws SQLException {
        // Obtém a conexão com o banco
        Connection conn = ConnectionFactory.getConnection();

        // Inicio do bloco de transação
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL)) {
            // Pega todos os usuário contidos no banco
            ResultSet rs = stmt.executeQuery();

            // Itera por todos os usuários retornados
            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                // Pega as informações do usuário
                String apelido = rs.getString("apelido");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                // Pega a foto do usuário
                Blob blob = rs.getBlob("foto");
                Foto foto = getFoto(blob);

                // Pega os telefones do usuário
                List<String> telefones = getTelefones(conn, apelido);

                // Adiciona o usuário à lista
                usuarios.add(new Usuario(apelido, nome, email, senha, foto, telefones));
            }

            // Encerra o ResultSet
            rs.close();
            // Retorna a lista
            return usuarios;
        } finally {
            // Retorna ao estado anterior
            conn.setAutoCommit(true);
            // E encerra a conexão com o banco
            ConnectionFactory.closeConnection(conn);
        }
    }

    private Foto getFoto(Blob blob) throws SQLException {
        // A principio a foto recebe null
        Foto foto = null;

        // Se o usuário tem foto salva no banco
        if (blob != null) {
            // Inicializa a foto com os dados obtidos do banco
            byte[] source = blob.getBytes(1, (int) blob.length());
            foto = new Foto(source);
        }

        // Caso contrário, será retornado null
        return foto;
    }

    private void createTelefones(Connection conn, Usuario usuario) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT_TELEFONES);

        // Itera através da lista de telefones
        for (String telefone : usuario.getTelefones()) {
            // E salva um por um no batch
            stmt.setString(1, usuario.getApelido());
            stmt.setString(2, telefone);
            stmt.addBatch();
        }

        // Por fim, executa o batch e salva tudo de uma vez no banco
        stmt.executeBatch();

        // E então, encerra o PreparedStatement
        stmt.close();
    }

    private void updateTelefones(Connection conn, Usuario usuario) throws SQLException {
        // Deleta todos os telefones do usuário do banco
        deleteTelefones(conn, usuario.getApelido());

        // E então, adiciona todos os novos
        createTelefones(conn, usuario);
    }

    private void deleteTelefones(Connection conn, String apelido) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_TELEFONES);

        // Define o apelido do usuário ao qual o telefones devem ser excluídos
        stmt.setString(1, apelido);

        // Excluí os telefones
        stmt.execute();
    }

    private List<String> getTelefones(Connection conn, String apelido) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SELECT_TELEFONES);

        // Define o apelido do usuário que deve ter os telefones retornados
        stmt.setString(1, apelido);

        // Pega todos os telefones do usuário
        ResultSet rs = stmt.executeQuery();

        // Itera pelos telefones retornados
        List<String> telefones = new ArrayList<>();
        while (rs.next()) {
            // Se houver algum, adiciona à lista
            String telefone = rs.getString("telefone");
            telefones.add(telefone);
        }

        // Encerra o ResultSet
        rs.close();

        // E retorna a lista
        return telefones;
    }
}