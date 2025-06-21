
# TaxiRideProcessor Unit Test Documentation

This document describes the unit test created for the `TaxiRideProcessor` service in the `processor` module of the `taxi-data-system` project.

**Objective:**

The test ensures that the method `processRide(TaxiRide ride)`:
- Correctly saves a taxi ride to the repository when a message is received.
- Uses `CircuitBreaker` logic as expected.
- Simulates the reception of a ride as if it came from RabbitMQ.
- Logs the process and verifies interactions using Mockito.

## Test Class: `TaxiRideProcessorTest`

### Tools Used
- **JUnit 5**
- **Mockito** (`@Mock`, `@InjectMocks`, `doAnswer`, `verify`)
- **System.out.println** to provide detailed feedback in terminal

### Test Method: `testProcessRide_savesTaxiRideToRepository()`

#### Given:
- A fully constructed `TaxiRide` object including:
    - `start`, `end`, and `importantPlaces` (as `Location` objects)
    - `startDate`, `endDate`, `price`, and `distanceKm`

#### When:
- The `processRide` method is called manually

#### Then:
- The `repository.save(ride)` method is invoked once.
- The expected log and print messages are displayed.
- No exceptions are thrown.

### Circuit Breaker Behavior
- A mock `CircuitBreakerRegistry` is injected.
- `doAnswer` is used to simulate the `executeRunnable` method of the circuit breaker.

### Notes
- Fields such as `id`, `createdAt`, `updatedAt`, and `version` remain `null`, as this is expected behavior in unit tests that do not involve a real database.

## Output Example

```text
Test started: processRide() should save the ride
Ride created: TaxiRide(id=null, start=Location(...), ...)
✔ Ride saved: New York City → Times Square
processRide() executed
Repository.save verified
```
