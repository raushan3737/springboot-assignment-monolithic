# Easycart
    
    EasyCart is a Java application designed to provide a simple and efficient way of managing orders for an online shopping system. It offers functionality to create, retrieve, update, and delete orders, as well as convert between different representations of orders.
    
    Features:
    Conversion between Order and OrderDTO objects using the ModelMapper library.
    CRUD (Create, Read, Update, Delete) operations for orders.
    Retrieval of all orders.
    Retrieval of an order by its ID.
    Saving an order.
    Deletion of an order by its ID.

    Technologies Used
    Java
    ModelMapper
    JUnit and Mockito for testing
    Spring Framework (Dependency Injection)
    Getting Started
    To run the EasyCart application, follow these steps:
    
    Clone the repository: git clone https://github.com/raushan3737/springboot-assignment-monolithic.git
    Navigate to the project directory: cd easycart
    Build the project: mvn clean install
    Run the application: java -jar target/easycart.jar

    Usage
    The EasyCart application provides a RESTful API to interact with orders. Below are the available endpoints:
    
    GET /orders: Retrieve all orders.
    GET /orders/{id}: Retrieve an order by its ID.
    POST /orders: Create a new order.
    PUT /orders/{id}: Update an existing order.
    DELETE /orders/{id}: Delete an order by its ID.
    You can use tools like cURL or Postman to make requests to these endpoints.