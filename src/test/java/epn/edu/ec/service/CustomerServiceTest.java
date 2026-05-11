package epn.edu.ec.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import epn.edu.ec.model.cake.CustomerResponse;
import epn.edu.ec.repository.CustomerRepository;
import epn.edu.ec.repository.model.Customer;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks 
    private CustomerService customerService;
    
    private Customer customerA;
    private Customer customerB;

    @BeforeEach 
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        
        customerA = Customer.builder()
            .id(1)
            .name("Dayanna")
            .phone("0912")
            .build();
        customerB = Customer.builder()
            .id(2)
            .name("Dilan")
            .phone("0913")
            .build();
    }

    @Test
    public void getAllCustomerNames_ShouldReturnAllCustomerNamesSorted() {
        //ARRANGE
        List<Customer> customers = Arrays.asList(customerB, customerA);
        when(customerRepository.findAll()).thenReturn(customers);

        //ACT
        List<String> customerNames = customerService.getAllCustomerNames();

        //ASSERT
        assertNotNull(customerNames);
        assertEquals(2, customerNames.size());
        assertEquals("Dayanna", customerNames.get(0));
        assertEquals("Dilan", customerNames.get(1));

        verify(customerRepository, times(1)).findAll();
    }
}
