package view.main;

import view.common.Button;
import view.common.ClickListener;

import javax.swing.*;
import java.awt.*;

public class Toolbar {

    public static final int HEIGHT = 42;

    private JPanel pnRoot;

    public Toolbar(ClickListener homeListener, ClickListener closeListener) {
        setupPanel();
        setupLogo();
        setupButtons(homeListener, closeListener);
    }

    public Component get() {
        return pnRoot;
    }

    public void setBounds(int x, int y, int width, int height) {
        pnRoot.setBounds(x, y, width, height);
    }

    private void setupPanel() {
        pnRoot = new JPanel();
        pnRoot.setLayout(new GridBagLayout());
        pnRoot.setBackground(new Color(0, 0, 0, 38));
    }

    private void setupLogo() {
        JLabel lbLogo = new JLabel();
        lbLogo.setIcon(new ImageIcon("res/trello_logo.png"));
        lbLogo.setVerticalAlignment(SwingConstants.CENTER);
        lbLogo.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 1;
        constr.weightx = 1.0;

        pnRoot.add(lbLogo, constr);
    }

    private void setupButtons(ClickListener homeListener, ClickListener closeListener) {
        // Define os constraints dos botões
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;

        // Adiciona o botão que dá acesso à página inicial
        view.common.Button btHome = new view.common.Button(new ImageIcon("res/ic_home.png"), homeListener);
        btHome.setPreferredSize(new Dimension(32, 32));

        constr.insets = new Insets(4, 3, 3, 0);
        constr.gridx = 0;
        pnRoot.add(btHome.get(), constr);

        // Adiciona o botão que fecha a aplicação
        view.common.Button btClose = new Button(new ImageIcon("res/ic_close.png"), closeListener);
        btClose.setPreferredSize(new Dimension(32, 32));

        constr.insets = new Insets(4, 0, 3, 3);
        constr.gridx = 2;
        pnRoot.add(btClose.get(), constr);
    }
}
