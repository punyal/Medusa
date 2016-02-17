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

import static com.punyal.medusa.constants.Defaults.*;
import static com.punyal.medusa.constants.JsonKeys.*;
import com.punyal.medusa.core.configuration.Configuration;
import static com.punyal.medusa.core.protocols.coap.DefaultsCoAP.*;
import org.eclipse.californium.core.CoapResource;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AuthenticationResource extends CoapResource {
    private final Configuration configuration;
    
    public AuthenticationResource(Configuration configuration) {
        super(RESOURCE_AUTHENTICATION_NAME, true);
        getAttributes().setTitle(RESOURCE_AUTHENTICATION_TITLE);
        this.configuration = configuration;
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        JSONObject json = new JSONObject();
        json.put(JSON_KEY_VERSION, MEDUSA_VERSION+"."+MEDUSA_SUBVERSION);
        exchange.respond(CONTENT, json.toJSONString(), APPLICATION_JSON);
    }
    
    @Override
    public void handlePOST(CoapExchange exchange) {
        JSONObject json = new JSONObject();
        json.put(JSON_KEY_VERSION, MEDUSA_VERSION+"."+MEDUSA_SUBVERSION);
        exchange.respond(CONTENT, json.toJSONString(), APPLICATION_JSON);
    }
}
