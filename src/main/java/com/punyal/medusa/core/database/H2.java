/*
 * The MIT License
 *
 * Copyright 2016 Pablo Puñal Pereira <pablo.punal@ltu.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.punyal.medusa.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.tools.Server;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class H2 implements IDataBase {
    private Server serverH2DB;
    private final String folderName;
    private boolean status;
    
    private Connection connection;
    
    public H2(String folderName) {
        this.folderName = folderName;
        try {
            serverH2DB = Server.createTcpServer(
                    new String[] { "-tcpPort", "1337", "-tcp",
                        "-tcpSSL", "-tcpAllowOthers" }).start();
            status = true;
        } catch (SQLException ex) {
            serverH2DB = null;
            status = false;
        }
                
        // Actions when exit
        Runtime.getRuntime().addShutdownHook(
            new Thread() {
                @Override
                public void run() {
                    serverH2DB.stop();
                }
            }
        );
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:"+folderName+":"+serverH2DB.getURL().substring(4));
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean isServerON() {
        return status;
    }

    @Override
    public String getName() {
        return H2.class.getSimpleName();
    }

    @Override
    public void stopServer() {
        serverH2DB.stop();
    }

    @Override
    public void startServer() {
        try {
            serverH2DB.start();
        } catch (SQLException ex) {
            Logger.getLogger(H2.class.getName()).log(Level.SEVERE, "error starting server");
        }
    }
    
    
}
