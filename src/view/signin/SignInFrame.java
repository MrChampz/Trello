package view.signin;

import conn.ConnectionFactory;
import model.bean.Usuario;
import model.dao.UsuarioDAO;
import model.dao.UsuarioDAOImpl;
import util.ScreenUtils;
import view.common.*;
import view.main.MainFrame;
import view.signup.SignUpFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SignInFrame extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private JPanel rootPanel;
    private SignInPanel signInPanel;

    public SignInFrame() throws IOException {
        setupFrame();
        setupRootPanel();
        setupToolbar();
        setupSignInPanel();
        center();
    }

    private void setupFrame() throws IOException {
        setTitle("Trello");
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(ImageIO.read(new File("res/trello_icon.png")));
    }

    private void setupRootPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(new Color(0, 121, 191));
        add(rootPanel);
    }

    private void setupToolbar() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 0;
        constr.weightx = 1.0;

        Toolbar toolbar = new Toolbar(false, true, true);
        toolbar.setCloseButtonClickListener(this::dispose);
        toolbar.setLogoAlignment(SwingConstants.LEADING);
        rootPanel.add(toolbar.get(), constr);
    }

    private void setupSignInPanel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 0; constr.gridy = 1;

        signInPanel = new SignInPanel(rootPanel.getBackground());
        signInPanel.setSignInButtonClickListener(this::onSignInButtonClick);
        signInPanel.setSignUpButtonClickListener(this::onSignUpButtonClick);

        rootPanel.add(signInPanel, constr);
    }

    private void center() {
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle screen = ScreenUtils.getMaxWindowSize(config);
        int x = (screen.width / 2) - (getWidth() / 2);
        int y = (screen.height / 2) - (getHeight() / 2);
        setLocation(x, y);
    }

    private void onSignInButtonClick() {
        String email = signInPanel.getEmail();
        String pass = signInPanel.getPassword();

        try {
            UsuarioDAO dao = new UsuarioDAOImpl(ConnectionFactory.getConnection());
            Usuario usuario = dao.login(email, pass);
            gotoMainFrame(usuario);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void onSignUpButtonClick() {
        try {
            SignUpFrame frame = new SignUpFrame();
            frame.setVisible(true);
            dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void gotoMainFrame(Usuario usuario) throws IOException {
        MainFrame frame = new MainFrame(usuario);
        frame.setVisible(true);
        dispose();
    }
}
