package tn.esprit.devops_project;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OperatorServiceImplTest {

    @Mock
    private OperatorRepository operatorRepository;

    @InjectMocks
    private OperatorServiceImpl operatorService;

    private Operator operator;
    private List<Operator> operatorList;

    @Before
    public void setUp() {
        operator = new Operator("John", "Doe", "password123");
        operatorList = new ArrayList<>();
        operatorList.add(new Operator("Jane", "Doe", "password123"));
        operatorList.add(new Operator("Alice", "Smith", "password456"));
    }

    @Test
    public void testRetrieveOperator() {
        Mockito.when(operatorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(operator));
        Operator retrievedOperator = operatorService.retrieveOperator(1L);
        Assert.assertNotNull(retrievedOperator);
        Assert.assertEquals("John", retrievedOperator.getFname());
    }

    @Test
    public void testRetrieveAllOperators() {
        Mockito.when(operatorRepository.findAll()).thenReturn(operatorList);
        List<Operator> allOperators = operatorService.retrieveAllOperators();
        Assert.assertEquals(2, allOperators.size());
    }

    @Test
    public void testAddOperator() {
        Operator newOperator = new Operator("Bob", "Johnson", "password789");
        Mockito.when(operatorRepository.save(Mockito.any(Operator.class)))
                .thenAnswer(invocation -> {
                    Operator savedOperator = invocation.getArgument(0);
                    operatorList.add(savedOperator);
                    return savedOperator;
                });
        operatorService.addOperator(newOperator);
        Assert.assertEquals(3, operatorList.size());
        Assert.assertEquals("Bob", operatorList.get(2).getFname());
    }

    @Test
    public void testDeleteOperator() {
        Long idToDelete = 1L;
        Mockito.doAnswer(invocation -> {
            operatorList.remove(0);
            return null;
        }).when(operatorRepository).deleteById(idToDelete);

        operatorService.deleteOperator(idToDelete);

        // Update the assertion to expect size 1 instead of 2
        Assert.assertEquals(1, operatorList.size());
    }

    @Test
    public void testUpdateOperator() {
        Operator updatedOperator = new Operator("John", "Doe", "newpassword");
        Mockito.when(operatorRepository.save(Mockito.any(Operator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Operator result = operatorService.updateOperator(updatedOperator);
        Assert.assertEquals("newpassword", result.getPassword());
    }
}
