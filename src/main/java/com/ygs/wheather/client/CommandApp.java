package com.ygs.wheather.client;

import org.apache.commons.cli.*;

import java.util.Scanner;

public class CommandApp {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        args =  new String[]{"-s=2333"};
        Option server = new Option("s", "server", true, "server ip address");
        server.setRequired(true);
        options.addOption(server);

        Option user_ip = new Option("i", "ip", true, "your ip address (optional)");
        user_ip.setRequired(false);
        options.addOption(user_ip);
       // args = new String[]{ "-" };
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd ;

        try {
            cmd = parser.parse(options, args);

            String serverIP = cmd.getOptionValue("server");
            if(cmd.hasOption("ip")){
                String ip = cmd.getOptionValue("ip");
                System.out.println(ip);}
            if(server!=null)
            System.out.println(serverIP);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
        session();


    }
    public static void session(){
        Scanner scanner = new Scanner(System.in);
        boolean  isGoing = true;
        while (isGoing){
            System.out.println("print command number for action:\n1. start connection" +
                    "\n2. disconnect\n3. help \n4. nuclear strike");
            int commandNumber = Integer.parseInt(scanner.nextLine());
            switch (commandNumber){
                case 1:
                System.out.println("Connection try");
                break;
                case 2:
                    System.out.println("Disconnect user");
                    isGoing=!isGoing;
                    break;
                case 3:
                    System.out.println("help? Phhhhhhp, you dont need help !");
                    break;
                case 4:
                    System.out.println("Boooom");
            }
        }
    }
}
