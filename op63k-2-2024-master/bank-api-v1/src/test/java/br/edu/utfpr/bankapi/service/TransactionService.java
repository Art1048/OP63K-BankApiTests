package br.edu.utfpr.bankapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.edu.utfpr.bankapi.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class TransactionService {

    @Autowired
    MockMvc mvc;

    @Autowired
    EntityManager entityManager;

    @Test
    void deveriaRetornar404ParaSaqueInvalido() throws Exception {
        // ARRANGE
        var json = "{}";

        // ACT

        var res = mvc.perform(
                MockMvcRequestBuilders
                        .post("/withdraw").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT

        Assertions.assertEquals(404, res.getStatus());

    }

    @Test
    void deveriaRetornar200ParaSaqueValido() throws Exception {
        // ARRANGE
        Account account = new Account("Felizberto", 987654321, 20000, 1000);

        entityManager.persist(account);

        var json = """

                {
                    "sourceAccountNumber": 987654321,
                    "amount": 100
                }

                """;

        // ACT

        var res = mvc.perform(
                MockMvcRequestBuilders
                        .post("/withdraw").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT

        Assertions.assertEquals(200 | 201, res.getStatus());

    }

    @Test
    void deveriaRetornar404ParaTrasacaoInvalido() throws Exception {
        // ARRANGE
        var json = "{}";

        // ACT

        var res = mvc.perform(
                MockMvcRequestBuilders
                        .post("/transfer").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT

        Assertions.assertEquals(404, res.getStatus());

    }


    @Test
    void deveriaRetornar200ParaTrasacaoValido() throws Exception {
        // ARRANGE
        Account account = new Account("Felizberto", 987654321, 20000, 1000);

        Account account2 = new Account("Felizberto2", 7654321, 20000, 1000);

        entityManager.persist(account);
        entityManager.persist(account2);

        var json = """

                {
                    "sourceAccountNumber": 987654321,
                    "receiverAccountNumber": 7654321,
                    "amount": 5000
                }

                """;

        // ACT

        var res = mvc.perform(
                MockMvcRequestBuilders
                        .post("/transfer").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT

        Assertions.assertEquals(201, res.getStatus());

    }

}
