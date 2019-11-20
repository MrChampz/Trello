package model.dao;

import model.bean.Projeto;
import model.bean.Tarefa;
import model.bean.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static HashMap<Integer, Projeto> pool;
    private static HashMap<String, HashMap<Integer, Projeto>> cache;

    private Connection conn;

    public ProjetoDAOImpl(Connection conn) {
        // Inicializa o pool e o cache
        if (pool == null) {
            pool = new HashMap<>();
            cache = new HashMap<>();
        }
        this.conn = conn;
    }

    @Override
    public void save(Projeto projeto) throws SQLException {
        // Salva o projeto
        int id = saveProjeto(projeto);
        projeto.setId(id);

        // Insere o proprietário como usuário do projeto
        saveUsuarioIntoProjeto(id, projeto.getProprietario());

        // Por fim, insere as tarefas desse projeto
        TarefaDAO dao = new TarefaDAOImpl(conn);
        dao.saveList(id, projeto.getTarefas());

        // Adiciona ao cache, para futuras buscas
        addToCache(projeto);
    }

    @Override
    public void update(Projeto projeto) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(UPDATE);

        // Atualiza o nome
        stmt.setString(1, projeto.getNome());

        // Define o id do projeto e o apelido do proprietário
        stmt.setInt(2, projeto.getId());
        stmt.setString(3, projeto.getProprietario().getApelido());

        // Atualiza os dados no banco
        stmt.executeUpdate();
        stmt.close();

        // Atualiza os usuários participantes
        updateUsuarios(projeto);

        // Atualiza as tarefas do projeto
        TarefaDAO dao = new TarefaDAOImpl(conn);
        dao.updateList(projeto.getId(), projeto.getTarefas());

        // Por fim, atualiza no cache
        addToCache(projeto);
    }

    @Override
    public void delete(int projetoId) throws SQLException {
        // É necessário remover os usuários participantes antes
        deleteUsuarios(projetoId);

        // Também, é necessário excluir todas as tarefas do projeto antes
        TarefaDAO dao = new TarefaDAOImpl(conn);
        dao.deleteAll(projetoId);

        // Assim, é possível excluir o projeto do banco
        PreparedStatement stmt = conn.prepareStatement(DELETE);

        // Define o id do projeto que deverá ser deletado
        stmt.setInt(1, projetoId);

        // Deleta do banco
        stmt.execute();
        stmt.close();

        // Então, remove o projeto do cache
        removeFromCache(projetoId);
    }

    @Override
    public Projeto get(int projetoId) throws SQLException {
        // Primeiramente, verifica se o projeto já não se encontra no pool
        if (pool.containsKey(projetoId)) return pool.get(projetoId);

        PreparedStatement stmt = conn.prepareStatement(SELECT);

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

            rs.close();
            stmt.close();

            // Pega os usuários participantes do projeto
            List<Usuario> participantes = getUsuarios(id);

            // Pega o usuário proprietário
            Usuario proprietario = getProprietario(prop, participantes);

            // Pega as tarefas do projeto
            TarefaDAO dao = new TarefaDAOImpl(conn);
            List<Tarefa> tarefas = dao.get(id);

            // Adiciona o projeto ao cache
            Projeto projeto = new Projeto(id, nome, proprietario, participantes, tarefas);
            addToCache(projeto);

            // Retorna o projeto
            return projeto;
        }

        rs.close();
        stmt.close();

        // Se o fluxo de execução chegar a esse ponto, nenhum projeto foi encontrado
        throw new SQLException("Projeto não encontrado!");
    }

    @Override
    public List<Projeto> getAll(String apelido) throws SQLException {
        // Primeiramente, verifica se já não existe no cache
        if (cache.containsKey(apelido)) {
            List<Projeto> projetos = new ArrayList<>();
            cache.get(apelido).forEach((id, proj) -> projetos.add(proj));
            return projetos;
        }

        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);

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
            List<Usuario> participantes = getUsuarios(id);

            // Pega o usuário proprietário
            Usuario proprietario = getProprietario(prop, participantes);

            // Pega as tarefas do projeto
            TarefaDAO dao = new TarefaDAOImpl(conn);
            List<Tarefa> tarefas = dao.get(id);

            Projeto projeto;
            // Primeiro verifica se o projeto já não está na memória
            if (pool.containsKey(id)) {
                // Se sim, só atualiza os dados
                projeto = pool.get(id);
                projeto.setNome(nome);
                projeto.setProprietario(proprietario);
                projeto.setUsuarios(participantes);
                projeto.setTarefas(tarefas);
            } else {
                // Caso contrário, cria um novo
                projeto = new Projeto(id, nome, proprietario, participantes, tarefas);
            }

            // Atualiza o projeto no cache
            addToCache(projeto);

            // Adiciona à lista de projetos
            projetos.add(projeto);
        }
        // Encerra tudo
        rs.close();
        stmt.close();

        // Retorna a lista
        return projetos;
    }

    @Override
    public void saveUsuarioIntoProjeto(int projetoId, Usuario usuario) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(INSERT_USUARIO);

        // Insere o id do projeto e o apelido do usuário participante
        stmt.setInt(1, projetoId);
        stmt.setString(2, usuario.getApelido());

        // Salva no banco
        stmt.execute();
        stmt.close();
    }

    @Override
    public void deleteUsuarioFromProjeto(int projetoId, String apelido) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_USUARIO);

        // Insere o id do projeto e o apelido do usuário que deve ser retirado
        stmt.setInt(1, projetoId);
        stmt.setString(2, apelido);

        // Retira o usuário
        stmt.execute();
        stmt.close();
    }

    private int saveProjeto(Projeto projeto) throws SQLException {
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

    private void saveUsuarios(Projeto projeto) throws SQLException {
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

    private void updateUsuarios(Projeto projeto) throws SQLException {
        // Deleta todos os usuários do projeto
        deleteUsuarios(projeto.getId());
        // Adiciona a lista de usuários atualizada
        saveUsuarios(projeto);
    }

    private void deleteUsuarios(int projetoId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE_USUARIOS);

        // Insere o id do projeto do qual os usuários devem ser deletados
        stmt.setInt(1, projetoId);

        // Retira os usuários
        stmt.execute();
        stmt.close();
    }

    private List<Usuario> getUsuarios(int projetoId) throws SQLException {
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
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario usuario = dao.get(apelido);

            // Adiciona o usuário à lista
            usuarios.add(usuario);
        }

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

    private void addToCache(Projeto projeto) {
        // Adiciona o projeto ao pool
        pool.put(projeto.getId(), projeto);

        // Itera por todos os usuários participantes do projeto
        for (Usuario u : projeto.getUsuarios()) {
            if (cache.containsKey(u.getApelido())) {
                // Se já houver algum projeto do usuário, adiciona à lista
                cache.get(u.getApelido()).put(projeto.getId(), projeto);
            } else {
                // Se não houver, cria um registro com os projetos do usuário no cache
                HashMap<Integer, Projeto> projetos = new HashMap<>();
                projetos.put(projeto.getId(), projeto);
                cache.put(u.getApelido(), projetos);
            }
        }
    }

    private void removeFromCache(int projetoId) {
        // Remove da lista de projetos de cada usuário participante do projeto
        cache.forEach((b, m) -> m.remove(projetoId));
        // E do pool
        pool.remove(projetoId);
    }
}
