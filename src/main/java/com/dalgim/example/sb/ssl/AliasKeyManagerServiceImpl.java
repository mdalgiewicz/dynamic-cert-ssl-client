package com.dalgim.example.sb.ssl;

import com.dalgim.example.sb.ssl.api.AliasX509KeyManager;
import com.dalgim.example.sb.ssl.api.CertificateAliasService;
import com.dalgim.example.sb.ssl.api.KeyManagerService;
import com.dalgim.example.sb.ssl.api.KeyStoreService;
import com.dalgim.example.sb.ssl.exception.SSLConfigurationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Created by Mateusz Dalgiewicz on 10.04.2017.
 */
@Component
public class AliasKeyManagerServiceImpl implements KeyManagerService {

    private final KeyStoreService keyStoreService;
    private final char[] keyManagerPassword;
    private final boolean isTrustedAll;
    private final CertificateAliasService certificateAliasService;

    @Autowired
    public AliasKeyManagerServiceImpl(KeyStoreService keyStoreService,
                                      @Value("${client.ssl.keystore-key-pass}") char[] keyManagerPassword,
                                      @Value("${client.ssl.trust-all}") boolean isTrustedAll, CertificateAliasService certificateAliasService) {
        this.keyStoreService = keyStoreService;
        this.keyManagerPassword = keyManagerPassword;
        this.isTrustedAll = isTrustedAll;
        this.certificateAliasService = certificateAliasService;
    }

    @Override
    public KeyManager[] initKeyManagers() {
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultAlgorithm);
            KeyStore keyStore = keyStoreService.initKeyStore();
            keyManagerFactory.init(keyStore, keyManagerPassword);
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
            return convertToAliasKeyManagers(keyManagers);
        } catch (NoSuchAlgorithmException e) {
            throw new SSLConfigurationRuntimeException("Error while getting instance of KeyManagerFactory: " + e);
        } catch (UnrecoverableKeyException | KeyStoreException e) {
            throw new SSLConfigurationRuntimeException("Error while initializing KeyManagerFactory: " + e);
        }
    }

    @Override
    public TrustManager[] initTrustManagers() {
        return isTrustedAll ? getTrustAllManagers() : getRealTrustManagers();
    }

    private TrustManager[] getRealTrustManagers() {
        String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
       try {
           TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
           KeyStore trustStore = keyStoreService.initTrustStore();
           trustManagerFactory.init(trustStore);
           return trustManagerFactory.getTrustManagers();
       } catch (NoSuchAlgorithmException e) {
           throw new SSLConfigurationRuntimeException("Error while getting instance of TrustManagerFactory: " + e);
       } catch (KeyStoreException e) {
           throw new SSLConfigurationRuntimeException("Error while initializing TrustManagerFactory: " + e);
       }
    }

    private TrustManager[] getTrustAllManagers() {
        return new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }};
    }

    private AliasX509KeyManager[] convertToAliasKeyManagers(KeyManager[] keyManagers) {
        return Arrays.stream(keyManagers)
                .map(this::convertToAliasKeyManager)
                .toArray(AliasX509KeyManager[]::new);
    }

    private AliasX509KeyManager convertToAliasKeyManager(KeyManager keyManager) {
        return new AliasX509KeyManager((X509KeyManager) keyManager, certificateAliasService);
    }
}
