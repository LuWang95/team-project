# UofT Personalized Student Timetable Generator

A comprehensive Java-based application that enables University of Toronto students to build, customize, and optimize their course schedules for both Fall and Winter semesters. The application generates valid timetables based on selected courses and degrees while minimizing walking distance between consecutive classes using real-world campus map data.

---

## Project Summary

This application allows UofT students to:
- Add individual courses or entire degree requirements to their schedule
- Generate conflict-free timetables for Fall and Winter semesters
- Customize scheduling preferences (preferred time of day: morning, afternoon, evening)
- Save and reload timetables in CSV format
- Export schedules as PNG images for easy sharing
- Sort multiple valid timetable options by total walking distance between classes

The application integrates with external mapping APIs to provide distance calculations between UofT campus buildings, helping students choose schedules that minimize transit time between classes.

---

## User Stories

### Adding and Removing Courses
**Responsible:** Jeremy Tam

*As a user, I can add and remove courses from my timetable so that my schedule only includes the courses I actually plan to take.*

### Adding and Removing Degrees
**Responsible:** Peter Xu

*As a user, I can add and remove degrees (and select a year) so that required courses for that degree and year are automatically added to my timetable.*

### Generating Timetable
**Responsible:** Lu Wang

*As a user, I can generate a timetable from my selected courses (and degrees) so that I see a valid schedule for both fall and winter semesters.*

### Regenerating Timetable
**Responsible:** Chenhao Sun

*As a user, I can regenerate the timetable so that I can explore alternative valid schedules while keeping the same course set.*

### Modifying Preferences
**Responsible:** Chenhao Sun

*As a user, I can modify my preferences (such as preferred time of day) after generating a timetable so that I can refine my schedule without starting over.*

### Saving and Loading Timetables
**Responsible:** Shiraz Ali

*As a user, I can save a timetable to a CSV file and export it as a PNG so that I can store and share my schedule outside the application.*

*As a user, I can load a previously saved timetable from CSV so that I can continue working with an existing schedule.*

### Sorting Timetables by Walking Distance
**Responsible:** Septian Dimas Pasaribu

*As a user, I can sort my set of generated timetables by total walking distance so that I can quickly choose the schedule that minimizes walking between classes.*

---

## APIs Used

This project integrates two external APIs to calculate walking distances between UofT campus buildings:

### Geoapify API
**Purpose:** Retrieves geographic coordinates (latitude/longitude) for specific UofT campus buildings using building codes from course location data.

### OpenRouteService API
**Purpose:** Calculates actual walking distances between two coordinate pairs, enabling the application to estimate how far students need to walk between consecutive classes.

**Implementation:** Both APIs are accessed through dedicated Data Access Objects (GeoapifyDAO and OpenRouteServiceDAO) that implement the `DistanceDataAccessInterface` used by the timetable sorting feature.

---

## Use Case Demonstrations

### Adding and Removing Courses

Users can add courses by entering the appropriate course code or remove them by selecting the deletion button.

<img width="841" alt="Adding and removing courses interface" src="https://github.com/user-attachments/assets/97a8e95c-fb67-46f9-957a-290bb669f3e2" />

Added courses are displayed in the final generated timetable.

<img width="851" alt="Generated timetable showing added courses" src="https://github.com/user-attachments/assets/808ef868-8d45-4543-8e29-c8c2c4a53858" />

#### Adding Course UML Diagram
<img width="847" alt="Add course UML diagram" src="https://github.com/user-attachments/assets/40c8c9cc-a499-4e25-86f5-ad385584c07a" />

#### Removing Course UML Diagram
<img width="839" alt="Remove course UML diagram" src="https://github.com/user-attachments/assets/868a202e-a824-40a4-b172-04d74c42ff69" />

---

### Adding and Removing Degrees

Users can add a degree by entering the degree code, which automatically adds all required courses for that program.

<img width="839" alt="Adding degree interface" src="https://github.com/user-attachments/assets/c7762954-b024-4180-a639-0c45096ac1fa" />

**Note:** Removing a degree does not remove the associated courses, allowing users to retain courses they still want in their schedule.

#### Adding Degree UML Diagram
<img width="837" alt="Add degree UML diagram" src="https://github.com/user-attachments/assets/4df7a34d-da37-4d8c-9742-fb20f1362a75" />

#### Removing Degree UML Diagram
<img width="848" alt="Remove degree UML diagram" src="https://github.com/user-attachments/assets/da2b5685-c81e-4438-b7f2-065900704a7f" />

---

### Generating the Timetable

The core functionality generates valid timetables for both Fall and Winter semesters based on selected courses, degrees, and time preferences.

<img width="849" alt="Generated timetable view" src="https://github.com/user-attachments/assets/c8e758e0-910d-46f0-8284-568430375f98" />

#### Generating Timetable UML Diagram
<img width="839" alt="Generate timetable UML diagram" src="https://github.com/user-attachments/assets/8ffb20f2-b42a-4ede-8528-84499798efba" />

---

### Regenerating the Timetable

Users can generate alternative valid timetables with the same course selections to explore different scheduling options.

<img width="860" alt="Regenerate timetable interface" src="https://github.com/user-attachments/assets/369f1db9-8d28-4dc2-8107-6b5e39a5cd26" />

#### Regenerating Timetable UML Diagram
<img width="765" alt="Regenerate timetable UML diagram" src="https://github.com/user-attachments/assets/902dc5a9-9cca-4919-bf47-6b02d8ba70e0" />

---

### Modifying Preferences

Users can return to the preferences screen to adjust settings (such as preferred time of day) after generating a timetable.

<img width="847" alt="Modify preferences interface" src="https://github.com/user-attachments/assets/8fd2905a-70c0-4f7c-978a-abadd81bdc89" />

#### Modifying Preferences UML Diagram
<img width="773" alt="Modify preferences UML diagram" src="https://github.com/user-attachments/assets/0956c494-8793-42e3-bce7-75c249178859" />

---

### Saving the Timetable

Users can save timetables in CSV format (for reloading) or export them as PNG images for sharing.

<img width="849" alt="Save timetable dialog" src="https://github.com/user-attachments/assets/4f3dd101-cabf-48e4-af35-6ec0b23d3b5b" />

#### Saving Timetable UML Diagram
<img width="860" alt="Save timetable UML diagram" src="https://github.com/user-attachments/assets/f3f1ff0a-feee-4159-a8f5-eb8e4f3d3413" />

---

### Loading the Timetable

Users can reload previously saved timetables from CSV files. Only CSV files generated by this application are accepted; invalid formats display an error message.

<img width="846" alt="Load timetable dialog" src="https://github.com/user-attachments/assets/15f17dfa-af33-4f0b-ae58-c1932a1e96cc" />

#### Loading Timetable UML Diagram
<img width="803" alt="Load timetable UML diagram" src="https://github.com/user-attachments/assets/f02d2945-1e78-48d4-9d43-49ee5c691c65" />

---

### Sorting the Timetable by Walking Distance

Users can sort multiple generated timetables by total walking distance to identify schedules that minimize transit time between consecutive classes.

<img width="855" alt="Sort timetable interface" src="https://github.com/user-attachments/assets/61294f42-8c08-406f-9daa-fb84a0722bb7" />

#### Sorting Timetable UML Diagram
<img width="863" alt="Sort timetable UML diagram" src="https://github.com/user-attachments/assets/1fb91be8-e1cd-44a9-b69a-8a502962d61d" />

---

## Architecture and Design Patterns

The application follows Clean Architecture principles and implements several design patterns:

### SOLID Principles
- **Dependency Inversion Principle:** Low coupling between architectural layers through input/output boundaries
- **Interface Segregation Principle:** Each use case has dedicated input/output boundaries with only relevant methods

### Design Patterns
- **Factory Pattern:** `SectionFactory` creates lecture, tutorial, or practical section objects based on section codes
- **Data Transfer Object (DTO):** `TimetableDTO` transfers data between interactor, presenter, and view layers

### Code Organization
- **app:** Application initialization and configuration
- **courseinfo:** Entity classes
- **generator:** Use cases, interface adapters, data access, and views

---

## Code Quality

- **Checkstyle:** Used throughout development to maintain consistent code quality
- **Code Review:** All pull requests thoroughly reviewed before merging
- **Test Coverage:** Comprehensive test cases implemented for core functionality

---

