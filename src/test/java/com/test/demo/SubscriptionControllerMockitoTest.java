package com.test.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.controller.SubscriptionController;
import com.test.demo.dto.SubscriptionDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SubscriptionController.class})
@WebAppConfiguration
public class SubscriptionControllerMockitoTest {

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SubscriptionController subscriptionController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesAccountController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("subscriptionController"));
    }

    @WithMockUser(username = "admin")
    @Test
    public void createSubscriptionTestMockito() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO()
                .setId(2)
                .setPrice(333.3)
                .setName("Name");

        String requestJson = new ObjectMapper().writeValueAsString(subscriptionDTO);

        this.mockMvc.perform(post("/api/subscriptions/createSubscription").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
