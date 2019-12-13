package view.signup;

import conn.ConnectionFactory;
import model.bean.Foto;
import model.bean.Usuario;
import model.dao.UsuarioDAO;
import model.dao.UsuarioDAOImpl;
import util.ScreenUtils;
import view.common.Toolbar;
import view.main.MainFrame;
import view.signin.SignInFrame;
import view.signin.SignInPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SignUpFrame extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private JPanel rootPanel;
    private SignUpFieldsPanel fieldsPanel;

    public SignUpFrame() throws IOException {
        setupFrame();
        setupRootPanel();
        setupToolbar();
        setupFieldsPanel();
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

        Toolbar toolbar = new Toolbar(true, true, true);
        toolbar.setCloseButtonClickListener(this::dispose);

        toolbar.setHomeButtonClickListener(this::onBackButtonClick);
        toolbar.setHomeButtonIcon(new ImageIcon("res/ic_back.png"));

        rootPanel.add(toolbar.get(), constr);
    }

    private void setupFieldsPanel() throws IOException {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 16, 16, 16);
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridy = 1;

        fieldsPanel = new SignUpFieldsPanel();
        fieldsPanel.setSignUpButtonClickListener(this::onSignUpButtonClick);

        rootPanel.add(fieldsPanel, constr);
    }

    private void center() {
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle screen = ScreenUtils.getMaxWindowSize(config);
        int x = (screen.width / 2) - (getWidth() / 2);
        int y = (screen.height / 2) - (getHeight() / 2);
        setLocation(x, y);
    }

    private void onBackButtonClick() {
        try {
            SignInFrame frame = new SignInFrame();
            frame.setVisible(true);
            dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void onSignUpButtonClick() {
        String nick = fieldsPanel.getNick();
        String name = fieldsPanel.getNameField();
        String email = fieldsPanel.getEmail();
        String pass = fieldsPanel.getPassword();
        Foto pic = fieldsPanel.getPicture();

        try {
            UsuarioDAO dao = new UsuarioDAOImpl(ConnectionFactory.getConnection());
            Usuario usuario = new Usuario(nick, name, email, pass, pic, new ArrayList<>());
            dao.save(usuario);
            gotoMainFrame(usuario);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void gotoMainFrame(Usuario usuario) throws IOException {
        MainFrame frame = new MainFrame(usuario);
        frame.setVisible(true);
        dispose();
    }
}
