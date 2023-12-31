package ContactsMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignupForm extends JFrame {

    JFileChooser filec;
    String imagePath = null;
    Connection con = myConnection.getConnection();
    SignupForm() {
        initSignUp();
    }

    public void initSignUp() {
        actionListener();
        mouseListener();

        setContentPane(mainPanel);
        setUndecorated(true);
        setSize(580, 780);
        setLocation(300, 100);
    }

    public void actionListener() {
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser();
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = txtFirstname.getText();
                String l = txtLastname.getText();
                String u = txtUsername.getText();
                String pa = new String(txtPassword.getPassword());
                String rpa = new String(txtRepass.getPassword());

                if (verifData()) {

                    try {
                        InputStream img = new FileInputStream(imagePath);

                        PreparedStatement pstmt;

                        String query = "INSERT INTO user (fname,lname,username,pass,pic) Values (?,?,?,?,?)";
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,f);
                        pstmt.setString(2,l);
                        pstmt.setString(3,u);
                        pstmt.setString(4,pa);
                        pstmt.setBlob(5,img);

                        if (isUsernameExist(txtUsername.getText())){
                            JOptionPane.showMessageDialog(null, "Username Already Exists");
                        }else {
                            if (pstmt.executeUpdate() != 0) {
                                JOptionPane.showMessageDialog(null, "Account Created");

                                LoginForm lf = new LoginForm();
                                lf.setVisible(true);
                                dispose();

                            } else {
                                JOptionPane.showMessageDialog(null, "Someting Wrong");
                            }
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm lif = new LoginForm();
                lif.setVisible(true);
                dispose();
            }
        });
    }

    public boolean verifData(){
        String f = txtFirstname.getText();
        String l = txtLastname.getText();
        String u = txtUsername.getText();
        String pa = new String(txtPassword.getPassword());
        String rpa = new String(txtRepass.getPassword());

        if (f.equals("") || l.equals("") || u.equals("") || pa.equals("") || rpa.equals("")){
            JOptionPane.showMessageDialog(null, "One or More Field are Empty");
            return false;
        } else if (!pa.equals(rpa)) {
            JOptionPane.showMessageDialog(null, "Inccorect Password");
            return false;
        } else if (imagePath == null) {
            JOptionPane.showMessageDialog(null, "No Image Selected");
            return false;
        }else {
            return true;
        }
    }

    public boolean isUsernameExist(String un){
        boolean uExist = false;
        String us = txtUsername.getText();
        String pa = new String(txtPassword.getPassword());
        PreparedStatement ps;
        ResultSet rs;

        try {
            String query = "SELECT * FROM user WHERE username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, us);

            rs = ps.executeQuery();
            if (rs.next()){
                uExist = true;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return uExist;
    }

    public void mouseListener() {
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

        lblAlready.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginForm lif = new LoginForm();
                lif.setVisible(true);
                //lif.pack();
                dispose();
            }
        });
    }

    public void fileChooser() {
        MyFunc mf = new MyFunc();
        imagePath = mf.browseImage(lblPicture);

    }

    public ImageIcon resizePic(String picPath) {
        ImageIcon myImg = new ImageIcon(picPath);
        Image img = myImg.getImage().getScaledInstance(lblPicture.getWidth(), lblPicture.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon myPicture = new ImageIcon(img);

        return myPicture;
    }

    private JPanel mainPanel;
    private JPanel panel_1;
    private JLabel lbl_x;
    private JLabel lbl_s;
    private JPanel panel_2;
    private JPasswordField txtPassword;
    private JTextField txtFirstname;
    private JButton btnSubmit;
    private JButton btnCancel;
    private JLabel lblAlready;
    private JButton btnBrowse;
    private JLabel lblPicture;
    private JPasswordField txtRepass;
    private JTextField txtUsername;
    private JTextField txtLastname;

    public static void main(String[] args) {
        SignupForm sf = new SignupForm();
        sf.setVisible(true);
    }
}
