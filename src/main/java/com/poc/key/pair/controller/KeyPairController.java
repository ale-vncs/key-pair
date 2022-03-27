package com.poc.key.pair.controller;

import com.poc.key.pair.service.DesafioService;
import com.poc.key.pair.service.KeyPairService;
import com.poc.key.pair.service.dto.CriarChaveRequestDTO;
import com.poc.key.pair.service.dto.ValidarMensagemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/keypair")
@RequiredArgsConstructor
public class KeyPairController {
    private final KeyPairService keyPairService;
    private final DesafioService desafioService;

    @GetMapping
    public String buscarChavePublicaPorClientId(
            @RequestParam("clientId") String clientId
    ) throws Exception {
        return keyPairService.buscarChavePublicPorClientId(clientId);
    }

    @GetMapping("/gerar")
    public String gerarChavePublica(
            @RequestBody @Valid CriarChaveRequestDTO body
    ) throws Exception {
        return keyPairService.gerarChavePublicaPorClientId(body.getClientId());
    }

    @GetMapping("/desafio/{clientId}")
    public String gerarDesafioPorClientId(
            @PathVariable("clientId") String clientId
    ) throws Exception {
        return desafioService.gerarDesafioPorClientId(clientId);
    }

    @PostMapping("/desafio/{clientId}")
    public String validarDesafioPorClientId(
            @PathVariable("clientId") String clientId,
            @RequestBody @Valid ValidarMensagemDTO body
    ) throws Exception {
        return desafioService.validarDesafioPorClientId(clientId, body.getMensagem());
    }
}
