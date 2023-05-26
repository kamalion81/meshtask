package com.mesh.task;

import com.fasterxml.jackson.databind.*;
import com.mesh.task.configuration.*;
import com.mesh.task.dao.*;
import com.mesh.task.dto.*;
import com.mesh.task.entity.*;
import com.mesh.task.mapper.*;
import com.mesh.task.service.*;
import jakarta.annotation.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.setup.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.*;

import java.math.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
public class TestUsers {

    private static PostgreSQLContainer postgreSQLContainer;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper mapper;

    @Autowired
    protected ObjectMapper objectMapper;


    @BeforeClass
    public static void setUp() {
        postgreSQLContainer =
            new PostgreSQLContainer("postgres:13")
                .withDatabaseName("test")
                .withUsername("testuser")
                .withPassword("testpass");

        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());


    }

    @AfterClass
    public static void tearDown() {
        postgreSQLContainer.close();
    }

    @Test
    public void test_get_user_returns_correct_user() throws Exception {
        // Arrange
        User user = new User();
        user.setName("Jhon");

        User saved = userRepository.save(user);
        saved.setEmails(List.of(new EmailData("dddd", saved)));
        saved.setPhones(List.of(new PhoneData("7777", saved)));
        userRepository.save(saved);

        // Act
        MvcResult response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/users/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        // Assert
        int status = response.getResponse().getStatus();
        // assertThat(status).isEqualTo(HttpStatus.OK.value());
        assertEquals(HttpStatus.OK.value(), status);
        UserDtoResponse totalResponse = objectMapper.readValue(response.getResponse().getContentAsString(), UserDtoResponse.class);
        assertEquals(1, totalResponse.getEmails().size());
        assertEquals(1, totalResponse.getPhones().size());
        assertEquals("dddd", totalResponse.getEmails().get(0).getEmail());

    }

    @Test
    public void transferTest() throws Exception {
        User user = new User();
        user.setName("FromUser");
        user.setPassword("$2a$10$D1GhkHhgnsl9N/JPlp07pu9NT/n0aDerBjaKM.LbvRSrlcR0TaXRu");
        User fromUser = userRepository.save(user);
        fromUser.setEmails(List.of(new EmailData("dddd", fromUser)));
        fromUser.setPhones(List.of(new PhoneData("7777", fromUser)));
        userRepository.save(fromUser);
        MvcResult response = mockMvc.perform(
            MockMvcRequestBuilders.post("/auth")
                .param("username", "dddd")
                .param("password", "resolution")
                .contentType(MediaType.TEXT_PLAIN))
            .andReturn();

        String token = response.getResponse().getContentAsString().substring(7);

        user = new User();
        user.setName("ToUser");
        User toUser = userRepository.save(user);

        Account accountFromUser = new Account();
        accountFromUser.setUser(fromUser);
        accountFromUser.setBalance(BigDecimal.valueOf(500));

        Account accountToUser = new Account();
        accountToUser.setUser(toUser);
        accountToUser.setBalance(BigDecimal.valueOf(100));

        Account savedFromUser = accountRepository.save(accountFromUser);
        Account savedToUser = accountRepository.save(accountToUser);

        service.transferMoney(token, toUser.getId(), BigDecimal.valueOf(200));


        assertEquals(BigDecimal.valueOf(300), accountRepository.findById(savedFromUser.getId()).get().getBalance());
        assertEquals(BigDecimal.valueOf(300), accountRepository.findById(savedToUser.getId()).get().getBalance());



    }


}
