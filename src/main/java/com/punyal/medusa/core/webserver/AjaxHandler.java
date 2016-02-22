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
package com.punyal.medusa.core.webserver;

import static com.punyal.medusa.constants.JsonKeys.*;
import com.punyal.medusa.core.configuration.Configuration;
import com.punyal.medusa.logger.MedusaLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AjaxHandler extends AbstractHandler {
    private static final MedusaLogger log = new MedusaLogger();
    private final AdminParser adminParser;
    private final Configuration configuration;
    
    public AjaxHandler(Configuration configuration) {
        adminParser = new AdminParser();
        this.configuration = configuration;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        JSONObject json = new JSONObject();
        switch (target.substring(1)) { // remove the initial "/"
            case "getServerInfo":
                log.debug("AjaxHandler - getServerInfo");
                json.put(JSON_KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                json.put(JSON_KEY_PUBLIC_IP, configuration.getPublicIP());
                json.put(JSON_KEY_LOCAL_IP, configuration.getLocalIP());
                //json.put(KEY_VERSION, UNIVERSE_VERSION+"."+UNIVERSE_SUBVERSION);
                //json.put(KEY_TOTAL_REQUEST, configuration.getLoggerTotalLogs());
                break;
            case "updateDevicesList":
                log.debug("AjaxHandler - updateDevicesList");
                //JSONArray jsonRequest = null;
                //BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                
                /*
                jsonRequest = (JSONArray) JSONValue.parse(br.readLine());
                                
                for(Object item: jsonRequest) {
                    log.debug(((JSONObject)item).toJSONString());
                }
                */
                
                
                json.put(JSON_KEY_RESPONSE, adminParser.parseRequest((JSONArray) JSONValue.parse(new BufferedReader(new InputStreamReader(request.getInputStream())).readLine())));
                break;
            default:
                log.error("AjaxHandler - "+target.substring(1)+" does not exist");
                break;
        }
        
        if (!json.isEmpty()) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println(json.toJSONString());
        }
    }
}
