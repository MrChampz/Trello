
import com.bulenkov.darcula.DarculaLaf;
import model.bean.Usuario;
import model.dao.UsuarioDAO;
import model.dao.UsuarioDAOImpl;

import javax.swing.*;
import java.sql.SQLException;
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
            // USUARIOS
            UsuarioDAO daoUsuario = new UsuarioDAOImpl();

            Usuario felps = daoUsuario.get("felps");

            frame.setNick(felps.getApelido());
            frame.setName(felps.getNome());
            frame.setEmail(felps.getEmail());
            frame.setPass(felps.getSenha());
            frame.setFoto(felps.getFoto());

            // PROJETOS
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
