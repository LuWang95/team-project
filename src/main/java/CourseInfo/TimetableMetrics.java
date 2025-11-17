package CourseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain service that computes metrics for a Timetable.
 *
 * Here, "gap" is counted in empty time slots between classes.
 */
public final class TimetableMetrics {

    private TimetableMetrics() {
        // Utility class; no instances allowed.
    }

    /**
     * Computes the total gap (in slots) between occupied slots for all days.
     *
     * Example for one day:
     *   occupied at 1, 4, 5  → gaps = (4 - 1 - 1) + (5 - 4 - 1) = 2 + 0 = 2 slots.
     */
    public static int totalGapSlots(Timetable timetable) {
        int days = timetable.getDayCount();
        int slots = timetable.getSlotCount();
        int totalGap = 0;

        for (int day = 0; day < days; day++) {
            List<Integer> occupied = new ArrayList<>();

            for (int slot = 0; slot < slots; slot++) {
                if (timetable.isOccupied(day, slot)) {
                    occupied.add(slot);
                }
            }

            if (occupied.size() < 2) {
                continue; // 0 or 1 class in that day → no gaps
            }

            for (int i = 1; i < occupied.size(); i++) {
                int prev = occupied.get(i - 1);
                int curr = occupied.get(i);
                int gap = curr - prev - 1;
                if (gap > 0) {
                    totalGap += gap;
                }
            }
        }

        return totalGap;
    }
}
