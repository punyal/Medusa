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
import java.net.InetAddress;
import java.security.SecureRandom;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class CryptoEngine {
    private final SecureRandom randomizer;
    
    public CryptoEngine() {
        randomizer = new SecureRandom();
        //this.db = db;
    }
    
    public synchronized Authenticator getNewAuthenticator(InetAddress address) {
        Authenticator authenticator = new Authenticator(generateNewAuthenticatorValue(), DEFAULT_AUTHENTICATOR_TIMEOUT);
        //Device device = new Device("null","null");
        //device.setAuthenticator(address, authenticator);
        //db.addDeviceInfo(device);
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
    
    
    
}
