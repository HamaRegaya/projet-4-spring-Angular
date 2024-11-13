package tn.esprit.devops_project;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OperatorServiceImplIntegrationTest {

    @Autowired
    OperatorServiceImpl operatorService;

    @Autowired
    OperatorRepository operatorRepository;

    Operator operator = new Operator("John", "Doe", "password123");

    @BeforeEach
    void setUp() {
        operatorRepository.deleteAll(); // Clean the database before each test
    }

    @Test
    @Order(1)
    void testAddOperator() {
        Operator savedOperator = operatorService.addOperator(operator);
        Assertions.assertNotNull(savedOperator);
        Assertions.assertNotNull(savedOperator.getIdOperateur());
        Assertions.assertEquals("John", savedOperator.getFname());
    }

    @Test
    @Order(2)
    void testRetrieveOperator() {
        Operator savedOperator = operatorService.addOperator(operator);
        Operator retrievedOperator = operatorService.retrieveOperator(savedOperator.getIdOperateur());
        Assertions.assertNotNull(retrievedOperator);
        Assertions.assertEquals("John", retrievedOperator.getFname());
    }

    @Test
    @Order(3)
    void testRetrieveAllOperators() {
        operatorService.addOperator(new Operator("Jane", "Doe", "password123"));
        operatorService.addOperator(new Operator("Alice", "Smith", "password456"));

        List<Operator> allOperators = operatorService.retrieveAllOperators();
        Assertions.assertEquals(2, allOperators.size());
    }

    @Test
    @Order(4)
    void testUpdateOperator() {
        Operator savedOperator = operatorService.addOperator(operator);
        savedOperator.setPassword("newpassword");
        Operator updatedOperator = operatorService.updateOperator(savedOperator);

        Assertions.assertEquals("newpassword", updatedOperator.getPassword());
    }

    @Test
    @Order(5)
    void testDeleteOperator() {
        Operator savedOperator = operatorService.addOperator(operator);
        operatorService.deleteOperator(savedOperator.getIdOperateur());

        Assertions.assertThrows(RuntimeException.class, () -> {
            operatorService.retrieveOperator(savedOperator.getIdOperateur());
        });
    }
}
