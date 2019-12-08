import conn.ConnectionFactory;
import model.bean.Projeto;
import model.dao.ProjetoDAO;
import model.dao.ProjetoDAOImpl;
import view.signin.SignInFrame;

import java.sql.Connection;

public class Program {

    public static void main(String[] args) {
        try {
            // Carrega o projeto do banco
            Connection conn = ConnectionFactory.getConnection();
            ProjetoDAO dao = new ProjetoDAOImpl(conn);
            Projeto projeto = dao.get(1);

            SignInFrame frame = new SignInFrame();
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}