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
- Docker (optional, for containerized deployment)

### Running the Application

#### Option 1: Local Development

1. Clone the repository
2. Navigate to the project directory
3. Build the application:

```bash
mvn clean compile
```

4. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

#### Option 2: Docker Container

1. **Using the build script (recommended):**

```bash
./build-docker.sh
```

2. **Manual Docker commands:**

```bash
# Build the Docker image
docker build -t parcel-tracking-tool:latest .

# Run the container
docker run -d --name parcel-tracking-container -p 8080:8080 parcel-tracking-tool:latest
```

3. **Using Docker Compose:**

```bash
# Build and start the application
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
```

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

## Error Handling

The application provides comprehensive error handling with detailed, user-friendly error responses:

### Exception Types

- **ResourceNotFoundException** (404): When requested resources (guests, parcels) don't exist
- **BusinessLogicException** (400): When business rules are violated (room occupied, guest not checked in, etc.)
- **ValidationException** (400): When input validation fails (required fields missing, invalid format)
- **RuntimeException** (500): For unexpected server errors

### Error Response Format

All errors return a standardized JSON response:

```json
{
  "timestamp": "2025-05-31T21:30:44.763307",
  "status": 400,
  "error": "Business Logic Violation",
  "message": "Room 102 is already occupied",
  "path": "/api/guests/check-in",
  "validationErrors": null
}
```

### Validation Errors

For validation failures, detailed field-specific errors are provided:

```json
{
  "timestamp": "2025-05-31T21:31:02.987985",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "path": "/api/guests/check-in",
  "validationErrors": {
    "roomNumber": "Room number is required",
    "name": "Guest name is required"
  }
}
```

### HTTP Status Codes

- **200 OK**: Successful operations
- **201 Created**: Resource successfully created
- **400 Bad Request**: Business logic violations or validation errors
- **404 Not Found**: Requested resource doesn't exist
- **500 Internal Server Error**: Unexpected server errors

## Database Schema

### Guests Table

| Column | Type | Description |
|--------|------|-------------|
| `id` | Primary Key | Unique guest identifier |
| `name` | VARCHAR | Guest name |
| `room_number` | VARCHAR | Room number |
| `check_in_time` | TIMESTAMP | Check-in timestamp |
| `check_out_time` | TIMESTAMP | Check-out timestamp (null if still checked in) |

### Parcels Table

| Column | Type | Description |
|--------|------|-------------|
| `id` | Primary Key | Unique parcel identifier |
| `tracking_number` | VARCHAR (Unique) | Unique tracking number |
| `sender` | VARCHAR | Sender name |
| `description` | VARCHAR | Parcel description |
| `arrival_time` | TIMESTAMP | When parcel arrived |
| `collection_time` | TIMESTAMP | When parcel was collected (null if not collected) |
| `is_collected` | BOOLEAN | Collection status flag |
| `guest_id` | Foreign Key | Reference to guests table |

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
