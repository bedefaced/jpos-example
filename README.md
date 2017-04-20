## jPOS-example
This is example of using jPOS. In this project jPOS (with some Java classes) works as SOAP server which proxies requests to ISO-8583 processing server and as ISO-8583 processing server.

### SOAP server
 - it's a JAX-WS web service that just does mapping of SOAP fields to ISO message fields, send it to ISO-8583 server and return ISO response as SOAP response
 - web service parameters are listed in **/src/dist/deploy/60_clientws.xml**
 - sources in **/src/main/java/middleware/webservice/**
 - messages are pre-processed before sending to ISO-8583 server by **/src/dist/deploy/30_processors.xml** jPOS script
 - by default runs on **http://localhost:8000/ws/**, WSDL: **http://localhost:8000/ws/?wsdl**
 - remote ISO-8583 server connection parameters are listed in **/src/dist/deploy/61_clientchannel.xml**

### ISO-8583 server

 - it is a dummy-server, that just responses OK (response code 0 in 39 field) to any request.
 - channel parameters are listed in **/src/dist/deploy/40_server.xml**
 - message processing is described by the same **/src/dist/deploy/30_processors.xml** jPOS script

### Build and run
Build with Gradle (or Gradle wrapper):

    gradlew installApp

 and run Q2 server:

    build/install/jpos-example/bin/q2

### Used jPOS Documentation

 - [jPOS-Project-Guide.pdf](http://jpos.org/doc/jPOS-Project-Guide.pdf)
 - [proguide-draft.pdf](http://jpos.org/doc/proguide-draft.pdf)
 - [jPOS-EE.pdf](http://jpos.org/doc/jPOS-EE.pdf)
 - [jPOS Blog](http://jpos.org/blog/)
 - [jPOS Google group](http://markmail.org/search/?q=com.googlegroups.jpos-users)
 - [jPOS Server](http://didikhari.web.id/java/jpos-server-forward-request-message-to-another-server/)
 - [jPOS keep-alive](https://groups.google.com/forum/#!topic/jpos-users/jxA2MGdqaaA)

### Remarks
Since jPOS runs as standalone Q2 server, it is possible to update message processors (or any other xml's) «on-the-fly», while jPOS is running.

### jPOS-template
This project is based on [jPOS Template](https://github.com/jpos/jPOS-template/blob/master/README.md). 
