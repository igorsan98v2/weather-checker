# weather-checker


Here it is example of usage Spring websocket and STOMP. This project use them for exchange information about 
weather beetwen server and client. First of all, client makes connection to server, then Server compute user 
location by ip and after that send lat. and long. coord of user region to weather service, after that server 
pull information about weather in region. This information always available to user by user request to server
and also by auto-notification every 60 seconds.

<hr></hr>
<h3>installation guide</h3>
<ol>
  <li> 
    clone this repo <code>git clone https://github.com/igorsan98v2/weather-checker</code>
  </li>
  <li>change directory to ./weather-checker</li>
  <li>run install by <code>mvn install</code> </li>
  <li>change directory to ./target</li>
  <li>run server by command: <code>java -jar weather_server-1.0-SNAPSHOT.jar</code> 
      or run client by command: <code>java -jar spring-client.jar </code>
  </li>
</ol>
<hr></hr>
<h3>Life hacks & tips</h3>
Did you know that client and server could be runned with helpful args?! Check it out!<br>
Client & server: 
<ul>
  <li><code>--help -h</code> it`s hard to imagine but this project support even this arg!</li>
  <li><code>--port -p</code> this arg change server port for server and connection server port.</li>
  
</ul>
Client:
<ul>
 <li><code>--ip -i</code> This closeful to magic arg could change your ip for server.
  Just write it and server will think that your from another city,region and etc. </li>
  <li><code>--server -s</code> You may change server adress, if you need it. Do you need it ?</li>
</ul>
It`s enough for life hacks, time to one tip,use this ip for change you location, if you need to test multiple connection to 
server with different anserws:
<ul>
  <li>31.43.110.0-31.43.110.255  Kiyv region</li>
  <li>31.131.32.0-31.131.39.255  Lviv region</li>
  <li>37.57.96.0-37.57.96.255  Dnepr region</li>
  <li>4.71.129.0-4.71.129.255 Los Angeles </li>
  <li>24.20.94.0-24.20.94.255 Vancouver</li>
</ul>
