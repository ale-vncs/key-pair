package com.poc.key.pair.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "keypair")
public class KeyPair {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "privateKey")
    private String privateKey;

    @Column(name = "publicKey")
    private String publicKey;
}
