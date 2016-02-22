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
package com.punyal.medusa.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MedusaDevice {
    private int id;
    private final String name;
    private String password;
    private boolean valid;
    private long lastLogin;
    private long timeout;
    
    public MedusaDevice(int id, String name, String password, boolean valid, long lastLogin, long timeout) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.valid = valid;
        this.lastLogin = lastLogin;
        this.timeout = timeout;
    }
    
    public MedusaDevice(int id, String name, String password, boolean valid, String lastLogin, String timeout) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.valid = valid;
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
        
        try {
            this.lastLogin = ((format).parse(lastLogin)).getTime();
        } catch (ParseException ex) {
            this.lastLogin = 0;
        }
        try {
            this.timeout = ((format).parse(timeout)).getTime();
        } catch (ParseException ex) {
            this.timeout = 0;
        }
        
    }
    
    public MedusaDevice(String name, String password) {
        this.id = 0;
        this.name = name;
        this.password = password;
        this.valid = false;
        this.lastLogin = 0;
        this.timeout = 0;
    }
    
    public void changePassword(String password) {
        this.password = password;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setValid() {
        valid = true;
    }
    
    public void setNotValid() {
        valid = false;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Humman readable configuration
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sb.append("Device id: ").append(id).append("\n");
        sb.append("\tName: ").append(name).append("\n");
        sb.append("\tPassword: ").append(password).append("\n");
        sb.append("\tValid: ").append(valid).append("\n");
        sb.append("\tLast Login: ").append(format.format(lastLogin)).append("\n");
        sb.append("\tTimeout:    ").append(format.format(timeout)).append("\n");
        return sb.toString();
    }
}
