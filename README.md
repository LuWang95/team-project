## Project summary

This application is a personalized student timetable generator for UofT that allows users to add individual courses or entire degrees, generate valid schedules for fall and winter, and adjust preferences such as preferred time of day. Users can save and reload timetables, export them as CSV or PNG, and sort different schedule options by estimated walking distance between consecutive classes using external mapping APIs.

## User stories

Current implemented and planned user stories include the following.

- As a user, I can add and remove courses from my timetable so that my schedule only includes the courses I actually plan to take.
- As a user, I can add and remove degrees (and select a year) so that required courses for that degree and year are automatically added to my timetable.
- As a user, I can generate a timetable from my selected courses (and degrees) so that I see a valid schedule for both fall and winter semesters.
- As a user, I can regenerate the timetable so that I can explore alternative valid schedules while keeping the same course set.
- As a user, I can modify my preferences (such as preferred time of day) after generating a timetable so that I can refine my schedule without starting over.
- As a user, I can save a timetable to a CSV file and export it as a PNG so that I can store and share my schedule outside the application.
- As a user, I can load a previously saved timetable from CSV so that I can continue working with an existing schedule.
- As a user, I can sort my set of generated timetables by total walking distance so that I can quickly choose the schedule that minimizes walking between classes.

Who is responsible for each story?

- Adding/removing courses: Jeremy Tam
- Adding/removing degrees: Peter Xu
- Generating Timetable: Lu Wang
- Regenerating timetable: Chenhao Sun
- Modifying preferences: Chenhao Sun
- Saving/loading timetables: Shiraz Ali
- Sorting timetables by walking distance: Septian 


## APIs used

This project uses mapping and routing APIs to estimate walking distances between class locations.

- Geoapify API  
  - Purpose: Look up geographic coordinates for UofT campus buildings based on building codes used in course locations.
- OpenRouteService API  
  - Purpose: Compute walking distances between two building coordinates to estimate how far a student needs to walk between consecutive classes in a given timetable.

These APIs are accessed through data access objects (e.g., Geoapify DAO and OpenRouteService DAO) that implement the DistanceDataAccessInterface used by the timetable sorting use case.

## Current functionality screenshots

Below is a checklist of suggested screenshots or short animations to include in this README as the project evolves.[1]

- Timet<img width="841" height="291" alt="Screenshot 2025-12-03 at 7 11 31 AM" src="https://github.com/user-attachments/assets/97a8e95c-fb67-46f9-957a-290bb669f3e2" />
able Builder main screen, showing:
  - Academic information (degree, year of study) and course list input.
  - Preferences section for selecting preferred time of day (morning/afternoon/evening).
- Generated timetable view:
  - Fall and winter timetable grids with courses placed in appropriate time slots.
  - Enrolled courses summary with course codes, names, and credits.
- Add/remove courses and degrees:
  - UI for entering a course or degree code and confirmation of successful addition/removal.
- Save/load timetables:
  - “Save CSV” and “Export PNG” dialogs and example of loading a timetable from a CSV file.
- Sorting by walking distance:
  - Control or button to sort timetables, and comparison of timetables before and after sorting by walking distance.

When available, add Markdown image links pointing to files in your repository, for example:

- ![Timetable Builder main view](docs/images/timetable-builderdocs/images/generated-timetable section with real image paths and new screenshots as features are implemented or changed.

[1](https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/145969727/d7e5c889-c756-4913-af10-8b34ec7b85a6/Project-Presentation.pdf)
