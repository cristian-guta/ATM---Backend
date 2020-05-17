package com.test.demo;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.repository.ClientRepository;
import com.test.demo.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountService accountService;

    @Autowired
    private ClientRepository clientRepository;

    Principal principal = new Principal() {
        @Override
        public String getName() {
            return "user";
        }
    };

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addAccountTest() {

        AccountDTO accountDTO = new AccountDTO().setAmount(2343.2).setName("af").setDetails("fd").setClient(clientRepository.findByUsername("user"));
        when(accountService.createAccount(accountDTO, principal)).thenReturn(accountDTO);

        AccountDTO acc = new AccountDTO().setName("afd").setAmount(23.5).setDetails("dsfs");
        accountService.createAccount(acc, principal);

        assertEquals(acc.getName(), "af");

    }

    @Test
    public void addAccountTest2() {

        AccountDTO accountDTO = new AccountDTO().setAmount(2343.2).setName("af").setDetails("fd")/*.setClient(clientRepository.findByUsername("user"))*/;
        accountService.createAccount(accountDTO, principal);
        verify(accountService, times(1)).createAccount(accountDTO, principal);
    }

    @Test
    public void getAccountsPageableTest() throws Exception {
        AccountDTO account = new AccountDTO();
        account.setId(10);
        account.setName("TestName");
        account.setDetails("TestDetails");
        account.setAmount(234.3);
        account.setClient(clientRepository.findByUsername("user"));

        AccountDTO account2 = new AccountDTO();
        account2.setId(11);
        account2.setName("TestName2");
        account2.setDetails("TestDetails2");
        account2.setAmount(234.32);
        account2.setClient(clientRepository.findByUsername("user"));

        Integer pageNo = 0;
        Integer pageSize = 5;

        List<AccountDTO> accountList = new ArrayList<>();
        accountList.add(account);
        accountList.add(account2);

        Page<AccountDTO> accounts = new PageImpl<>(accountList);

        when(accountService.getAllAccounts(pageNo, pageSize)).thenReturn(accounts);

        assertEquals(2, accounts.getNumberOfElements());
        assertEquals(1, accounts.getTotalPages());
    }

    @Test
    public void deleteAccount() {

        AccountDTO accountDTO = new AccountDTO().setAmount(242.4).setDetails("dfd").setName("fs").setId(7);
        ResultDTO resultDTO = new ResultDTO();
        when(accountService.deleteAccount(accountDTO.getId())).thenReturn(resultDTO.setMessage("Success!").setStatus(true));

        accountService.deleteAccount(accountDTO.getId());
        verify(accountService, times(1)).deleteAccount(accountDTO.getId());
        verifyNoMoreInteractions(accountService);
        assertEquals(true, resultDTO.isStatus());
    }

    @Test
    public void deleteAccountDoesNotExist() {

        ResultDTO resultDTO = new ResultDTO();
        int id = 10;
        when(accountService.deleteAccount(10)).thenReturn(resultDTO.setMessage("Not found!").setStatus(false));

        accountService.deleteAccount(id);
        verify(accountService, times(1)).deleteAccount(id);
        verifyNoMoreInteractions(accountService);
        assertEquals(false, resultDTO.isStatus());
    }
}
