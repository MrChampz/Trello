package view;

import model.bean.Tarefa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskCard extends Card {

    private static final int WIDTH = 256;
    private static final int ROUND = 4;

    private Tarefa tarefa;

    public TaskCard(Tarefa tarefa) {
        super(ROUND, new Color(255, 255, 255), new Color(178, 185, 197));

        this.tarefa = tarefa;
        setupIndicator();
        setupTitle();
        setupIcons();

        // Depois de organizar todos os componentes, define a largura
        setWidth(WIDTH);
        //setMinimumWidth(WIDTH);
    }

    private void setupIndicator() {
        // Instancia o indicador de prioridade
        Indicator ind = new Indicator();

        // Define a cor do indicador, de acordo com a prioridade da tarefa
        switch (tarefa.getPrioridade()) {
            case ALTA:
                ind.setColor(new Color(235, 90, 70));
                break;
            case MEDIA:
                ind.setColor(new Color(255, 159, 26));
                break;
            case BAIXA:
                ind.setColor(new Color(242, 214, 0));
                break;
        }

        // Calcula as margens necessárias
        int marginTop = 6;
        int marginBottom = -3;
        int marginLeft = 8;
        int marginRight = WIDTH - ind.getWidth() - marginLeft;

        // Configura as constraints do GridBagLayout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(marginTop, marginLeft, marginBottom, marginRight);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.gridx = 0; constr.gridy = 0;
        constr.weightx = 1.0;

        // Adiciona o indicador ao card
        add(ind.get(), constr);
    }

    private void setupTitle() {
        // Instancia o label do título
        MultilineLabel lbTitle = new MultilineLabel(tarefa.getTitulo());

        // Define a cor do texto
        lbTitle.setTextColor(new Color(23, 43, 77));

        // E as margens
        lbTitle.setBorder(new EmptyBorder(6, 0, 4, 0));

        // Configura as constraints do GridBarLayout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 5, 8);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 1;
        constr.weightx = 0.5; constr.weighty = 0.5;

        // Adiciona o label ao card
        add(lbTitle.get(), constr);
    }

    private void setupIcons() {
        // Se a tarefa conter descrição, adiciona um ícone indicando
        if (tarefa.getDescricao() != null && !tarefa.getDescricao().isEmpty()) {
            JLabel icDescription = new JLabel();

            // Define o arquivo do ícone
            icDescription.setIcon(new ImageIcon("res/ic_description.png"));

            // E as margens
            icDescription.setBorder(new EmptyBorder(0, 0, 4, 0));

            // Configura as constraints
            GridBagConstraints constr = new GridBagConstraints();
            constr.insets = new Insets(0, 8, 5, 8);
            constr.anchor = GridBagConstraints.PAGE_START;
            constr.fill = GridBagConstraints.HORIZONTAL;
            constr.gridx = 0; constr.gridy = 2;
            constr.weightx = 0.5; constr.weighty = 0.5;

            // Por fim, adiciona ao card
            add(icDescription, constr);
        }
    }
}
