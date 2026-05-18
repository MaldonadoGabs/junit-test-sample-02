package epn.edu.ec.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DiscountServiceTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private DiscountService discountService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void calculateDiscount_ShouldApplyVolumeDiscount_WhenQuantityGreaterThan10() {
        // ARRANGE
        double total = 200.0;
        int quantity = 11;
        Long customerId = 1L;

        // ACT
        double discount = discountService.calculateDiscount(total, quantity, customerId);

        // ASSERT
        assertEquals(200.0 * 0.15, discount, 0.0001);
        verify(customerService, never()).isVipCustomer(anyLong());
    }

    @Test
    public void calculateDiscount_ShouldApplyVipDiscount_WhenTotalGreaterThan500AndVip() {
        // ARRANGE
        double total = 600.0;
        int quantity = 5;
        Long customerId = 2L;
        when(customerService.isVipCustomer(customerId)).thenReturn(true);

        // ACT
        double discount = discountService.calculateDiscount(total, quantity, customerId);

        // ASSERT
        assertEquals(600.0 * 0.10, discount, 0.0001);
        verify(customerService, times(1)).isVipCustomer(customerId);
    }

    @Test
    public void calculateDiscount_ShouldNotApplyVipDiscount_WhenTotalGreaterThan500ButNotVip() {
        // ARRANGE
        double total = 600.0;
        int quantity = 5;
        Long customerId = 5L;
        when(customerService.isVipCustomer(customerId)).thenReturn(false);

        // ACT
        double discount = discountService.calculateDiscount(total, quantity, customerId);

        // ASSERT
        assertEquals(0.0, discount, 0.0001);
        verify(customerService, times(1)).isVipCustomer(customerId);
    }

    @Test
    public void calculateDiscount_ShouldReturnZero_WhenQuantityExactly10() {
        // ARRANGE
        double total = 300.0;
        int quantity = 10;
        Long customerId = 3L;

        // ACT
        double discount = discountService.calculateDiscount(total, quantity, customerId);

        // ASSERT
        assertEquals(0.0, discount, 0.0001);
        verify(customerService, never()).isVipCustomer(anyLong());
    }

    @Test
    public void calculateDiscount_ShouldReturnZero_WhenTotalExactly500() {
        // ARRANGE
        double total = 500.0; // exactamente 500 -> total > 500 es false
        int quantity = 5;
        Long customerId = 4L;
        when(customerService.isVipCustomer(customerId)).thenReturn(true); // no debería ser llamado

        // ACT
        double discount = discountService.calculateDiscount(total, quantity, customerId);

        // ASSERT
        assertEquals(0.0, discount, 0.0001);
        verify(customerService, never()).isVipCustomer(anyLong());
    }

    @Test
    public void calculateDiscount_ShouldThrowIllegalArgumentException_ForInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> {
            discountService.calculateDiscount(-1.0, 5, 1L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            discountService.calculateDiscount(100.0, 0, 1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            discountService.calculateDiscount(100.0, -2, 1L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            discountService.calculateDiscount(100.0, 5, null);
        });
    }
}