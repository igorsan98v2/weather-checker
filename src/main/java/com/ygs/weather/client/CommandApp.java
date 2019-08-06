package com.ygs.weather.client;

import com.ygs.weather.server.IP;
import com.ygs.weather.server.PingPong;
import com.ygs.weather.server.Request;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.lang.invoke.MethodHandles;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.ygs.weather.server.SocketConfig.*;
import static com.ygs.weather.server.WheatherController.ENDPOINT_REGISTER;

public class CommandApp {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String ENDPOINT_CONNECT = "/connect";
    private static String URL = "ws://localhost:8083/gs-guide-websocket";
    private static final String WEBSOCKET_URI = "ws://localhost:8083";
    private static final String WEBSOCKET_TOPIC = "/topic/greetings/";
    private static BlockingQueue<String> queue1 = new LinkedBlockingDeque<>();
    private static BlockingQueue<String> userQueue = new LinkedBlockingDeque<>();
    private static BlockingQueue<PingPong> pingQueue = new LinkedBlockingDeque<>();
    private static WebSocketStompClient stompClient;
    private static StompSession stompSession;
    private static String connection_uri;
    private static boolean isServerSession=false;
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option server = new Option("s", "server", true, "set server ip address, default is localhost");
        server.setRequired(false);
        options.addOption(server);
        Option help = new Option("h","help",false,"provide help info");
        options.addOption(help);
        Option user_ip = new Option("i", "ip", true, "your ip address (optional)");
        user_ip.setRequired(false);
        options.addOption(user_ip);
        Option port = new Option("p","port",false,"set server port (default 8083)");
        port.setRequired(false);
        options.addOption(port);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd ;

        try {
            cmd = parser.parse(options, args);

            String serverIP = cmd.getOptionValue("server");
            String ip = null;
            String portNum = cmd.getOptionValue("port");
            if(cmd.hasOption("ip")){
                 ip = cmd.getOptionValue("ip");

                System.out.println(ip);

            }
            else {
                IP_Parser ipParser = new IP_Parser();
                 ip = ipParser.getIP().getIP();
            }
            if(serverIP == null){
                serverIP="localhost";
            }

            if (cmd.hasOption("help")){
                formatter.printHelp("utility-name", options);
                System.exit(1);
            }
            if(!cmd.hasOption("port")){
                portNum="8083";
            }
            session(serverIP,ip,portNum);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }



    }
    private static void session(String server,String ip,String port){
        Scanner scanner = new Scanner(System.in);
        boolean  isGoing = true;
        while (isGoing){
            System.out.println("print command number for action:\n1. start connection" +
                    "\n2. Change server ip\n3. Change user ip" +
                    "\n4. Change server port"+
                    "\n5. Show server ip and user ip " +
                    "\n6. Exit\n");
            System.out.print("Enter: ");
            String commandNumber = scanner.nextLine();
            switch (commandNumber){
                case "1":
                System.out.println("Connect to server");
                   if(connect(ip,server,port))
                       try {
                           final String username  = userQueue.take();
                            server_session(scanner,username);
                       }
                       catch (InterruptedException e){
                           e.printStackTrace();
                       }

                break;
                case "2":
                    System.out.print("server ip:");
                    server = scanner.nextLine();
                    break;
                case "3":
                    System.out.print("user ip:");
                    ip = scanner.nextLine();
                    break;
                case "4":
                    System.out.print("server port");
                    port = scanner.nextLine();
                case "5":
                    System.out.println("server: "+server+":"+port+"\nip: "+ip);
                    break;
                case "6":
                    System.out.println("Exit");
                    isGoing=!isGoing;
            }
        }
    }
    private  static void server_session(Scanner scanner,String username){
        isServerSession = true;


        try {
            while (isServerSession) {
                System.out.println("Print command number for action:\n1. get info about weather" +
                        "\n2. get ping\n3. disconnect from server\n");
                System.out.print("Enter: ");
                String commandNumber = scanner.nextLine();
                switch (commandNumber) {
                    case "1":

                        Request request = new Request(username);
                        stompSession.send("/topic/weather", request);
                        Thread.sleep(300);//just need to have nice output
                        break;
                    case "2":

                        PingPong pingPong = new PingPong(username);
                        stompSession.send("/topic/ping", pingPong);
                        pingPong = pingQueue.take();
                        System.out.println("server ping:"+pingPong.calcDelay());
                        break;
                    case "3":
                        try {
                            setDown();
                        } catch (Exception e) {
                            System.out.println("Something goes wrong " + e.getMessage());
                        }
                        System.out.println("Exit");
                        isServerSession = !isServerSession;
                }

            }
            System.out.println("Back to previous menu");
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private static boolean connect(String userIP,String serverIP,String port){

        IP ip = new IP(userIP);
        try {
            setUp(serverIP,port);

            try {
                Thread.sleep(100);
                stompSession.send(ENDPOINT_REGISTER, ip);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            System.out.println("Connection error:"+e.getMessage());
            return false;
        }
        return true;
    }
    private static void disconnect(){
        try {
            setDown();
        }
        catch (Exception e){
            System.out.println("Bad disconnection");
        }
    }
    public static void setUp(String serverIP,String port) throws Exception {
        final String wsUrl ="ws://"+serverIP+":"+port+ENDPOINT_CONNECT;
        connection_uri = wsUrl;
        stompClient = createWebSocketClient();
        stompSession = stompClient.connect(connection_uri, new MultiUserHandler()).get();
        stompSession.subscribe(SUBSCRIBE_QUEUE,
                new FrameHandler((payload) -> {
                    try { if(payload.getWeather()!=null); }
                    catch (Exception e){ e.printStackTrace(); }
                }));
        stompSession.subscribe(SUBSCRIBE_USER_PREFIX +SUBSCRIBE_USER_REPLY,
                new FrameHandler((payload) -> {
                    try {
                        if(payload.getWeather()!=null) {
                            log.info("--> " + SUBSCRIBE_USER_PREFIX + SUBSCRIBE_USER_REPLY + " (cli1) : " + payload);
                        }
                        userQueue.offer(payload.getUsername());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }));

        stompSession.subscribe(SUBSCRIBE_USER_PREFIX +PING_REPLY,
                new PingPongFrameHandler((payload) -> {
                    try {
                        payload.setPong();
                        pingQueue.offer(payload);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }));
        stompSession.subscribe(SERVER_STOP_TOPIC,new StopMessageHandler((payload) -> {
            log.info(payload.toString());
            isServerSession=false;
            System.out.println("Type any key for continue");
        }));
    }
    public static void setDown()throws Exception{
        stompSession.disconnect();
        stompClient.stop();

    }
    public static WebSocketStompClient   createWebSocketClient(){
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
        container.setDefaultMaxTextMessageBufferSize(1024 * 1024);

        WebSocketStompClient client= new WebSocketStompClient(new StandardWebSocketClient(container));
        client.setMessageConverter(new MappingJackson2MessageConverter());
        client.setInboundMessageSizeLimit(Integer.MAX_VALUE);

        return client;
    }

}
