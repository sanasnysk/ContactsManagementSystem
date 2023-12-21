package ContactsMS;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MyContacts extends JFrame{

    String imagePath = null;
    public static int currentUserId;
    int pos = 0;
    MyContacts(){ // part  start
        initComponent();

        tblContact.setShowGrid(true);
        tblContact.setGridColor(Color.BLUE);
        tblContact.setSelectionBackground(Color.lightGray);

        JTableHeader tableHeader = tblContact.getTableHeader();
        tableHeader.setForeground(Color.BLUE);
        tableHeader.setFont(new Font("Tahoma", Font.BOLD,14));
        tableHeader.setBackground(new Color(220,220,220));

        //System.out.println(currentUserId + " From Contact");

    }

    public void initComponent(){
        getContentPane().setBackground(Color.WHITE);

        actionListener();
        populateJTable();
        tableMouseClicked();
        mouseListener();

        btnEditContact.setEnabled(false);
        btnDeleteContact.setEnabled(false);

        String cid = lblContactId.getText();
        if (!cid.equals(0)){
            btnRefresh.setEnabled(false);
        }

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
                int uid = currentUserId;

                byte[] img = null;
                if (imagePath != null){
                    try {
                        Path pth = Paths.get(imagePath);
                        img = Files.readAllBytes(pth);

                        contact c = new contact(null,fn,ln,gu,po,em,ad, img,uid);
                        contactQuery cq = new contactQuery();
                        cq.insertContact(c);

                        tableReset();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "No Image");
                }
            }
        });

        btnEditContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(lblContactId.getText());
                String fn = txtFname.getText();
                String ln = txtLname.getText();
                String gu = (String) cboxGroup.getSelectedItem();
                String po = txtPhone.getText();
                String em = txtEmail.getText();
                String ad = areaAddress.getText();
                int uid = currentUserId;

                if (!lblContactId.getText().equals("")){
                    // if the user want to update the data and the image
                    if (imagePath != null){
                        byte[] img = null;
                        try {
                            Path pth = Paths.get(imagePath);
                            img = Files.readAllBytes(pth);

                            contact c = new contact(id,fn,ln,gu,po,em,ad, img,uid);
                            contactQuery cq = new contactQuery();
                            cq.updateContact(c, true);

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }else {
                        contact c = new contact(id,fn,ln,gu,po,em,ad, null,uid);
                        contactQuery cq = new contactQuery();
                        cq.updateContact(c, false);
                    }

                    tableReset();
                }else {
                    JOptionPane.showMessageDialog(null, "Select A Contact From Table");
                }
            }
        });

        btnDeleteContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(lblContactId.getText());

                if (!lblContactId.getText().equals("")){
                    contactQuery cq = new contactQuery();
                    cq.deleteContact(id);

                    tableReset();
                }else {
                    JOptionPane.showMessageDialog(null, "Select A Contact From Table");
                }

            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableReset();
            }
        });

        txtFname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnRefresh.setEnabled(true);
            }
        });

        btnFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pos = 0;
                showData(pos);
            }
        });

        btnLast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pos = tblContact.getRowCount() - 1;
                showData(pos);
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pos != tblContact.getRowCount() - 1){
                    pos++;
                    showData(pos);
                }else {
                    pos = tblContact.getRowCount() - 1;
                    showData(pos);
                }

            }
        });

        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pos != 0){
                    pos--;
                    showData(pos);
                }else {
                    pos = 0;
                    showData(pos);
                }

            }
        });

    }

    private void showData(int index){
        lblContactId.setText(tblContact.getValueAt(index,0).toString());
        txtFname.setText(tblContact.getValueAt(index,1).toString());
        txtLname.setText(tblContact.getValueAt(index,2).toString());
        cboxGroup.setSelectedItem(tblContact.getValueAt(index,3).toString());
        txtPhone.setText(tblContact.getValueAt(index,4).toString());
        txtEmail.setText(tblContact.getValueAt(index,5).toString());
        areaAddress.setText(tblContact.getValueAt(index,6).toString());

        Image pic = ((ImageIcon) tblContact.getValueAt(index, 7))
                .getImage()
                .getScaledInstance(lblPic.getWidth(), lblPic.getHeight(),
                        Image.SCALE_SMOOTH);
        ImageIcon img = new ImageIcon(pic);
        lblPic.setIcon(img);

        btnAddContact.setEnabled(false);
        btnEditContact.setEnabled(true);
        btnDeleteContact.setEnabled(true);
        btnRefresh.setEnabled(true);
    }

    public void populateJTable(){
        contactQuery cq = new contactQuery();
        ArrayList<contact> acList = cq.contactArrayList(currentUserId);
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
                                           .getScaledInstance(70,50,Image.SCALE_SMOOTH));
            rows[i][7] = pic;
        }

        MyModel mmd = new MyModel(rows,colNames);
        tblContact.setModel(mmd);
        tblContact.setRowHeight(50);
        tblContact.getColumnModel().getColumn(7).setPreferredWidth(70);

    }

    public void tableMouseClicked(){
        tblContact.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = tblContact.getSelectedRow();
                lblContactId.setText(tblContact.getValueAt(rowIndex,0).toString());
                txtFname.setText(tblContact.getValueAt(rowIndex,1).toString());
                txtLname.setText(tblContact.getValueAt(rowIndex,2).toString());
                cboxGroup.setSelectedItem(tblContact.getValueAt(rowIndex,3).toString());
                txtPhone.setText(tblContact.getValueAt(rowIndex,4).toString());
                txtEmail.setText(tblContact.getValueAt(rowIndex,5).toString());
                areaAddress.setText(tblContact.getValueAt(rowIndex,6).toString());

                Image pic = ((ImageIcon) tblContact.getValueAt(rowIndex, 7))
                        .getImage()
                        .getScaledInstance(lblPic.getWidth(), lblPic.getHeight(),
                                Image.SCALE_SMOOTH);
                ImageIcon img = new ImageIcon(pic);
                lblPic.setIcon(img);

                btnAddContact.setEnabled(false);
                btnEditContact.setEnabled(true);
                btnDeleteContact.setEnabled(true);
                btnRefresh.setEnabled(true);

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
    }

    public void tableReset(){
        lblContactId.setText("0");
        txtFname.setText("");
        txtLname.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        areaAddress.setText("");
        cboxGroup.setSelectedIndex(0);
        lblPic.setIcon(null);

        populateJTable();

        btnAddContact.setEnabled(true);
        btnEditContact.setEnabled(false);
        btnDeleteContact.setEnabled(false);
        btnRefresh.setEnabled(false);

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
    private JButton btnRefresh;
    private JButton btnEditContact;
    private JLabel lblContactId;
    private JButton btnDeleteContact;
    private JButton btnFirst;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnLast;

    public static void main(String[] args) {
        MyContacts mc = new MyContacts();
        mc.setVisible(true);
    }
}
