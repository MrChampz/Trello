package view.main;

import model.bean.Tarefa;
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

    private MultilineLabel lbTitle;
    private JPanel pnTasks;
    private view.common.Button btAdd;

    private List<Tarefa> tarefas;

    public ColumnCard(String title) {
        super(ROUND, new Color(235, 236, 240), null);

        // Inicializa a lista de tarefas vazia
        tarefas = new ArrayList<>();

        setupTitle(title);
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
        constr.gridy = tarefa.getOrdem();
        constr.weightx = 1.0;

        // Adiciona o card ao layout
        pnTasks.add(new TaskCard(tarefa), constr);

        // Ajusta o tamanho do container
        adjustBounds();
    }

    public void setTitle(String title) {
        lbTitle.setText(title);
    }

    private void setupTitle(String title) {
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
        scroll.setHorizontalScrollBar(new RoundedScrollBar());
        scroll.setBorder(null);

        add(scroll, constr);
    }

    private void setupButton() {
        btAdd = new Button("Adicionar outro cartão", this::click);
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

    private void click() {
        Tarefa t = new Tarefa(0, "Tuts tuts tuts", "T", Tarefa.Prioridade.ALTA, Tarefa.Estado.NOVA, tarefas.size(), null);
        add(t);
    }
}
