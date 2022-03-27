package com.poc.key.pair.service.dto;

import lombok.Getter;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class PemDTO {
    private static final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

    private final String breakLine = "\n";

    @Getter
    private final String publicKey;

    @Getter
    private final String privateKey;

    @Getter
    private final String pemPublicKey;

    @Getter
    private final String pemPrivateKey;

    public PemDTO(KeyPair keyPair) {
        this.pemPublicKey = buildPemPublicKey(keyPair.getPublic());
        this.pemPrivateKey = buildPemPrivateKey(keyPair.getPrivate());
        this.privateKey = parseString(keyPair.getPrivate().getEncoded());
        this.publicKey = parseString(keyPair.getPublic().getEncoded());
    }

    private String buildPemPublicKey(PublicKey publicKey) {
        var publicBuilder = new StringBuilder();

        String formatKey = formatPem(parseString(publicKey.getEncoded()));

        publicBuilder.append(BEGIN_PUBLIC_KEY);
        publicBuilder.append(breakLine);
        publicBuilder.append(formatKey);
        publicBuilder.append(breakLine);
        publicBuilder.append(END_PUBLIC_KEY);

        return publicBuilder.toString();
    }

    private String buildPemPrivateKey(PrivateKey privateKey) {
        var privateBuild = new StringBuilder();

        String formatKey = formatPem(parseString(privateKey.getEncoded()));

        privateBuild.append(BEGIN_RSA_PRIVATE_KEY);
        privateBuild.append(breakLine);
        privateBuild.append(formatKey);
        privateBuild.append(breakLine);
        privateBuild.append(END_RSA_PRIVATE_KEY);

        return privateBuild.toString();
    }

    private String formatPem(String format) {
        return format.replaceAll("(.{64})", "$1\n");
    }

    private String parseString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
