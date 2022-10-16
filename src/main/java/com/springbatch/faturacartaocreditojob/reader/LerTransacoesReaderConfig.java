package com.springbatch.faturacartaocreditojob.reader;

import com.springbatch.faturacartaocreditojob.dominio.CartaoCredito;
import com.springbatch.faturacartaocreditojob.dominio.Cliente;
import com.springbatch.faturacartaocreditojob.dominio.Transacao;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Configuration
public class LerTransacoesReaderConfig {

    @Bean
    public JdbcCursorItemReader<Transacao> lerTransacoesReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transacao>()
                .name("lerTransacoesReader")
                .dataSource(dataSource)
                .sql("select * from transacao join cartao_credito using(numero_cartao_credito) order by numero_cartao_credito")
                .rowMapper(rowMapperTransacao())
                .build();
    }

    private RowMapper<Transacao> rowMapperTransacao() {
        return (rs, rowNum) -> {
            CartaoCredito cartaoCredito = new CartaoCredito();
            cartaoCredito.setNumeroCartaoCredito(rs.getInt("numero_cartao_credito"));

            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("cliente"));

            cartaoCredito.setCliente(cliente);

            Transacao transacao = new Transacao();
            transacao.setId(rs.getInt("id"));
            transacao.setCartaoCredito(cartaoCredito);
            transacao.setData(rs.getDate("data"));
            transacao.setValor(rs.getDouble("valor"));
            transacao.setDescricao(rs.getString("descricao"));

            return transacao;
        };
    }
}