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
import com.punyal.medusa.utils.DateUtils;
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
    
    /***************************************************************************
    *                       GENERIC DATABASE FUNCTIONALITIES                   *
    ***************************************************************************/
    
    /* Initiate DBs */
    public static void initiate(IDataBase database) {
        log.debug("Initiate database");
        initiateAdminTable(database);
        initiateDevicesTable(database);
        initiateAuthenticatorsTable(database);
    }
    
    /* Delete DBs */
    public static void delete(IDataBase database) {
        deleteAdminTable(database);
        deleteDevicesTable(database);
        deleteAuthenticatorsTable(database);
    }
    
    /* List all Tables */
    public static String getTables(IDataBase database) {
        ResultSet result;
        StringBuilder sb = new StringBuilder();
        try {
            String sql = "SHOW TABLES;";
            log.debug(sql);
            result = database.getConnection().createStatement().executeQuery(sql);
            while (result.next())
                sb.append(result.getString(1)).append(" ");
                
        } catch (SQLException ex) {
            log.error("Reading tables: "+ex.getMessage());
        }
        return sb.toString();
    }
    
    
    /***************************************************************************
    *                       SPECIFIC DATABASE FUNCTIONALITIES                  *
    ***************************************************************************/
    
    /*--------------------------[ADMIN]---------------------------------------*/
    private static void initiateAdminTable(IDataBase database) {
        log.debug("Initiate Admin Table");
        try {
            log.debug(DEFAULT_DB_TABLE_ADMIN_INIT);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_INIT);
            log.debug(DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_ADMIN_ADD_INITIAL_VALUES);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Admin table: "+ex.getMessage());
        }
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
    
    public static void changeAdminPass(IDataBase database, String newPass) {
        try {
            String sql = "UPDATE "+
                    DEFAULT_DB_TABLE_ADMIN+" SET PASS='"+newPass+"' WHERE ID='1';";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (NullPointerException|SQLException ex) {
            log.error("Changing Admin Password: "+ex.getMessage());
        }
    }
    
    /*--------------------------[AUTHENTICATORS]------------------------------*/
    private static void initiateAuthenticatorsTable(IDataBase database) {
        // For security reasons this table must be deleted on each restart
        deleteAuthenticatorsTable(database);
        log.debug("Initiate Authenticators Table");
        try {            
            log.debug(DEFAULT_DB_TABLE_AUTHENTICATORS_INIT);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_AUTHENTICATORS_INIT);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Authenticators table: "+ex.getMessage());
        }
    }
    
    private static void deleteAuthenticatorsTable(IDataBase database) {
        log.debug("Delete Authenticators Table");
        try {
            String sql = "DROP TABLE "+DEFAULT_DB_TABLE_AUTHENTICATORS;
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Deleting Authenticators table: "+ex.getMessage());
        }
    }
    
    public static void addNewAuthenticator(IDataBase database, String authenticator, String address, long timeout) {
        log.debug("Adding new Authenticator");
        try {
            String sql = "INSERT INTO "+DEFAULT_DB_TABLE_AUTHENTICATORS
                    +"(IP, AUTHENTICATOR, TIMEOUT) VALUES ('"+address+"','"+authenticator+"','"+timeout+"');";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Adding new Authenticator: "+ex.getMessage());
        }
    }
    
    private static void updateAuthenticatorsList(IDataBase database) {
        log.debug("Updating Authenticators List");
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_AUTHENTICATORS
                    +" WHERE TIMEOUT < "+System.currentTimeMillis()+";";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            List<Integer> toRemove = new ArrayList<>();
            while(rs.next()) {
                toRemove.add(rs.getInt("ID"));
            }
            removeAuthenticators(database, toRemove);
        } catch (SQLException ex) {
            log.error("Updating Authenticators List: "+ex.getMessage());
        }
    }
    
    private static void removeAuthenticators(IDataBase database, List<Integer> idList) {
        log.debug("Removing Authenticators");
        if (idList.size() < 1) {
            log.debug("No Authenticators to remove");
            return;
        }
        StringBuilder list = new StringBuilder();
        idList.stream().forEach((id) -> {
            list.append(",").append(id);
        });
        
        try {
            String sql = "DELETE FROM "+DEFAULT_DB_TABLE_AUTHENTICATORS
                    +" WHERE ID IN ("+list.substring(1)+");";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Deleting Authenticators: "+ex.getMessage());
        }
        
    }
    
    public static List<String> findAuthenticatorsByAddress(IDataBase database, String address) {
        updateAuthenticatorsList(database);
        log.debug("Finding Authenticators by Address");
        List<String> authenticatorList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_AUTHENTICATORS
                    +" WHERE IP = '"+address+"';";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            log.debug("Found Authenticators:");
            while(rs.next()) {
                log.debug("["+rs.getString("AUTHENTICATOR")+"] valid till "+DateUtils.long2DateMillis(rs.getLong("TIMEOUT")));
                authenticatorList.add(rs.getString("AUTHENTICATOR"));
            }
        } catch (SQLException ex) {
            log.error("Finding Authenticators by Address: "+ex.getMessage());
        }
        return authenticatorList;        
    }
    
    /*--------------------------[DEVICES]-------------------------------------*/
    private static void initiateDevicesTable(IDataBase database) {
        log.debug("Initiate Devices Table");
        try {
            log.debug(DEFAULT_DB_TABLE_DEVICES_INIT);
            database.getConnection().createStatement().executeUpdate(DEFAULT_DB_TABLE_DEVICES_INIT);
            
        } catch (SQLException ex) {
            log.debug("Error initiating Devices table: "+ex.getMessage());
        }
    }
    
    private static void deleteDevicesTable(IDataBase database) {
        log.debug("Delete Devices Table");
        try {
            String sql = "DROP TABLE "+DEFAULT_DB_TABLE_DEVICES;
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Deleting Devices table: "+ex.getMessage());
        }
    }
    
    public static int addNewDevice(IDataBase database, String name, String password, int timeout) {
        MedusaDevice device = new MedusaDevice(name.toLowerCase(), password);
        if (timeout < 1) timeout = 1;
        if (timeout > 120) timeout = 120;
        device.setTimeoutMinutes(timeout);
        return addNewDevice(database, device);
    }
    
    public static int addNewDevice(IDataBase database, MedusaDevice device) {
        log.debug("Adding New Device");
        if (device.getPassword().isEmpty()) return 1;
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES
                    +" WHERE NAME = '"+device.getName()+"';";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            if (rs.next()) // This item is already on the table
            {
                log.debug("addNewDevice: "+device.getName()+" is already at the database");
                return 2;
            }
            sql = "INSERT INTO "+DEFAULT_DB_TABLE_DEVICES+"("+KEY_DEVICES_NAME+", "+
                    KEY_DEVICES_PASSWORD+", "+KEY_DEVICES_ADDRESS+", "+KEY_DEVICES_TICKET+
                    ", "+KEY_DEVICES_VALID+", "+KEY_DEVICES_EXPIRETIME+", "+
                    KEY_DEVICES_LASTLOGIN+", "+KEY_DEVICES_PROTOCOLS+", "+KEY_DEVICES_TIMEOUT+
                    ") VALUES ('"+device.getName()+"','"+device.getPassword()+"','"+device.getAddress()+"','"+device.getTicket()+
                    "','"+(device.isValid()?1:0)+"','"+device.getExpireTime()+"','"+device.getLastLogin()+"','"+device.getProtocols()+"','"+device.getTimeoutMillis()+"');";
            
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Adding Devices table: "+ex.getMessage());
            return -1;
        }
        return 0;
    }
    
    public static void deleteDevice(IDataBase database, int id) {
        log.debug("Deleting Device");
        try {
            String sql = "DELETE FROM "+DEFAULT_DB_TABLE_DEVICES+" WHERE ID = "+id;
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("delete device: "+ex.getMessage());
        }
    }
    
    public static int changeDevicePassword(IDataBase database, int id, String newPass) {
        log.debug("Changing Password for Device");
        if (newPass.isEmpty()) return 1;
        try {
            String sql = "UPDATE "+
                    DEFAULT_DB_TABLE_DEVICES+" SET PASS='"+newPass+"' WHERE ID='"+id+"';";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Changing Password: "+ex.getMessage());
            return -1;
        }
        return 0;
    }
    
    private static void updateDevicesList(IDataBase database) {
        log.debug("Updating Devices List");
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES
                    +" WHERE "+KEY_DEVICES_EXPIRETIME+" < "+System.currentTimeMillis()+";";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            StringBuilder toRemove = new StringBuilder();
            
            //List<Integer> toRemove = new ArrayList<>();
            while(rs.next()) {
                toRemove.append(",").append(rs.getInt(KEY_DEVICES_ID));
            }
            if (!toRemove.toString().isEmpty()) {
                sql = "UPDATE "+DEFAULT_DB_TABLE_DEVICES+" SET VALID=0 WHERE "
                        +KEY_DEVICES_ID+" IN ("+toRemove.toString().substring(1)+");";
                log.debug(sql);
                database.getConnection().createStatement().executeUpdate(sql);
            } else {
                log.debug("Nothing to update");
            }
            
        } catch (SQLException ex) {
            log.error("Updating Devices List: "+ex.getMessage());
        }
    }
    
    public static MedusaDevice getDeviceByName(IDataBase database, String name) {
        updateDevicesList(database);
        log.debug("Get device by Name");
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES+" WHERE "+KEY_DEVICES_NAME+" = '"+name+"';";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            if(rs.next())
                return new MedusaDevice(
                        rs.getInt(KEY_DEVICES_ID),
                        rs.getString(KEY_DEVICES_NAME),
                        rs.getString(KEY_DEVICES_PASSWORD),
                        rs.getString(KEY_DEVICES_ADDRESS),
                        rs.getString(KEY_DEVICES_TICKET),
                        rs.getBoolean(KEY_DEVICES_VALID),
                        rs.getLong(KEY_DEVICES_EXPIRETIME),
                        rs.getLong(KEY_DEVICES_LASTLOGIN),
                        rs.getString(KEY_DEVICES_PROTOCOLS),
                        rs.getInt(KEY_DEVICES_TIMEOUT));
        } catch (SQLException ex) {
            log.error("getDeviceByName: "+ex.getMessage());
        }
        return null;
    }
    
    public static MedusaDevice getDeviceByTicket(IDataBase database, String ticket) {
        updateDevicesList(database);
        log.debug("Get device by Ticket");
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES+" WHERE "+KEY_DEVICES_TICKET+" = '"+ticket+"' AND '"+KEY_DEVICES_VALID+"' = 1;";
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            if(rs.next())
                return new MedusaDevice(
                        rs.getInt(KEY_DEVICES_ID),
                        rs.getString(KEY_DEVICES_NAME),
                        rs.getString(KEY_DEVICES_PASSWORD),
                        rs.getString(KEY_DEVICES_ADDRESS),
                        rs.getString(KEY_DEVICES_TICKET),
                        rs.getBoolean(KEY_DEVICES_VALID),
                        rs.getLong(KEY_DEVICES_EXPIRETIME),
                        rs.getLong(KEY_DEVICES_LASTLOGIN),
                        rs.getString(KEY_DEVICES_PROTOCOLS),
                        rs.getInt(KEY_DEVICES_TIMEOUT));
        } catch (SQLException ex) {
            log.error("getDeviceByTicket: "+ex.getMessage());
        }
        return null;
    }
    
    public static List<MedusaDevice> getDevicesList(IDataBase database) {
        updateDevicesList(database);
        log.debug("Get devices List");
        List<MedusaDevice> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM "+DEFAULT_DB_TABLE_DEVICES;
            log.debug(sql);
            ResultSet rs = database.getConnection().createStatement().executeQuery(sql);
            while(rs.next())
                list.add(new MedusaDevice(
                        rs.getInt(KEY_DEVICES_ID),
                        rs.getString(KEY_DEVICES_NAME),
                        rs.getString(KEY_DEVICES_PASSWORD),
                        rs.getString(KEY_DEVICES_ADDRESS),
                        rs.getString(KEY_DEVICES_TICKET),
                        rs.getBoolean(KEY_DEVICES_VALID),
                        rs.getLong(KEY_DEVICES_EXPIRETIME),
                        rs.getLong(KEY_DEVICES_LASTLOGIN),
                        rs.getString(KEY_DEVICES_PROTOCOLS),
                        rs.getInt(KEY_DEVICES_TIMEOUT)));
        } catch (SQLException ex) {
            log.error("getDevicesList: "+ex.getMessage());
        }
        return list;
    }
    
    public static void updateDeviceTicket(IDataBase database, MedusaDevice device) {
        log.debug("Updating Device Ticket");
        try {
            String sql = "UPDATE "+DEFAULT_DB_TABLE_DEVICES+" SET "+KEY_DEVICES_ADDRESS+"='"+device.getAddress()+"', "+KEY_DEVICES_LASTLOGIN+"="+device.getLastLogin()+", "+KEY_DEVICES_EXPIRETIME+"="+device.getExpireTime()+", "+KEY_DEVICES_TICKET+"='"+device.getTicket()+"', "+KEY_DEVICES_PROTOCOLS+"='"+device.getProtocols()+"', "+KEY_DEVICES_VALID+"=1  WHERE ID='"+device.getId()+"';";
            log.debug(sql);
            database.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Updating Device Ticket: "+ex.getMessage());
        }
    }
}
