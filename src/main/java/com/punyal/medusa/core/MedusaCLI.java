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
package com.punyal.medusa.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MedusaCLI {
    private final CommandLineParser parser;
    private final Options options;
    
    public MedusaCLI() {
        parser = new DefaultParser();
        options = new Options();
        options.addOption("h", "help", false, "Show this info");
        options.addOption(OptionBuilder.withArgName("hots> <database> <user> <pass").hasArgs(4).withValueSeparator(' ').withDescription("MySQL database configuration").create("MySQL"));
        options.addOption(OptionBuilder.withArgName("database").hasArgs(1).withValueSeparator().withDescription("H2 database configuration").create("H2"));
        //options.addOption(OptionBuilder.withArgName("newAdminUser> <newAdminPass").hasArgs(2).withValueSeparator(' ').withDescription("Reset databases and set new admin").create("resetDB"));
        options.addOption(OptionBuilder.withArgName("port").hasArgs(1).withValueSeparator().withDescription("Set CoAP port").create("coap"));
        options.addOption(OptionBuilder.withArgName("port").hasArgs(1).withValueSeparator().withDescription("Set Web port").create("web"));
        options.addOption(OptionBuilder.withArgName("level").hasArgs(1).withValueSeparator().withDescription("Set logger level [DEBUG, INFO, WARNING, ERROR, CRITICAL]").create("log"));
    }
    
    public CommandLine getCLI(String[] args) throws ParseException {
        return parser.parse(options, args);
    }
    
    public Options getOptions() {
        return options;
    }
}
