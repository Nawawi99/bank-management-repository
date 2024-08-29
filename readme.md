# Customer and Account Management Microservices

## Design Decisions
The core design approach was to maintain clean code architecture and follow the **Database per Service Pattern**. This ensures that both the Customer and Account services remain loosely coupled, with no direct database relationships. Instead, events are used to manage interactions between the services.

## Key Features
- **Customer Service**: Manages customers with validation on customer data (ID, name, type, etc.).
- **Account Service**: Manages accounts with tight constraints like account numbers tied to customer IDs and limitations on account types and counts.
- **Event-Driven Architecture**: When a customer is deleted, an event is emitted, and the Account service listens to the event to delete all associated accounts.
- **Tight Validation**: Ensures all inputs conform to strict requirements (e.g., 7-digit customer ID, 10-digit account ID).
- **Controller Advice**: Centralized exception handling to catch and translate exceptions into useful, descriptive responses with appropriate status codes.
- **Test-Driven Development (TDD)**: Comprehensive unit testing followed the AAA (Arrange-Act-Assert) pattern to ensure a minimum of 70% coverage.
- **Spring Profiles**: Implemented to handle different environments and configurations cleanly.

## Assumptions and Shortcomings
- **Assumptions**: Both services are independent, with `customerId` stored in the Account service to avoid direct relational links between them.
- **Shortcomings**: Spring Security was not implemented due to the absence of a service registry and a gateway. Introducing Spring Security in this setup could lead to significant duplication of code and data management, which would conflict with the core design decision of maintaining a clean, decoupled architecture with a database per service.