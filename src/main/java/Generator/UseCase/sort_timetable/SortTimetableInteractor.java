package Generator.UseCase.sort_timetable;

import CourseInfo.Timetable;

import java.util.List;

/**
 * Use case interactor for sorting timetables.
 *
 * Depends on:
 *  - SortTimetableOutputBoundary (presenter)
 *  - TimetableSortStrategy (strategy)
 */
public class SortTimetableInteractor implements SortTimetableInputBoundary {

    private final SortTimetableOutputBoundary outputBoundary;
    private final TimetableSortStrategy strategy;

    public SortTimetableInteractor(SortTimetableOutputBoundary outputBoundary,
                                   TimetableSortStrategy strategy) {
        this.outputBoundary = outputBoundary;
        this.strategy = strategy;
    }

    @Override
    public void sort(SortTimetableRequestModel requestModel) {
        List<Timetable> sorted = strategy.sort(requestModel.getTimetables());
        SortTimetableResponseModel responseModel =
                new SortTimetableResponseModel(sorted);
        outputBoundary.present(responseModel);
    }
}
