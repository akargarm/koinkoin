/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.request;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import dev.koin.register.RegisterService;
import dev.koin.transaction.TransactionService;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
//import org.web3j.protocol.exceptions.TransactionTimeoutException;

/**
 *
 * @author akargarm
 */
@WebServlet(name = "RequestServlet", urlPatterns = {"/RequestServlet"})
public class RequestServlet extends HttpServlet {

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
            out.println("<title>Servlet RequestServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RequestServlet at " + request.getContextPath() + "</h1>");
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
        RequestService req = new RequestService();
        TransactionService transaction = new TransactionService();
        
        //User input from HTML form
        String username = request.getParameter("username");
        String koin = request.getParameter("koin");
        BigInteger koinBigInteger = new BigInteger(koin);
//        Part privateKey = request.getPart("key");
        String password = request.getParameter("password");
        
//        BigDecimal etherToExchange = new BigDecimal(koinBigInteger.divide(BigInteger.valueOf(50000)));
        BigDecimal bigIntegerToBigDecimal = new BigDecimal(koinBigInteger);
        BigDecimal conversionRate = BigDecimal.valueOf(50000);
        
        BigDecimal etherToExchange = bigIntegerToBigDecimal.divide(conversionRate);
        
        System.out.println(koinBigInteger.toString());
        System.out.println(etherToExchange.toString());
        
        Connection con = RegisterService.connectToDB();
        //Defining parameters for addFileToKeystore function
//        String path = "src/main/resources";
        ResultSet rs = RegisterService.findUser(con, req, username);
//        String fileName = req.getFileName(privateKey);
//        PrintWriter writer = response.getWriter();
        
        
        
//        req.addFileToKeystore(writer, path, fileName, privateKey);
        Credentials credentials = null;
        try {
//            if (rs.next()) {
                System.out.println("HEREEE");
                
//                credentials = WalletUtils.loadCredentials(password, "src/main/resources/" + rs.getString(6));
//                System.out.println(credentials.getAddress());
//            }
//            if(rs.next()) {
                rs.next();
                credentials = req.connectToEthereumWallet(password, "src/main/resources/", rs.getString(6));
//            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Web3j web3 = req.getWeb3();
        System.out.println("Connected");
        
        boolean koinSupplySufficient = req.koinSupplySufficient(koinBigInteger, credentials);
        boolean etherSupplySufficient = transaction.etherSupplySufficient(web3, credentials, etherToExchange);
        
        if(koinSupplySufficient == true && etherSupplySufficient == true) {
            transaction.sendEther(web3, credentials, etherToExchange);
            req.transferKoin(koinBigInteger, credentials);
            System.out.println("KOIN transfer completed");
        }
        else if(koinSupplySufficient == false) {
            System.out.println("Transfer failed...insufficient KOIN supply");
        }
        
        else if(etherSupplySufficient == false) {
            System.out.println("Transfer failed...insufficient Ether supply");
        }
        
        RegisterService.closeConnection(con);
        
//        final AsyncContext ac = request.startAsync(request, response);
//        ac.setTimeout(10 * 60 * 1000);
//        
//        final Executor watcherExecutor = Executors.newCachedThreadPool();
//        watcherExecutor.execute(new AsyncProcessor(ac));
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