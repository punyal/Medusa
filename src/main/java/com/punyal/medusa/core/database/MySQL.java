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

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MySQL implements IDataBase {
    private final MySQLconf mySQLconf;
    
    public MySQL(String host, String database, String user, String password) {
        mySQLconf = new MySQLconf(host, database, user, password);
    }
    
    public MySQL(MySQLconf mySQLconf) {
        this.mySQLconf = mySQLconf;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://"+mySQLconf.getHost()+"/"+mySQLconf.getDatabase(), mySQLconf.getUser(), mySQLconf.getPassword());
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean isServerON() {
        // Think how to do it
        return true;
    }

    @Override
    public void stopServer() {
        // nothing here
    }

    @Override
    public void startServer() {
        // nothing here
    }

    @Override
    public String getName() {
        return MySQL.class.getSimpleName();
    }
    
}
