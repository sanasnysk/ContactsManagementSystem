package ContactsMS;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class contactQuery {

    Connection con = myConnection.getConnection();
    public void insertContact(contact cont){
        boolean contactIsCreated = true;
        PreparedStatement pstmt;
        String query = "INSERT INTO mycontact (fname, lname, groupc, phone, email, address, pic, userid) Values (?,?,?,?,?,?,?,?)";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, cont.getFname());
            pstmt.setString(2, cont.getLname());
            pstmt.setString(3, cont.getGroupc());
            pstmt.setString(4, cont.getPhone());
            pstmt.setString(5, cont.getEmail());
            pstmt.setString(6, cont.getAddress());
            pstmt.setBytes(7, cont.getPic());
            pstmt.setInt(8, cont.getUid());

            if (pstmt.executeUpdate() != 0) {
                JOptionPane.showMessageDialog(null, "New Contact Added");
                contactIsCreated = true;
            } else {
                JOptionPane.showMessageDialog(null, "Someting Wrong");
                contactIsCreated = false;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public ArrayList<contact> contactArrayList(){
        ArrayList<contact> cal = new ArrayList<>();

        Statement st;
        ResultSet rs;
        String q = "SELECT id,fname,lname,groupc,phone,email,address,pic FROM mycontact";
        try {
            st = con.createStatement();
            rs = st.executeQuery(q);

            contact ct;
            while (rs.next()){
                ct = new contact(rs.getInt("id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("groupc"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getBytes("pic"),
                        0);

                cal.add(ct);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cal;
    }

}
