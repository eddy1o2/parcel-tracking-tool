# Hotel Parcel Tracking System

A Spring Boot application for hotel receptionists to track parcels for guests. This system helps receptionists decide whether to accept parcels for guests and allows checking for parcels available for pickup when guests check out.

## Features

- **Guest Management**: Check-in and check-out guests
- **Parcel Tracking**: Accept, track, and manage parcel collection
- **Business Logic**: Only accept parcels for checked-in guests
- **Availability Check**: View uncollected parcels for guests during checkout
- **REST API**: Full REST API with JSON payloads
- **Swagger Documentation**: Interactive API documentation
- **Unit Tests**: Comprehensive test coverage

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Swagger/OpenAPI 3**
- **JUnit 5** & **Mockito** for testing
- **Maven** for dependency management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console (for database inspection)

## API Endpoints

### Guest Management

- `POST /api/guests/check-in` - Check in a guest
- `PUT /api/guests/{guestId}/check-out` - Check out a guest by ID
- `GET /api/guests/checked-in` - Get all checked-in guests
- `GET /api/guests` - Get all guests
- `GET /api/guests/{guestId}` - Get guest by ID
- `GET /api/guests/room/{roomNumber}/status` - Check if guest is checked in

### Parcel Management

- `POST /api/parcels/accept` - Accept a parcel for a guest
- `PUT /api/parcels/{parcelId}/collect` - Mark parcel as collected
- `PUT /api/parcels/tracking/{trackingNumber}/collect` - Collect parcel by tracking number
- `GET /api/parcels/guest/{guestId}/available` - Get available parcels for guest
- `GET /api/parcels/room/{roomNumber}/available` - Get available parcels by room number
- `GET /api/parcels/uncollected` - Get all uncollected parcels
- `GET /api/parcels/checked-in-guests` - Get parcels for checked-in guests
- `GET /api/parcels` - Get all parcels
- `GET /api/parcels/tracking/{trackingNumber}` - Get parcel by tracking number

## Usage Examples

### 1. Check in a guest

```bash
curl -X POST http://localhost:8080/api/guests/check-in \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "roomNumber": "101"
  }'
```

### 2. Accept a parcel for a guest

```bash
curl -X POST http://localhost:8080/api/parcels/accept \
  -H "Content-Type: application/json" \
  -d '{
    "trackingNumber": "TRK123456",
    "sender": "Amazon",
    "description": "Package",
    "guestId": 1
  }'
```

### 3. Check available parcels for a room during checkout

```bash
curl http://localhost:8080/api/parcels/room/101/available
```

### 4. Collect a parcel

```bash
curl -X PUT http://localhost:8080/api/parcels/tracking/TRK123456/collect
```

## Business Rules

1. **Parcel Acceptance**: Parcels can only be accepted for guests who are currently checked in
2. **Guest Check-in**: Only one guest can be checked into a room at a time
3. **Parcel Collection**: Parcels can be marked as collected, preventing duplicate collection
4. **Tracking Numbers**: Each parcel must have a unique tracking number

## Database Schema

### Guests Table
- `id` (Primary Key)
- `name` (Guest name)
- `room_number` (Room number)
- `check_in_time` (Check-in timestamp)
- `check_out_time` (Check-out timestamp, null if still checked in)

### Parcels Table
- `id` (Primary Key)
- `tracking_number` (Unique tracking number)
- `sender` (Sender name)
- `description` (Parcel description)
- `arrival_time` (When parcel arrived)
- `collection_time` (When parcel was collected, null if not collected)
- `is_collected` (Boolean flag)
- `guest_id` (Foreign key to guests table)

## Testing

Run the unit tests:

```bash
mvn test
```

The project includes comprehensive unit tests for:
- Service layer business logic
- Repository queries
- Controller endpoints
- Edge cases and error scenarios

## Development

### Project Structure

```
src/
├── main/
│   ├── java/com/hotel/parceltracking/
│   │   ├── controller/     # REST controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Data access layer
│   │   ├── service/       # Business logic
│   │   └── ParcelTrackingApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/hotel/parceltracking/
        └── service/       # Unit tests
```

### Key Design Decisions

1. **H2 Database**: Chosen for simplicity and ease of setup
2. **JPA/Hibernate**: For object-relational mapping and database operations
3. **DTO Pattern**: Separation between API contracts and internal models
4. **Service Layer**: Business logic encapsulation
5. **Comprehensive Testing**: Unit tests with Mockito for isolated testing
