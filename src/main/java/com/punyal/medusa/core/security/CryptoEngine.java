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
import static com.punyal.medusa.constants.JsonKeys.*;
import com.punyal.medusa.core.MedusaDevice;
import com.punyal.medusa.core.database.DBtools;
import com.punyal.medusa.core.database.IDataBase;
import com.punyal.medusa.logger.MedusaLogger;
import com.punyal.medusa.utils.DateUtils;
import java.net.InetAddress;
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
    
    public CryptoEngine() {
        randomizer = new SecureRandom();
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
    
    private String ByteArray2Hex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
    
    public synchronized JSONObject getTicket(IDataBase database, InetAddress address, String name, String password) {
        JSONObject json = new JSONObject();
        // Check if there are name and password
        if (name != null && password != null) {
            if (!name.isEmpty() && !password.isEmpty()) {
                // Check if there is some valid authenticator for that IP
                List<String> authenticators = DBtools.findAuthenticatorsByAddress(database, address.getHostAddress());
                if (!authenticators.isEmpty()) {
                    // find user on the database
                    MedusaDevice device = DBtools.getDeviceByName(database, name);
                    if (device != null) {
                        // Check if the encrypted pass matches
                        
                        /*
                        if (password is correct)
                            generate ticket;
                        else error;
                        */
                        
                        
                    } else { // Wrong name
                        log.debug("Wrong name");
                        json.put(JSON_KEY_ERROR, "Wrong name");
                    }
                } else { // No valid authenticators
                    log.debug("No valid authenticators");
                    json.put(JSON_KEY_ERROR, "No valid authenticators");
                }
            } else { // No name or no pass parameter
                log.debug("Empty name or password");
                json.put(JSON_KEY_ERROR, "Empty name or password");
            }
        } else { // No name or no pass parameter
            log.debug("No name or password parameter");
            json.put(JSON_KEY_ERROR, "No name or password parameter");
        }
        return json;
    }
    
}
