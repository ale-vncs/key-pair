package com.poc.key.pair.service;

import com.poc.key.pair.entity.Desafio;
import com.poc.key.pair.repository.DesafioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Log4j2
@RequiredArgsConstructor
public class DesafioService {
    private static final String RSA = "RSA";
    private final KeyPairService keyPairService;
    private final DesafioRepository repository;

    public String gerarDesafioPorClientId(String clientId) throws Exception {
        log.info("Gerando desafio para o clientId: {}", clientId);
        Cipher cipher = cipherInstance(Cipher.ENCRYPT_MODE, clientId);
        String message = randomMessage();

        log.info("Mensagem desafio: {}", message);

        persistirDesafio(clientId, message);

        byte[] secretMessage = message.getBytes(StandardCharsets.UTF_8);
        byte[] encrypetdSecretMessage = cipher.doFinal(secretMessage);

        log.info("Enviando ao client o desafio");
        return Base64.getEncoder().encodeToString(encrypetdSecretMessage);
    }

    public String validarDesafioPorClientId(String clientId, String mensagem) throws Exception {
        log.info("Validando desafio");
        Cipher cipher = cipherInstance(Cipher.DECRYPT_MODE, clientId);

        byte[] decryptedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(mensagem));
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

        log.info("Mensagem desafio: {}", decryptedMessage);
        if( validarDesafio(clientId, decryptedMessage)) {
            return "Desafio valido";
        }
        return "Desafio não valido";
    }

    private String randomMessage() {
        return RandomStringUtils.random(18, true, true);
    }

    private Cipher cipherInstance(int cipherMode, String clientId) throws Exception {
        var keyPair = keyPairService.buscarKeyPairPorClientId(clientId);
        Cipher cipher = Cipher.getInstance(RSA);

        cipher.init(cipherMode, keyPair.getPrivate());

        return cipher;
    }

    private void persistirDesafio(String clientId, String desafio) {
        var model = repository.findById(clientId).orElse(new Desafio());
        model.setClientId(clientId);
        model.setDesafio(desafio);
        repository.save(model);
    }

    private boolean validarDesafio(String clientId, String desafio) throws Exception {
        var model = repository.findById(clientId).orElseThrow(() -> new Exception("Client não encontrado"));
        return model.getDesafio().equals(desafio);
    }
}
