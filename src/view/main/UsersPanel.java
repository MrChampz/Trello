package view.main;

import model.bean.Foto;
import model.bean.Projeto;
import model.bean.Usuario;
import view.common.Button;
import view.common.CircularImageView;
import view.common.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class UsersPanel {

    private JPanel pnRoot;

    private Projeto projeto;
    private int lastIndex;

    public UsersPanel(Projeto projeto, ClickListener inviteListener) {
        this.projeto = projeto;
        setupPanel();
        setupUsers();
        setupButton(inviteListener);
    }

    public Component get() {
        return pnRoot;
    }

    private void setupPanel() {
        pnRoot = new JPanel();
        pnRoot.setLayout(new GridBagLayout());
        pnRoot.setBackground(new Color(0, 0, 0, 0));
    }

    private void setupUsers() {
        try {
            JPanel pnUsers = new JPanel();
            pnUsers.setLayout(new GridBagLayout());
            pnUsers.setBackground(new Color(0, 0, 0, 0));

            GridBagConstraints constr = new GridBagConstraints();
            constr.insets = new Insets(0, -2, 0, 0);

            Usuario proprietario = projeto.getProprietario();
            Foto foto = proprietario.getFoto();
            CircularImageView ivUser = new CircularImageView(foto.getBinary(), 32);
            ivUser.setBadge(new ImageIcon("res/ic_project_owner.png"));

            constr.gridx = lastIndex++;
            pnUsers.add(ivUser.get(), constr);

            for (Usuario user : projeto.getUsuarios()) {
                if (!user.getApelido().equals(proprietario.getApelido())) {
                    foto = user.getFoto();
                    ivUser = new CircularImageView(foto.getBinary(), 32);

                    constr.gridx = lastIndex++;

                    pnUsers.add(ivUser.get(), constr);
                }
            }

            constr.insets = new Insets(2, 4, 0, 0);
            constr.ipadx = 5;
            constr.gridx = 0;
            pnRoot.add(pnUsers, constr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setupButton(ClickListener inviteListener) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 0, 0);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = lastIndex;

        view.common.Button btInvite = new Button("Convidar", inviteListener);
        btInvite.setWidth(80);
        btInvite.setFont(new Font("Helvetica", Font.PLAIN, 14));
        btInvite.setColor(new Color(255, 255, 255, 51));
        btInvite.setHoverColor(new Color(255, 255, 255, 76));
        btInvite.setClickColor(new Color(255, 255, 255, 102));

        pnRoot.add(btInvite.get(), constr);
    }
}
