package com.ygs.wheather.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.invoke.MethodHandles;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.ygs.wheather.server.WheatherController.ENDPOINT_REGISTER;
import static com.ygs.wheather.server.SocketConfig.*;
import static java.net.NetworkInterface.getNetworkInterfaces;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
public class ClientSocket {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String ENDPOINT_CONNECT = "/connect";
    private static String URL = "ws://localhost:8083/gs-guide-websocket";
    static final String WEBSOCKET_URI = "ws://localhost:8083";
    static final String WEBSOCKET_TOPIC = "/topic/greetings/";

    private static WebSocketStompClient stompClient1;
    private static StompSession stompSession1;

    private static WebSocketStompClient stompClient2;
    private static StompSession stompSession2;

    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
            while (net.hasMoreElements()) {
                NetworkInterface networkInterface = net.nextElement();
                Enumeration<InetAddress> add = networkInterface.getInetAddresses();
                while (add.hasMoreElements()) {
                    InetAddress a = add.nextElement();
                    if (!a.isLoopbackAddress()
                            && !a.getHostAddress().contains(":")) {

                            System.out.println( "getIPV4 : " + a.getHostAddress());

                       // return a.getHostAddress();
                    }
                }
            }

        }

        catch (SocketException e){
            e.printStackTrace();
        }
        try {

            setUp();
            BlockingQueue<String> queue1 = new LinkedBlockingDeque<>();
            BlockingQueue<String> userQueue1 = new LinkedBlockingDeque<>();
            stompSession1.subscribe(SUBSCRIBE_QUEUE,
                    new FrameHandler((payload) -> {
                        log.info("--> " + SUBSCRIBE_QUEUE + " (cli1) : " + payload);
                        queue1.offer(payload.toString());
                    }));
            stompSession1.subscribe(SUBSCRIBE_USER_PREFIX +SUBSCRIBE_USER_REPLY,
                    new FrameHandler((payload) -> {
                        log.info("--> " + SUBSCRIBE_USER_PREFIX + SUBSCRIBE_USER_REPLY + " (cli1) : " + payload);
                        userQueue1.offer(payload.toString());
                    }));
            Thread.currentThread().sleep(100);

            log.info("### client2 subscribes");
            BlockingQueue<String> queue2 = new LinkedBlockingDeque<>();
            BlockingQueue<String> userQueue2 = new LinkedBlockingDeque<>();
            stompSession2.subscribe(SUBSCRIBE_QUEUE,
                    new FrameHandler((payload) -> {
                        log.info("--> " +SUBSCRIBE_QUEUE + " (cli2) : " + payload);
                        queue2.offer(payload.toString());
                    }));
            stompSession2.subscribe(SUBSCRIBE_USER_PREFIX +SUBSCRIBE_USER_REPLY,
                    new FrameHandler((payload) -> {
                        log.info("--> " + SUBSCRIBE_USER_PREFIX + SUBSCRIBE_USER_REPLY + " (cli2) : " + payload);
                        userQueue2.offer(payload.toString());
                    }));

            Thread.currentThread().sleep(100);

            log.info("### client1 registers");
            stompSession1.send(ENDPOINT_REGISTER, "hello guys");
            Thread.currentThread().sleep(100);
            System.out.println(userQueue1.poll());
            System.out.println(queue1.poll());
            System.out.println(queue2.poll());
            Thread.currentThread().sleep(5000);
            System.out.println("info about usernames:"+userQueue1.poll());
            System.out.println(queue1.poll());
            System.out.println(queue2.poll());


            // Assert.assertEquals("Thanks for your registration!", userQueue1.poll());
           // Assert.assertEquals("Someone just registered saying: hello guys", queue1.poll());
           // Assert.assertEquals("Someone just registered saying: hello guys", queue2.poll());

            Thread.currentThread().sleep(100);

            log.info("### client2 registers");
            stompSession2.send(ENDPOINT_REGISTER, "yo!");
            Thread.currentThread().sleep(100);
          //  Assert.assertEquals("Thanks for your registration!", userQueue2.poll());
           // Assert.assertEquals("Someone just registered saying: yo!", queue1.poll());
            //Assert.assertEquals("Someone just registered saying: yo!", queue2.poll());
            new Scanner(System.in).nextLine();
            setDown();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }

      // Don't close immediately.
    }
    public static WebSocketStompClient   createWebSocketClient(){
        WebSocketStompClient client= new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(new StringMessageConverter());
        return client;
    }
    public static void setUp() throws Exception {
        String wsUrl =WEBSOCKET_URI+ClientSocket.ENDPOINT_CONNECT;

        stompClient1 = createWebSocketClient();
        stompSession1 = stompClient1.connect(wsUrl, new MultiUserHandler()).get();

        stompClient2 = createWebSocketClient();
        stompSession2 = stompClient2.connect(wsUrl, new MultiUserHandler()).get();
    }
    public static void setDown()throws Exception{
        stompSession1.disconnect();
        stompClient1.stop();

        stompSession2.disconnect();
        stompClient2.stop();
    }
}
