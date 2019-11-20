
import com.bulenkov.darcula.DarculaLaf;
import conn.ConnectionFactory;
import model.bean.Mensagem;
import model.bean.Usuario;
import model.dao.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        TestFrame frame = new TestFrame("Trello");

        try {
            Connection conn = ConnectionFactory.getConnection();

            // USUARIOS
            UsuarioDAO daoUsuario = new UsuarioDAOImpl(conn);
            Usuario felps = daoUsuario.get("felps");
            Usuario alinee = daoUsuario.get("alinee");

            frame.setNick(felps.getApelido());
            frame.setName(felps.getNome());
            frame.setEmail(felps.getEmail());
            frame.setPass(felps.getSenha());
            frame.setFoto(felps.getFoto());

            ConnectionFactory.closeConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
