package com.zemoso.order.service.impl;


import com.zemoso.order.service.dto.OrderDTO;
import com.zemoso.order.service.entity.Order;
import com.zemoso.order.service.repository.OrderRepository;
import com.zemoso.order.service.service.OrderServiceImpl;
import com.zemoso.order.service.utils.DateFormatter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DateFormatter dateFormatter;

    @InjectMocks
    private OrderServiceImpl orderService;

    private static final Long ORDER_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 1L;

    @Test
    public void convertOrderToDTOTest() {
        // Create a sample order
        Order order = new Order();
        order.setId(ORDER_ID);

        // Invoke the method
        OrderDTO orderDTO = orderService.convertOrderToDTO(order);

        // Assert the result
        assertNotNull(orderDTO);
        assertEquals(ORDER_ID, orderDTO.getId());
    }

    @Test
    public void convertOrderToEntityTest() {
        // Create a sample orderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(ORDER_ID);

        // Invoke the method
        Order order = orderService.convertOrderToEntity(orderDTO);

        // Assert the result
        assertNotNull(order);
        assertEquals(ORDER_ID, order.getId());
    }

    @Test
    public void getAllOrdersTest() {
        // Create a list of sample orders
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        // Mock the behavior of the orderRepository
        when(orderRepository.findAll()).thenReturn(orders);

        // Invoke the method
        List<Order> result = orderService.getAllOrders();

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void getOrderByIdTest() {
        // Create a sample order
        Order order = new Order();
        order.setId(ORDER_ID);

        // Mock the behavior of the orderRepository
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        // Invoke the method
        Order result = orderService.getOrderById(ORDER_ID);

        // Assert the result
        assertNotNull(result);
        assertEquals(ORDER_ID, result.getId());
    }

    @Test
    public void addOrderValidTest() {
        // Create a sample orderDTO
        OrderDTO orderDTO = createSampleOrderDTO();

        // Mock the behavior of the restTemplate
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new UserDTO());

        // Mock the behavior of the orderRepository
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        // Invoke the method
        OrderDTO result = orderService.addOrder(orderDTO);

        // Assert the result
        assertNotNull(result);
    }

    @Test
    public void addOrderInvalidUserTest() {
        // Create a sample orderDTO with invalid user
        OrderDTO orderDTO = createSampleOrderDTO();

        // Mock the behavior of the restTemplate to return null for user
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        // Invoke the method
        OrderDTO result = orderService.addOrder(orderDTO);

        // Assert the result
        assertNull(result);
    }

    @Test
    public void addOrderInvalidProductsTest() {
        // Create a sample orderDTO with invalid products
        OrderDTO orderDTO = createSampleOrderDTO();
        orderDTO.getProducts().get(0).setPrice(null);

        // Mock the behavior of the restTemplate
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new UserDTO());

        // Invoke the method
        OrderDTO result = orderService.addOrder(orderDTO);

        // Assert the result
        assertNull(result);
    }

    @Test
    public void deleteOrderByIdTest() {
        // Mock the behavior of the orderRepository
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(new Order()));

        // Invoke the method
        String result = orderService.deleteOrderById(ORDER_ID);

        // Assert the result
        assertNotNull(result);
        assertEquals("Order deleted with id: " + ORDER_ID, result);
    }

    @Test
    public void deleteOrderByIdNotFoundTest() {
        // Mock the behavior of the orderRepository
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        // Invoke the method
        String result = orderService.deleteOrderById(ORDER_ID);

        // Assert the result
        assertNotNull(result);
        assertEquals("Order can't be deleted with id: " + ORDER_ID, result);
    }

    private OrderDTO createSampleOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(ORDER_ID);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID);
        orderDTO.setUser(userDTO);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(PRODUCT_ID);
        productDTO.setPrice(BigDecimal.TEN);
        List<ProductDTO> products = new ArrayList<>();
        products.add(productDTO);
        orderDTO.setProducts(products);

        return orderDTO;
    }
}
