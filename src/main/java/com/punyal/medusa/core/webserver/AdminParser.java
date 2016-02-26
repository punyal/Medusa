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
import com.punyal.medusa.core.MedusaDevice;
import com.punyal.medusa.core.configuration.Configuration;
import com.punyal.medusa.core.database.DBtools;
import com.punyal.medusa.logger.MedusaLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AdminParser {
    private static final MedusaLogger log = new MedusaLogger();
    private final Configuration configuration;
    
    
    public AdminParser(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public JSONObject parseRequest(JSONArray allItems) {
        log.debug("Parsing "+allItems.size()+" devices");
        JSONObject response = new JSONObject();
        MedusaDevice device;
        JSONObject json;
        for (Object item: allItems) {
            json = (JSONObject) item;
            log.debug(json.toJSONString());
            // Ignore cases with empty name
            if (!json.get(JSON_KEY_NAME).toString().isEmpty()) {
                device = new MedusaDevice(json.get(JSON_KEY_NAME).toString(), json.get(JSON_KEY_PASSWORD).toString());
                log.debug(device.toString());

                switch(json.get(JSON_KEY_STATUS).toString()) {
                    case "OK":
                        log.debug("OK");
                        break;
                    case "new":
                        log.debug("new");
                        switch (DBtools.addNewDevice(configuration.getDatabase(), json.get(JSON_KEY_NAME).toString(), json.get(JSON_KEY_PASSWORD).toString(), Integer.parseInt(json.get(JSON_KEY_TIMEOUT).toString()))) {
                            case 0:
                                response.put(json.get(JSON_KEY_NAME).toString(), "added");
                                break;
                            case 1:
                                response.put(json.get(JSON_KEY_NAME).toString(), "empty pass");
                                break;
                            case 2:
                                response.put(json.get(JSON_KEY_NAME).toString(), "dupplicated");
                                break;
                            default:
                                response.put(json.get(JSON_KEY_NAME).toString(), "error");
                                break;       
                        }
                        break;
                    case "delete":
                        log.debug("delete");
                        DBtools.deleteDevice(configuration.getDatabase(), Integer.parseInt(json.get(JSON_KEY_ID).toString()));
                        response.put(json.get(JSON_KEY_NAME).toString(), "deleted");
                        break;
                    case "changed":
                        log.debug("changed");
                        switch(DBtools.changeDevicePassword(configuration.getDatabase(), Integer.parseInt(json.get(JSON_KEY_ID).toString()), json.get(JSON_KEY_PASSWORD).toString())) {
                            case 0:
                                response.put(json.get(JSON_KEY_NAME).toString(), "updated");
                                break;
                            case 1:
                                response.put(json.get(JSON_KEY_NAME).toString(), "empty pass");
                                break;
                            default:
                                response.put(json.get(JSON_KEY_NAME).toString(), "error");
                                break;
                        }
                        break;
                    default:
                        log.error("Admin Parser: unknown Status "+json.get(JSON_KEY_STATUS).toString());
                }
            }
        }
        return response;
    }
}
