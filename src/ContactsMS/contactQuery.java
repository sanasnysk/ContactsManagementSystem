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
            } else {
                JOptionPane.showMessageDialog(null, "Someting Wrong");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void updateContact(contact cont, boolean withImage){
        PreparedStatement pstmt;
        String updateQuery = "";

        if (withImage == true){
            // if the user want to update the contact profile picture to (사용자가 연락처 프로필 사진을 업데이트하려는 경우)
            updateQuery = "UPDATE mycontact SET fname = ?, lname = ?, groupc = ?, phone = ?, email = ?, address = ?, pic = ? WHERE id = ?";
            try {
                pstmt = con.prepareStatement(updateQuery);
                pstmt.setString(1, cont.getFname());
                pstmt.setString(2, cont.getLname());
                pstmt.setString(3, cont.getGroupc());
                pstmt.setString(4, cont.getPhone());
                pstmt.setString(5, cont.getEmail());
                pstmt.setString(6, cont.getAddress());
                pstmt.setBytes(7, cont.getPic());
                pstmt.setInt(8, cont.getCid());

                if (pstmt.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, "Contact Data Edited");
                } else {
                    JOptionPane.showMessageDialog(null, "Someting Wrong");
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            // the user want to keep the same image (사용자는 동일한 이미지를 유지하고 싶어합니다.)
            updateQuery = "UPDATE mycontact SET fname = ?, lname = ?, groupc = ?, phone = ?, email = ?, address = ? WHERE id = ?";

            try {
                pstmt = con.prepareStatement(updateQuery);
                pstmt.setString(1, cont.getFname());
                pstmt.setString(2, cont.getLname());
                pstmt.setString(3, cont.getGroupc());
                pstmt.setString(4, cont.getPhone());
                pstmt.setString(5, cont.getEmail());
                pstmt.setString(6, cont.getAddress());
                pstmt.setInt(7, cont.getCid());

                if (pstmt.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, "Contact Data Edited");
                } else {
                    JOptionPane.showMessageDialog(null, "Someting Wrong");
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


    }

    public void deleteContact(int cid){
        PreparedStatement pstmt;
        String delQuery = "DELETE FROM mycontact WHERE id = ?";

        try {
            pstmt = con.prepareStatement(delQuery);
            pstmt.setInt(1, cid);

            if (pstmt.executeUpdate() != 0) {
                JOptionPane.showMessageDialog(null, "Contact Data Deleted");
            } else {
                JOptionPane.showMessageDialog(null, "Someting Wrong");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public ArrayList<contact> contactArrayList(int userId){
        ArrayList<contact> cal = new ArrayList<>();

        Statement st;
        ResultSet rs;
        String q = "SELECT id,fname,lname,groupc,phone,email,address,pic FROM mycontact WHERE userid = '" + userId + "'";
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
                        userId);

                cal.add(ct);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cal;
    }

}
