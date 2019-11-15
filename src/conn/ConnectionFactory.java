package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";

    private static final String USER = "trello";
    private static final String PASS = "trello";

    private static final String DB = "trello";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception ex) {
            throw new RuntimeException("Erro na conexão com o banco! " + ex);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn == null) return;
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
