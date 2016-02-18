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
package com.punyal.medusa;

import com.punyal.medusa.core.configuration.Configuration;
import com.punyal.medusa.core.protocols.IProtocol;
import com.punyal.medusa.core.protocols.Protocols;
import com.punyal.medusa.core.protocols.coap.CoAP;
import com.punyal.medusa.core.webserver.WebServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Medusa implements Runnable {
    private static final Logger log = Logger.getLogger(Medusa.class.getName());
    private final Configuration configuration;
    private final Protocols protocols;
    private final WebServer webServer;
    
    public Medusa() {
        log.setLevel(Level.ALL);
        configuration = new Configuration();
        log.log(Level.INFO, configuration.toString());
        
        webServer = new WebServer(configuration);
        
        // Declare Protocols
        protocols = new Protocols(new IProtocol[]{new CoAP(configuration)});
        
        // Actions when exit
        Runtime.getRuntime().addShutdownHook(
            new Thread() {
                @Override
                public void run() {
                    protocols.stopServers();
                    try {
                        webServer.stop();
                    } catch (Exception ex) {
                        log.log(Level.SEVERE, "WebServer", ex);
                    }
                }
            }
        );
    }

    @Override
    public void run() {
        log.log(Level.INFO, "Medusa ALIVE!");
        // TODO: Check Configuration
        
        // Start Protocols
        /*log.log(Level.INFO, protocols.toString());
        protocols.startServers();
        
        // Start Web server
        try {
            webServer.start();
            webServer.join();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "WebServer", ex);
        }*/
        
        log.log(Level.OFF, "Medusa DEAD!");
    }
    
    
    
}
