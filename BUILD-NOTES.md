# Car Booking CLI — Build Notes

Personal revision guide. Use this to **recall how to write the code**, not just what the app does.

---

## How to use this doc

| When | What to do |
|------|------------|
| **Daily (10 min)** | Write the [Daily Practice Lines](#daily-practice-lines) from memory in a blank file |
| **Before coding** | Read [Key Concepts to Recall](#key-concepts-to-recall) — cover answers, test yourself |
| **Before a PR** | Run the [Self-Test](#self-test-before-submitting) + trace [Book a Car flow](#flow-1-book-a-car) aloud |
| **When stuck** | Check [Gotchas](#gotchas) and [The 3 Patterns](#the-3-coding-patterns) |

---

## Key concepts to recall

Cover the right column. Say or write the answer before revealing it.

### Architecture

| Concept | Recall |
|---------|--------|
| How many layers? | **4** — Main → Service → DAO interface → DAO implementation → (POJOs underneath) |
| Which direction can code call? | **Down only.** Main → Service → DAO. Never skip. |
| Who creates DAO objects? | **Main** — then passes them into service constructors |
| Who validates business rules? | **Service layer** — not DAO, not Main |
| Who talks to the user? | **Main only** — CLI input/output |
| What is a POJO? | Plain object holding data — `User`, `Car`, `CarBooking` |
| What is a DAO? | Data Access Object — reads/writes stored data |
| What is an interface? | A **contract** — lists methods, no implementation |

### Phase 2 — Interfaces & DI

| Concept | Recall |
|---------|--------|
| Why interfaces? | Swap **implementations** without changing services |
| Why dependency injection? | Main **chooses** which DAO to use; services don't `new` their own |
| Why Serializable? | Java can only **write objects to file** if they implement it |
| File DAO vs memory DAO | File = **read → modify → write**. Memory = **`.add()` to field** |
| Soft delete | Sets status to `CANCELLED` — booking stays in list |

### Phase 3 — Lists

| Concept | Recall |
|---------|--------|
| Why Lists over arrays? | Arrays are **fixed size**; lists **grow** with `.add()` |
| What is a generic? | `List<User>` — tells Java what type the list holds |
| `List.of()` vs `ArrayList` | `List.of()` = **immutable** fixed data. `ArrayList` = **mutable**, grows |
| `Collections.emptyList()` | Returns empty list when **no file exists** — immutable |
| Refactor order | **Interfaces → implementations → services → Main** |
| Why bottom-up? | Compiler errors become your **checklist** |

### The swap table (must be automatic)

| Array | List |
|-------|------|
| `User[]` | `List<User>` |
| `.length` | `.size()` |
| `[i]` | `.get(i)` |
| `.length == 0` | `.isEmpty()` |
| `new T[n]` + copy loop | `new ArrayList<>()` + `.add()` |

### Method naming trap

| DAO says | Service says |
|----------|--------------|
| `getCars()` | `getAllCars()` |
| `getUsers()` | `getAllUsers()` |
| `getBookings()` | `getAllBookings()` |

DAO = interface name. Service = name Main already used in Phase 1.

---

## Architecture

```
┌─────────────────────────────────────┐
│  Main.java          CLI / menu      │  ← user input, display, wires dependencies
├─────────────────────────────────────┤
│  Services           business logic  │  ← validate, calculate, filter, orchestrate
│  UserService / CarService           │
│  CarBookingService                  │
├─────────────────────────────────────┤
│  DAO interfaces     contracts       │  ← WHAT: method signatures only
│  UserDao / CarDao / CarBookingDao   │
├─────────────────────────────────────┤
│  DAO implementations              │  ← HOW: array (memory) or file (disk)
│  *ArrayDataAccessService            │
│  CarBookingFileDataAccessService    │
├─────────────────────────────────────┤
│  POJOs + enums                      │  ← User, Car, CarBooking, Brand, BookingStatus
└─────────────────────────────────────┘
```

**Why layers?** Change one layer without rewriting everything. Example: swap file storage for a database later — only DAO implementations change.

---

## Package map

| Package | Classes |
|---------|---------|
| `com.johnscode` | `Main` |
| `com.johnscode.user` | `User`, `UserDao`, `UserArrayDataAccessService`, `UserService` |
| `com.johnscode.car` | `Car`, `Brand`, `CarDao`, `CarArrayDataAccessService`, `CarService` |
| `com.johnscode.booking` | `CarBooking`, `BookingStatus`, `CarBookingDao`, `CarBookingArrayDataAccessService`, `CarBookingFileDataAccessService`, `CarBookingService` |

---

## The 3 coding patterns

Every method you write is one of these. Recognise which one you're doing.

### Pattern 1 — Pass-through
Service returns what the DAO returns. Change the type, keep the body minimal.

```java
public List<User> getAllUsers() {
    return userDao.getUsers();
}
```

**Used in:** `getAllUsers`, `getAllCars`, `getAllBookings`, `getCars`, `getUsers`, `getBookings`

---

### Pattern 2 — Grow a list
Data starts empty and items get added over time.

```java
private static List<CarBooking> bookings = new ArrayList<>();

public void saveBooking(CarBooking booking) {
    bookings.add(booking);
}
```

**Used in:** `CarBookingArrayDataAccessService.saveBooking`, file DAO save (with read/write wrapper)

**File version adds read → add → write:**
```java
List<CarBooking> bookings = new ArrayList<>(readBookingsFromFile());
bookings.add(booking);
writeBookingsToFile(bookings);
```

---

### Pattern 3 — Filter a list
Loop through everything, `.add()` only what matches.

```java
List<Car> electricCars = new ArrayList<>();
for (int i = 0; i < allCars.size(); i++) {
    Car car = allCars.get(i);
    if (car.isElectric()) {
        electricCars.add(car);
    }
}
return electricCars;
```

**Used in:** `getElectricCars`, `getAvailableCars`, `getAvailableElectricCars`, `getBookingsByUserId`

Phase 4 replaces this loop with Streams — same idea, different syntax.

---

## Phase 2 — Step by step (how we wrote it)

```
1.  Split each DAO into interface + *ArrayDataAccessService class
2.  Rename DAO methods: getCars, findCarById, getUsers, findUserById, getBookings, findBookingById
3.  Move getBookingsByUserId filtering from DAO → CarBookingService
4.  Add Serializable to User, Car, CarBooking (+ serialVersionUID)
5.  Create CarBookingFileDataAccessService (ObjectInputStream / ObjectOutputStream)
6.  Add constructor injection to all three services
7.  Wire everything in Main — Main creates DAOs, passes to services
```

### Interface + implementation (write from memory)

```java
// Interface — contract only
public interface CarDao {
    List<Car> getCars();
    Car findCarById(UUID carId);
}

// Implementation — storage logic
public class CarArrayDataAccessService implements CarDao {
    // ...
}
```

### Dependency injection (write from memory)

```java
// Main creates concrete classes
CarDao carDao = new CarArrayDataAccessService();
CarService carService = new CarService(carDao);  // passed IN, not created inside
```

**Why?** `CarService` depends on `CarDao` (interface). It doesn't know or care if data is in an array, file, or database.

---

## Phase 3 — Step by step (how we wrote it)

```
1.  Create branch: git checkout -b lists
2.  Update DAO interfaces: Car[] → List<Car> (all three interfaces)
3.  Update array DAOs:
      - Seed data (users, cars) → List.of(...)
      - Bookings → new ArrayList<>()
4.  Update file DAO:
      - readBookingsFromFile() → List<CarBooking>
      - empty return → Collections.emptyList()
      - saveBooking → new ArrayList<>(read...) + add + write
5.  Update services: return types + filter loops (Pattern 3)
6.  Update Main: List<T> variables, .size(), .get(i), .isEmpty()
7.  Delete old bookings.dat (was saved as array)
8.  Compile — fix whatever the compiler points at
```

### Immutable vs mutable — decision tree

```
Does this data grow after startup?
├── NO  → List.of(...)           (users, cars)
└── YES → new ArrayList<>()      (bookings)

Reading from file and nothing exists yet?
└── Collections.emptyList()      (then wrap in ArrayList before .add())
```

---

## Core flows (trace aloud)

### Flow 1 — Book a car

```
Main.bookCar()
  │  display users + available cars
  │  read userId, carId, startDate, endDate from scanner
  ▼
CarBookingService.bookCar()
  │  validate IDs and dates not null
  │  userService.getUserById()     → null? throw
  │  carService.findCarById()      → null? throw
  │  startDate not in past
  │  endDate after startDate
  │  isCarBooked()                 → true? throw
  │  calculate price (days × daily rate)
  │  create CarBooking object
  ▼
CarBookingDao.saveBooking()
  │  File: readBookingsFromFile()
  │  new ArrayList<>(...) + .add(booking)
  │  writeBookingsToFile()
  ▼
Main prints BOOKING SUCCESSFUL
```

### Flow 2 — Delete booking

```
Main.deleteBooking()
  │  show all bookings
  │  read bookingId from scanner
  ▼
CarBookingService.deleteBooking()
  │  findBookingById() → null? return false
  ▼
CarBookingDao.deleteBooking()
  │  find booking, setStatus(CANCELLED)
  │  write back to file
  ▼
Main prints success or not found
```

### Flow 3 — View available cars

```
Main → carBookingService.getAvailableCars()
  │  get all cars from CarService
  │  for each car: isCarBooked(today, tomorrow)?
  │  if NOT booked → add to result list
  ▼
Main → displayCars(list)
```

---

## Daily practice lines

**How:** Open a blank file each day. Type these from memory — no copy-paste. Check against project after. Takes ~10 minutes.

### Monday — Interface + DI

```java
public interface UserDao {
    List<User> getUsers();
    User findUserById(UUID userId);
}

CarDao carDao = new CarArrayDataAccessService();
CarService carService = new CarService(carDao);
```

### Tuesday — Seed data + find by ID loop

```java
private static final List<User> users;

static {
    users = List.of(
        new User(UUID.fromString("..."), "James")
    );
}

for (int i = 0; i < users.size(); i++) {
    User currentUser = users.get(i);
    if (currentUser.getId().equals(userId)) {
        return currentUser;
    }
}
return null;
```

### Wednesday — Grow a list (memory DAO)

```java
private static List<CarBooking> bookings = new ArrayList<>();

public void saveBooking(CarBooking booking) {
    bookings.add(booking);
}
```

### Thursday — File DAO (read / save / empty)

```java
if (!file.exists()) {
    return Collections.emptyList();
}

List<CarBooking> bookings = new ArrayList<>(readBookingsFromFile());
bookings.add(booking);
writeBookingsToFile(bookings);
```

### Friday — Filter pattern

```java
List<Car> result = new ArrayList<>();
for (int i = 0; i < allCars.size(); i++) {
    Car car = allCars.get(i);
    if (car.isElectric()) {
        result.add(car);
    }
}
return result;
```

### Saturday — Swap table drill

Write the full array → list table from memory (see [Key Concepts](#the-swap-table-must-be-automatic)).

### Sunday — Trace + self-test

1. Trace **Book a car** flow aloud (see above)
2. Answer all 10 [self-test questions](#self-test-before-submitting) without looking

---

## Lines you should be able to write blind

These are the highest-value lines. If these are automatic, you can rebuild most of the project.

```java
// 1. Interface method signature
List<Car> getCars();

// 2. Immutable seed list
users = List.of(new User(...), new User(...));

// 3. Mutable growing list
private static List<CarBooking> bookings = new ArrayList<>();

// 4. Loop access
for (int i = 0; i < list.size(); i++) {
    Thing item = list.get(i);
}

// 5. Empty check
if (list.isEmpty()) { return; }

// 6. Add to list
list.add(item);

// 7. Filter pattern (one line inside loop)
if (condition) { result.add(item); }

// 8. File empty return
return Collections.emptyList();

// 9. File save (mutable copy before add)
List<CarBooking> bookings = new ArrayList<>(readBookingsFromFile());
bookings.add(booking);
writeBookingsToFile(bookings);

// 10. DI wiring
CarService carService = new CarService(carDao);
```

---

## Wiring in Main (full block to recall)

```java
CarBookingDao carBookingDao = new CarBookingFileDataAccessService("bookings.dat");
CarDao carDao = new CarArrayDataAccessService();
UserDao userDao = new UserArrayDataAccessService();

CarService carService = new CarService(carDao);
UserService userService = new UserService(userDao);
CarBookingService carBookingService = new CarBookingService(
        carBookingDao, carService, userService
);
```

---

## CarBookingService — method responsibilities

| Method | Pattern | What it does |
|--------|---------|--------------|
| `bookCar()` | orchestration | Validate → check availability → price → save |
| `isCarBooked()` | filter/check | Loop bookings, check same car + ACTIVE + date overlap |
| `getAllBookings()` | pass-through | Returns DAO list |
| `getBookingsByUserId()` | filter | Loop all bookings, add matching user |
| `getAvailableCars()` | filter | All cars minus booked today |
| `getAvailableElectricCars()` | filter | Available cars where `isElectric()` |
| `deleteBooking()` | orchestration | Find booking → DAO cancel → return boolean |

---

## Gotchas

| Problem | Cause | Fix |
|---------|-------|-----|
| GitHub shows `^M` | UTF-16 encoding | Save as UTF-8 |
| Pull blocked by `bookings.dat` | Tracked file + local copy | Move aside → pull → `git rm --cached` |
| `displayCars` won't compile | Param still `Car[]` | Change to `List<Car>` |
| Runtime deserialization error | Old array-format `bookings.dat` | Delete file |
| Can't `.add()` after empty read | `Collections.emptyList()` is immutable | Wrap in `new ArrayList<>(...)` |
| `getElectricCars` greyed out | Unused — menu uses `getAvailableElectricCars` | Still update for Phase 3 completeness |

---

## Self-test before submitting

Answer without opening code:

1. What are the 4 layers and what does each do?
2. Why change DAO interfaces before implementations?
3. Why does file DAO need read → add → write but memory DAO just `.add()`?
4. When do you use `List.of()` vs `new ArrayList<>()`?
5. What's the difference between `getCars()` and `getAllCars()`?
6. Where does dependency injection happen?
7. What does `deleteBooking` actually do? (removed from list?)
8. Write the array → list swap for `.length`, `[i]`, and empty check.
9. What are the 3 coding patterns?
10. Trace book-a-car through all layers.

**Scoring:** 8+ without hesitation = ready to move on. Below 8 = do one daily practice session.

---

## Test data

| | Name | UUID |
|---|------|------|
| User | James | `8ca51d2b-aaaf-4bf2-834a-e02964e10fc3` |
| User | John | `b10d126a-3608-4980-9f9c-aa179f5cebc3` |
| User | Alex | `7e8b2f7c-dcb4-4b18-8d74-f0766363a11c` |
| Car | Tesla | `a40b7081-3c55-4f87-81d5-cd03c02f0021` |
| Car | Toyota | `df63c985-4e76-48af-9c8f-a539de9269c4` |

**Menu test sequence:** `4 → 1 → 4 → 2 → 4 → 8`

**Booking dates:** always use future dates (e.g. `2026-08-26` → `2026-08-28`)

---

## Phase roadmap

| Phase | Branch | What you learn to write |
|-------|--------|-------------------------|
| 1 ✅ | `initial-implementation` | Basic CLI, arrays, loops |
| 2 ✅ | `interfaces-and-di` | Interfaces, Serializable, file I/O, DI |
| 3 ✅ | `lists` | Generics, List, ArrayList, Collections |
| 4 | `streams` | `.stream()`, `.filter()`, `.collect()` |
| 5 | `maven-and-tests` | pom.xml, JUnit, `@Test` |
| 6 | `spring-boot` | `@RestController`, `@Autowired`, JPA |

Same architecture every phase. New tools inside each layer.

---

## Becoming a developer — what "at your fingertips" really means

You don't memorise 16 files. You memorise:

1. **Layer order** — where code belongs
2. **3 patterns** — pass-through, grow, filter
3. **The swap table** — array ↔ list (later: loop ↔ stream)
4. **One flow traced end-to-end** — book a car

Everything else is lookup until you've seen it 3–4 times. Daily practice lines above are how you get there.

**10 min/day > 2 hours once a week.**
