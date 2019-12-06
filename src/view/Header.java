package view;

import model.bean.Projeto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Header {

    private JPanel pnRoot;

    private Projeto projeto;

    public Header(Projeto projeto, ClickListener inviteListener) {
        this.projeto = projeto;
        setupPanel();
        setupTitle();
        setupDivisor();
        setupUsersPanel(inviteListener);
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

    private void setupTitle() {
        JLabel lbTitle = new JLabel(projeto.getNome(), JLabel.CENTER);
        lbTitle.setBorder(new EmptyBorder(0, 12, 0, 12));
        lbTitle.setFont(new Font("Helvetica", Font.BOLD, 18));
        lbTitle.setForeground(Color.WHITE);

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(8, 8, 8, 4);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = 0;
        constr.weighty = 1.0;

        pnRoot.add(lbTitle, constr);
    }

    private void setupDivisor() {
        JLabel lbDivisor = new JLabel();
        lbDivisor.setBackground(new Color(255, 255, 255, 61));
        lbDivisor.setOpaque(true);
        lbDivisor.setPreferredSize(new Dimension(1, 16));

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(8, 4, 8, 8);
        constr.fill = GridBagConstraints.NONE;
        constr.gridx = 1;

        pnRoot.add(lbDivisor, constr);
    }

    private void setupUsersPanel(ClickListener inviteListener) {
        UsersPanel pnUsers = new UsersPanel(projeto, inviteListener);

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(8, 0, 8, 0);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = 2;

        pnRoot.add(pnUsers.get(), constr);
    }

    private void setupExpansor() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 3;
        constr.weightx = 1.0;
        pnRoot.add(new JLabel(), constr);
    }
}
