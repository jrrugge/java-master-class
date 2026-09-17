# Car Booking App — My Build Notes

These notes help me remember **how to build the app** and **why we built it that way**.

This file is for **my learning only**. I keep it on my computer to study from.

---

## Contents

1. [Where I am in the course](#where-i-am-in-the-course)
2. [How to use these notes](#how-to-use-these-notes)
3. [What the app is made of](#what-the-app-is-made-of)
4. [Important words explained](#important-words-explained)
5. [Which file do I change?](#which-file-do-i-change)
6. [The three types of code I write again and again](#the-three-types-of-code-i-write-again-and-again)
7. [Phase 2 — What we did and why](#phase-2--what-we-did-and-why)
8. [Phase 3 — What we did and why](#phase-3--what-we-did-and-why)
9. [Phase 4 — What we did and why](#phase-4--what-we-did-and-why)
10. [Loops compared to Streams](#loops-compared-to-streams)
11. [What happens when a user books a car](#what-happens-when-a-user-books-a-car)
12. [Arrays compared to Lists](#arrays-compared-to-lists)
13. [Practice for 10 minutes each day](#practice-for-10-minutes-each-day)
14. [Problems I ran into and how I fixed them](#problems-i-ran-into-and-how-i-fixed-them)
15. [Test the app — step by step](#test-the-app--step-by-step)
16. [Questions to ask myself before finishing a phase](#questions-to-ask-myself-before-finishing-a-phase)
17. [My own notes — what I learned](#my-own-notes--what-i-learned)
18. [What is coming next in the course](#what-is-coming-next-in-the-course)

---

## Where I am in the course

| Phase | Branch name | What we changed | Done? |
|-------|-------------|-----------------|-------|
| 1 | initial-implementation | Built the first version of the app | Yes |
| 2 | interfaces-and-di | Split code into layers; saved bookings to a file | Yes |
| 3 | lists | Changed arrays to Lists | Yes |
| 4 | streams | Changed filter/search loops to Streams | Yes — ready to submit |
| 5 | maven-and-tests | Will add Maven and tests | Not started |
| 6 | spring-boot | Will turn it into a web API | Not started |

**Commit message for Phase 4:** `refactor: replace imperative loops with streams`

---

## How to use these notes

| When | What to do |
|------|------------|
| **Each day (10 minutes)** | Open a blank file. Try to write the [daily practice](#practice-for-10-minutes-each-day) from memory. Then check this project. |
| **Before I start coding** | Read [Important words](#important-words-explained) and cover the answers with your hand. Test yourself. |
| **Before I submit work** | Run through the [test steps](#test-the-app--step-by-step) and answer the [questions](#questions-to-ask-myself-before-finishing-a-phase). |
| **When I am stuck** | Look at [Which file do I change?](#which-file-do-i-change) and [Problems I ran into](#problems-i-ran-into-and-how-i-fixed-them). |

---

## What the app is made of

Think of the app like a stack of layers. **Each layer has one job.** Code in a higher layer talks to the layer below it. It does not skip layers.

```
┌─────────────────────────────────────┐
│  Main                               │  Shows the menu. Reads what the user types.
├─────────────────────────────────────┤
│  Services                           │  Applies the rules (dates valid? car free?)
│  UserService, CarService,           │
│  CarBookingService                  │
├─────────────────────────────────────┤
│  DAO interfaces                     │  A list of rules: "any storage class must
│  UserDao, CarDao, CarBookingDao     │  have these methods"
├─────────────────────────────────────┤
│  DAO implementations                │  Where data actually lives (memory or file)
│  *ArrayDataAccessService,           │
│  CarBookingFileDataAccessService    │
├─────────────────────────────────────┤
│  Data objects                       │  User, Car, CarBooking, Brand, BookingStatus
└─────────────────────────────────────┘
```

**Simple rule:** Main talks to Services. Services talk to DAOs. Main never talks to DAOs directly.

**Why do we do this?** If we change how data is stored (memory, file, database later), we only change the bottom layer. The menu and the rules stay the same.

---

## Important words explained

Cover the answer on the right. Try to say it out loud before you read it.

### The structure of the app

| Word | Plain English meaning |
|------|----------------------|
| **Main** | The starting point. Shows the menu to the user. |
| **Service** | Where the business rules live (Is the car free? Is the date valid?). |
| **DAO** | Data Access Object. Code that reads and saves data. |
| **Interface** | A promise. It lists method names but has no working code inside. |
| **Implementation** | The real code that keeps data in memory or in a file. |
| **POJO** | A simple class that holds data (like User or Car). |
| **Dependency injection** | Main creates the parts and passes them in. Services do not create their own DAOs. |

### Phase 2 ideas

| Question | Answer |
|----------|--------|
| Why use an interface? | So we can swap how data is stored without rewriting the services. |
| Why save to a file? | So bookings still exist when the app is closed and opened again. |
| Why Serializable? | Java can only save an object to a file if the class allows it. |
| What is a soft delete? | We mark a booking as CANCELLED. We do not remove it from the list. |

### Phase 3 ideas

| Question | Answer |
|----------|--------|
| Why use a List instead of an array? | An array has a fixed size. A List can grow when we add items. |
| What is `List<User>`? | A List that only holds User objects. |
| When do I use `List.of()`? | For fixed data that never changes (our 3 users, 4 cars). |
| When do I use `new ArrayList<>()`? | For data that grows (bookings). |
| What order do I change files? | Interfaces first, then implementations, then services, then Main. |

### Phase 4 ideas

| Question | Answer |
|----------|--------|
| What is a Stream? | A way to process a List step by step without writing a manual loop. |
| What does `.stream()` do? | Turns a List into a Stream so you can filter or search it. |
| What does `.filter()` do? | Keeps only items that match a rule. |
| What does `.collect(Collectors.toList())` do? | Turns the Stream back into a List. |
| What does `.findFirst().orElse(null)` do? | Finds one item, or returns null if none found. |
| What does `.anyMatch()` do? | Returns true if at least one item matches (used in `isCarBooked`). |
| What is `Car::isElectric`? | A method reference — shorthand for `car -> car.isElectric()`. |
| What is a lambda? | A short piece of code like `car -> car.getId().equals(carId)`. |
| Did Phase 4 change the layers? | No. Same Main → Service → DAO structure. Only the code inside methods changed. |

### Method names that look similar

| In the DAO | In the Service |
|------------|----------------|
| `getCars()` | `getAllCars()` |
| `getUsers()` | `getAllUsers()` |
| `getBookings()` | `getAllBookings()` |

The DAO uses the course names. The Service keeps the names Main already used.

---

## Which file do I change?

When I want to change something, I open the right file:

| I want to… | Open this file |
|------------|----------------|
| Change the menu or what the user sees | `Main.java` |
| Change which storage class the app uses | `Main.java` (where DAOs are created) |
| Add a rule (e.g. check if a car is free) | `CarBookingService.java` |
| Change what methods every DAO must have | `CarBookingDao.java` (or UserDao / CarDao) |
| Change in-memory booking storage | `CarBookingArrayDataAccessService.java` |
| Change file booking storage | `CarBookingFileDataAccessService.java` |
| Change the list of users or cars | `UserArrayDataAccessService.java` or `CarArrayDataAccessService.java` |

---

## The three types of code I write again and again

Almost every method is one of these three types.

### Type 1 — Pass it through

The Service gets data from the DAO and sends it back. Almost no extra code.

*Example:* `getAllUsers()` calls `userDao.getUsers()` and returns the result.

### Type 2 — Add to a growing list

Bookings are added over time. We use a List that can grow.

*Example:* `bookings.add(booking);`

**In memory:** add directly to the list.

**In a file:** read the list from disk → add the new booking → write the list back to disk.

### Type 3 — Filter a list

Look at every item. Keep only the ones that match a rule.

*Phase 3 example:* loop + `.add()` to a new list.

*Phase 4 example:* `.stream().filter(...).collect(Collectors.toList())`.

---

## Phase 2 — What we did and why

**Goal:** Organise the code and save bookings to a file.

**Steps we followed:**

1. Split each DAO into an interface and a separate class.
2. Rename methods to match the course (e.g. `getCars`, `findCarById`).
3. Move filtering (e.g. bookings by user) up into the Service.
4. Add `Serializable` to User, Car, and CarBooking so they can be saved to a file.
5. Create `CarBookingFileDataAccessService` to read and write `bookings.dat`.
6. Pass DAOs into Services through constructors (dependency injection).
7. Wire everything together in Main.

**Branch:** `interfaces-and-di`

**Commit message:** `refactor: extract dao interfaces and apply dependency injection`

---

## Phase 3 — What we did and why

**Goal:** Replace arrays with Lists across the whole app.

**Why?** Arrays cannot grow easily. Lists can. Adding a booking becomes one line: `bookings.add(booking)`.

**Steps we followed:**

1. Create branch: `git checkout -b lists`
2. Change the three DAO interfaces: `Car[]` becomes `List<Car>` (and same for User, CarBooking).
3. Change array DAO classes:
   - Users and cars → `List.of(...)` (fixed data)
   - Bookings → `new ArrayList<>()` (grows over time)
4. Change file DAO:
   - Read and write a `List`, not an array
   - If no file exists yet → return `Collections.emptyList()`
   - Before adding → wrap in `new ArrayList<>(...)` because empty list cannot be changed
5. Change Services — return types and filter loops
6. Change Main — use `List`, `.size()`, `.get(i)`, `.isEmpty()`
7. Delete old `bookings.dat` if it was saved in the old array format
8. Compile and fix errors one file at a time

**Branch:** `lists`

**Commit message:** `refactor: replace arrays with lists`

---

## Phase 4 — What we did and why

**Goal:** Replace filter and search loops with the Java Streams API.

**Why?** Same behaviour as Phase 3, but shorter and clearer code. This prepares us for functional-style Java used later in the course.

**What changed:** Code **inside** methods only. No changes to interfaces, Main menu, or how the app works.

**Import needed in service classes:**

```java
import java.util.stream.Collectors;
```

**Steps we followed:**

1. Create branch: `git checkout -b streams`
2. Start with the easiest method: `CarService.getElectricCars()`
3. Change filter methods in `CarBookingService`:
   - `getBookingsByUserId()` → `.filter().collect()`
   - `getAvailableCars()` → `.filter().collect()`
   - `getAvailableElectricCars()` → `.filter(Car::isElectric).collect()`
   - `isCarBooked()` → `.anyMatch()` (not collect — we need true/false)
4. Change find methods in DAOs:
   - `findCarById`, `findUserById`, `findBookingById` → `.filter().findFirst().orElse(null)`
5. Leave alone:
   - `Main.java` display loops (need index for "User number: 1")
   - `saveBooking()` and `deleteBooking()` (add/change data, not filter)
6. Compile and test all menu options

**Branch:** `streams`

**Commit message:** `refactor: replace imperative loops with streams`

**Files we changed:**

| File | Methods changed |
|------|-----------------|
| `CarService.java` | `getElectricCars()` |
| `CarBookingService.java` | `getBookingsByUserId`, `getAvailableCars`, `getAvailableElectricCars`, `isCarBooked` |
| `CarArrayDataAccessService.java` | `findCarById()` |
| `UserArrayDataAccessService.java` | `findUserById()` |
| `CarBookingArrayDataAccessService.java` | `findBookingById()` |
| `CarBookingFileDataAccessService.java` | `findBookingById()` |

---

## Loops compared to Streams

Learn these three patterns. They cover almost all of Phase 4.

### Pattern 1 — Filter into a new List

**Loop (Phase 3):**
```java
List<Car> electricCars = new ArrayList<>();
for (Car currentCar : allCars) {
    if (currentCar.isElectric()) {
        electricCars.add(currentCar);
    }
}
return electricCars;
```

**Stream (Phase 4):**
```java
return allCars.stream()
        .filter(Car::isElectric)
        .collect(Collectors.toList());
```

Used in: `getElectricCars`, `getBookingsByUserId`, `getAvailableCars`, `getAvailableElectricCars`.

---

### Pattern 2 — Find one item (or null)

**Loop:**
```java
for (Car currentCar : cars) {
    if (currentCar.getId().equals(carId)) {
        return currentCar;
    }
}
return null;
```

**Stream:**
```java
return cars.stream()
        .filter(car -> car.getId().equals(carId))
        .findFirst()
        .orElse(null);
```

Used in: all three `findById` methods in the DAO classes.

---

### Pattern 3 — Check if any item matches (true/false)

**Loop:**
```java
for (CarBooking booking : bookings) {
    if (sameCar && active && overlappingDates) {
        return true;
    }
}
return false;
```

**Stream:**
```java
return bookings.stream()
        .anyMatch(booking -> sameCar && active && overlappingDates);
```

Used in: `isCarBooked()`.

---

### Date overlap logic (isCarBooked)

Two date ranges overlap when **both** are true:

```java
booking.getStartDate().isBefore(endDate)      // booking starts before my end date
&& startDate.isBefore(booking.getEndDate())  // my start is before booking ends
```

---

## What happens when a user books a car

Follow this path from top to bottom:

1. **Main** shows users and available cars. User types IDs and dates.
2. **Main** sends that information to `CarBookingService.bookCar()`.
3. **CarBookingService** checks:
   - User exists
   - Car exists
   - Dates are valid
   - Car is not already booked (`isCarBooked` uses `.anyMatch()` in Phase 4)
   - Price is calculated
4. **CarBookingService** creates a `CarBooking` object and calls `saveBooking()` on the DAO.
5. **File DAO** reads the current list from disk, adds the new booking, writes the list back.
6. **Main** prints "BOOKING SUCCESSFUL".

---

## Arrays compared to Lists

Learn this table. It comes up in every phase.

| With an array (old) | With a List (new) |
|---------------------|-------------------|
| `User[]` | `List<User>` |
| `users.length` | `users.size()` |
| `users[i]` | `users.get(i)` |
| `users.length == 0` | `users.isEmpty()` |
| Make a bigger array and copy everything | `list.add(item)` |

**Fixed data (users, cars):** use `List.of(...)`

**Data that grows (bookings):** use `new ArrayList<>()`

**No file yet (empty read):** use `Collections.emptyList()`, then copy into `new ArrayList<>(...)` before you add anything.

---

## Practice for 10 minutes each day

Open a new empty Java file. Type from memory. Do not copy and paste. Then check this project.

| Day | What to practice |
|-----|------------------|
| **Monday** | The four layers and what each one does |
| **Tuesday** | Write `getElectricCars()` as a stream from memory |
| **Wednesday** | Write `findCarById()` with `.findFirst().orElse(null)` |
| **Thursday** | Write `isCarBooked()` with `.anyMatch()` |
| **Friday** | Write `getBookingsByUserId()` with `.filter().collect()` |
| **Saturday** | The [Loops compared to Streams](#loops-compared-to-streams) three patterns |
| **Sunday** | Trace [book a car](#what-happens-when-a-user-books-a-car) out loud. Answer the [questions below](#questions-to-ask-myself-before-finishing-a-phase) |

---

## Problems I ran into and how I fixed them

| Problem | Why it happened | Fix |
|---------|-----------------|-----|
| File looked broken on GitHub (`^M` everywhere) | File was saved in wrong text format (UTF-16) | Save as UTF-8 |
| Git would not pull | `bookings.dat` was tracked and I also had a local copy | Move file aside, pull, then stop tracking it |
| App crashed when opening bookings | Old `bookings.dat` was saved as an array, not a List | Delete `bookings.dat` and start fresh |
| `displayCars` would not compile | Method still said `Car[]` but body used List methods | Change parameter to `List<Car>` |
| Could not `.add()` after reading empty file | `Collections.emptyList()` cannot be changed | Use `new ArrayList<>(readBookingsFromFile())` first |
| Cursor name on my commit | I let the tool run git commit | I run all git commands myself. Turn off co-author in Cursor settings. |
| Changed Car/User to records by mistake | Not part of Phase 3 — IntelliJ suggestion or extra change | Revert to normal classes with getters |
| `return false` after stream in `isCarBooked` | Leftover line after converting to stream — unreachable code | Remove it; the stream `.anyMatch()` already returns true/false |
| Duplicate variable in enhanced loop | Started enhanced loop but left old `.get(i)` line | Remove the old line — enhanced loop gives you each item |
| Git pull blocked by BUILD-NOTES.md | Local untracked file clashed with tracked file on remote | Move file aside, pull, then `git rm --cached` to stop tracking |
| Accidentally used records in Phase 3 | Records are Phase 6 — not needed yet | Stick to normal classes until the course covers it |

---

## Test the app — step by step

Before I submit a phase, I run the app and check these menu options:

| Step | Menu option | What I expect |
|------|-------------|---------------|
| 1 | 4 — View all bookings | Empty list or existing bookings show correctly |
| 2 | 5 — View available cars | Shows cars not booked today |
| 3 | 6 — View available electric cars | Shows Tesla only (if not booked) |
| 4 | 1 — Book a car | Booking succeeds and shows price |
| 5 | 4 — View all bookings | New booking appears with ACTIVE status |
| 6 | 3 — View user bookings | Shows bookings for that user only |
| 7 | 2 — Delete booking | Message says deleted; status becomes CANCELLED |
| 8 | 8 — Exit | App closes cleanly |

**Test user (James):** `8ca51d2b-aaaf-4bf2-834a-e02964e10fc3`

**Test car (Toyota):** `df63c985-4e76-48af-9c8f-a539de9269c4`

**Test car (Tesla, electric):** `a40b7081-3c55-4f87-81d5-cd03c02f0021`

**Test dates:** use dates in the future, e.g. `2026-09-10` to `2026-09-12`

**Before testing:** delete `bookings.dat` if it exists from an older version.

---

## Questions to ask myself before finishing a phase

Answer without opening the code.

1. What are the four layers and what does each one do?
2. Why do I change DAO interfaces before I change the other files?
3. When do I use `List.of()` and when do I use `new ArrayList<>()`?
4. What is the difference between `getCars()` and `getAllCars()`?
5. Where does dependency injection happen?
6. What does delete booking actually do?
7. What are the three Stream patterns (filter/collect, findFirst, anyMatch)?
8. When do I use `.filter().collect()` vs `.anyMatch()`?
9. Why did we leave Main display loops as normal loops?
10. Can I explain what happens when a user books a car, layer by layer?

If I can answer at least 8 without hesitation, I am ready to move on.

---

## My own notes — what I learned

Write in your own words. Short sentences are fine.

### Phase 3 — in my own words

**Why we did it:** Lists can grow when we add bookings. Arrays need a new bigger array every time.

**The hardest part for me:** Changing every file in the right order — interfaces first, then implementations, then services, then Main.

**One thing I will remember:** `List.of()` for fixed data, `new ArrayList<>()` for data that grows.

**Something I got wrong the first time:** Forgetting to change method parameters like `displayCars(Car[])` to `List<Car>`.

---

### Phase 4 — in my own words

**Why we did it:** Streams replace manual filter loops with shorter, clearer code. Same app behaviour, better style.

**The hardest part for me:** Knowing which stream method to use — `.collect()` for a list, `.findFirst()` for one item, `.anyMatch()` for true/false.

**One thing I will remember:** The pipeline is always `list.stream()` → do something → finish with `.collect()` or `.findFirst()` or `.anyMatch()`.

**Something I got wrong the first time:** Leaving `return false;` after the stream in `isCarBooked()` — the stream already returns true or false.

**Date overlap:** Both conditions must be true — booking starts before my end date AND my start is before booking ends.

**What I did NOT change:** Main display loops (need index), saveBooking, deleteBooking.

---

## What is coming next in the course

| Phase | Branch | Simple explanation |
|-------|--------|-------------------|
| 5 | maven-and-tests | Use Maven to build the project and write automated tests |
| 6 | spring-boot | Turn the CLI into a web API |

The layers stay the same. Only the tools inside each layer change.

---

## How to become faster at this

I do not need to memorise every file. I need to remember:

1. **The order of layers** — Main, Service, DAO, data
2. **The three code types** — pass through, add to list, filter list
3. **The array → List table**
4. **The three Stream patterns** — filter/collect, findFirst, anyMatch
5. **One full path** — booking a car from menu to file

Ten minutes of practice each day helps more than reading for two hours once a week.

**I do all git commands myself:** branch, commit, push, and pull request.
