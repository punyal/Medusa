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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.store.fs.FileUtils;
import org.h2.tools.Server;
import java.sql.Statement;
import org.h2.jdbc.JdbcSQLException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ConfigurationH2DB {
    private Server serverDB;
    
    public ConfigurationH2DB() {
        try {
            String path = System.getProperty("user.dir")+"/DB/";
            File fpath = new File(path);
            fpath.mkdirs();
            FileUtils.deleteRecursive(path, true);
            String dbName = "tata";
            String connection = "jdbc:h2:file:" + path + dbName;
            //serverDB = Server.createTcpServer(connection);
            serverDB = Server.createTcpServer(
                        new String[] { "-tcpPort", "1337", "-tcp",
                                "-tcpSSL", "-tcpAllowOthers" }).start();
           // serverDB.start();
           
           System.out.println("\n\nH2\n"+serverDB.getStatus()+"\n\n");
           
           
           
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConfigurationH2DB.class.getName()).log(Level.SEVERE, null, ex);
            }
           Connection conn = DriverManager.getConnection("jdbc:h2:H2://127.0.1.1:1337");
           
           
           
           System.out.println(conn.toString());
           
           Statement statement = conn.createStatement();
           
           //statement.execute("CREATE DATABASE TEST");
           
           System.out.println("CREATED!!");
           //Connection conn2 = DriverManager.getConnection("jdbc:h2:{{ssl}://127.0.1.1:1337}/test");
           
           //statement.execute("CREATE DATABASE test1");
           
           
           //System.out.println(conn2.toString());
           
           
           //Statement statement1 = conn2.createStatement();
           //System.out.println(statement1);
           
           ResultSet result;
           
           //statement.executeUpdate("DROP TABLE TEST;");
           try {
            statement.executeUpdate("CREATE TABLE TEST(ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR);");
           } catch (JdbcSQLException ex) {
               System.out.println("Database already created");
           }
           statement.executeUpdate("INSERT INTO TEST (NAME) VALUES ('pablo');");
                   
                   
           result = statement.executeQuery("SELECT * FROM TEST");
           
           
           while (result.next()) {
               System.out.println(result.getString("ID") + " - " + result.getString("NAME"));
           }
               
           
           
           
           //conn2.close();
           conn.close();
           
           
            serverDB.stop();
            //serverDB = Server.createTcpServer().start();
        } catch (SQLException ex) {
            Logger.getLogger(ConfigurationH2DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
