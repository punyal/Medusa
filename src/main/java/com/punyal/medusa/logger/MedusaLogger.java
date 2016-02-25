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
package com.punyal.medusa.logger;

import static com.punyal.medusa.constants.Defaults.*;
import static com.punyal.medusa.logger.MedusaLogger.GrainLevel.*;
import com.punyal.medusa.utils.DateUtils;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MedusaLogger {
    private final GrainLevel levelFilter;
    
    public MedusaLogger() {
        levelFilter = DEFAULT_LOGGER_LEVEL; // TODO: improve this
    }
    
    private void print(GrainLevel level, String toPrint) {
        if (levelFilter.value <= level.value)
            System.out.println(DateUtils.long2DateMillis(System.currentTimeMillis())+" ["+level+"] "+toPrint);
        
    }
    
    public void debug(String toPrint) {
        print(DEB, toPrint);
    }
    
    public void info(String toPrint) {
        print(INF, toPrint);
    }
    
    public void warning(String toPrint) {
        print(WAR, toPrint);
    }
    
    public void error(String toPrint) {
        print(ERR, toPrint);
    }
    
    public void critical(String toPrint) {
        print(CRI, toPrint);
    }
    
    public enum GrainLevel {
        DEB       (1),
        INF        (2),
        WAR     (3),
        ERR       (4),
        CRI    (5);
        
        /* The code value. */
	public final int value;
        
        /**
         * Instantiates a new code with the specified code value.
         * 
         * @param value the integer value of the code
         */
        GrainLevel(int value) {
            this.value = value;
        }
        public static GrainLevel valueOf(int value) {
            switch (value) {
                   case   1: return DEB;
                   case   2: return INF;
                   case   3: return WAR;
                   case   4: return ERR;
                   case 255: return CRI;
                   default: throw new IllegalArgumentException("Unknown Attribute Field "+value); 
            }
        }
    }    
}
