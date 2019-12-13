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

    public void setColor(Color color) {
        pnRoot.setBackground(color);
    }

    public void setHomeButtonColor(Color color) {
        btHome.setColor(color);
    }

    public void setHomeButtonHoverColor(Color color) {
        btHome.setHoverColor(color);
    }

    public void setHomeButtonClickColor(Color color) {
        btHome.setClickColor(color);
    }

    public void setHomeButtonIcon(Icon icon) {
        btHome.setIcon(icon);
    }

    public void setCloseButtonColor(Color color) {
        btClose.setColor(color);
    }

    public void setCloseButtonHoverColor(Color color) {
        btClose.setHoverColor(color);
    }

    public void setCloseButtonClickColor(Color color) {
        btClose.setClickColor(color);
    }

    public void setCloseButtonIcon(Icon icon) {
        btClose.setIcon(icon);
    }

    public Component get() {
        return pnRoot;
    }

    private void setupPanel() {
        pnRoot = new JPanel();
        pnRoot.setLayout(new GridBagLayout());
        pnRoot.setBackground(new Color(0, 0, 0, 38));
    }

    private void setupLogo() {
        // Configura os constraints
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0;
        constr.gridx = 1;

        // Adiciona o expansor que manterá os botões nos lugares corretos
        pnRoot.add(new JLabel(), constr);

        if (showLogo) {
            // Se o logo for exibido, configura seus constraints
            constr.insets = new Insets(0, 0, -HEIGHT, 0);
            constr.gridwidth = 3;
            constr.gridx = 0;

            // Abre a partir do arquivo
            Icon icon = new ImageIcon("res/trello_logo.png");
            lbLogo = new JLabel(icon, CENTER);

            // Adiciona ao panel
            pnRoot.add(lbLogo, constr);
        }
    }

    private void setupButtons() {
        // Define os constraints dos botões
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;

        if (showHome) {
            // Adiciona o botão que dá acesso à página inicial
            btHome = new view.common.Button(new ImageIcon("res/ic_home.png"));
            btHome.setBorder(new EmptyBorder(4, 4, 4, 4));

            constr.insets = new Insets(4, 3, 3, 0);
            constr.gridx = 0;
            pnRoot.add(btHome.get(), constr);
        }

        if (showClose) {
            // Adiciona o botão que fecha a aplicação
            btClose = new Button(new ImageIcon("res/ic_close.png"));
            btClose.setBorder(new EmptyBorder(4, 4, 4, 4));

            constr.insets = new Insets(4, 0, 3, 3);
            constr.gridx = 2;
            pnRoot.add(btClose.get(), constr);
        }
    }
}
