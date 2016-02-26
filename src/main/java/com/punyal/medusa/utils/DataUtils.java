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
package com.punyal.medusa.utils;

import com.punyal.medusa.constants.Defaults;
import java.nio.charset.Charset;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class DataUtils {
    public static byte[] string2byteArray(String s) {
        if (s == null) return (new byte[0]);
        return s.getBytes(Charset.forName(Defaults.DEFAULT_CHARSET));
    }
    
    public static byte[] hexString2byteArray(String h) {
        byte[] data = new byte[(int)h.length()/2];
        for (int i=0; i<h.length(); i+=2)
            data[i/2] = (byte) ((Character.digit(h.charAt(i),16) << 4) +
                    Character.digit(h.charAt(i+1), 16));
        return data;
    }
    
    public static String printByteArray(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for(byte b:byteArray)
            sb.append(",").append(b);
        return sb.toString().substring(1);
    }
    
    public static String byteArray2hexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b:byteArray)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
}
