package view.main;

import model.bean.Projeto;
import model.bean.Usuario;
import view.common.Button;
import view.project.ProjectFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ProjectCardsPanel extends JPanel {

    private static final int COLUMN_COUNT = 3;

    private JPanel projectsPanel;
    private Button newProjectButton;
    private GridBagConstraints projectConstr;

    private int xIndex = 0;
    private int yIndex = 0;

    private Usuario usuario;

    public ProjectCardsPanel(Usuario usuario) {
        this.usuario = usuario;
        setupPanel();
        setupProjectsPanel();
        setupVerticalExpansor();
        setupHorizontalExpansor();
        setupButton();
    }

    public void add(Projeto projeto) {
        removeButtonFromPanel();

        projectConstr.gridx = xIndex++;
        projectConstr.gridy = yIndex;

        ProjectCard projectCard = new ProjectCard(projeto);
        projectCard.setClickListener(this::onProjectClick);
        projectsPanel.add(projectCard, projectConstr);

        if (xIndex > (COLUMN_COUNT - 1)) {
            xIndex = 0;
            yIndex++;
        }

        addButtonToPanel();
        recalculate();
    }

    public void recalculate() {
        revalidate();
        repaint();
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    private void setupProjectsPanel() {
        projectsPanel = new JPanel();
        projectsPanel.setLayout(new GridBagLayout());
        projectsPanel.setOpaque(false);

        add(projectsPanel, new GridBagConstraints());
    }

    private void setupVerticalExpansor() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.VERTICAL;
        constr.weighty = 1.0;
        constr.gridy = 1;
        add(new JLabel(), constr);
    }

    private void setupHorizontalExpansor() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridheight = 2;
        constr.gridx = 1;
        add(new JLabel(), constr);
    }

    private void setupButton() {
        projectConstr = new GridBagConstraints();
        projectConstr.insets = new Insets(0, 6, 8, 6);
        projectConstr.fill = GridBagConstraints.HORIZONTAL;
        projectConstr.weightx = 1.0;

        newProjectButton = new Button("Criar novo projeto");
        newProjectButton.setPreferredSize(new Dimension(ProjectCard.WIDTH, ProjectCard.HEIGHT));
        newProjectButton.setFont(new Font("Helvetica", Font.PLAIN, 14));
        newProjectButton.setTextColor(new Color(23, 43, 77));
        newProjectButton.setColor(new Color(240, 242, 245));
        newProjectButton.setHoverColor(new Color(231, 233, 237));
        newProjectButton.setClickColor(new Color(228, 240, 246));
        newProjectButton.setRound(6);
        newProjectButton.setClickListener(this::onButtonClick);

        addButtonToPanel();
    }

    private void addButtonToPanel() {
        projectConstr.gridx = xIndex;
        projectConstr.gridy = yIndex;
        projectsPanel.add(newProjectButton.get(), projectConstr);
    }

    private void removeButtonFromPanel() {
        projectsPanel.remove(newProjectButton.get());
    }

    private void onProjectClick(Projeto projeto) {
        try {
            ProjectFrame frame = new ProjectFrame(usuario, projeto);
            frame.setVisible(true);
            frame.requestFocus();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void onButtonClick() {
        try {
            NewProjectFrame frame = new NewProjectFrame(this, usuario);
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
