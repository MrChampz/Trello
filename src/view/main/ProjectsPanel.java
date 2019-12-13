package view.main;

import model.bean.Projeto;
import model.bean.Usuario;
import view.common.RoundedScrollBar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class ProjectsPanel extends JPanel {

    private ProjectCardsPanel cardsPanel;

    private Usuario usuario;

    public ProjectsPanel(Usuario usuario) {
        this.usuario = usuario;
        setupPanel();
        setupProjectLabel();
        setupProjectCardsPanel();
        addAll(usuario.getProjetos());
    }

    public void add(Projeto projeto) {
        cardsPanel.add(projeto);
    }

    public void addAll(List<Projeto> projetos) {
        for (Projeto projeto : projetos) {
            add(projeto);
        }
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(250, 251, 252));
    }

    private void setupProjectLabel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(16, 16, 3, 16);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;

        add(new ProjectLabel(), constr);
    }

    private void setupProjectCardsPanel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 10, 10, 5);
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 0; constr.gridy = 1;

        cardsPanel = new ProjectCardsPanel(usuario);
        JScrollPane scroll = new JScrollPane(cardsPanel);
        setupScrollPane(scroll);

        add(scroll, constr);
    }

    private void setupScrollPane(JScrollPane scroll) {
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBar(new RoundedScrollBar());
        scroll.setBorder(null);

        // Altera o background da viewport
        final JViewport viewport = scroll.getViewport();
        viewport.setBackground(new Color(255, 255, 255, 0));
        scroll.setViewport(viewport);
    }
}
