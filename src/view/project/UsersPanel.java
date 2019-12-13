package view.project;

import model.bean.Foto;
import model.bean.Projeto;
import model.bean.Usuario;
import view.common.Button;
import view.common.CircularImageView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersPanel {

    private JPanel pnRoot;
    private JPanel pnUsers;

    private Projeto projeto;
    private List<Usuario> membros;
    private int lastIndex;

    public UsersPanel(Projeto projeto) {
        this.projeto = projeto;
        this.membros = new ArrayList<>();
        setupPanel();
        setupUsers();
        setupButton();
    }

    public void recalculate() {
        pnRoot.setPreferredSize(null);
        pnRoot.revalidate();
    }

    public void add(Usuario usuario) throws IOException {
        Usuario proprietario = projeto.getProprietario();
        if (!usuario.getApelido().equals(proprietario.getApelido())) {
            addUser(usuario, false);
        }
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
            GridBagConstraints constr = new GridBagConstraints();
            constr.insets = new Insets(2, 4, 0, 0);
            constr.ipadx = 5;
            constr.gridx = 0;

            pnUsers = new JPanel();
            pnUsers.setLayout(new GridBagLayout());
            pnUsers.setBackground(new Color(0, 0, 0, 0));

            Usuario proprietario = projeto.getProprietario();
            addUser(proprietario, true);

            for (Usuario user : projeto.getMembros()) {
                add(user);
            }

            pnRoot.add(pnUsers, constr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setupButton() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 8, 0, 0);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = lastIndex;

        view.common.Button btInvite = new Button("Convidar", this::onInviteClick);
        btInvite.setWidth(80);
        btInvite.setFont(new Font("Helvetica", Font.PLAIN, 14));
        btInvite.setColor(new Color(255, 255, 255, 51));
        btInvite.setHoverColor(new Color(255, 255, 255, 76));
        btInvite.setClickColor(new Color(255, 255, 255, 102));

        pnRoot.add(btInvite.get(), constr);
    }

    private void addUser(Usuario usuario, boolean proprietario) throws IOException {
        // Primeiro, adiciona à lista
        membros.add(usuario);

        // Configura os constraints
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, -2, 0, 0);
        constr.gridx = lastIndex++;

        // Recupera a foto do usuário
        Foto foto = usuario.getFoto();
        CircularImageView ivUser = new CircularImageView(foto.getBinary(), 32);

        // Se o usuário for proprietário do projeto, adiciona a badge
        if (proprietario) {
            ivUser.setBadge(new ImageIcon("res/ic_project_owner.png"));
        }

        // Adiciona ao panel
        pnUsers.add(ivUser.get(), constr);

        // Reajusta o panel
        recalculate();
    }

    private void onInviteClick() {
        try {
            AddMemberFrame frame = new AddMemberFrame(this, projeto);
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
