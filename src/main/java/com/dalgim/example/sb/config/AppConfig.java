package com.dalgim.example.sb.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Created by dalgim on 09.04.2017.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Value(value = "classpath:ssl/client-keystore.jks")
    private Resource keystoreResource;
    @Value(value = "classpath:ssl/client-truststore.jks")
    private Resource truststoreResource;
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String TRUSTSTORE_TYPE = "JKS";
    //On production environment passwords should be encrypted
    private static final char[] KEYSTORE_PASSWORD = new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    private static final char[] TRUSTSTORE_PASSWORD = new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    private static final char[] KMF_PASSWORD =  new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    private static final String SSL_PROTOCOL = "TLS";

    private final CertificateAliasService certificateAliasService;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private HttpClient httpClient() {
        return HttpClients.custom().setSSLContext(sslContext()).build();
    }
    SSLContext sslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            KeyManager[] keyManagers = keyManagerFactory().getKeyManagers();
            AliasX509KeyManager[] aliasX509KeyManagers = createAliasKeyManagers(keyManagers);
            TrustManager[] trustManagers = trustManagerFactory().getTrustManagers();
            sslContext.init(aliasX509KeyManagers, trustManagers, new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while getting instance of SSLContext: " + e);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Error while initializing SSLContext: " + e);
        }
    }

    AliasX509KeyManager[] createAliasKeyManagers(KeyManager[]keyManagers) {
        if (keyManagers != null) {
            AliasX509KeyManager[] aliasKeyManagers = new AliasX509KeyManager[keyManagers.length];
            for (short i = 0; i < keyManagers.length; i++) {
                aliasKeyManagers[i] = new AliasX509KeyManager((X509KeyManager) keyManagers[i], certificateAliasService);
            }
            return aliasKeyManagers;
        }
        return null;
    }

    KeyManagerFactory keyManagerFactory() {
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultAlgorithm);
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(keystoreResource.getInputStream(), KEYSTORE_PASSWORD);
            keyManagerFactory.init(keyStore, KMF_PASSWORD);
            return keyManagerFactory;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while getting instance of KeyManagerFactory: " + e);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Error while getting instance of KeyStore: " + e);
        } catch (CertificateException | IOException e) {
            throw new RuntimeException("Error while loading KeyStore: " + e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException("Error while initializing KeyManagerFactory: " + e);
        }
    }

    TrustManagerFactory trustManagerFactory() {
        String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            KeyStore trustStore = KeyStore.getInstance(TRUSTSTORE_TYPE);
            trustStore.load(truststoreResource.getInputStream(), TRUSTSTORE_PASSWORD);
            trustManagerFactory.init(trustStore);
            return trustManagerFactory;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while getting instance of TrustManagerFactory: " + e);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Error while getting instance of TrustStore: " + e);
        } catch (CertificateException | IOException e) {
            throw new RuntimeException("Error while loading TrustStore: " + e);
        }
    }
}
