package view.project;

import conn.ConnectionFactory;
import model.bean.Projeto;
import model.bean.Tarefa;
import model.bean.Usuario;
import model.dao.TarefaDAO;
import model.dao.TarefaDAOImpl;
import util.ScreenUtils;
import view.common.*;
import view.common.Button;
import view.common.TextArea;
import view.common.TextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskFrame extends JFrame {

    private Card rootCard;
    private TextField taskTitle;
    private TextArea taskDesc;
    private ComboBox taskPriority;
    private Button deleteButton;
    private Button saveButton;
    private ColumnCard parent;

    private Rectangle maxWndSize;

    private Usuario usuario;
    private Projeto projeto;
    private Tarefa tarefa;

    public TaskFrame(ColumnCard parent, Projeto projeto, Tarefa tarefa) throws IOException {
        this.usuario = usuario;
        this.parent = parent;
        this.projeto = projeto;
        this.tarefa = tarefa;
        calculateBounds();
        setupFrame();
        setupRootCard();
        setupToolbar();
        setupTaskTitleField();
        setupTaskDescField();
        setupTaskPriorityField();
        setupButtons();
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
        rootCard = new Card(600, 520, 6, Color.white);
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

    private void setupTaskTitleField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 5, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 1;

        taskTitle = new TextField("Titulo da tarefa", tarefa.getTitulo());
        taskTitle.setBackground(new Color(221, 221, 211));
        taskTitle.setRound(8);

        rootCard.add(taskTitle, constr);
    }

    private void setupTaskDescField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 5, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.weighty = 1.0;
        constr.gridy = 2;

        taskDesc = new TextArea("Descrição...", tarefa.getDescricao());
        taskDesc.setPreferredSize(new Dimension(100, 300));
        taskDesc.setBackground(new Color(221, 221, 211));
        taskDesc.setRound(8);

        rootCard.add(taskDesc, constr);
    }

    private void setupTaskPriorityField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 5, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.weighty = 1.0;
        constr.gridy = 3;

        Tarefa.Prioridade[] items = new Tarefa.Prioridade[] {
            Tarefa.Prioridade.ALTA,
            Tarefa.Prioridade.MEDIA,
            Tarefa.Prioridade.BAIXA
        };

        ComboBoxModel<Tarefa.Prioridade> model = new DefaultComboBoxModel<>(items);
        taskPriority = new ComboBox("Prioridade", model);
        taskPriority.setSelectedItem(tarefa.getPrioridade());
        taskPriority.setBackground(new Color(221, 221, 211));
        taskPriority.setRound(8);

        rootCard.add(taskPriority, constr);
    }

    private void setupButtons() {
        GridBagConstraints constr = new GridBagConstraints();

        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(200, 50));
        container.setLayout(new GridBagLayout());
        container.setOpaque(false);

        // O botão excluir só deve ser exibido se a tarefa já existir
        if (tarefa.getId() != Tarefa.ID_DESCONHECIDO) {
            constr.insets = new Insets(0, 0, 0, 10);
            constr.fill = GridBagConstraints.HORIZONTAL;
            constr.weightx = 1.0; constr.weighty = 1.0;
            constr.gridx = 0;

            deleteButton = new Button("Excluir");
            deleteButton.setClickListener(this::onDeleteClick);
            deleteButton.setPreferredSize(new Dimension(100, 50));
            deleteButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
            deleteButton.setRound(8);

            deleteButton.setColor(new Color(251, 22, 0));
            deleteButton.setHoverColor(new Color(208, 21, 0));
            deleteButton.setClickColor(new Color(191, 21, 0));
            deleteButton.setTextColor(Color.white);

            container.add(deleteButton.get(), constr);
        }

        // Botão salvar
        constr.insets = new Insets(0, 0, 0, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 1;

        saveButton = new Button("Salvar");
        saveButton.setClickListener(this::onSaveClick);
        saveButton.setPreferredSize(new Dimension(100, 50));
        saveButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        saveButton.setRound(8);

        saveButton.setColor(new Color(97, 189, 79));
        saveButton.setHoverColor(new Color(80, 167, 63));
        saveButton.setClickColor(new Color(75, 158, 59));
        saveButton.setTextColor(Color.white);

        container.add(saveButton.get(), constr);

        // Adiciona o container dos botões
        constr.insets = new Insets(0, 10, 8, 10);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 0; constr.gridy = 4;

        rootCard.add(container, constr);
    }

    private void onDeleteClick() {
        // Indica ao usuário que a tarefa está sendo excluída
        saveButton.setText("Excluindo...");

        try {
            // Obtém uma conexão com o banco e inicializa o DAO
            Connection conn = ConnectionFactory.getConnection();
            TarefaDAO dao = new TarefaDAOImpl(conn);

            // Excluí do banco
            dao.delete(projeto.getId(), tarefa.getId());

            // Remove do projeto
            List<Tarefa> tarefas = projeto.getTarefas();
            tarefas.remove(tarefa);
            projeto.setTarefas(tarefas);

            // Remove da coluna
            parent.remove(tarefa);

            // Por fim, fecha o frame
            dispose();
        } catch (SQLException ex) {
            // Em caso de erro, retorna ao estado inicial
            saveButton.setText("Excluir");
            ex.printStackTrace();
        }
    }

    private void onSaveClick() {
        // Indica ao usuário que a tarefa está sendo salva
        saveButton.setText("Salvando...");

        try {
            // Obtém uma conexão com o banco e inicializa o DAO
            Connection conn = ConnectionFactory.getConnection();
            TarefaDAO dao = new TarefaDAOImpl(conn);

            // Armazena os dados da tarefa
            String title = taskTitle.getText();
            String desc = taskDesc.getText();
            Tarefa.Prioridade priority = (Tarefa.Prioridade)taskPriority.getSelectedItem();

            tarefa.setTitulo(title);
            tarefa.setDescricao(desc);
            tarefa.setPrioridade(priority);

            // Se a tarefa não tiver id definido, deve-se salvar uma nova no banco
            // Caso contrário, atualiza a já existente
            if (tarefa.getId() == Tarefa.ID_DESCONHECIDO) {
                // Salva no banco
                dao.save(projeto.getId(), tarefa);

                // Adiciona ao projeto
                List<Tarefa> tarefas = projeto.getTarefas();
                tarefas.add(tarefa);
                projeto.setTarefas(tarefas);

                // Adiciona à coluna
                parent.add(tarefa);
            } else {
                // Tarefa já existente, só atualiza
                dao.update(projeto.getId(), tarefa);
                parent.update(tarefa);
            }

            // Por fim, fecha o frame
            dispose();
        } catch (SQLException ex) {
            // Em caso de erro, retorna ao estado inicial
            saveButton.setText("Salvar");
            ex.printStackTrace();
        }
    }
}