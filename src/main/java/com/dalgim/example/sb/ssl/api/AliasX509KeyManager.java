package com.dalgim.example.sb.ssl.api;

import com.dalgim.example.sb.ssl.exception.SSLConfigurationRuntimeException;
import javax.net.ssl.X509KeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Created by Mateusz Dalgiewicz on 09.04.2017.
 */
public class AliasX509KeyManager implements X509KeyManager {
    private X509KeyManager sourceKeyManager;
    private CertificateAliasService certificateAliasService;

    public AliasX509KeyManager(X509KeyManager sourceKeyManager, CertificateAliasService aliasService) {
        this.sourceKeyManager = sourceKeyManager;
        this.certificateAliasService = aliasService;
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] principals, Socket socket) {
        String certificateAlias = getCertificateAlias();
        for (String keyType : keyTypes) {
            String[] clientAliases = sourceKeyManager.getClientAliases(keyType, principals);
            if (clientAliases != null) {
                for (String alias : clientAliases) {
                    if (alias.equals(certificateAlias)) {
                        return certificateAlias;
                    }
                }
            }
        }
        throw new SSLConfigurationRuntimeException("No valid aliases found!");
    }

    private String getCertificateAlias() {
        return certificateAliasService.findCertificateAlias()
                .orElseThrow(() -> new SSLConfigurationRuntimeException("Certificate alias cannot be null or empty!"));
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return sourceKeyManager.chooseServerAlias(keyType, issuers, socket);
    }

    public X509Certificate[] getCertificateChain(String alias) {
        return sourceKeyManager.getCertificateChain(alias);
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return sourceKeyManager.getClientAliases(keyType, issuers);
    }

    public PrivateKey getPrivateKey(String alias) {
        return sourceKeyManager.getPrivateKey(alias);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return sourceKeyManager.getServerAliases(keyType, issuers);
    }
}
