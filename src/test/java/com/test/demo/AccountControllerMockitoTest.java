package com.test.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.controller.AccountController;
import com.test.demo.dto.AccountDTO;
import com.test.demo.service.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.security.Principal;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountController.class})
@WebAppConfiguration
public class AccountControllerMockitoTest {


    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountController accountController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    Principal principal = new Principal() {
        @Override
        public String getName() {
            return "user1";
        }
    };

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesAccountController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("accountController"));
    }

    @Test
    @WithMockUser(value = "admin")
    public void getAccountByClientCnpTest() throws Exception {

        AccountDTO accountDTO = new AccountDTO()
                .setAmount(444.4)
                .setId(4)
                .setName("Name");

        when(accountService.getAccountById(4)).thenReturn(accountDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/4").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(accountDTO))).andDo(print())
                .andExpect(jsonPath("$.name").value(accountDTO.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void Deposit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts/deposit/{id}/{amount}", 2, 500).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void CreateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO()
                .setAmount(444.4)
                .setName("Name");

        String requestJson = new ObjectMapper().writeValueAsString(accountDTO);

        when(accountService.createAccount(accountDTO, principal)).thenReturn(accountDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/create")
                .content(requestJson))
                .andExpect(status().isOk());


    }
}