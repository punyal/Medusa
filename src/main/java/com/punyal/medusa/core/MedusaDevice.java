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

import com.punyal.medusa.utils.DateUtils;
import java.net.InetAddress;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MedusaDevice {
    private int id;
    private final String name;
    private String password;
    private String address;
    private String ticket;
    private boolean valid;
    private long timeout;
    private long lastLogin;
    private String protocols;
    
    public MedusaDevice(int id, String name, String password, String address, String ticket, boolean valid, long timeout, long lastLogin, String protocols) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.ticket = ticket;
        this.valid = valid;
        this.lastLogin = lastLogin;
        this.timeout = timeout;
        this.protocols = protocols;
    }
    
    public MedusaDevice(int id, String name, String password, String address, String ticket, boolean valid, String timeout, String lastLogin, String protocols) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.ticket = ticket;        
        this.valid = valid;
        this.lastLogin = DateUtils.date2Long(lastLogin);
        this.timeout = DateUtils.date2Long(timeout);
        this.protocols = protocols;
    }
    
    public MedusaDevice(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = "";
        this.ticket = "";
        this.valid = false;
        this.lastLogin = 0;
        this.timeout = 0;
        this.protocols = "";
    }
    
    public MedusaDevice(String name, String password) {
        this.id = 0;
        this.name = name;
        this.password = password;
        this.address = "";
        this.ticket = "";
        this.valid = false;
        this.lastLogin = 0;
        this.timeout = 0;
        this.protocols = "";
    }
    
    public void changePassword(String password) {
        this.password = password;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setAddress(InetAddress address) {
        this.address = address.getHostAddress();
    }
    
    public void setValid() {
        valid = true;
    }
    
    public void setNotValid() {
        valid = false;
    }
    
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public void setProtocols(String protocols) {
        this.protocols = protocols;
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
    
    public String getAddress() {
        return address;
    }
    
    public String getTicket() {
        return ticket;
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
    
    public String getProtocols() {
        return protocols;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Humman readable configuration
        sb.append("Device id: ").append(id).append("\n");
        sb.append("\tName: ").append(name).append("\n");
        sb.append("\tPassword: ").append(password).append("\n");
        sb.append("\tIP: ").append(address).append("\n");
        sb.append("\tTicket: ").append(ticket).append("\n");
        sb.append("\tValid: ").append(valid).append("\n");
        sb.append("\tTimeout:    ").append(DateUtils.long2DateMillis(timeout)).append("\n");
        sb.append("\tLast Login: ").append(DateUtils.long2DateMillis(lastLogin)).append("\n");
        sb.append("\tProtocols: ").append(protocols).append("\n");
        return sb.toString();
    }
}
