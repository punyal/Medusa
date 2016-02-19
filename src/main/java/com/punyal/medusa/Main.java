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

import com.punyal.medusa.core.MedusaCLI;
import com.punyal.medusa.core.database.MySQLconf;
import com.punyal.medusa.logger.MedusaLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Main {
    private static final MedusaLogger log = new MedusaLogger();
    
    public static void main(String[] args) {
        MedusaCLI cli = new MedusaCLI();
        Medusa medusa = new Medusa();
        
        try {
            CommandLine cmd = cli.getCLI(args);
            
            if (cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Medusa", cli.getOptions());
                System.exit(0);
            }
            
            if (cmd.hasOption("MySQL")) {
                MySQLconf mySQLconf = new MySQLconf(cmd.getOptionValues("MySQL"));
                log.debug(mySQLconf.toString());
                medusa.configMySQL(mySQLconf);
            }
            
        } catch (ParseException ex) {
            log.error("Parsing failed. Reason: "+ex.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Medusa", cli.getOptions());
            System.exit(1);
        }
        
        
        medusa.run();
    }
}
