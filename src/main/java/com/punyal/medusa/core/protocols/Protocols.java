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
package com.punyal.medusa.core.protocols;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Protocols {
    private final List<IProtocol> protocols;
    
    public Protocols(IProtocol[] protocolArray) {
        protocols = Arrays.asList(protocolArray);
    }
    
    public void startServers() {
        protocols.stream().forEach((protocol) -> {
            protocol.start();
        });
    }
    
    public void stopServers() {
        protocols.stream().forEach((protocol) -> {
            protocol.stop();
        });
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Humman readable configuration
        sb.append("List of protocols\n");
        protocols.stream().forEach((protocol) -> {
            sb.append(protocol.getName()).append("\n");
        });
        return sb.toString();
    }
}
