package com.springbatch.faturacartaocreditojob.processor;


import com.springbatch.faturacartaocreditojob.dominio.Cliente;
import com.springbatch.faturacartaocreditojob.dominio.FaturaCartaoCredito;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarregarDadosClienteProcessor implements ItemProcessor<FaturaCartaoCredito, FaturaCartaoCredito> {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public FaturaCartaoCredito process(FaturaCartaoCredito item) {
        String uri = String.format("http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d", item.getCartaoCredito().getCliente().getId());
        ResponseEntity<Cliente> response = restTemplate.getForEntity(uri, Cliente.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new ValidationException("Cliente não encontrado!");

        item.setCliente(response.getBody());
        return item;
    }
}