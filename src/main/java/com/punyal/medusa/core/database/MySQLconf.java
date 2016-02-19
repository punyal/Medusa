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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MySQLconf {
    private final String host;
    private final String database;
    private final String user;
    private final String password;

    public MySQLconf(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }
    
    public MySQLconf(String[] mySQLconf) {
        this.host = mySQLconf[0];
        this.database = mySQLconf[1];
        this.user = mySQLconf[2];
        this.password = mySQLconf[3];
    }
    
    public String getHost() {
        return host;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPassword() {
        return password;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MySQL server configuration:\n");
        sb.append(" Host: ").append(host.toString()).append("\n");
        sb.append(" Database: ").append(database).append("\n");
        sb.append(" User: ").append(user).append("\n");
        sb.append(" Pass: ").append(password).append("\n");
        return sb.toString();
    }
}
