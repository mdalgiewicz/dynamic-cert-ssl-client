package com.dalgim.example.sb.ssl;

import com.dalgim.example.sb.ssl.api.CertificateAliasService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Mateusz Dalgiewicz on 09.04.2017.
 */
@Service
public class CertificateAliasServiceImpl implements CertificateAliasService {

    private static final List<String> validAliases = Lists.newArrayList(
            "clientkey1",
            "clientkey2",
            "clientkey3"
    );

    @Override
    public Optional<String> findCertificateAlias() {
        String value = randAlias();
        System.out.println(value);
        return Optional.of(value);
    }

    private String randAlias() {
        return validAliases.get(new Random().nextInt(validAliases.size()));
    }
}
