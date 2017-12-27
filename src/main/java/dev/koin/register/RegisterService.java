/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.register;

import dev.koin.request.RequestService;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akargarm
 */
public class RegisterService {
    
    public static Connection connectToDB() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/koin", "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return con;
        }
    }
    
    public static boolean addToUsersTable(Connection con, String username, String firstName, String lastName, String password, String fileName, InputStream inputStream) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO user (user_name, first_name, last_name, password, keystore_file_name, keystore_file) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, password);
            statement.setString(5, fileName);
            
            if (inputStream != null) {
                statement.setBlob(6, inputStream);
            }
            
            statement.executeUpdate();
            flag = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return flag;
        }
    }
    
    public static ResultSet findUser(Connection con, RequestService req, String username) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM user WHERE user_name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, username);
            rs = statement.executeQuery();
//            sql = "SELECT * FROM user WHERE keystore_file = ?";
//            statement = con.prepareStatement(sql);
//            statement.setString(1, fileName);
//            rs = statement.executeQuery(sql);
            if (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(6));
//                flag = true;
//                req.connectToEthereumWallet(password, path, rs.getString(6));
            }
            rs.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return rs;
//            return flag;
        }
    }
    
    public static boolean closeConnection(Connection con) {
        boolean flag = false;
        try {
            con.close();
            flag = true;
        } catch (SQLException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return flag;
        }
    }
}
