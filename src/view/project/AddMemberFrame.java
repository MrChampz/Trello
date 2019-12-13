package view.project;

import conn.ConnectionFactory;
import model.bean.Projeto;
import model.bean.Usuario;
import model.dao.ProjetoDAO;
import model.dao.ProjetoDAOImpl;
import model.dao.UsuarioDAO;
import model.dao.UsuarioDAOImpl;
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

public class AddMemberFrame extends JFrame {

    private Card rootCard;
    private TextField userNick;
    private Button addButton;
    private UsersPanel parent;

    private Rectangle maxWndSize;

    private Projeto projeto;

    public AddMemberFrame(UsersPanel parent, Projeto projeto) throws IOException {
        this.parent = parent;
        this.projeto = projeto;
        calculateBounds();
        setupFrame();
        setupRootCard();
        setupToolbar();
        setupUserNickField();
        setupAddButton();
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

    private void setupUserNickField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 5, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 1;

        userNick = new TextField("Digite o apelido do usuário...");
        userNick.setBackground(new Color(221, 221, 211));
        userNick.setRound(8);

        rootCard.add(userNick, constr);
    }

    private void setupAddButton() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 8, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 2;

        addButton = new Button("Adicionar");
        addButton.setClickListener(this::onAddClick);
        addButton.setPreferredSize(new Dimension(300, 50));
        addButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        addButton.setRound(8);

        addButton.setColor(new Color(97, 189, 79));
        addButton.setHoverColor(new Color(80, 167, 63));
        addButton.setClickColor(new Color(75, 158, 59));
        addButton.setTextColor(Color.white);

        rootCard.add(addButton.get(), constr);
    }

    private void onAddClick() {
        // Indica ao usuário que o projeto está sendo salvo
        addButton.setText("Adicionando...");

        try {
            // Obtém uma conexão com o banco e inicializa o DAO
            Connection conn = ConnectionFactory.getConnection();
            UsuarioDAO userDAO = new UsuarioDAOImpl(conn);
            ProjetoDAO projDAO = new ProjetoDAOImpl(conn);

            // Tenta encontrar o usuário
            String nick = userNick.getText();
            Usuario usuario = userDAO.get(nick);

            // Adiciona o usuário ao projeto
            List<Usuario> membros = projeto.getMembros();
            membros.add(usuario);
            projeto.setMembros(membros);

            // Atualiza no banco
            projDAO.update(projeto);

            // Adiciona ao panel
            parent.add(usuario);

            // Por fim, fecha o frame
            dispose();
        } catch (Exception ex) {
            // Em caso de erro, retorna ao estado inicial
            addButton.setText("Adicionar");
            ex.printStackTrace();
        }
    }
}