package view.project;

import model.bean.Projeto;
import model.bean.Tarefa;
import model.bean.Usuario;
import view.common.MultilineLabel;
import view.common.RoundedScrollBar;
import view.common.Button;
import view.common.Card;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class ColumnCard extends Card {

    public static final int WIDTH = 272;
    private static final int ROUND = 6;

    private static final String TITLE_NOVA = "Novas";
    private static final String TITLE_ANDAMENTO = "Em andamento";
    private static final String TITLE_CONCLUIDA = "Concluídas";

    public enum Tipo { NOVA, ANDAMENTO, CONCLUIDA }

    private MultilineLabel lbTitle;
    private JPanel pnTasks;
    private Button btAdd;

    private Tipo tipo;
    private int lastOrderIndex = 0;

    private Usuario usuario;
    private Projeto projeto;
    private List<Tarefa> tarefas;

    public ColumnCard(Usuario usuario, Projeto projeto, Tipo tipo) {
        super(ROUND, new Color(235, 236, 240));
        this.usuario = usuario;
        this.projeto = projeto;
        this.tipo = tipo;

        // Inicializa a lista de tarefas vazia
        tarefas = new ArrayList<>();

        setupTitle();
        setupTaskPanel();
        setupButton();

        // Depois de organizar todos os componentes, define a largura
        setPreferredWidth(WIDTH);
        setMinimumWidth(WIDTH);
    }

    public void add(Tarefa tarefa) {
        // Primeiro, adiciona a tarefa à lista
        tarefas.add(tarefa);

        // Define as constraints do card
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(4,0,4,3);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = lastOrderIndex = tarefa.getOrdem();
        constr.weightx = 1.0;

        // Adiciona o card ao layout
        TaskCard card = new TaskCard(tarefa);
        card.setClickListener(this::onTaskClick);
        pnTasks.add(card, constr);

        // Ajusta o tamanho do container
        adjustBounds();
    }

    public void update(Tarefa tarefa) {
        if (tarefas.contains(tarefa)) {
            int index = tarefas.indexOf(tarefa);

            // Recupara o card e atualiza
            TaskCard card = (TaskCard)pnTasks.getComponent(index);
            card.update();

            // Ajusta o tamanho do container
            adjustBounds();
        }
    }

    public void remove(Tarefa tarefa) {
        if (tarefas.contains(tarefa)) {
            // Recupera o indice da tarefa
            int index = tarefas.indexOf(tarefa);

            // Remove da lista
            tarefas.remove(tarefa);

            // Remove do panel
            pnTasks.remove(index);

            // Ajusta o tamanho do container
            adjustBounds();
        }
    }

    public void setTitle(String title) {
        lbTitle.setText(title);
    }

    private void setupTitle() {
        String title = "";

        switch (tipo) {
            case NOVA:
                title = TITLE_NOVA;
                break;
            case ANDAMENTO:
                title = TITLE_ANDAMENTO;
                break;
            case CONCLUIDA:
                title = TITLE_CONCLUIDA;
                break;
        }

        // Instancia o label do título
        lbTitle = new MultilineLabel(title);

        // Define a fonte e a cor do texto
        lbTitle.setFont(
            new Font("Helvetica", Font.BOLD, 14),
            new Color(62, 79, 107)
        );

        // E as margens
        lbTitle.setBorder(new EmptyBorder(9,8,9,8));

        // Configura as constraints do GridBarLayout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 8, 5, 8);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0;
        constr.weightx = 0.5;

        // Adiciona o label ao card
        add(lbTitle.get(), constr);
    }

    private void setupTaskPanel() {
        // Configura as constraints do GridBarLayout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 0, 5);
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridy = 1;

        pnTasks = new JPanel();
        pnTasks.setLayout(new GridBagLayout());
        pnTasks.setOpaque(false);

        JScrollPane scroll = new JScrollPane(pnTasks);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBar(new RoundedScrollBar());
        scroll.setBorder(null);

        add(scroll, constr);
    }

    private void setupButton() {
        btAdd = new Button("Adicionar outro cartão", this::onButtonClick);
        btAdd.setBorder(new EmptyBorder(8, 8, 8, 8));
        btAdd.setFont(new Font("Helvetica", Font.PLAIN, 14));
        btAdd.setTextColor(new Color(94, 108, 132));

        btAdd.setColor(new Color(255, 255, 255, 0));
        btAdd.setHoverColor(new Color(0, 0, 0, 30));
        btAdd.setClickColor(new Color(0, 0, 0, 40));

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(4, 8, 8, 8);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 2;

        add(btAdd.get(), constr);
    }

    public void adjustBounds() {
        // Revalida o container inteiro, assim serão recalculadas as medidas
        recalculate();

        int diff = lbTitle.getHeight() + btAdd.getHeight();
        int cardHeight = getPreferredHeight();
        int containerHeight = getParent().getPreferredSize().height;
        int maxHeight = containerHeight - diff;

        if (cardHeight > 0 && maxHeight > 0) { // TODO: Tratar melhor!
            if (cardHeight > maxHeight) {
                setMinimumHeight(containerHeight);
            }
        }
    }

    private void onTaskClick(Tarefa tarefa) {
        openTaskFrame(tarefa);
    }

    private void onButtonClick() {
        try {
            Tarefa tarefa = new Tarefa(
                "",
                "",
                null,
                lastOrderIndex + 1,
                usuario
            );

            switch (tipo) {
                case NOVA:
                    tarefa.setEstado(Tarefa.Estado.NOVA);
                    break;
                case ANDAMENTO:
                    tarefa.setEstado(Tarefa.Estado.ANDAMENTO);
                    break;
                case CONCLUIDA:
                    tarefa.setEstado(Tarefa.Estado.CONCLUIDA);
                    break;
            }

            openTaskFrame(tarefa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openTaskFrame(Tarefa tarefa) {
        try {
            TaskFrame frame = new TaskFrame(this, projeto, tarefa);
            frame.setVisible(true);
            frame.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
