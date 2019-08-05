package com.ygs.wheather.client;

import com.ygs.wheather.common.IP;
import com.ygs.wheather.common.PingPong;
import com.ygs.wheather.common.Request;
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

import static com.ygs.wheather.server.SocketConfig.*;
import static com.ygs.wheather.server.WheatherController.ENDPOINT_REGISTER;

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
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        args =  new String[]{"-s=localhost"};
        Option server = new Option("s", "server", true, "server ip address");
        server.setRequired(false);
        options.addOption(server);

        Option user_ip = new Option("i", "ip", true, "your ip address (optional)");
        user_ip.setRequired(false);
        options.addOption(user_ip);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd ;

        try {
            cmd = parser.parse(options, args);

            String serverIP = cmd.getOptionValue("server");
            String ip = null;
            if(cmd.hasOption("ip")){
                 ip = cmd.getOptionValue("ip");

                System.out.println(ip);

            }
            else {
                IP_Parser ipParser = new IP_Parser();
                 ip = ipParser.getIP().getIP();
            }
            if(serverIP ==null){
                serverIP="localhost";
            }
            session(serverIP,ip);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }



    }
    private static void session(String server,String ip){
        Scanner scanner = new Scanner(System.in);
        boolean  isGoing = true;
        while (isGoing){
            System.out.println("print command number for action:\n1. start connection" +
                    "\n2. disconnect\n3. Change user ip\n4. Show server ip and user ip " +
                    "\n5. Exit");
            int commandNumber = Integer.parseInt(scanner.nextLine());
            switch (commandNumber){
                case 1:
                System.out.println("Connect to server");
                   if(connect(ip,server))
                       try {
                           final String username  = userQueue.take();
                            server_session(scanner,username);
                       }
                       catch (InterruptedException e){
                           e.printStackTrace();
                       }

                break;
                case 2:
                    System.out.println("server ip:");
                    server = scanner.nextLine();
                    break;
                case 3:
                    System.out.println("user ip:");
                    ip = scanner.nextLine();
                    break;

                case 4:
                    System.out.println("server: "+server+"\nip: "+ip);
                    break;
                case 5:
                    System.out.println("Exit");
                    isGoing=!isGoing;
            }
        }
    }
    private  static void server_session(Scanner scanner,String username){
        boolean  isGoing = true;
      //  Thread.sleep(100);

        try {
            while (isGoing) {
                System.out.println("Print command number for action:\n1. get info about weather" +
                        "\n2. get ping\n3. disconnect from server");
                int commandNumber = Integer.parseInt(scanner.nextLine());
                switch (commandNumber) {
                    case 1:
                        System.out.println("Погода нынче следующая:");

                        Request request = new Request(username);

                        stompSession.send("/topic/weather", request);
                        break;
                    case 2:

                        PingPong pingPong = new PingPong(username);
                        stompSession.send("/topic/ping", pingPong);
                        pingPong = pingQueue.take();
                        System.out.println("server ping:"+pingPong.calcDelay());
                        break;
                    case 3:
                        try {
                            setDown();
                        } catch (Exception e) {
                            System.out.println("Something goes wrong " + e.getMessage());
                        }
                        System.out.println("Exit");
                        isGoing = !isGoing;
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private static boolean connect(String userIP,String serverIP){

        IP ip = new IP(userIP);
        try {
            setUp(serverIP);

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
    public static void setUp(String serverIP) throws Exception {
        final String wsUrl ="ws://"+serverIP+":8083"+ClientSocket.ENDPOINT_CONNECT;
        connection_uri = wsUrl;
        stompClient = createWebSocketClient();
        stompSession = stompClient.connect(connection_uri, new MultiUserHandler()).get();
        stompSession.subscribe(SUBSCRIBE_QUEUE,
                new FrameHandler((payload) -> {
                    try {
                    //    log.info("--> " + SUBSCRIBE_QUEUE + " (cli1) : " + payload);
                        queue1.offer(payload.toString());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }));
        stompSession.subscribe(SUBSCRIBE_USER_PREFIX +SUBSCRIBE_USER_REPLY,
                new FrameHandler((payload) -> {
                    try {
                        log.info("--> " + SUBSCRIBE_USER_PREFIX + SUBSCRIBE_USER_REPLY + " (cli1) : " + payload);
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
                        //log.info("--> " + SUBSCRIBE_USER_PREFIX + PING_REPLY + " (cli1) : " + payload.calcDelay() +"ms");
                        //userQueue.offer(payload.getUsername());
                        pingQueue.offer(payload);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


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
        //new StringMessageConverter();
        return client;
    }

}
