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
package com.punyal.medusa.constants;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Defaults {
    /* Version Control */
    public static final int MEDUSA_VERSION = 2;
    public static final int MEDUSA_SUBVERSION = 1;
    
    /* Web Server */
    public static final int DEFAULT_WEB_PORT = 5000;
    public static final String DEFAULT_WEB_PATH = "/web";
    
    /* Databases */
    // H2
    public static final String DEFAULT_H2_FOLDER = "MedusaH2";
    
    // Tables
    public static final String DEFAULT_DB_TABLE_ADMIN = "ADMIN";
    public static final String DEFAULT_DB_TABLE_ADMIN_INIT = "CREATE TABLE "+
            DEFAULT_DB_TABLE_ADMIN+" ( ID int NOT NULL AUTO_INCREMENT, NAME varchar(255) NOT NULL, PASS varchar(255), PRIMARY KEY (ID));";
    public static final String DEFAULT_DB_TABLE_ADMIN_USER = "admin";
    public static final String DEFAULT_DB_TABLE_ADMIN_PASS = "admin";
    public static final String DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES = "INSERT INTO "+
            DEFAULT_DB_TABLE_ADMIN+"(NAME, PASS) VALUES ('"+DEFAULT_DB_TABLE_ADMIN_USER+
            "', '"+DEFAULT_DB_TABLE_ADMIN_PASS+"')";
    
    
    /* Authenticator */
    public static final long DEFAULT_AUTHENTICATOR_TIMEOUT = 15000;// miliseconds
    
}
