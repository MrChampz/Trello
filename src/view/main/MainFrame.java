package view.main;

import model.bean.Usuario;
import util.ScreenUtils;
import view.common.Toolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class MainFrame extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JPanel rootPanel;

    private Usuario usuario;

    public MainFrame(Usuario usuario) throws IOException {
        this.usuario = usuario;
        setupFrame();
        setupRootPanel();
        setupToolbar();
        setupProjectsPanel();
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
        rootPanel.add(toolbar.get(), constr);
    }

    private void setupProjectsPanel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 0; constr.gridy = 1;

        rootPanel.add(new ProjectsPanel(usuario), constr);
    }

    private void center() {
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle screen = ScreenUtils.getMaxWindowSize(config);
        int x = (screen.width / 2) - (getWidth() / 2);
        int y = (screen.height / 2) - (getHeight() / 2);
        setLocation(x, y);
    }
}
