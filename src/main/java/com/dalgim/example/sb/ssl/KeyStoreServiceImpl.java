package com.dalgim.example.sb.ssl;

import com.dalgim.example.sb.ssl.api.KeyStoreService;
import com.dalgim.example.sb.ssl.exception.SSLConfigurationRuntimeException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by Mateusz Dalgiewicz on 10.04.2017.
 */
@Component
public class KeyStoreServiceImpl implements KeyStoreService {

    private static final String PROVIDER = "JKS";
    private final String keystoreLocation;
    private final char[] keystorePassword;
    private final String truststoreLocation;
    private final char[] truststorePassword;

    @Autowired
    public KeyStoreServiceImpl(@Value("${client.ssl.keystore}") String keystoreLocation,
                               @Value("${client.ssl.keystore-pass}") char[] keystorePassword,
                               @Value("${client.ssl.truststore}") String truststoreLocation,
                               @Value("${client.ssl.truststore-pass}") char[] truststorePassword) {

        this.keystoreLocation = keystoreLocation;
        this.keystorePassword = keystorePassword;
        this.truststoreLocation = truststoreLocation;
        this.truststorePassword = truststorePassword;
    }

    @Override
    public KeyStore initKeyStore() {
        return initKeyStore(keystoreLocation, keystorePassword);
    }

    @Override
    public KeyStore initTrustStore() {
        return initKeyStore(truststoreLocation, truststorePassword);
    }

    private KeyStore initKeyStore(String keystoreLocation, char[] password) {
        Preconditions.checkNotNull(keystoreLocation, "KeyStore location cannot be null!");
        Preconditions.checkNotNull(password, "KeyStore password cannot be null!");

        try (FileInputStream fileInputStream = new FileInputStream(keystoreLocation)) {
            KeyStore keyStore = KeyStore.getInstance(PROVIDER);
            keyStore.load(fileInputStream, password);
            return keyStore;
        } catch (IOException e) {
            throw new SSLConfigurationRuntimeException("Error while getting keystore from file path: " + e);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SSLConfigurationRuntimeException("Error while getting instance of keystore: " + e);
        }
    }
}
