/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.register;

import dev.koin.request.RequestService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.web3j.crypto.Credentials;

/**
 *
 * @author akargarm
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
@MultipartConfig
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Collect user credentials
        RequestService req = new RequestService();
        String username = request.getParameter("username");
        System.out.println(username);
        String firstName = request.getParameter("fName");
        System.out.println(firstName);
        String lastName = request.getParameter("lName");
        System.out.println(lastName);
        Part privateKey = request.getPart("key");
        String password = request.getParameter("password");

        //Defining parameters for addFileToKeystore function
        String path = "src/main/resources";
        String fileName = req.getFileName(privateKey);
        PrintWriter writer = response.getWriter();

        //Adding user private key to keystore
        req.addFileToKeystore(writer, path, fileName, privateKey);

//        InputStream inputStream = new FileInputStream(new File(path + "/" + fileName));
        InputStream inputStream = privateKey.getInputStream();

        //Connect to Koin database
        Connection con = RegisterService.connectToDB();
        ResultSet userExists = RegisterService.findUser(con, req, username);
        
        try {
            if(!userExists.next()) {
                Credentials credentials = req.connectToEthereumWallet(password, path, fileName);
                if (credentials.getAddress().isEmpty() == false) {
                    System.out.println("YESSSS");
//                    String encryptedPassword = EncryptionService.encrypt(password);
//                    RegisterService.addToUsersTable(con, username, firstName, lastName, encryptedPassword, fileName, inputStream);
                    RegisterService.addToUsersTable(con, username, firstName, lastName, password, fileName, inputStream);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            userExists.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if(userExists.next()) {
                System.out.println("User already exists");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        RegisterService.closeConnection(con);
        
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/koin", "root", "");
//            String sql = "INSERT INTO user (user_name, first_name, last_name, password, keystore_file_name, keystore_file) values (?, ?, ?, ?, ?, ?)";
//            PreparedStatement statement = con.prepareStatement(sql);
//            statement.setString(1, username);
//            statement.setString(2, firstName);
//            statement.setString(3, lastName);
//            statement.setString(4, password);
//            statement.setString(5, fileName);
//            if (inputStream != null) {
//                statement.setBlob(6, inputStream);
//            }
//            
//            statement.executeUpdate();
            //here sonoo is database name, root is username and password  
//            sql = "SELECT * FROM user WHERE user_name = ?";
//            statement = con.prepareStatement(sql);
//            statement.setString(1, username);
//            ResultSet rs = statement.executeQuery();
////            sql = "SELECT * FROM user WHERE keystore_file = ?";
////            statement = con.prepareStatement(sql);
////            statement.setString(1, fileName);
////            rs = statement.executeQuery(sql);
//            while (rs.next()) {
//                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(6));
//                req.connectToEthereumWallet(password, path, rs.getString(6));
//            }
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
