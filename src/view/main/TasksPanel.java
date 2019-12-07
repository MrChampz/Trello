package view.main;

import model.bean.Projeto;
import model.bean.Tarefa;

import javax.swing.*;
import java.awt.*;

public class TasksPanel {

    private static final int MARGIN_BOTTOM = 30;

    private JPanel pnRoot;

    private Projeto projeto;
    private int maxHeight;

    public TasksPanel(Projeto projeto, int maxHeight) {
        this.projeto = projeto;
        this.maxHeight = maxHeight;
        setupPanel();
        setupColumns();
        setupExpansor();
    }

    public Component get() {
        return pnRoot;
    }

    private void setupPanel() {
        pnRoot = new JPanel();
        pnRoot.setLayout(new GridBagLayout());
        pnRoot.setOpaque(false);
    }

    private void setupColumns() {
        ColumnCard ccNovas = createColumn("Novas", 0);
        ColumnCard ccAndamento = createColumn("Em andamento", 1);
        ColumnCard ccConcluidas = createColumn("Concluídas", 2);

        for (Tarefa t : projeto.getTarefas()) {
            switch (t.getEstado()) {
                case NOVA: ccNovas.add(t); break;
                case ANDAMENTO: ccAndamento.add(t); break;
                case CONCLUIDA: ccConcluidas.add(t); break;
            }
        }
    }

    private void setupExpansor() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0;
        constr.weighty = 1.0;
        constr.gridx = 3;

        pnRoot.add(new JLabel(), constr);
    }

    private ColumnCard createColumn(String title, int gridx) {
        // Constraints do layout
        GridBagConstraints constr = new GridBagConstraints();

        // Cria o card com o título
        ColumnCard column = new ColumnCard(title);

        // Define o container do card
        int pnColumnHeight = maxHeight - MARGIN_BOTTOM;
        JPanel pnColumn = new JPanel();
        pnColumn.setPreferredSize(new Dimension(ColumnCard.WIDTH, pnColumnHeight));
        pnColumn.setLayout(new GridBagLayout());
        pnColumn.setOpaque(false);

        // Adiciona o card ao container
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 0;
        constr.weightx = 1.0; constr.weighty = 0.0;
        pnColumn.add(column, constr);

        // E um expansor, para manter o card sempre justo
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 0; constr.gridy = 1;
        constr.weighty = 1.0;
        pnColumn.add(new JLabel(), constr);

        // Declara os constraints do container no pnRoot
        constr.insets = new Insets(0, 8, MARGIN_BOTTOM, 0);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = gridx; constr.gridy = 0;
        constr.weightx = 0.0; constr.weighty = 1.0;
        constr.gridheight = 2;

        // E então, o adiciona
        pnRoot.add(pnColumn, constr);

        // No final, retorna uma referência para o ColumnCard criado,
        // pois será necessária para adicionar novas tarefas.
        return column;
    }
}
