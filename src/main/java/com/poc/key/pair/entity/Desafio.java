package com.poc.key.pair.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "desafio")
public class Desafio {

    @Id
    @Column(name = "clientId")
    private String clientId;

    @Column(name = "desafio")
    private String desafio;
}
