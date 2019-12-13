import view.signin.SignInFrame;

public class Program {

    public static void main(String[] args) {
        try {
            SignInFrame frame = new SignInFrame();
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}