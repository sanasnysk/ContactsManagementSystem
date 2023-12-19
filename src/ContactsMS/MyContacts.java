package ContactsMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MyContacts extends JFrame{

    String imagePath = null;
    MyContacts(){
        initComponent();
        actionListener();
        populateJTable();

    }

    public void initComponent(){
        getContentPane().setBackground(Color.WHITE);

        mouseListener();

        setContentPane(mainPanel);
        setUndecorated(true);
        setSize(1250, 750);
        setLocation(300, 50);
    }

    public void actionListener() {
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFunc mf = new MyFunc();
                imagePath = mf.browseImage(lblPic);
            }
        });

        btnAddContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fn = txtFname.getText();
                String ln = txtLname.getText();
                String gu = (String) cboxGroup.getSelectedItem();
                String po = txtPhone.getText();
                String em = txtEmail.getText();
                String ad = areaAddress.getText();

                byte[] img = null;
                try {
                    Path pth = Paths.get(imagePath);
                    img = Files.readAllBytes(pth);
                }catch (Exception ex){
                    ex.printStackTrace();
                }


                contact c = new contact(null,fn,ln,gu,po,em,ad, img,0);
                contactQuery cq = new contactQuery();
                cq.insertContact(c);

                populateJTable();

            }
        });

    }

    public void populateJTable(){
        contactQuery cq = new contactQuery();
        ArrayList<contact> acList = cq.contactArrayList();
        String[] colNames = {"Id", "First Name", "Last Name", "Group", "Phone", "Email", "Address", "Picture"};
        Object[][] rows = new Object[acList.size()][8];

        for (int i = 0; i < acList.size(); i++) {
            rows[i][0] = acList.get(i).getCid();
            rows[i][1] = acList.get(i).getFname();
            rows[i][2] = acList.get(i).getLname();
            rows[i][3] = acList.get(i).getGroupc();
            rows[i][4] = acList.get(i).getPhone();
            rows[i][5] = acList.get(i).getEmail();
            rows[i][6] = acList.get(i).getAddress();

            ImageIcon pic = new ImageIcon(new ImageIcon(acList.get(i)
                                           .getPic()).getImage()
                                           .getScaledInstance(70,40,Image.SCALE_SMOOTH));
            rows[i][7] = pic;
        }

        MyModel mmd = new MyModel(rows,colNames);
        tblContact.setModel(mmd);
        tblContact.setRowHeight(40);
        tblContact.getColumnModel().getColumn(7).setPreferredWidth(70);

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
    }
    private JPanel mainPanel;
    private JPanel panel_1;
    private JLabel lbl_x;
    private JLabel lbl_s;
    private JPanel panel_2;
    private JTextField txtFname;
    private JTextArea areaAddress;
    private JButton btnBrowse;
    private JLabel lblPic;
    private JComboBox cboxGroup;
    public JLabel lblUserPic;
    public JLabel lblUsername;
    private JTextField txtLname;
    private JTextField txtPhone;
    private JButton btnAddContact;
    private JTextField txtEmail;
    private JTable tblContact;
    private JScrollPane jsPane;

    public static void main(String[] args) {
        MyContacts mc = new MyContacts();
        mc.setVisible(true);
    }
}
