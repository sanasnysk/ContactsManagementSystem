package ContactsMS;

import java.sql.*;

public class myConnection {

    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contactsms", "root", "123456");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return con;
    }
}
