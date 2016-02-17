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
package com.punyal.medusa.core.db;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class NoDB implements IDB {
    private final List <Device> devices;
    
    public NoDB() {
        devices = new ArrayList<>();
    }

    @Override
    public synchronized void addDeviceInfo(Device device) {
        if (device.getID().equals("null") || findDevice(device.getID()) == null)
            devices.add(device);
        
        System.out.println(toString());
    }

    @Override
    public synchronized void updateDeviceInfo(Device device) {
        removeDeviceInfo(findDevice(device.getID()));
        addDeviceInfo(device);
    }

    @Override
    public synchronized void removeDeviceInfo(Device device) {
        devices.remove(device);
    }

    @Override
    public synchronized Device findDevice(String id) {
        for (Device device:devices)
            if (device.getID().equals(id))
                return device;
        return null;
    }

    @Override
    public synchronized List<Device> findDevice(InetAddress address) {
        List<Device> toReturn = new ArrayList<>();
        devices.stream().filter((device) -> (device.getAddress().equals(address))).forEach((device) -> {
            toReturn.add(device);
        });
        return toReturn;
    }
    
    
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        // Humman readable configuration
        sb.append("List of Devices\n");
        devices.stream().forEach((device) -> {
            sb.append(device.getID()).append("\t").append(device.getAddress()).append("\t").append(device.getAuthenticator().getValue()).append("\t").append(device.getAuthenticator().getTimeout()).append("\n");
        });
        sb.append("\n");
        return sb.toString();
    }
    
}
