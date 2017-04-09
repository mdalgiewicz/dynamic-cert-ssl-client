package com.dalgim.example.sb.config;

import java.util.Optional;

/**
 * Created by dalgim on 09.04.2017.
 */
public interface CertificateAliasService {

    Optional<String> findCertificateAlias();
}
