# Simple REST SSL client based on Spring Boot - with dynamic selection SSL certificate 

### Choices
* JDK 8
* SpringBoot v1.5.2
^ SSL based on Java Keystore

### About
This example project shows how to get a ssl connection using multiple client certificate.
Our client will be connect with simple rest server descripted in [dynamic-cert-ssl-server](https://ub.com/dalgim/dynamic-cert-ssl-server).

Basically clients uses only one certificate to get ssl connection. But sometimes you can to be forced to select certificate in your application used any condition.
This project shows how we can get this. 

### How to use
First of all you must create _client-keystore.jks_ with your certificates and _client-truststore.jks_ with server certificate.
To check how to do this open this project [dynamic-cert-ssl-server](https://ub.com/dalgim/dynamic-cert-ssl-server).
Once we did this we have to configure application ssl properties. It isn't spring boot properties but our custom properties used in SSL service implementation.

```properties
server:  
  client:
    ssl:
      keystore: C:\client-keystore.jks 
      keystore-pass: password 
      keystore-key-pass: password 
      truststore: C:\client-truststore.jks
      truststore-pass: password
      trust-all: false
```

**trust-all** - When parameter is set to *true* it means client will trust all server with ssl connection, it is good for test when we not contain server certificate but for production this way is not recommended.
 When paramater is set to *false* then we must contains server certificate in truststore otherwise the ssl connection wont work.
**keystore-key-pass** - By default ssl connection is getting uses one client certificate but in our case we have more then one certificate and we must set the same password for all of them it is required to load key manager factory properly.

### Building and running application

Run "mvn clean install"
```sh
mvn clean install
```
next
```sh
mvn spring-boot:run
```