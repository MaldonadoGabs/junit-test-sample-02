package epn.edu.ec.service;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

import epn.edu.ec.model.cake.CustomerResponse;
import epn.edu.ec.repository.CustomerRepository;
import epn.edu.ec.repository.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(this::customerResponse)
                .sorted(Comparator.comparing(CustomerResponse::getName))
                .collect(toList());
    }

    public List<String> getAllCustomerNames() {
        log.info("Fetching all customer names");
        return customerRepository.findAll().stream()
                .map(Customer::getName)
                .sorted()
                .collect(toList());
    }

    private CustomerResponse customerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .build();
    }
}
