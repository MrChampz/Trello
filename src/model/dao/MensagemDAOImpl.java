package model.dao;

import model.bean.Mensagem;
import model.bean.Mensagem.*;
import model.bean.Projeto;
import model.bean.Tarefa;
import model.bean.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MensagemDAOImpl implements MensagemDAO {

    private static final String INSERT     = "INSERT INTO mensagem (remetente, destinatario, texto, estado) " +
                                             "VALUES (?,?,?,?)";
    private static final String UPDATE     = "UPDATE mensagem SET texto = ?, estado = ?, timestamp_visualizacao = ? " +
                                             "WHERE id = ? AND remetente = ? AND destinatario = ?";
    private static final String DELETE     = "DELETE FROM mensagem WHERE id = ?";
    private static final String SELECT     = "SELECT * FROM mensagem WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM mensagem WHERE remetente = ? OR destinatario = ?";
    private static final String SELECT_ALL_SEND     = "SELECT * FROM mensagem WHERE remetente = ?";
    private static final String SELECT_ALL_RECEIVED = "SELECT * FROM mensagem WHERE destinatario = ?";

    private static HashMap<Integer, Mensagem> pool;
    private static HashMap<String, HashMap<Integer, Mensagem>> cache;

    private Connection conn;

    public MensagemDAOImpl(Connection conn) {
        // Inicializa o pool
        if (pool == null) {
            pool = new HashMap<>();
            cache = new HashMap<>();
        }
        this.conn = conn;
    }

    @Override
    public void save(Mensagem mensagem) throws SQLException {
        // O segundo parâmetro faz com que seja retornado o id gerado
        PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

        // Insere os dados do projeto
        stmt.setString(1, mensagem.getRemetente().getApelido());
        stmt.setString(2, mensagem.getDestinatario().getApelido());
        stmt.setString(3, mensagem.getTexto());
        stmt.setInt(4, mensagem.getEstado().toInt());

        // Faz a inserção no banco
        stmt.execute();

        // Salva o id gerado
        int id = Mensagem.ID_DESCONHECIDO;
        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) id =  keys.getInt(1);
        mensagem.setId(id);

        // Encerra tudo
        keys.close();
        stmt.close();

        // Adiciona ao cache, para futuras buscas
        addToCache(mensagem);
    }

    @Override
    public void update(Mensagem mensagem) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(UPDATE);

        // Atualiza o texto, estado e timestamp de visualização
        stmt.setString(1, mensagem.getTexto());
        stmt.setInt(2, mensagem.getEstado().toInt());
        stmt.setTimestamp(3, new Timestamp(mensagem.getTsVisualizacao().getTime()));

        // Define o id da mensagem, o apelido do remetente e o apelido do destinatario
        stmt.setInt(4, mensagem.getId());
        stmt.setString(5, mensagem.getRemetente().getApelido());
        stmt.setString(6, mensagem.getDestinatario().getApelido());

        // Atualiza os dados no banco
        stmt.executeUpdate();
        stmt.close();

        // Por fim, atualiza no cache
        addToCache(mensagem);
    }

    @Override
    public void delete(int mensagemId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(DELETE);

        // Define o id do projeto que deverá ser deletado
        stmt.setInt(1, mensagemId);

        // Deleta do banco
        stmt.execute();
        stmt.close();

        // Então, remove a mensagem do cache
        removeFromCache(mensagemId);
    }

    @Override
    public Mensagem get(int mensagemId) throws SQLException {
        // Primeiramente, verifica se a mensagem já não se encontra no pool
        if (pool.containsKey(mensagemId)) return pool.get(mensagemId);

        PreparedStatement stmt = conn.prepareStatement(SELECT);

        // Define o id da mensagem a ser retornada
        stmt.setInt(1, mensagemId);

        // Tenta pegar a mensagem no banco
        ResultSet rs = stmt.executeQuery();

        // Se houver algum resultado
        if (rs.next()) {
            // Pega as informações da mensagem
            int id = rs.getInt("id");
            String remetenteApelido = rs.getString("remetente");
            String destinatarioApelido = rs.getString("destinatario");
            String texto = rs.getString("texto");
            Estado estado = Estado.fromInt(rs.getInt("estado"));
            Date tsEnvio = new Date(rs.getTimestamp("timestamp_envio").getTime());

            // Verifica se a mensagem já foi visualizada, se sim, salva o timestamp da visualização.
            Date tsVisualizacao = null;
            if (rs.getDate("timestamp_visualizacao") != null) {
                Timestamp ts = rs.getTimestamp("timestamp_visualizacao");
                tsVisualizacao = new Date(ts.getTime());
            }

            rs.close();
            stmt.close();

            // Busca os dados desse usuário no banco
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario remetente = dao.get(remetenteApelido);
            Usuario destinatario = dao.get(destinatarioApelido);

            // Adiciona a mensagem ao cache
            Mensagem mensagem = new Mensagem(
                id,
                remetente,
                destinatario,
                texto,
                estado,
                tsEnvio,
                tsVisualizacao
            );
            addToCache(mensagem);

            // Retorna a mensagem
            return mensagem;
        }

        rs.close();
        stmt.close();

        // Se o fluxo de execução chegar a esse ponto, nenhuma mensagem foi encontrada
        throw new SQLException("Mensagem não encontrada!");
    }

    @Override
    public List<Mensagem> getAll(String apelido) throws SQLException {
        // Primeiramente, verifica se já não existe no cache
        if (cache.containsKey(apelido)) {
            List<Mensagem> mensagens = new ArrayList<>();
            cache.get(apelido).forEach((id, msg) -> mensagens.add(msg));
            return mensagens;
        }

        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);

        // Define o apelido do usuário que deverá ter suas mensagens retornadas
        stmt.setString(1, apelido);
        stmt.setString(2, apelido);

        // Pega as mensagens do usuário
        ResultSet rs = stmt.executeQuery();

        // Itera por tudo que foi retornado
        List<Mensagem> mensagens = new ArrayList<>();
        while (rs.next()) {
            // Pega as informações da mensagem
            int id = rs.getInt("id");
            String remetenteApelido = rs.getString("remetente");
            String destinatarioApelido = rs.getString("destinatario");
            String texto = rs.getString("texto");
            Estado estado = Estado.fromInt(rs.getInt("estado"));
            Date tsEnvio = new Date(rs.getTimestamp("timestamp_envio").getTime());

            // Verifica se a mensagem já foi visualizada, se sim, salva o timestamp da visualização.
            Date tsVisualizacao = null;
            if (rs.getDate("timestamp_visualizacao") != null) {
                Timestamp ts = rs.getTimestamp("timestamp_visualizacao");
                tsVisualizacao = new Date(ts.getTime());
            }

            // Busca os dados desse usuário no banco
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario remetente = dao.get(remetenteApelido);
            Usuario destinatario = dao.get(destinatarioApelido);

            Mensagem mensagem;
            // Primeiro verifica se a mensagem já não está na memória
            if (pool.containsKey(id)) {
                // Se sim, só atualiza os dados
                mensagem = pool.get(id);
                mensagem.setTexto(texto);
                mensagem.setEstado(estado);
                mensagem.setTsVisualizacao(tsVisualizacao);
            } else {
                // Caso contrário, cria uma nova
                mensagem = new Mensagem(
                    id,
                    remetente,
                    destinatario,
                    texto,
                    estado,
                    tsEnvio,
                    tsVisualizacao
                );
            }

            // Atualiza a mensagem no cache
            addToCache(mensagem);

            // Adiciona à lista de mensagens
            mensagens.add(mensagem);
        }
        // Encerra tudo
        rs.close();
        stmt.close();

        // Retorna a lista
        return mensagens;
    }

    @Override
    public List<Mensagem> getAllSent(String apelido) throws SQLException {
        // Primeiramente, verifica se já não existe no cache
        if (cache.containsKey(apelido)) {
            List<Mensagem> mensagens = new ArrayList<>();
            cache.get(apelido).forEach((id, msg) -> {
                var remetente = msg.getRemetente().getApelido();
                if (apelido.equals(remetente)) mensagens.add(msg);
            });
            return mensagens;
        }

        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SEND);

        // Define o apelido do usuário que deverá ter suas mensagens retornadas
        stmt.setString(1, apelido);

        // Pega as mensagens do usuário
        ResultSet rs = stmt.executeQuery();

        // Itera por tudo que foi retornado
        List<Mensagem> mensagens = new ArrayList<>();
        while (rs.next()) {
            // Pega as informações da mensagem
            int id = rs.getInt("id");
            String remetenteApelido = rs.getString("remetente");
            String destinatarioApelido = rs.getString("destinatario");
            String texto = rs.getString("texto");
            Estado estado = Estado.fromInt(rs.getInt("estado"));
            Date tsEnvio = new Date(rs.getTimestamp("timestamp_envio").getTime());

            // Verifica se a mensagem já foi visualizada, se sim, salva o timestamp da visualização.
            Date tsVisualizacao = null;
            if (rs.getDate("timestamp_visualizacao") != null) {
                Timestamp ts = rs.getTimestamp("timestamp_visualizacao");
                tsVisualizacao = new Date(ts.getTime());
            }

            // Busca os dados desse usuário no banco
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario remetente = dao.get(remetenteApelido);
            Usuario destinatario = dao.get(destinatarioApelido);

            Mensagem mensagem;
            // Primeiro verifica se a mensagem já não está na memória
            if (pool.containsKey(id)) {
                // Se sim, só atualiza os dados
                mensagem = pool.get(id);
                mensagem.setTexto(texto);
                mensagem.setEstado(estado);
                mensagem.setTsVisualizacao(tsVisualizacao);
            } else {
                // Caso contrário, cria uma nova
                mensagem = new Mensagem(
                        id,
                        remetente,
                        destinatario,
                        texto,
                        estado,
                        tsEnvio,
                        tsVisualizacao
                );
            }

            // Atualiza a mensagem no cache
            addToCache(mensagem);

            // Adiciona à lista de mensagens
            mensagens.add(mensagem);
        }
        // Encerra tudo
        rs.close();
        stmt.close();

        // Retorna a lista
        return mensagens;
    }

    @Override
    public List<Mensagem> getAllReceived(String apelido) throws SQLException {
        // Primeiramente, verifica se já não existe no cache
        if (cache.containsKey(apelido)) {
            List<Mensagem> mensagens = new ArrayList<>();
            cache.get(apelido).forEach((id, msg) -> {
                var destinatario = msg.getDestinatario().getApelido();
                if (apelido.equals(destinatario)) mensagens.add(msg);
            });
            return mensagens;
        }

        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_RECEIVED);

        // Define o apelido do usuário que deverá ter suas mensagens retornadas
        stmt.setString(1, apelido);

        // Pega as mensagens do usuário
        ResultSet rs = stmt.executeQuery();

        // Itera por tudo que foi retornado
        List<Mensagem> mensagens = new ArrayList<>();
        while (rs.next()) {
            // Pega as informações da mensagem
            int id = rs.getInt("id");
            String remetenteApelido = rs.getString("remetente");
            String destinatarioApelido = rs.getString("destinatario");
            String texto = rs.getString("texto");
            Estado estado = Estado.fromInt(rs.getInt("estado"));
            Date tsEnvio = new Date(rs.getTimestamp("timestamp_envio").getTime());

            // Verifica se a mensagem já foi visualizada, se sim, salva o timestamp da visualização.
            Date tsVisualizacao = null;
            if (rs.getDate("timestamp_visualizacao") != null) {
                Timestamp ts = rs.getTimestamp("timestamp_visualizacao");
                tsVisualizacao = new Date(ts.getTime());
            }

            // Busca os dados desse usuário no banco
            UsuarioDAO dao = new UsuarioDAOImpl(conn);
            Usuario remetente = dao.get(remetenteApelido);
            Usuario destinatario = dao.get(destinatarioApelido);

            Mensagem mensagem;
            // Primeiro verifica se a mensagem já não está na memória
            if (pool.containsKey(id)) {
                // Se sim, só atualiza os dados
                mensagem = pool.get(id);
                mensagem.setTexto(texto);
                mensagem.setEstado(estado);
                mensagem.setTsVisualizacao(tsVisualizacao);
            } else {
                // Caso contrário, cria uma nova
                mensagem = new Mensagem(
                        id,
                        remetente,
                        destinatario,
                        texto,
                        estado,
                        tsEnvio,
                        tsVisualizacao
                );
            }

            // Atualiza a mensagem no cache
            addToCache(mensagem);

            // Adiciona à lista de mensagens
            mensagens.add(mensagem);
        }
        // Encerra tudo
        rs.close();
        stmt.close();

        // Retorna a lista
        return mensagens;
    }

    private void addToCache(Mensagem mensagem) {
        // Adiciona a mensagem ao pool
        pool.put(mensagem.getId(), mensagem);

        String remetente = mensagem.getRemetente().getApelido();
        String destinatario = mensagem.getDestinatario().getApelido();

        // Adiciona a mensagem para o remetente
        if (cache.containsKey(remetente)) {
            // Se já houver alguma mensagem do usuário, adiciona à lista
            cache.get(remetente).put(mensagem.getId(), mensagem);
        } else {
            // Se não houver, cria um registro com as mensagens do usuário no cache
            HashMap<Integer, Mensagem> mensagens = new HashMap<>();
            mensagens.put(mensagem.getId(), mensagem);
            cache.put(remetente, mensagens);
        }

        // Adiciona a mensagem para o destinatario
        if (cache.containsKey(destinatario)) {
            // Se já houver alguma mensagem do usuário, adiciona à lista
            cache.get(destinatario).put(mensagem.getId(), mensagem);
        } else {
            // Se não houver, cria um registro com as mensagens do usuário no cache
            HashMap<Integer, Mensagem> mensagens = new HashMap<>();
            mensagens.put(mensagem.getId(), mensagem);
            cache.put(destinatario, mensagens);
        }
    }

    private void removeFromCache(int mensagemId) {
        // Remove da lista de mensagens de cada usuário
        cache.forEach((b, m) -> m.remove(mensagemId));
        // E do pool
        pool.remove(mensagemId);
    }
}
