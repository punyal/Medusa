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

import com.punyal.medusa.logger.MedusaLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class DateUtils {
    private static final MedusaLogger log = new MedusaLogger();
    
    private static long dateSeconds2Long(String date) {
        try {
            return ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date)).getTime();
        } catch (ParseException ex) {
            log.debug("dateSeconds2Long('"+date+"'): "+ex.getMessage());
            return 0;
        }
    }
    
    private static long dateMillis2Long(String date) {
        try {
            return ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).parse(date)).getTime();
        } catch (ParseException ex) {
            log.debug("dateMillis2Long('"+date+"'): "+ex.getMessage());
            return 0;
        }
    }
    
    public static long date2Long(String date) {
        long millis = dateMillis2Long(date);
        if (millis > 0) return millis;
        else return dateSeconds2Long(date);
    }
    
    public static String long2DateSeconds(long millis) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(millis)));
    }
    
    public static String long2DateMillis(long millis) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(millis)));
    }
    
}
