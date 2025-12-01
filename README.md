# Team Project

## 1. Overview 
Our team project is an application that helps Arts & Sciences students at the University of Toronto generate, compare, and refine timetables based on selected courses and degrees and time preferences. 
By entering degree programs and course codes, studens are able to:
1) Generate timetables that satisfy degree requirements.
2) Apply time preferences (morning/afternoon/evening) and insert academic information (year of study).
3) Sort timetables by walking distance between buildings or other criteria.
4) Fix or manually edit individual sections to collaborate with friends.
5) Save drafts of timetables for future reference.

## 2. User Stories

| #  | User Story                                                                               | Person(s) Responsible          |
|----|--------------------------------------------------------------------------------------------------------|-------------------------------|
| 1  | Compare different timetables in a clear view to choose the best one.                                  | Chenhao Sun                   |
| 2  | Fix (lock) specific lecture sections so they stay with friends’ schedules.                             | Shiraz Ali                    |
| 3  | Sort timetables (e.g., by gaps between lectures / walking distance).                                   | Septian Pasaribu        |
| 4  | Manually modify lecture sections to customize a timetable.                                             | Lu Wang     |
| 5  | Save drafts of timetables so decisions don’t need to be immediate.                                     | Jeremy Tam             |
| 6  | Start from the minimum set of required courses and add electives as desired.                           | Peter Xu        |
| 7  | Choose between equivalent courses (MAT235 vs MAT237, CSC236 vs CSC240).                                | Peter Xu / Lu Wang                    |
| 8  | See conflicts highlighted while editing, to avoid invalid schedules.                                   | Shiraz Ali / Septian Pasaribu                    |
| 9  | Apply time-of-day preferences for lecture sections.                                                    | Chenhao Sun / Jeremy Tam               |
| 10 | See where elective slots appear and fill them with chosen courses.                                     | Whole team      |

## 3. Use Cases 

### 1) Add Course
Allows users to manually add individual courses to their timetable by entering course codes. The system validates the course code, retrieves available lecture sections from the course database, and adds the selected course to the user's course list. This enables students to include elective courses beyond their degree requirements and customize their academic schedule.
### 2) Remove Course
Enables users to delete courses from their selected course list. When a course is removed, all associated lecture sections are cleared from the timetable, freeing up those time slots. The system checks if the course is locked (fixed) before deletion and provides appropriate warnings to prevent accidental removal of important courses.
### 3) Add Degree
Allows users to select their academic program (degree/major) from the Faculty of Arts & Science. The system automatically retrieves all mandatory courses required for that degree and presents equivalent course options (e.g., MAT235 vs MAT237, CSC236 vs CSC240) for user selection. This streamlines the course selection process by ensuring degree requirements are met.
### 4) Remove Degree
Enables users to remove a previously added degree from their preferences. When a degree is removed, all associated mandatory courses are cleared from the course list, allowing users to start fresh or switch to a different program without manually removing individual courses.
### 5) Generate Timetable
Creates optimized timetables based on selected courses and user preferences (time of day, year of study). The system generates multiple valid timetable combinations by finding non-conflicting lecture sections for all courses, applying sorting algorithms to rank timetables by criteria such as walking distance between classes and time gaps between lectures. Users can view different generated options.
### 6) Sort Timetable
Organizes generated timetables according to user-selected criteria, such as minimizing walking distance between consecutive classes, reducing gaps between lectures, or prioritizing preferred time slots (morning/afternoon/evening). This helps students quickly identify the most convenient schedule from multiple generated options.
### 7) Regenerate Timetable
Generates a new timetable configuration from the pool of possible schedules without changing user preferences or selected courses. This allows users to cycle through different valid combinations of lecture sections to find alternative schedules that better suit their needs, while keeping locked (fixed) courses unchanged.
### 8) Save Timetable
Exports the current timetable to a CSV file that users can save to their local computer. The file includes complete schedule information for both Fall and Winter semesters, preserving all course selections and lecture timings. This enables students to keep drafts of their schedules without making immediate enrollment decisions.
### 9) Load Timetable
Imports a previously saved timetable from a CSV file, restoring the complete schedule including all courses, lecture sections, and timing information for both Fall and Winter semesters.



Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.
