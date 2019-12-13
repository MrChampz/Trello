package view.project;

import model.bean.Projeto;
import model.bean.Usuario;
import util.ColorUtils;
import util.ScreenUtils;
import view.common.Toolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ProjectFrame extends JFrame {

    private JPanel rootPanel;

    private Color background;
    private Rectangle maxWndSize;
    private int maxTaskPanelHeight;

    private Usuario usuario;
    private Projeto projeto;

    public ProjectFrame(Usuario usuario, Projeto projeto) throws IOException {
        this.usuario = usuario;
        this.projeto = projeto;

        calculateBounds();
        setupFrame();
        setupBackground();
        setupRootPanel();
    }

    private void calculateBounds() {
        GraphicsConfiguration config = getGraphicsConfiguration();

        // Calcula o tamanho máximo que a janela pode ter
        maxWndSize = ScreenUtils.getMaxWindowSize(config);

        // Calcula o tamanho máximo que as colunas do projeto podem ter.
        // Aqui é pego a altura máxima da janela, e subtraido desta a altura
        // da Toolbar e do Header (diff).
        int diff = Toolbar.HEIGHT + Header.HEIGHT;
        maxTaskPanelHeight = maxWndSize.height - diff;
    }

    private void setupFrame() throws IOException {
        setTitle("Trello");
        setMaximizedBounds(maxWndSize);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(ImageIO.read(new File("res/trello_icon.png")));
    }

    private void setupBackground() {
        background = ColorUtils.toAwtColor(projeto.getColor());
    }

    private void setupRootPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);
        setupToolbar();
        setupHeader();
        setupTasksPanel();
        add(rootPanel);
    }

    private void setupToolbar() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 0;
        constr.weightx = 1.0;

        Toolbar toolbar = new Toolbar();
        toolbar.setHomeButtonClickListener(this::dispose);
        toolbar.setCloseButtonClickListener(this::dispose);
        rootPanel.add(toolbar.get(), constr);
    }

    private void setupHeader() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 1;
        constr.weightx = 1.0;

        Header header = new Header(projeto);
        rootPanel.add(header.get(), constr);
    }

    private void setupTasksPanel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 0; constr.gridy = 2;
        constr.weightx = 1.0; constr.weighty = 1.0;

        TasksPanel tasksPanel = new TasksPanel(usuario, projeto, maxTaskPanelHeight);
        rootPanel.add(tasksPanel.get(), constr);
    }
}
