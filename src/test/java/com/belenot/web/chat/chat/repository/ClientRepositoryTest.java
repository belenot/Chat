package com.belenot.web.chat.chat.repository;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.belenot.web.chat.chat.domain.Client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @ParameterizedTest
    @CsvFileSource(resources = "ClientRepository.save.csv")
    //Order
    public void saveTest(int id, String login, String password) {
        clientRepository.save(new Client(id, login, password));
    }
    @ParameterizedTest
    @CsvFileSource(resources = "ClientRepository.save.csv")
    public void findByLoginTest(int id, String login, String password) {
        Client client = clientRepository.findByLogin(login);
        assertNotNull(client);
        assertEquals(password, client.getPassword());
    }
}