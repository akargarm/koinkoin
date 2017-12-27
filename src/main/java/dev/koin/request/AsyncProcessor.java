/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.request;

import dev.koin.transaction.TransactionService;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.servlet.AsyncContext;
import javax.servlet.http.Part;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

/**
 *
 * @author akargarm
 */
public class AsyncProcessor implements Runnable {
    private AsyncContext asyncContext;
    
    public AsyncProcessor(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        
        
    }
}
