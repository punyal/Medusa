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

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public enum Error {
    CLIENT_NO_VALID_PARAMETERS(     100, "No valid parameters"),
    CLIENT_EMPTY_PARAMETERS(        101, "No valid parameters"),
    CLIENT_NO_VALID_ADDRESS(        102, "No valid parameters"),
    CLIENT_NO_REGISTERED(           110, "Client no registered"),
    CLIENT_TICKET_EXPIRED(          111, "Client Ticket expired"),
    CLIENT_WRONG_PASSWORD(          112, "Client wrong password"),
    REMOTE_NO_REGISTERED(           200, "Remote no registered"),
    REMOTE_TICKET_EXPIRED(          201, "Remote Ticket expired"),
    SERVER_AUTHENTICATOR_EXPIRED(   300, "Authenticator expired");
    
    private final int code;
    private final String description;
    
    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return "Error "+code + ": " + description;
    }
}
