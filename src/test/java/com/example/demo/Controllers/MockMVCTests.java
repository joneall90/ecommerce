package com.example.demo.Controllers;


import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMVCTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockHttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testCreateHappyUser() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test1");
        userRequest.setPassword("1234567");
        userRequest.setConfirmPassword("1234567");

        JsonObject userLogin = new JsonObject();
        userLogin.addProperty("username","test1");
        userLogin.addProperty("password","1234567");

        MvcResult entityResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
                .andReturn();
           User user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), User.class);
                user.setPassword("1234567");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(String.valueOf(userLogin)).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        request.addParameter("Authorization",result.getResponse().getHeader("Authorization"));
    }

    @Test
    public void modifyCartTest() throws Exception {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        JsonObject userLogin = new JsonObject();
        userLogin.addProperty("username","test");
        userLogin.addProperty("password","1234567");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("1234567");
        userRequest.setConfirmPassword("1234567");

        MvcResult entityResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
                .andReturn();

        MvcResult resultLogin = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(String.valueOf(userLogin)).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                .header("Authorization", resultLogin.getResponse().getHeader("Authorization"))
                .content(objectMapper.writeValueAsString(modifyCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.total").exists())
                .andReturn();
    }
    @Test
    public void submitOrderTest() throws Exception {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test3");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        JsonObject userLogin = new JsonObject();
        userLogin.addProperty("username","test3");
        userLogin.addProperty("password","1234567");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test3");
        userRequest.setPassword("1234567");
        userRequest.setConfirmPassword("1234567");

        MvcResult entityResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
                .andReturn();

        MvcResult resultLogin = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(String.valueOf(userLogin)).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                .header("Authorization", resultLogin.getResponse().getHeader("Authorization"))
                .content(objectMapper.writeValueAsString(modifyCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.total").exists())
                .andReturn();

        MvcResult resultOrder = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/submit/test3")
                .header("Authorization", resultLogin.getResponse().getHeader("Authorization"))
                .content(objectMapper.writeValueAsString(modifyCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.total").exists())
                .andReturn();

    }
}
