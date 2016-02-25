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
package com.punyal.medusa.core.protocols.coap;

import com.punyal.medusa.core.configuration.Configuration;
import com.punyal.medusa.core.protocols.IProtocol;
import com.punyal.medusa.logger.MedusaLogger;
import org.eclipse.californium.core.CoapServer;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class CoAP implements IProtocol {
    private static final MedusaLogger log = new MedusaLogger();
    private final Configuration configuration;
    private final CoapServer coapServer;
    
    public CoAP(Configuration configuration) {
        this.configuration = configuration;
        coapServer = new CoapServer(configuration.getConfigurationCoapServer().getPort());
        coapServer.add(new AuthenticationResource(configuration), new AuthorizationResource(configuration));
    }

    @Override
    public void start() {
        log.debug("Starting CoAP Server at port: "+configuration.getConfigurationCoapServer().getPort());
        coapServer.start();
    }

    @Override
    public void stop() {
        log.debug("Stoping CoAP Server");
        coapServer.stop();
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
    
}
