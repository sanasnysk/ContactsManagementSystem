package ContactsMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame{
    Connection con = myConnection.getConnection();
    PreparedStatement pstmt;
    ResultSet rs;
    LoginForm(){
        initLoginForm();
        mouseListener();
        actionListener();

    }

    public void initLoginForm(){
        getContentPane().setBackground(Color.WHITE);

        setContentPane(mainPanel);
        setUndecorated(true);
        setSize(500, 350);
        setLocation(300, 150);

    }

    public void actionListener(){
        showPassCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassCheckBox.isSelected()){
                    txtPassword.setEchoChar((char) 0);
                }else {
                    txtPassword.setEchoChar('★');
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUser.getText();
                String pas = new String(txtPassword.getPassword());
                try {
                    String query = "SELECT username, pass, pic FROM user WHERE username = ? AND pass = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, user);
                    pstmt.setString(2,pas);

                    rs = pstmt.executeQuery();
                    if (rs.next()){
                        MyContacts mcf = new MyContacts();
                        mcf.setVisible(true);
                        //mcf.pack();
                        mcf.setLocationRelativeTo(null);
                        mcf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dispose();

                        mcf.lblUserPic.setIcon(new MyFunc().resizePic(null, rs.getBytes(3), mcf.lblUserPic.getWidth(), mcf.lblUserPic.getHeight()));
                        mcf.lblUsername.setText(rs.getString(1));

                    }else {
                        JOptionPane.showMessageDialog(null, "Login Error");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void mouseListener(){
        lbl_s.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setState(JFrame.ICONIFIED);
            }
        });

        lbl_x.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        lblAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SignupForm suf = new SignupForm();
                suf.setVisible(true);
                //suf.pack();
                suf.setLocationRelativeTo(null);
                suf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
    }

    private JPanel mainPanel;
    private JPanel panel_1;
    private JPanel panel_2;
    private JLabel lbl_s;
    private JLabel lbl_x;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JCheckBox showPassCheckBox;
    private JButton btnLogin;
    private JLabel lblAccount;
    private JButton btnCancel;

    public static void main(String[] args) {
        LoginForm lf = new LoginForm();
        lf.setVisible(true);
    }
}
