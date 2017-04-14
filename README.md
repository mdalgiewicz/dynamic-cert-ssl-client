# Simple REST SSL client based on Spring Boot - with dynamic selection SSL certificate 

### Choices
* JDK 8
* SpringBoot v1.5.2
^ SSL based on Java Keystore

### About
This example project shows how to get a ssl connection using multiple client certificate.
Our client will be connect with simple rest server descripted in [dynamic-cert-ssl-server](https://github.com/dalgim/dynamic-cert-ssl-server).

Basically clients uses only one certificate to get ssl connection. But sometimes you can to be forced to select certificate in your application used any condition.
This project shows how we can get this. 
