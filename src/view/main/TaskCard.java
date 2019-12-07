package view.main;

import model.bean.Tarefa;
import view.common.Card;
import view.common.MultilineLabel;
import view.main.Indicator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskCard extends Card implements Scrollable {

    public static final int WIDTH = 246;
    private static final int ROUND = 4;

    private Tarefa tarefa;

    public TaskCard(Tarefa tarefa) {
        super(ROUND, new Color(255, 255, 255), new Color(178, 185, 197));

        this.tarefa = tarefa;
        setupIndicator();
        setupTitle();
        setupIcons();

        // Depois de organizar todos os componentes, define a largura
        setPreferredWidth(WIDTH);
        setMinimumWidth(WIDTH);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.width;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        final Container viewport = getParent();
        return viewport.getWidth() > getMinimumWidth();
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return true;
    }

    private void setupIndicator() {
        // JPanel que servirá de container pro indicador e pro seu extensor
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

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

        // Configura as constraints do GridBagLayout para o indicador e expansor
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 0, 0);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.gridx = 0; constr.gridy = 0;

        // Adiciona o indicador ao card
        panel.add(ind.get(), constr);

        // Adiciona o expansor
        constr.gridx = 1; constr.gridy = 0;
        constr.weightx = 1.0;
        panel.add(new JLabel(), constr);

        // Configura as constraints do GridBagLayout para o JPanel
        constr.insets = new Insets(6, 0, -3, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 0; constr.gridy = 0;
        constr.weightx = 1.0;
        add(panel, constr);
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
