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
package com.punyal.medusa.core.configuration;

import static com.punyal.medusa.constants.Defaults.*;
import com.punyal.medusa.core.database.DBtools;
import com.punyal.medusa.core.database.H2;
import com.punyal.medusa.core.database.H2conf;
import com.punyal.medusa.core.database.IDataBase;
import com.punyal.medusa.core.database.MySQL;
import com.punyal.medusa.core.database.MySQLconf;
import com.punyal.medusa.core.security.CryptoEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Configuration {
    /* All configurations here */
    private final ConfigurationWebServer confWebServer;
    private final ConfigurationCoAPServer confCoapServer;
    private final CryptoEngine cryptoEngine;
    private IDataBase database;
    private boolean error;
    private String errorMessage;
    private String publicIP;
    private String localIP;
    
    public Configuration(MySQLconf mySQLconf, H2conf h2conf, int coapPort, int webPort) {
        /* Public IP */
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            publicIP = in.readLine();
        } catch(IOException ex) {
            publicIP = "unknown";
        }
        
        try {
            /* Local IP */        
            localIP = "";
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    InetAddress i = (InetAddress) ee.nextElement();
                    System.out.println(i.getHostAddress());
                    localIP = localIP + " [" + i.getHostAddress()+"] ";
                }
            }
        } catch (SocketException ex) {
            localIP = "unknown";
        }
        
        /* Set Database */
        H2 h2 = (h2conf == null ) ?
                new H2(DEFAULT_H2_FOLDER):
                new H2(h2conf);
        MySQL mySQL = (mySQLconf == null) ?
                new MySQL(DEFAULT_MYSQL_HOST, DEFAULT_MYSQL_DATABASE, DEFAULT_MYSQL_USER, DEFAULT_MYSQL_PASSWORD):
                new MySQL(mySQLconf);
        database = DBtools.orchestrate(new IDataBase[]{mySQL, h2});
        if (database == null) setConfigurationError("No database");
        else DBtools.initiate(database);
        
        /* Load defaults */
        confCoapServer = new ConfigurationCoAPServer(coapPort);
        confWebServer = new ConfigurationWebServer(webPort);
        cryptoEngine = new CryptoEngine();
    }
    
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Humman readable configuration
        sb.append("Medusa Configuration\n");
        sb.append("Version: ").append(MEDUSA_VERSION).append(".").append(MEDUSA_SUBVERSION).append("\n");
        sb.append("[Database]\n");
        if (database == null) sb.append(" No database selected!\n");
        else sb.append(" Type:").append(database.getName()).append("\n").append(" Tables: ").append(DBtools.getTables(database)).append("\n");
        sb.append("[Web Services]\n");
        sb.append(" - Port:").append(confWebServer.getPort()).append("\n");
        sb.append(" - FilesPath:").append(confWebServer.getFilesPath()).append("\n");
        sb.append("[CoAP Services]\n");
        return sb.toString();
    }
    
    public IDataBase getDatabase() {
        return database;
    }
    
    public String getPublicIP() {
        return publicIP;
    }
    
    public String getLocalIP() {
        return localIP;
    }
    
    public ConfigurationWebServer getConfigurationWebServer() {
        return confWebServer;
    }
    
    public ConfigurationCoAPServer getConfigurationCoapServer() {
        return confCoapServer;
    }
    
    public CryptoEngine getCryptoEngine() {
        return cryptoEngine;
    }
    
    public void setConfigurationError(String errorMessage) {
        this.errorMessage = errorMessage;
        error = true;
    }
    
    public boolean isOK() {
        return !error;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
