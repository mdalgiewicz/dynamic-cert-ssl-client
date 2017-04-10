package com.dalgim.example.sb.ssl.api;

import java.util.Optional;

/**
 * Created by Mateusz Dalgiewicz on 09.04.2017.
 */
public interface CertificateAliasService {

    Optional<String> findCertificateAlias();
}
