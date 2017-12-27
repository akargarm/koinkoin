/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.request;

import dev.koin.KoinToken_sol_KoinToken;
import dev.koin.transaction.TransactionService;
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
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
//import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

/**
 *
 * @author Terrapin
 */
public class RequestService {

    Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
    Credentials credentials;
    Credentials userCredentials;
    KoinToken_sol_KoinToken koinToken;
//    public static void main(String[] args) {

    public RequestService() {
        try {
    this.credentials = WalletUtils.loadCredentials("test", "src/main/resources/UTC--2017-09-05T23-58-08.153000000Z--0146e80a7f3fee9c789a779fac835bda983ea2c8.json");
        } catch (IOException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CipherException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.koinToken = KoinToken_sol_KoinToken.load("0x13B507B9554B231eBFdAF7B99Af96890D4b58A53",
                web3, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);

//        try {
//            this.credentials = WalletUtils.loadCredentials("test", "src/main/resources/UTC--2017-09-05T23-58-08.153000000Z--0146e80a7f3fee9c789a779fac835bda983ea2c8.json");
//        } catch (IOException ex) {
//            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (CipherException ex) {
//            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void requestVersion() {
        try {
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("Client version: " + clientVersion);
        } catch (InterruptedException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public String getKoinSymbol() {
//        String symbolAsString = null;
//        try {
////            KoinToken_sol_KoinToken koinToken = KoinToken_sol_KoinToken.load("0x13B507B9554B231eBFdAF7B99Af96890D4b58A53",
////                    web3, credentials, BigInteger.ZERO, BigInteger.ZERO);
//            Utf8String symbol = koinToken.symbol().get();
//            symbolAsString = symbol.getValue();
////            return symbolAsString;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return symbolAsString;
//    }
    
    public String getKoinSymbol() {
        String symbol = null;
        try {
            symbol = koinToken.symbol().sendAsync().get();
        } catch (InterruptedException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return symbol;
    }

    public boolean createWalletFile(String password) {
        boolean flag = false;
        Path p1 = Paths.get("src/main/resources");

        return flag;
    }

    public boolean checkIfValidAddress(String address) {
        if (WalletUtils.isValidAddress(address)) {
            return true;
        }
        return false;
    }

    public String getFileName(Part part) {
        String partHeader = part.getHeader("content-disposition");
//        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public void addFileToKeystore(PrintWriter writer, String path, String fileName, Part filePart) {
        OutputStream out = null;
        InputStream fileContent = null;

        try {
            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            fileContent = filePart.getInputStream();

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + path);
//            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
//                    new Object[]{fileName, path});
        } catch (Exception e) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + e.getMessage());

//            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
//                    new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileContent != null) {
                try {
                    fileContent.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    public Credentials connectToEthereumWallet(String password, String pathToWalletFile, String walletFileName) {
        try {
            this.credentials = WalletUtils.loadCredentials(password, pathToWalletFile + "/" + walletFileName);
            System.out.println("Credentials loaded...");
            return credentials;
        } catch (IOException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CipherException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credentials;
    }

//    public Uint256 getBalance(String address) {
//        Uint256 balance = null;
//        try {
////            balance = koinToken.balanceOf(new Address(address)).get();
//            balance = koinToken.balanceOf(address);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        Uint256 balance = koinToken.balanceOf(address);
//        return balance;
//    }
    
    public BigInteger getBalance(Credentials credentials) {
        BigInteger balance = null;
        try {
            balance = koinToken.balanceOf(credentials.getAddress()).sendAsync().get();
        } catch (Exception ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

//    public boolean transferKoin(BigDecimal koin) {
//        String userAddress = credentials.getAddress();
//        System.out.println("User address is " + userAddress + " requesting " + koin.toString() + " koin");
//        Address srcAddress = new Address("0x0146e80a7f3fee9c789a779fac835bda983ea2c8");
//        Address toAddress = new Address(userAddress);
//
//        //        koinToken.transferFrom(new Address("0x0146e80a7f3fee9c789a779fac835bda983ea2c8"), new Address(userAddress), new Uint256(koin.toBigInteger()));
//        koinToken.transferFrom(srcAddress, toAddress, new Uint256(koin.toBigInteger()));
//        return true;
//    }
    public boolean transferKoin(BigInteger koin, Credentials recipientCred) {
        try {
//            System.out.println(recipientCred.getAddress().toString());
            this.credentials = WalletUtils.loadCredentials("test", "src/main/resources/UTC--2017-09-05T23-58-08.153000000Z--0146e80a7f3fee9c789a779fac835bda983ea2c8.json");
            System.out.println("I'm here");
        } catch (Exception e) {
            System.err.println("Caked");
        }
        try {
            //        String srcAddressString = credentials.getAddress();
//        Address srcAddress = new Address(srcAddressString);
//        this.credentials = connectToEthereumWallet(password, pathToWalletFile, walletFileName);
//        String toAddressString = credentials.getAddress(); 
//        Address toAddress = new Address(toAddressString);
//        Uint256 value = new Uint256(koin.toBigInteger());
//        koinToken.transferFrom(srcAddressString, toAddressString, koin.toBigInteger());
//            BigInteger value = koin.toBigInteger();
//            System.out.println(value.toString());
            TransactionReceipt koinReceipt;
            this.getBalance(credentials);
            koinReceipt = koinToken._transfer(recipientCred.getAddress(), koin).sendAsync().get();
        } catch (Exception e) {
            System.err.println("Still caked");
        }
        return true;
    }
    
    public boolean koinSupplySufficient(BigInteger koin, Credentials recipientCred) {
        boolean flag = false;
        try {
            this.credentials = WalletUtils.loadCredentials("test", "src/main/resources/UTC--2017-09-05T23-58-08.153000000Z--0146e80a7f3fee9c789a779fac835bda983ea2c8.json");
            if(this.getBalance(credentials).compareTo(koin) >= 0) {
                System.out.println("Sufficient koin funds");
                flag = true;
            }
            else {
                flag = false;
            }
        } catch (Exception e) {
            System.err.println("Failed");
        }
        finally {
            return flag;
        }
    }
    
    public boolean transferEther(String password, String path, String walletFile) {
        boolean flag = false;
        try {
            TransactionReceipt transactionReceipt;
            System.out.println("Present Project Directory : " + System.getProperty("user.dir"));
            transactionReceipt = Transfer.sendFunds(web3, credentials, "0x0146e80a7f3fee9c789a779fac835bda983ea2c8", BigDecimal.valueOf(0.2), Convert.Unit.ETHER).sendAsync().get();
            System.out.println("Funds transfer completed: " + transactionReceipt.getBlockHash());
            flag = true;
        } catch (Exception e) {
            System.err.println("Failure");
            flag = false;
        }
//        if (flag == true) {
//            transferKoin(password, path, walletFile, BigDecimal.valueOf(1000));
//        }
        return flag;
    }
        
    public Web3j getWeb3() {
        return this.web3;
    }
    
    public Credentials getCredentials() {
        return this.credentials;
    }
}
