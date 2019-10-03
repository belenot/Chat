# chat
Chat with websocket and spring boot  

Used technologies:  
  * Backend: Java, Spring Boot, Spring(MVC, Websocket, Security, Data JPA), Hibernate/JPA  
  * Frontend: ReactJS, React Hooks, React Context, React-Bootstrap  
  * Database: Postgresql, H2  
  * Deploy: heroku  
-----------
Description: First, client has to sign up, than after authentication he is allowed to main app. That part is controlled via **Spring Security**. Client can join to room, so he can be authorizated for given room. Client may create room, so he will be admin, and can ban other clients. Such events as banned, leaved, message comming handled within **WebSocket** and **STOMP** protocols. Authorizated clients are being subscribed to given room, so they can listen event. Also they can send messages throught websocket support, althought it could be done with simple http.   
On java side there is eventpublisher responsible for publishing room events, whose are sended into stomp message broker.

Currently, given screenshots are outdated. Need some cosmetic changes, before it can be fully described here

Authorization page
![screenshot_1](https://github.com/belenot/chat/blob/master/Screenshot%20from%202019-09-04%2007-20-35.png?raw=true)  

Chat page
![screenshot_2](https://github.com/belenot/chat/blob/master/Screenshot%20from%202019-09-04%2007-20-02.png?raw=true)
