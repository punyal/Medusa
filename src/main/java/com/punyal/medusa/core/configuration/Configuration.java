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
import com.punyal.medusa.core.database.IDataBase;
import com.punyal.medusa.core.database.MySQL;
import com.punyal.medusa.core.database.MySQLconf;
import com.punyal.medusa.core.security.CryptoEngine;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Configuration {
    /* All configurations here */
    private final ConfigurationWebServer confWebServer;
    private final CryptoEngine cryptoEngine;
    private IDataBase database;
    private boolean error;
    private String errorMessage;
    
    public Configuration() {
        /* Set DataBase */
        database = DBtools.orchestrate(new IDataBase[]{new MySQL(DEFAULT_MYSQL_HOST, DEFAULT_MYSQL_DATABASE, DEFAULT_MYSQL_USER, DEFAULT_MYSQL_PASSWORD),new H2(DEFAULT_H2_FOLDER)});
        if (database == null) setConfigurationError("No database");
        else DBtools.initiate(database);
        
        
        /* Load defaults */
        confWebServer = new ConfigurationWebServer();
        cryptoEngine = new CryptoEngine();
        
    }
    
    public void configMySQL(MySQLconf newConf) {
        database = DBtools.orchestrate(new IDataBase[]{new MySQL(newConf), database});
        if (database == null) setConfigurationError("No database");
        else DBtools.initiate(database);
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
    
    public ConfigurationWebServer getConfigurationWebServer() {
        return confWebServer;
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
