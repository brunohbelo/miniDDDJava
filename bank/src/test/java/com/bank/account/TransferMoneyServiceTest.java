package com.bank.account;

import com.bank.account.model.Account;
import com.bank.account.model.AccountNumber;
import com.bank.account.application.TransferMoneyService;
import com.bank.account.application.dto.TransferDTO;
import com.bank.account.fake.MemoryRepositoryAccount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferMoneyServiceTest {
    @Test
    public void testTransferMoneyServiceSuccess() {
        MemoryRepositoryAccount repository = new MemoryRepositoryAccount();
        Account accountFrom = new Account(new AccountNumber("123456"), 5000.0);
        Account accountTo = new Account(new AccountNumber("654321"), 5000.0);

        repository.add(accountFrom);
        repository.add(accountTo);

        TransferMoneyService appService = new TransferMoneyService(repository);
        
        TransferDTO dto = new TransferDTO();
        dto.setAccountFrom("123456");
        dto.setAccountTo("654321");
        dto.setValue(100.0);

        appService.transfer(dto);

        assertEquals(4900.0, repository.get(new AccountNumber("123456")).getBalance());
        assertEquals(5100.0, repository.get(new AccountNumber("654321")).getBalance());
    }

    @Test
    public void testTransferMoneyServiceFailureAccountNotFound() {
        MemoryRepositoryAccount repository = new MemoryRepositoryAccount();
        Account accountFrom = new Account(new AccountNumber("123456"), 5000.0);
        Account accountTo = new Account(new AccountNumber("654321"), 5000.0);

        repository.add(accountFrom);
        repository.add(accountTo);

        TransferMoneyService appService = new TransferMoneyService(repository);
        
        TransferDTO dto = new TransferDTO();
        dto.setAccountFrom("654321");
        dto.setAccountTo("444444");
        dto.setValue(100.0);

        assertThrows(IllegalArgumentException.class, () -> {
            appService.transfer(dto);
        });
    }
}
