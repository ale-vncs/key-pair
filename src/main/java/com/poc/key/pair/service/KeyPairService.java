package com.poc.key.pair.service;

import com.poc.key.pair.repository.KeyPairRepository;
import com.poc.key.pair.service.dto.PemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeyPairService {

    private static final String RSA = "RSA";
    private final KeyPairRepository repository;

    public String buscarChavePublicPorClientId(String clientId) throws Exception {
        var pem = new PemDTO(buscarKeyPairPorClientId(clientId));
        return pem.getPemPublicKey();
    }

    public String gerarChavePublicaPorClientId(String clientId) throws Exception {
        try {
            log.info("Gerando chaves para o cliente: {}", clientId);
            KeyPair keyPair = this.gerarChaveRSA();

            log.info("Salvando chaves no banco de dados...");
            var pem = this.persistirChave(clientId, keyPair);

            log.info("retornado chave publica");
            return pem.getPemPublicKey();
        } catch (Exception e) {
            log.error("Não foi possivel gerar as chaves parao cliente: {}", clientId);
            log.error(e);
            throw new Exception("");
        }
    }

    public KeyPair buscarKeyPairPorClientId(String clientId) throws Exception {
        var model = repository.findByClientId(clientId).orElseThrow(() -> new Exception("Cliente não encontrado"));

        byte[] bytePrivateKey = Base64.getDecoder().decode(model.getPrivateKey());
        byte[] bytePublicKey = Base64.getDecoder().decode(model.getPublicKey());

        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(bytePrivateKey);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(bytePublicKey);

        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);
        PublicKey publicKey = keyFactory.generatePublic(publicSpec);

        return new KeyPair(publicKey, privateKey);
    }

    private PemDTO persistirChave(String clientId, KeyPair keyPair) {
        var model = repository.findByClientId(clientId).orElse(new com.poc.key.pair.entity.KeyPair());

        var pem = new PemDTO(keyPair);

        model.setClientId(clientId);
        model.setPrivateKey(pem.getPrivateKey());
        model.setPublicKey(pem.getPublicKey());

        repository.save(model);

        return pem;
    }

    private KeyPair gerarChaveRSA() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
