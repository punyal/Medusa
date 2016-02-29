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
package com.punyal.medusa.core.security;

import static com.punyal.medusa.constants.Defaults.*;
import com.punyal.medusa.constants.Error;
import static com.punyal.medusa.constants.JsonKeys.*;
import com.punyal.medusa.constants.Warning;
import com.punyal.medusa.core.MedusaDevice;
import com.punyal.medusa.core.database.DBtools;
import com.punyal.medusa.core.database.IDataBase;
import com.punyal.medusa.logger.MedusaLogger;
import com.punyal.medusa.utils.DataUtils;
import com.punyal.medusa.utils.DateUtils;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class CryptoEngine {
    private static final MedusaLogger log = new MedusaLogger();
    private final SecureRandom randomizer;
    private final String secretKey;
    
    public CryptoEngine(String secretKey) {
        randomizer = new SecureRandom();
        this.secretKey = secretKey;
    }
    
    public synchronized Authenticator getNewAuthenticator(IDataBase database, InetAddress address) {
        long timeout = System.currentTimeMillis()+DEFAULT_AUTHENTICATOR_TIMEOUT; // Actualtime + timeout in ms
        Authenticator authenticator = new Authenticator(generateNewAuthenticatorValue(), timeout);
        log.info("Created Authenticator ["+authenticator.getValue()+"] @("+address.getHostAddress()+") valid till "+DateUtils.long2DateMillis(timeout));
        DBtools.addNewAuthenticator(database, authenticator.getValue(), address.getHostAddress(),authenticator.getTimeout());
        return authenticator;
    }
    
    private String generateNewAuthenticatorValue() {
        return ByteArray2Hex(random16bytes());
    }
    
    private byte[] random16bytes() {
        byte bytes[] = new byte[16];
        randomizer.nextBytes(bytes);
        return bytes;
    }
    
    private byte[] random8bytes() {
        byte bytes[] = new byte[8];
        randomizer.nextBytes(bytes);
        return bytes;
    }
    
    private byte[] random4bytes() {
        byte bytes[] = new byte[4];
        randomizer.nextBytes(bytes);
        return bytes;
    }
    
    private String ByteArray2Hex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
    
    public synchronized JSONObject getTicket(IDataBase database, String protocol, InetAddress address, String name, String encryptedPassword) {
        log.debug("Getting Ticket");
        JSONObject json = new JSONObject();
        // Check input
        if (name == null || encryptedPassword == null) {
            log.debug(Error.CLIENT_NO_VALID_PARAMETERS.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_NO_VALID_PARAMETERS.getCode());
            return json;
        }
        if (name.isEmpty() || encryptedPassword.isEmpty()) {
            log.debug(Error.CLIENT_EMPTY_PARAMETERS.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_EMPTY_PARAMETERS.getCode());
            return json;
        }
        
        // Find user
        MedusaDevice device = DBtools.getDeviceByName(database, name);
        if (device == null) {
            log.debug(Error.CLIENT_NO_REGISTERED.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_NO_REGISTERED.getCode());
            return json;
        }
        log.debug("New device is trying to Authenticate\n"+device.toString());
        
        // Check authenticators
        List<String> authenticators = DBtools.findAuthenticatorsByAddress(database, address.getHostAddress());
        if (authenticators.isEmpty()) {
            log.debug(Error.SERVER_AUTHENTICATOR_EXPIRED.toString());
            json.put(JSON_KEY_ERROR, Error.SERVER_AUTHENTICATOR_EXPIRED.getCode());
            return json;
        }
        
        // Check Password
        if (!checkPassword(authenticators, device.getPassword(), encryptedPassword)) {
            log.debug(Error.CLIENT_WRONG_PASSWORD.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_WRONG_PASSWORD.getCode());
            return json;
        }
        
        device.setProtocols(protocol);
        findTicket(database, address, device);
        json.put(JSON_KEY_TICKET, device.getTicket());
        json.put(JSON_KEY_EXPIRE_TIME, device.getExpireTime());
            
        return json;
    }
    
    public synchronized JSONObject checkTicket(IDataBase database, String protocol, InetAddress address, String ticket, String remoteAddress, String remoteTicket) {
        log.debug("Checking Ticket");
        JSONObject json = new JSONObject();
        // Check incoming parameters
        if (database == null || protocol == null || address == null || ticket == null || remoteAddress == null || remoteTicket == null) {
            log.debug(Error.CLIENT_NO_VALID_PARAMETERS.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_NO_VALID_PARAMETERS.getCode());
            return json;
        }
        
        // Check requester
        MedusaDevice requester = DBtools.getDeviceByTicket(database, ticket);
        if (requester == null) {
            log.debug(Error.CLIENT_NO_REGISTERED.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_NO_REGISTERED.getCode());
            return json;
        }
        if (!requester.getAddress().equals(address.getHostAddress())) {
            log.warning("Someone from ["+address.getHostAddress()+"] is trying to use Ticket ["+requester.getTicket()+"] when expected address is ["+requester.getAddress()+"]");
            log.debug(Warning.CLIENT_WRONG_ADDRESS.toString());
            json.put(JSON_KEY_WARNING, Warning.CLIENT_WRONG_ADDRESS.getCode());
        }
        if (!requester.isValid()) {
            log.warning("Someone from ["+requester.getAddress()+"] is trying to access the system with a expired ticket since "+DateUtils.long2DateMillis(requester.getExpireTime()));
            log.debug(Error.CLIENT_TICKET_EXPIRED.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_TICKET_EXPIRED.getCode());
            return json;
        }
        
        // Check remote
        MedusaDevice remote = DBtools.getDeviceByTicket(database, remoteTicket);
        if (remote == null) {
            log.warning("Someone from ["+remoteAddress+"] is trying to access with a faked ticket");
            log.debug(Error.REMOTE_NO_REGISTERED.toString());
            json.put(JSON_KEY_ERROR, Error.REMOTE_NO_REGISTERED.getCode());
            return json;
        }
        if (!remote.getAddress().equals(remoteAddress)) {
            log.warning("Someone from ["+remoteAddress+"] is trying to access to ["+requester.getAddress()+"] using Ticket ["+remote.getTicket()+"] when expected address is ["+remote.getAddress()+"]");
            log.debug(Warning.REMOTE_DIFFENT_ADRRESS_ACCESS.toString());
            json.put(JSON_KEY_WARNING, Warning.REMOTE_DIFFENT_ADRRESS_ACCESS.getCode());
        }
        if (!remote.isValid()) {
            log.warning("Someone from ["+remote.getAddress()+"] is trying to access to ["+requester.getAddress()+"] with a expired ticket since "+DateUtils.long2DateMillis(remote.getExpireTime()));
            log.debug(Error.CLIENT_TICKET_EXPIRED.toString());
            json.put(JSON_KEY_ERROR, Error.CLIENT_TICKET_EXPIRED.getCode());
            return json;
        }
        
        log.debug("Ticket Authorization completed!");
        log.info("Communication authorized between ["+requester.getAddress()+"] and ["+remote.getAddress()+"]");
        json.put(JSON_KEY_VALID, true);
        json.put(JSON_KEY_TIMEOUT,remote.getExpireTime()-System.currentTimeMillis());
        
        return json;
    }
    
    private boolean checkPassword(List<String> authenticators, String originalPassword, String encryptedPassword) {
        return authenticators.stream().anyMatch((authenticator) -> (encryptedPassword.equals(encrypt(authenticator, originalPassword))));
    }
    
    private String encrypt(String authenticator, String password) {
        try {
            log.debug("encrypt secretKey["+secretKey+"] authenticator["+authenticator+"] password["+password+"]");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b_secretKey = DataUtils.string2byteArray(secretKey);
            byte[] b_authenticator = DataUtils.hexString2byteArray(authenticator);
            byte[] b_password = DataUtils.string2byteArray(password);
            
            if (b_authenticator.length != 16) {
                log.critical("Authenticator with wrong length!");
                return null;
            }
            
            int len = 0, tot_len = 0;
            
            // Check final length to prevent errors;
            if (b_password.length%16!=0) tot_len = 16;
            tot_len += ((int)b_password.length/16)*16;
            
            // Create crypted array
            byte[] crypted = new byte[tot_len];
            byte[] b_tmp = new byte[b_secretKey.length+b_authenticator.length];
            byte[] c_tmp = new byte[16];
            
            System.arraycopy(b_secretKey, 0, b_tmp, 0, b_secretKey.length);
            System.arraycopy(b_authenticator, 0, b_tmp, b_secretKey.length, b_authenticator.length);
            b_tmp = md.digest(DataUtils.string2byteArray(DataUtils.byteArray2hexString(b_tmp)));
            
            while (len < tot_len) {
                if((b_password.length - len) < 16) {
                    System.arraycopy(b_password, len, c_tmp, 0, b_password.length-len);
                    for (int i=b_password.length-len; i<16; i++)
                        c_tmp[i] = 0;
                } else System.arraycopy(b_password, len, c_tmp, 0, 16);
                for (int i=0; i<16; i++)
                    c_tmp[i] = (byte)(0xFF & ((int)c_tmp[i])^((int)b_tmp[i]));
                System.arraycopy(c_tmp, 0, crypted, len, 16);
                len += 16;
            }
            log.debug("Encrypted: "+DataUtils.byteArray2hexString(crypted));
            return DataUtils.byteArray2hexString(crypted);
            
        } catch (NoSuchAlgorithmException ex) {
            log.critical("Encrypt: "+ex.getMessage());
            return null;
        }
    }
    
    private void findTicket(IDataBase database, InetAddress address, MedusaDevice device) {
        // Check if the previous ticket is still valid
        if (device.isValid()) {
            log.debug("Previuos Ticket is still valid");
            device.setLastLogin(System.currentTimeMillis());
            device.setAddress(address);
            DBtools.updateDeviceTicket(database, device);
            return;
        }
        String ticket = generateNewTicket(database);
        log.debug("New generated Ticket ["+ticket+"]");
        device.setTicket(ticket);
        device.setAddress(address);
        device.setLastLogin(System.currentTimeMillis());
        device.setExpireTime(System.currentTimeMillis()+device.getTimeoutMillis());
        DBtools.updateDeviceTicket(database, device);
    }
    
    private String generateNewTicket(IDataBase database) {
        log.debug("generateNewTicket");
        String newTicket;
        do newTicket = DataUtils.byteArray2hexString(random4bytes());
        while (DBtools.getDeviceByTicket(database, newTicket) != null);
        return newTicket;
    }
}
