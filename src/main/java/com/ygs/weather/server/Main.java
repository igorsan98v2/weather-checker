package com.ygs.weather.server;

import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String []args){
        Options options = new Options();
        Option port = new Option("p","port",true,"set server port (default 8083)");
        Option help = new Option("h","help",false,"help info");
        port.setRequired(false);
        help.setRequired(false);
        options.addOption(port);
        options.addOption(help);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd ;
        String portNum = "8083";
        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption("port")){
                portNum = cmd.getOptionValue("port");
            }
            if(cmd.hasOption("help")){
                formatter.printHelp("help",options);
                System.exit(1);
            }
        }
        catch (ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
            SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", portNum));
        app.run(args);

    }
}
