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

import com.punyal.medusa.logger.MedusaLogger;
import static com.punyal.medusa.logger.MedusaLogger.GrainLevel.*;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Defaults {
    /* Version Control */
    public static final int MEDUSA_VERSION = 2;
    public static final int MEDUSA_SUBVERSION = 1;
    
    /* Logger */
    public static final String DEFAULT_LOGGER_NAME = "MedusaLog";
    public static final MedusaLogger.GrainLevel DEFAULT_LOGGER_LEVEL = DEB;
    
    /* Web Server */
    public static final int DEFAULT_WEB_PORT = 5000;
    public static final String DEFAULT_WEB_PATH = "/web";
    
    /* Databases */
    // H2
    public static final String DEFAULT_H2_FOLDER = "MedusaH2";
    
    // MySQL
    public static final String DEFAULT_MYSQL_HOST = "localhost";
    public static final String DEFAULT_MYSQL_DATABASE = "Medusa";
    public static final String DEFAULT_MYSQL_USER = "";
    public static final String DEFAULT_MYSQL_PASSWORD = "";
    
    // Tables
    public static final String DEFAULT_DB_TABLE_ADMIN = "ADMIN";
    public static final String DEFAULT_DB_TABLE_ADMIN_INIT = "CREATE TABLE "+
            DEFAULT_DB_TABLE_ADMIN+" ( ID int NOT NULL AUTO_INCREMENT, NAME varchar(255) NOT NULL, PASS varchar(255), PRIMARY KEY (ID));";
    public static final String DEFAULT_DB_TABLE_ADMIN_USER = "admin";
    public static final String DEFAULT_DB_TABLE_ADMIN_PASS = "admin";
    public static final String DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES = "INSERT INTO "+
            DEFAULT_DB_TABLE_ADMIN+"(NAME, PASS) VALUES ('"+DEFAULT_DB_TABLE_ADMIN_USER+
            "', '"+DEFAULT_DB_TABLE_ADMIN_PASS+"')";
    public static final String DEFAULT_DB_TABLE_AUTHENTICATORS = "AUTHENTICATORS";
    public static final String DEFAULT_DB_TABLE_AUTHENTICATORS_INIT = "CREATE TABLE "+
            DEFAULT_DB_TABLE_AUTHENTICATORS+" ( ID int NOT NULL AUTO_INCREMENT, IP varchar(32) NOT NULL, AUTHENTICATOR varchar(32), TIMEOUT BIGINT, PRIMARY KEY (ID));";
    public static final String DEFAULT_DB_TABLE_DEVICES = "DEVICES";
    public static final String DEFAULT_DB_TABLE_DEVICES_INIT = "CREATE TABLE "+
            DEFAULT_DB_TABLE_DEVICES+"( "+Defaults.KEY_DEVICES_ID+" int NOT NULL AUTO_INCREMENT, "+
            Defaults.KEY_DEVICES_NAME+" varchar(255) NOT NULL, "+
            Defaults.KEY_DEVICES_PASSWORD+" varchar(255), "+
            Defaults.KEY_DEVICES_ADDRESS+" varchar(20), "+
            Defaults.KEY_DEVICES_TICKET+" varchar(20), "+
            Defaults.KEY_DEVICES_VALID+" boolean, "+
            Defaults.KEY_DEVICES_EXPIRETIME+" BIGINT, "+
            Defaults.KEY_DEVICES_LASTLOGIN+" BIGINT, "+
            Defaults.KEY_DEVICES_PROTOCOLS+" varchar(255), "+
            Defaults.KEY_DEVICES_TIMEOUT+" int, PRIMARY KEY ("+Defaults.KEY_DEVICES_ID+"));";
    public static final String KEY_DEVICES_ID = "ID";
    public static final String KEY_DEVICES_NAME = "NAME";
    public static final String KEY_DEVICES_PASSWORD = "PASS";
    public static final String KEY_DEVICES_ADDRESS = "ADDRESS";
    public static final String KEY_DEVICES_TICKET = "TICKET";
    public static final String KEY_DEVICES_VALID = "VALID";
    public static final String KEY_DEVICES_EXPIRETIME = "EXPIRETIME";
    public static final String KEY_DEVICES_LASTLOGIN = "LASTLOGIN";
    public static final String KEY_DEVICES_PROTOCOLS = "PROTOCOLS";
    public static final String KEY_DEVICES_TIMEOUT = "TIMEOUT";
    
    /* Authenticator */
    public static final long DEFAULT_AUTHENTICATOR_TIMEOUT = 15000;// miliseconds
    
    /* Ticket */
    public static final int DEFAULT_TICKET_TIMEOUT = 3600000; // 1hour in millis
    
    /* Security */
    public static final String DEFAULT_SECRET_KEY = "nosecretkey";
    
    /* Conversion */
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    
}
