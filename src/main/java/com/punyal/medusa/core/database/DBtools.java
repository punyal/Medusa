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
import com.punyal.medusa.core.MedusaDevice;
import com.punyal.medusa.logger.MedusaLogger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
        log.debug("Initiate database");
        initiateAdminTable(database);
        initiateDevicesTable(database);
    }
    
    private static void initiateAdminTable(IDataBase database) {
        log.debug("Initiate Admin Table");
        try {
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_INIT);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Admin table: "+ex.getMessage());
        }
    }
    
    private static void initiateDevicesTable(IDataBase database) {
        log.debug("Initiate Devices Table");
        try {
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_DEVICES_INIT);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Devices table: "+ex.getMessage());
        }
    }
    
    public static void delete(IDataBase database) {
        deleteAdminTable(database);
        deleteDevicesTable(database);
    }
    
    private static void deleteAdminTable(IDataBase database) {
        try {
            String sql = "DROP TABLE "+DEFAULT_DB_TABLE_ADMIN;
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Deleting Admin table: "+ex.getMessage());
        }
    }
    
    private static void deleteDevicesTable(IDataBase database) {
        try {
            String sql = "DROP TABLE "+DEFAULT_DB_TABLE_DEVICES;
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Deleting Devices table: "+ex.getMessage());
        }
    }
    
    public static void addNewDevice(IDataBase database, String name, String password) {
        addNewDevice(database, name, password, "", "", false, 0, 0, "");
    }
    
    public static void addNewDevice(IDataBase database, String name, String password, String address, String ticket, boolean valid, long timeout, long lastlogin, String protocols) {
        String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES
                    +" WHERE NAME = '"+name+"';";
        try {
            //SELECT * FROM `DEVICES` WHERE `NAME` = 'pablo'
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            if (rs.next()) // This item is already on the table
            {
                log.debug("addNewDevice: "+name+" is already at the database");
                return;
            }
            sql = "INSERT INTO "+DEFAULT_DB_TABLE_DEVICES
                    +"(NAME, PASS, ADDRESS, TICKET, VALID, TIMEOUT, LASTLOGIN, PROTOCOLS) VALUES ('"+name+"','"+password+"','"+address+"','"+ticket+"','"+(valid?1:0)+"','"+timeout+"','"+lastlogin+"','"+protocols+"');";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Adding Devices table: "+ex.getMessage());
        }
    }
    
    public static void deleteDevice(IDataBase database, int id) {
        String sql = "DELETE FROM "+DEFAULT_DB_TABLE_DEVICES+" WHERE ID = "+id;
        try {
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("delete device: "+ex.getMessage());
        }
    }
    
    public static List<MedusaDevice> getDevicesList(IDataBase database) {
        List<MedusaDevice> list = new ArrayList<>();
        
        try {
            ResultSet rs = database.getConnection().createStatement().executeQuery("SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES);
            
            while(rs.next())
                list.add(new MedusaDevice(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getBoolean(6), rs.getLong(7), rs.getLong(8), rs.getString(9)));
            
        } catch (SQLException ex) {
            log.error("getDevicesList: "+ex.getMessage());
        }
        
        return list;
    } 
    
    public static void changeAdminPass(IDataBase database, String newPass) {
        String sql = "UPDATE "+
                    DEFAULT_DB_TABLE_ADMIN+" SET PASS='"+newPass+"' WHERE ID='1';";
        try {
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (NullPointerException|SQLException ex) {
            log.error("Changing Admin Password: "+ex.getMessage());
        }
    }
    
    public static String getTables(IDataBase database) {
        String sql = "SHOW TABLES;";
        ResultSet result;
        StringBuilder sb = new StringBuilder();
        try {
            log.debug(sql);
            result = database.getConnection().createStatement().executeQuery(sql);
            while (result.next())
                sb.append(result.getString(1)).append(" ");
                
        } catch (SQLException ex) {
            log.error("Reading tables: "+ex.getMessage());
        }
        return sb.toString();
    }
}
