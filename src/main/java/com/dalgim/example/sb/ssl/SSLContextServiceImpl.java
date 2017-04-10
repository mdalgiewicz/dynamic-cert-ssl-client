package com.dalgim.example.sb.ssl;

import com.dalgim.example.sb.ssl.api.KeyManagerService;
import com.dalgim.example.sb.ssl.api.SSLContextService;
import com.dalgim.example.sb.ssl.exception.SSLConfigurationRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Mateusz Dalgiewicz on 10.04.2017.
 */
@Component
@RequiredArgsConstructor
public class SSLContextServiceImpl implements SSLContextService {

    private static final String SSL_PROTOCOL_ALIAS = "TLS";
    private final KeyManagerService keyManagerService;

    @Override
    public SSLContext initSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL_ALIAS);
            KeyManager[] keyManagers = keyManagerService.initKeyManagers();
            TrustManager[] trustManagers = keyManagerService.initTrustManagers();
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            throw new SSLConfigurationRuntimeException("Error while getting instance of SSLContext: " + e);
        } catch (KeyManagementException e) {
            throw new SSLConfigurationRuntimeException("Error while initializing SSLContext: " + e);
        }
    }
}
