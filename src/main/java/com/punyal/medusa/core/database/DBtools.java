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

import static com.punyal.medusa.constants.Defaults.*;
import com.punyal.medusa.logger.MedusaLogger;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class DBtools {
    private static final MedusaLogger log = new MedusaLogger();
    public static IDataBase orchestrate(IDataBase[] databases) {
        log.debug("Database system Orchestration");
        for (IDataBase database: databases) {
            if (database.isServerON() && database.getConnection() != null) {
                log.debug(database.getName()+" (selected)");
                return database;
            }
            log.debug(database.getName()+" (discarted)");
        }
        log.debug("No database system selected");
        return null;
    }
    
    public static void initiate(IDataBase database) {
        log.debug("Initiate Admin Table");
        initiateAdmin(database);
    }
    
    private static void initiateAdmin(IDataBase database) {
        try {
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_INIT);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Admin table: "+ex.getMessage());
        }
    }
    
    public static void delete(IDataBase database) {
        deleteAdmin(database);
    }
    
    public static void deleteAdmin(IDataBase database) {
        try {
            database.getConnection().createStatement().executeUpdate("DROP TABLE "+DEFAULT_DB_TABLE_ADMIN);
            
        } catch (SQLException ex) {
            log.error("Deleting Admin table: "+ex.getMessage());
        }
    }
    
    public static void changeAdminPass(IDataBase database, String newPass) {
        try {
            database.getConnection().createStatement().executeUpdate("UPDATE "+
                    DEFAULT_DB_TABLE_ADMIN+" SET PASS='"+newPass+"' WHERE ID='1';");
        } catch (NullPointerException|SQLException ex) {
            log.error("Changing Admin Password: "+ex.getMessage());
        }
    }
    
    public static String getTables(IDataBase database) {
        ResultSet result;
        StringBuilder sb = new StringBuilder();
        try {
            result = database.getConnection().createStatement().executeQuery("SHOW TABLES;");
            while (result.next())
                sb.append(result.getString(1)).append(" ");
                
        } catch (SQLException ex) {
            log.error("Reading tables: "+ex.getMessage());
        }
        return sb.toString();
    }
}
