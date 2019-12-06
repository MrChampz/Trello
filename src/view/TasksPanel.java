package view;

import model.bean.Projeto;
import model.bean.Tarefa;

import javax.swing.*;
import java.awt.*;

public class TasksPanel {

    private JPanel pnRoot;
    private ColumnCard ccNovas;
    private ColumnCard ccAndamento;
    private ColumnCard ccConcluidas;

    private Projeto projeto;

    public TasksPanel() {
        setupPanel();
        setupColumns();
        setupExpansor();
    }

    public void loadProjeto(Projeto projeto) {
        this.projeto = projeto;

        for (Tarefa t : projeto.getTarefas()) {
            switch (t.getEstado()) {
                case NOVA: ccNovas.add(t); break;
                case ANDAMENTO: ccAndamento.add(t); break;
                case CONCLUIDA: ccConcluidas.add(t); break;
            }
        }
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
        ccNovas = createColumn("Novas", 0);
        ccAndamento = createColumn("Em andamento", 1);
        ccConcluidas = createColumn("Concluídas", 2);
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
        JPanel pnColumn = new JPanel();
        pnColumn.setLayout(new GridBagLayout());
        pnColumn.setOpaque(false);

        // Adiciona o card ao container
        constr.fill = GridBagConstraints.NONE;
        constr.gridx = 0; constr.gridy = 0;
        constr.weighty = 0.0;
        pnColumn.add(column.get(), constr);

        // E um expansor, para manter o card sempre justo
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 0; constr.gridy = 1;
        constr.weighty = 1.0;
        pnColumn.add(new JLabel(), constr);

        // Declara os constraints do container no pnRoot
        constr.insets = new Insets(0, 8, 30, 0);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = gridx; constr.gridy = 0;
        constr.gridheight = 2;
        constr.weighty = 1.0;

        // E então, o adiciona
        pnRoot.add(pnColumn, constr);

        // No final, retorna uma referência para o ColumnCard criado,
        // pois será necessária para adicionar novas tarefas.
        return column;
    }
}
