package view.main;

import conn.ConnectionFactory;
import model.bean.Projeto;
import model.bean.Usuario;
import model.dao.ProjetoDAO;
import model.dao.ProjetoDAOImpl;
import util.ScreenUtils;
import view.common.Button;
import view.common.Card;
import view.common.TextField;
import view.common.Toolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NewProjectFrame extends JFrame {

    private Card rootCard;
    private TextField projectName;
    private Button saveButton;
    private ProjectCardsPanel parent;

    private Rectangle maxWndSize;

    private Usuario usuario;

    public NewProjectFrame(ProjectCardsPanel parent, Usuario usuario) throws IOException {
        this.parent = parent;
        this.usuario = usuario;
        calculateBounds();
        setupFrame();
        setupRootCard();
        setupToolbar();
        setupProjectNameField();
        setupSaveButton();
    }

    private void calculateBounds() {
        GraphicsConfiguration config = getGraphicsConfiguration();

        // Calcula o tamanho máximo que a janela pode ter
        maxWndSize = ScreenUtils.getMaxWindowSize(config);
    }

    private void setupFrame() throws IOException {
        setTitle("Trello");
        setLayout(new GridBagLayout());

        setMaximizedBounds(maxWndSize);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);

        setBackground(new Color(0, 0, 0, 100));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(ImageIO.read(new File("res/trello_icon.png")));
    }

    private void setupRootCard() {
        rootCard = new Card(600, 165, 6, Color.white);
        add(rootCard, new GridBagConstraints());
    }

    private void setupToolbar() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 8, 3, 8);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 0;

        Toolbar toolbar = new Toolbar(false, false, true);
        toolbar.setColor(new Color(255, 255, 255, 0));

        toolbar.setCloseButtonClickListener(this::dispose);
        toolbar.setCloseButtonColor(new Color(221, 221, 211));
        toolbar.setCloseButtonHoverColor(new Color(221, 221, 211));
        toolbar.setCloseButtonClickColor(new Color(221, 221, 211));
        toolbar.setCloseButtonIcon(new ImageIcon("res/ic_close_dark.png"));

        rootCard.add(toolbar.get(), constr);
    }

    private void setupProjectNameField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 5, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 1;

        projectName = new TextField("Nome do projeto...");
        projectName.setBackground(new Color(221, 221, 211));
        projectName.setRound(8);

        rootCard.add(projectName, constr);
    }

    private void setupSaveButton() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 8, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 2;

        saveButton = new Button("Salvar");
        saveButton.setClickListener(this::onSaveClick);
        saveButton.setPreferredSize(new Dimension(300, 50));
        saveButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        saveButton.setRound(8);

        saveButton.setColor(new Color(97, 189, 79));
        saveButton.setHoverColor(new Color(80, 167, 63));
        saveButton.setClickColor(new Color(75, 158, 59));
        saveButton.setTextColor(Color.white);

        rootCard.add(saveButton.get(), constr);
    }

    private void onSaveClick() {
        // Indica ao usuário que o projeto está sendo salvo
        saveButton.setText("Salvando...");

        try {
            // Obtém uma conexão com o banco e inicializa o DAO
            Connection conn = ConnectionFactory.getConnection();
            ProjetoDAO dao = new ProjetoDAOImpl(conn);

            // Salva no banco
            String name = projectName.getText();
            Projeto projeto = new Projeto(name, usuario);
            dao.save(projeto);

            // Adiciona ao usuário
            List<Projeto> projetos = usuario.getProjetos();
            projetos.add(projeto);
            usuario.setProjetos(projetos);

            // Adiciona ao panel
            parent.add(projeto);

            // Por fim, fecha o frame
            dispose();
        } catch (SQLException ex) {
            // Em caso de erro, retorna ao estado inicial
            saveButton.setText("Salvar");
            ex.printStackTrace();
        }
    }
}