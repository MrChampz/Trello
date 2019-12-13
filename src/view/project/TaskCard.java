package view.project;

import model.bean.Tarefa;
import view.common.Card;
import view.common.MultilineLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TaskCard extends Card implements Scrollable, MouseListener {

    public static final int WIDTH = 246;
    private static final int ROUND = 4;

    private MultilineLabel lbTitle;
    private Indicator indicator;
    private JLabel icDesc;
    private GridBagConstraints icDescConstr;

    private ClickListener listener;
    private Tarefa tarefa;

    public TaskCard(Tarefa tarefa) {
        super(ROUND, new Color(255, 255, 255), new Color(178, 185, 197));
        this.tarefa = tarefa;
        setupCard();
        setupIndicator();
        setupTitle();
        setupDescIcon();

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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.onClick(tarefa);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void update() {
        // Atualiza o indicador
        setupIndicatorColor();

        // Atualiza o título da tarefa
        lbTitle.setText(tarefa.getTitulo());

        // Atualiza o ícone de descrição
        String desc = tarefa.getDescricao();
        if (desc != null && !desc.isEmpty()) {
            addDescIcon();
        } else {
            removeDescIcon();
        }

        // Por fim, recalcula os bounds do card
        recalculate();
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    private void setupCard() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    private void setupIndicator() {
        // JPanel que servirá de container pro indicador e pro seu extensor
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        // Instancia o indicador de prioridade
        indicator = new Indicator();

        // Define a cor do indicador, de acordo com a prioridade da tarefa
        setupIndicatorColor();

        // Configura as constraints do GridBagLayout para o indicador e expansor
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 0, 0);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.gridx = 0; constr.gridy = 0;

        // Adiciona o indicador ao card
        panel.add(indicator.get(), constr);

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

    private void setupIndicatorColor() {
        switch (tarefa.getPrioridade()) {
            case ALTA:
                indicator.setColor(new Color(235, 90, 70));
                break;
            case MEDIA:
                indicator.setColor(new Color(255, 159, 26));
                break;
            case BAIXA:
                indicator.setColor(new Color(242, 214, 0));
                break;
        }
    }

    private void setupTitle() {
        // Instancia o label do título
        lbTitle = new MultilineLabel(tarefa.getTitulo());

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

    private void setupDescIcon() {
        // Configura as constraints
        icDescConstr = new GridBagConstraints();
        icDescConstr.insets = new Insets(0, 8, 5, 8);
        icDescConstr.anchor = GridBagConstraints.PAGE_START;
        icDescConstr.fill = GridBagConstraints.HORIZONTAL;
        icDescConstr.gridx = 0; icDescConstr.gridy = 2;
        icDescConstr.weightx = 0.5; icDescConstr.weighty = 0.5;

        // Configura o ícone
        icDesc = new JLabel();
        icDesc.setIcon(new ImageIcon("res/ic_description.png"));
        icDesc.setBorder(new EmptyBorder(0, 0, 4, 0));

        // Por fim, adiciona ao card
        String desc = tarefa.getDescricao();
        if (desc != null && !desc.isEmpty()) {
            addDescIcon();
        }
    }

    private void addDescIcon() {
        add(icDesc, icDescConstr);
    }

    private void removeDescIcon() {
        remove(icDesc);
    }

    public interface ClickListener {
        void onClick(Tarefa tarefa);
    }
}
