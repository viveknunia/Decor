# E-commerce API Project

This project is a basic eCommerce web application built using Spring Boot. It includes APIs for managing users, carts, orders, products, and categories.

## Database Schema

![Database Schema](![image](https://github.com/viveknunia/ECommerceBackend/assets/45126941/9c24246b-7e90-4eed-a129-d6d063f332fc)
)

### Tables

- **User**
  - `id` (INT, PK)
  - `name` (VARCHAR)

- **Cart**
  - `id` (INT, PK)
  - `user_id` (INT, FK)

- **Order**
  - `id` (INT, PK)
  - `user_id` (INT, FK)

- **CartItem**
  - `id` (INT, PK)
  - `cart_id` (INT, FK)
  - `product_id` (INT, FK)
  - `quantity` (INT)

- **OrderItem**
  - `id` (INT, PK)
  - `order_id` (INT, FK)
  - `product_id` (INT, FK)
  - `quantity` (INT)

- **Product**
  - `id` (INT, PK)
  - `name` (VARCHAR)
  - `price` (INT)
  - `stockQuantity` (INT)
  - `category_id` (INT, FK)

- **Category**
  - `id` (INT, PK)
  - `name` (VARCHAR)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven
- IntelliJ IDEA or any other IDE of your choice

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/ecommerce-backend.git
   cd ecommerce-backend
