package view.common;

import view.common.Button;
import view.common.ClickListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static javax.swing.SwingConstants.*;

public class Toolbar {

    public static final int HEIGHT = 42;

    private JPanel pnRoot;
    private JLabel lbLogo;
    private Button btHome;
    private Button btClose;

    private boolean showHome;
    private boolean showLogo;
    private boolean showClose;

    public Toolbar() {
        this(true, true, true);
    }

    public Toolbar(boolean showHome, boolean showLogo, boolean showClose) {
        this.showHome = showHome;
        this.showLogo = showLogo;
        this.showClose = showClose;

        setupPanel();
        setupLogo();
        setupButtons();
    }

    public void setHomeButtonClickListener(ClickListener listener) {
        btHome.setClickListener(listener);
    }

    public void setCloseButtonClickListener(ClickListener listener) {
        btClose.setClickListener(listener);
    }

    public void setLogoAlignment(int alignment) {
        if (lbLogo == null) return;
        lbLogo.setHorizontalAlignment(alignment);
        if (alignment == LEADING || alignment == LEFT) {
            lbLogo.setBorder(new EmptyBorder(0, 10, 0, 0));
        } else if (alignment == TRAILING || alignment == RIGHT) {
            lbLogo.setBorder(new EmptyBorder(0, 0, 0, 10));
        }
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
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 1;
        constr.weightx = 1.0;

        if (showLogo) {
            lbLogo = new JLabel();
            lbLogo.setIcon(new ImageIcon("res/trello_logo.png"));
            lbLogo.setVerticalAlignment(SwingConstants.CENTER);
            lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
            pnRoot.add(lbLogo, constr);
        } else {
            pnRoot.add(new JLabel(), constr);
        }
    }

    private void setupButtons() {
        // Define os constraints dos botões
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;

        if (showHome) {
            // Adiciona o botão que dá acesso à página inicial
            btHome = new view.common.Button(new ImageIcon("res/ic_home.png"));
            btHome.setPreferredSize(new Dimension(32, 32));

            constr.insets = new Insets(4, 3, 3, 0);
            constr.gridx = 0;
            pnRoot.add(btHome.get(), constr);
        }

        if (showClose) {
            // Adiciona o botão que fecha a aplicação
            btClose = new Button(new ImageIcon("res/ic_close.png"));
            btClose.setPreferredSize(new Dimension(32, 32));

            constr.insets = new Insets(4, 0, 3, 3);
            constr.gridx = 2;
            pnRoot.add(btClose.get(), constr);
        }
    }
}
