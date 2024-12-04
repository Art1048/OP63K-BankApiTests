package br.edu.utfpr.bankapi.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.utfpr.bankapi.dto.DepositDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.repository.AccountRepository;
import br.edu.utfpr.bankapi.repository.TransactionRepository;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;


@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AvailableAccountValidation accountValidation;


    @InjectMocks
    TransactionService transactionService;

    @Captor
    ArgumentCaptor<Transaction> transCaptor;

    @Test
    void deveriaDepositar() throws NotFoundException{

        //Arrange
        double saldoInicial = 145.24;
        var depositDTO = new DepositDTO(12345, 1000);
        var receiverAccount = new Account("John Smith", 12345, saldoInicial, 0);

        BDDMockito.given(accountValidation.validate(receiverAccount.getNumber())).willReturn(receiverAccount);

        //ACT

        transactionService.deposit(depositDTO);

        //Assert

        BDDMockito.then(transactionRepository).should().save(BDDMockito.any());


        BDDMockito.then(transactionRepository)
            .should()
            .save(transCaptor.capture());

        Transaction transactionSalva = transCaptor.getValue();

        org.junit.jupiter.api.Assertions.assertEquals(receiverAccount, transactionSalva.getReceiverAccount());

        org.junit.jupiter.api.Assertions.assertEquals(depositDTO.amount(), transactionSalva.getAmount());

        org.junit.jupiter.api.Assertions.assertEquals(TransactionType.DEPOSIT, transactionSalva.getType());

        org.junit.jupiter.api.Assertions.assertEquals(saldoInicial + depositDTO.amount(), transactionSalva.getReceiverAccount().getBalance());



    }

}
