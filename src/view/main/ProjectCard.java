package view.main;

import model.bean.Projeto;
import util.ColorUtils;
import view.common.Card;
import view.project.ProjectFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProjectCard extends Card implements MouseListener {

    public static final int WIDTH = 248;
    public static final int HEIGHT = 100;
    private static final int ROUND = 6;

    private ClickListener listener;
    private Projeto projeto;

    public ProjectCard(Projeto projeto) {
        super(WIDTH, HEIGHT, ROUND, new Color(255, 255, 255));
        this.projeto = projeto;
        setupCard();
        setupName();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.onClick(projeto);
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

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    private void setupCard() {
        // Caso não haja uma cor, gera uma aleatória
        if (projeto.getColor() == null) {
            projeto.setColor(ColorUtils.getRandomColor());
        }

        setBackground(ColorUtils.toAwtColor(projeto.getColor()));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    private void setupName() {
        // Instancia o label do nome
        JLabel nameLabel = new JLabel(projeto.getNome());

        // Define a fonte
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));

        // Define a cor do texto
        nameLabel.setForeground(Color.white);

        // Configura as constraints do GridBarLayout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(8, 8, 8, 8);
        constr.anchor = GridBagConstraints.PAGE_START;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0; constr.weighty = 0.5;
        constr.gridx = 0; constr.gridy = 0;

        // Adiciona o label ao card
        add(nameLabel, constr);
    }

    public interface ClickListener {
        void onClick(Projeto projeto);
    }
}
