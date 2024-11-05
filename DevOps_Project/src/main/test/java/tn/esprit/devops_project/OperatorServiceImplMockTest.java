package tn.esprit.devops_project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.OperatorServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class OperatorServiceImplTest {

    @Mock
    OperatorRepository operatorRepository;

    @InjectMocks
    OperatorServiceImpl operatorService;

    Operator operator = new Operator("John", "Doe", "password123");
    List<Operator> operatorList = new ArrayList<>() {
        {
            add(new Operator("Jane", "Doe", "password123"));
            add(new Operator("Alice", "Smith", "password456"));
        }
    };

    @Test
    @Order(1)
    void testRetrieveOperator() {
        Mockito.when(operatorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(operator));
        Operator retrievedOperator = operatorService.retrieveOperator(1L);
        Assertions.assertNotNull(retrievedOperator);
        Assertions.assertEquals("John", retrievedOperator.getFname());
    }

    @Test
    @Order(2)
    void testRetrieveAllOperators() {
        Mockito.when(operatorRepository.findAll()).thenReturn(operatorList);
        List<Operator> allOperators = operatorService.retrieveAllOperators();
        Assertions.assertEquals(2, allOperators.size());
    }

    @Test
    @Order(3)
    void testAddOperator() {
        Operator newOperator = new Operator("Bob", "Johnson", "password789");

        Mockito.when(operatorRepository.save(Mockito.any(Operator.class)))
                .thenAnswer(invocation -> {
                    Operator savedOperator = invocation.getArgument(0);
                    operatorList.add(savedOperator);
                    return savedOperator;
                });

        operatorService.addOperator(newOperator);

        Assertions.assertEquals(3, operatorList.size());
        Assertions.assertEquals("Bob", operatorList.get(2).getFname());
    }

    @Test
    @Order(4)
    void testDeleteOperator() {
        Long idToDelete = 1L;
        Mockito.doAnswer(invocation -> {
            operatorList.remove(0);
            return null;
        }).when(operatorRepository).deleteById(idToDelete);

        operatorService.deleteOperator(idToDelete);

        Assertions.assertEquals(2, operatorList.size());
    }

    @Test
    @Order(5)
    void testUpdateOperator() {
        Operator updatedOperator = new Operator("John", "Doe", "newpassword");

        Mockito.when(operatorRepository.save(Mockito.any(Operator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Operator result = operatorService.updateOperator(updatedOperator);

        Assertions.assertEquals("newpassword", result.getPassword());
    }
}
