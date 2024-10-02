# Food Application

## Overview

The Food Application is a Java-based project designed to connect food providers and collectors. It enables users to manage food items, track wasted items, and facilitate requests for food supplies. The application utilizes JavaFX for the user interface and MySQL for data storage, ensuring a seamless experience for both providers and collectors.

## Features

- **User Registration and Authentication**: Supports multiple user roles (collectors and providers) with secure authentication.
- **Food Item Management**: Providers can add and manage food items, including tracking wasted items.
- **Order Creation**: Collectors can place orders for available food items from providers.
- **Request Management**: Collectors can send requests for additional food items linked to their orders.
- **Order Tracking**: Both collectors and providers can view and manage existing orders and requests.
- **Map Viewer**: Displays nearby providers for easy access and interaction.

## Technologies Used

- **Java**: The primary programming language for the application.
- **JavaFX**: For creating the user interface.
- **MySQL**: For database management.
- **JDBC**: For connecting to the MySQL database.

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Ensure that JDK 11 or higher is installed on your system.
- **MySQL**: Install MySQL and create a database named `food_application`.
- **JavaFX SDK**: Download the JavaFX SDK and ensure it is correctly configured in your IDE.

### Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/bunnysunny24/Food_gui.git
   cd Food_gui
   ```

2. **Database Configuration**:
   - Open MySQL and create the required tables using the provided SQL commands:
     ```sql
     CREATE DATABASE food_application;
     USE food_application;
     -- Create tables here (users, food_items, orders, wasted_items, collector_requests)
     ```

3. **Configure Database Connection**:
   - Update the `DatabaseConnection` class with your MySQL username, password, and database URL if necessary.

4. **JavaFX Configuration**:
   - Ensure that the JavaFX library is included in your project. Update your `launch.json` and `settings.json` files as needed.

5. **Build and Run**:
   - Use your IDE (e.g., IntelliJ IDEA, Eclipse) to build and run the application.
   - Alternatively, compile and run using the command line.

## Usage

1. **Register a User**:
   - Collectors and providers can register using the application interface.
   
2. **Log In**:
   - Users can log in with their credentials.

3. **Food Item Management**:
   - Providers can add food items and log wasted items.

4. **Place Orders**:
   - Collectors can browse food items and place orders.

5. **Send Requests**:
   - Collectors can send requests for additional food items.

6. **View Orders**:
   - Users can view their orders and manage requests.

## Contribution

Contributions are welcome! If you have suggestions or improvements, feel free to submit a pull request or open an issue in the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **OpenJFX**: For providing the JavaFX framework.
- **MySQL**: For the relational database management system.

