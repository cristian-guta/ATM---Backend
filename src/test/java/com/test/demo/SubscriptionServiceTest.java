package com.test.demo;

import com.test.demo.dto.ClientDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.dto.SubscriptionDTO;
import com.test.demo.service.ClientService;
import com.test.demo.service.SubscriptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClientService clientService;

    private MockMvc mockMvc;

    Principal principal = new Principal() {
        @Override
        public String getName() {
            return "user";
        }
    };

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void getAllAvailableSubsTest() {

        List<SubscriptionDTO> subscriptionDTOList = new ArrayList<>();
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO().setPrice(23424.3).setName("nume1");
        SubscriptionDTO subscriptionDTO2 = new SubscriptionDTO().setPrice(23424.3).setName("nume2");
        SubscriptionDTO subscriptionDTO3 = new SubscriptionDTO().setPrice(23424.3).setName("nume3");

        subscriptionDTOList.add(subscriptionDTO);
        subscriptionDTOList.add(subscriptionDTO2);
        subscriptionDTOList.add(subscriptionDTO3);

        when(subscriptionService.getAllAvailableSubs()).thenReturn(subscriptionDTOList);
        List<SubscriptionDTO> list = subscriptionService.getAllAvailableSubs();
        assertEquals(4, list.size());

    }

    @Test
    public void getClientSubscriptionTest() {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "user";
            }
        };
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO().setPrice(23424.3).setName("nume1");

        ClientDTO clientDTO = new ClientDTO().setUsername("user").setSubscriptionDTO(subscriptionDTO);

        when(subscriptionService.getClientSubscription(principal)).thenReturn(subscriptionDTO);

        subscriptionService.getClientSubscription(principal);
        assertEquals("nume1", subscriptionDTO.getName());
    }

    @Test
    public void activateSubscriptionTest() throws IOException {


        int id = 1;
        ResultDTO resultDTO = new ResultDTO();
        when(subscriptionService.activateSubscription(principal, id)).thenReturn(resultDTO.setStatus(true).setMessage("Subscription activated!"));

        subscriptionService.activateSubscription(principal, id);
        assertEquals(true, resultDTO.isStatus());
    }

    // similar method for deactivating a subscription

    @Test
    public void createSubscriptionTest() {

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        when(subscriptionService.createSubscription(subscriptionDTO)).thenReturn(subscriptionDTO.setName("numeSubscriptie"));

        subscriptionService.createSubscription(subscriptionDTO);
        verify(subscriptionService, times(1)).createSubscription(subscriptionDTO);
        assertEquals("numeSubscriptie", subscriptionDTO.getName());
    }

    @Test
    public void deleteSubscription() {
        int id = 5;
        ResultDTO resultDTO = new ResultDTO();
        when(subscriptionService.deleteSubscription(id, principal)).thenReturn(resultDTO.setStatus(true));

        subscriptionService.deleteSubscription(id, principal);
        assertEquals(true, resultDTO.isStatus());
        verify(subscriptionService, times(1)).deleteSubscription(id, principal);
        verifyNoMoreInteractions(subscriptionService);
    }

    @Test
    public void updateSubscription() {

        int id = 3;

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO().setName("nume 1");
        String newMessage = "new";
        when(subscriptionService.updateSubscription(id, subscriptionDTO)).thenReturn(subscriptionDTO.setName(newMessage));

        subscriptionService.updateSubscription(id, subscriptionDTO);
        assertEquals("new3", subscriptionDTO.getName());
    }

}
