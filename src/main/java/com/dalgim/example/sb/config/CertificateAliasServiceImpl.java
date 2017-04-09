package com.dalgim.example.sb.config;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by dalgim on 09.04.2017.
 */
@Service
public class CertificateAliasServiceImpl implements CertificateAliasService {
    @Override
    public Optional<String> findCertificateAlias() {
        return Optional.of("clientkey2");
    }
}
