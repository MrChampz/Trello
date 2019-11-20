package model.dao;

import conn.ConnectionFactory;
import model.bean.Foto;
import model.bean.Mensagem;
import model.bean.Projeto;
import model.bean.Usuario;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String INSERT     = "INSERT INTO usuario (apelido, nome, email, senha, foto) " +
                                             "VALUES (?,?,?,?,?)";
    private static final String UPDATE     = "UPDATE usuario SET nome = ?, senha = ?, foto = ? " +
                                             "WHERE apelido = ?";
    private static final String DELETE     = "DELETE FROM usuario WHERE apelido = ?";
    private static final String SELECT     = "SELECT * FROM usuario WHERE apelido = ?";
    private static final String SELECT_ALL = "SELECT * FROM usuario";

    private static final String INSERT_TELEFONES = "INSERT INTO usuario_telefone (usuario_apelido, telefone) " +
                                                   "VALUES (?,?)";
    private static final String DELETE_TELEFONES = "DELETE FROM usuario_telefone " +
                                                   "WHERE usuario_apelido = ?";
    private static final String SELECT_TELEFONES = "SELECT telefone FROM usuario_telefone " +
                                                   "WHERE usuario_apelido = ?";

    private static HashMap<String, Usuario> cache;

    private Connection conn;

    public UsuarioDAOImpl(Connection conn) {
        // Inicializa o cache
        if (cache == null) {
            cache = new HashMap<>();
        }
        this.conn = conn;
    }

    @Override
    public void save(Usuario usuario) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT);

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
        stmt.close();

        // Insere os telefones no banco
        saveTelefones(usuario);

        // Adiciona ao cache, para futuras buscas
        cache.put(usuario.getApelido(), usuario);
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(UPDATE);

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
        stmt.close();

        // Atualiza os telefones
        updateTelefones(usuario);

        // Atualiza o usuário no cache também
        cache.put(usuario.getApelido(), usuario);
    }

    @Override
    public void delete(String apelido) throws SQLException {
        // É necessário excluir os telefones antes
        deleteTelefones(apelido);

        // Assim, é possível excluir o usuário da tabela também
        PreparedStatement stmt = conn.prepareStatement(DELETE);

        // Define o apelido do usuário que deverá ser deletado
        stmt.setString(1, apelido);

        // Deleta do banco
        stmt.execute();
        stmt.close();

        // Por fim, remove do cache também
        cache.remove(apelido);
    }

    @Override
    public Usuario get(String apelido) throws SQLException {
        // Primeiramente, verifica no cache
        if (cache.containsKey(apelido)) return cache.get(apelido);

        PreparedStatement stmt = conn.prepareStatement(SELECT);

        // Define o apelido do usuário a ser retornado
        stmt.setString(1, apelido);

        // Tenta pegar o usuário no banco
        ResultSet rs = stmt.executeQuery();

        // Se houver algum resultado
        Usuario usuario;
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
            List<String> telefones = getTelefones(apel);

            rs.close();
            stmt.close();

            // Adiciona o usuário ao cache
            usuario = new Usuario(apel, nome, email, senha, foto, telefones);
            cache.put(usuario.getApelido(), usuario);
        } else {
            rs.close();
            stmt.close();

            // Se o fluxo de execução chegar a esse ponto, nenhum usuário foi encontrado
            throw new SQLException("Usuário não encontrado!");
        }

        // Agora recupera os dados (projetos e mensagens) do usuário.
        ProjetoDAO daoProj = new ProjetoDAOImpl(conn);
        MensagemDAO daoMsg = new MensagemDAOImpl(conn);
        restore(daoProj, daoMsg, usuario);

        // Retorna o usuário
        return usuario;
    }

    @Override
    public List<Usuario> getAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);

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
            List<String> telefones = getTelefones(apelido);

            // Adiciona o usuário ao cache e à lista
            Usuario usuario = new Usuario(apelido, nome, email, senha, foto, telefones);
            usuarios.add(usuario);
            cache.put(usuario.getApelido(), usuario);
        }

        rs.close();
        stmt.close();

        // Agora recupera os dados (projetos e mensagens) dos usuários.
        ProjetoDAO daoProj = new ProjetoDAOImpl(conn);
        MensagemDAO daoMsg = new MensagemDAOImpl(conn);
        for (Usuario u : usuarios) { restore(daoProj, daoMsg, u); }

        // Retorna a lista
        return usuarios;
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

    private void saveTelefones(Usuario usuario) throws SQLException {
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

    private void updateTelefones(Usuario usuario) throws SQLException {
        // Deleta todos os telefones do usuário do banco
        deleteTelefones(usuario.getApelido());

        // E então, adiciona todos os novos
        saveTelefones(usuario);
    }

    private void deleteTelefones(String apelido) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_TELEFONES);

        // Define o apelido do usuário ao qual o telefones devem ser excluídos
        stmt.setString(1, apelido);

        // Excluí os telefones
        stmt.execute();
    }

    private List<String> getTelefones(String apelido) throws SQLException {
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

    /*
     *  Recupera os dados do usuário (projetos e mensagens).
     *  Primeiro é necessário adicioná-lo ao cache, para só então requisitar seus projetos,
     *  pois, os projetos mantém uma referência de seus usuários, e no caso de tal usuário
     *  não estar em cache, o ProjetoDAO tentará pegá-lo do banco de forma recursiva.
     */
    private void restore(ProjetoDAO daoProj, MensagemDAO daoMsg, Usuario usuario) throws SQLException {
        // Pega os projetos do usuário
        List<Projeto> projetos = daoProj.getAll(usuario.getApelido());
        usuario.setProjetos(projetos);

        // Pega as mensagens do usuário
        List<Mensagem> mensagens = daoMsg.getAll(usuario.getApelido());
        usuario.setMensagens(mensagens);

        // Atualiza o usuário no cache
        cache.put(usuario.getApelido(), usuario);
    }
}