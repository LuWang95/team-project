package app;

import java.awt.*;

import javax.swing.*;

import generator.data_access.FileUserDataAccessObject;
import generator.interface_adapter.*;
import generator.interface_adapter.display_timetable.DisplayTimetableController;
import generator.interface_adapter.display_timetable.DisplayTimetablePresenter;
import generator.interface_adapter.display_timetable.DisplayTimetableViewModel;
import generator.interface_adapter.set_preferences.SetPreferencesController;
import generator.interface_adapter.set_preferences.SetPreferencesPresenter;
import generator.interface_adapter.set_preferences.SetPreferencesViewModel;
import generator.use_case.add_course.AddCourseInputBoundary;
import generator.use_case.add_course.AddCourseInteractor;
import generator.use_case.add_course.AddCourseOutputBoundary;
import generator.use_case.add_degree.AddDegreeInputBoundary;
import generator.use_case.add_degree.AddDegreeInteractor;
import generator.use_case.add_degree.AddDegreeOutputBoundary;
import generator.use_case.generate_timetable.GenerateTimetableInputBoundary;
import generator.use_case.generate_timetable.GenerateTimetableInteractor;
import generator.use_case.generate_timetable.GenerateTimetableOutputBoundary;
import generator.use_case.regenerate_timetable.RegenerateTimetableInputBoundary;
import generator.use_case.regenerate_timetable.RegenerateTimetableInteractor;
import generator.use_case.regenerate_timetable.RegenerateTimetableOutputBoundary;
import generator.use_case.remove_course.RemoveCourseInputBoundary;
import generator.use_case.remove_course.RemoveCourseInteractor;
import generator.use_case.remove_course.RemoveCourseOutputBoundary;
import generator.use_case.remove_degree.RemoveDegreeInputBoundary;
import generator.use_case.remove_degree.RemoveDegreeInteractor;
import generator.use_case.remove_degree.RemoveDegreeOutputBoundary;
import generator.use_case.return_to_prefs.ReturnToPrefsInputBoundary;
import generator.use_case.return_to_prefs.ReturnToPrefsInteractor;
import generator.use_case.return_to_prefs.ReturnToPrefsOutputBoundary;
import generator.view.*;
import generator.interface_adapter.save_timetable.SaveTimetableController;
import generator.interface_adapter.save_timetable.SaveTimetablePresenter;
import generator.interface_adapter.save_timetable.SaveTimetableViewModel;
import generator.use_case.save_timetable.SaveTimetableInputBoundary;
import generator.use_case.save_timetable.SaveTimetableInteractor;
import generator.use_case.save_timetable.SaveTimetableOutputBoundary;

import generator.interface_adapter.load_timetable.LoadTimetableController;
import generator.interface_adapter.load_timetable.LoadTimetablePresenter;
import generator.use_case.load_timetable.LoadTimetableInputBoundary;
import generator.use_case.load_timetable.LoadTimetableInteractor;
import generator.use_case.load_timetable.LoadTimetableOutputBoundary;

import generator.data_access.DistanceDataAccessObject;
import generator.use_case.sort_timetable.DistanceDataAccessInterface;
import generator.use_case.sort_timetable.SortTimetableInputBoundary;
import generator.use_case.sort_timetable.SortTimetableInteractor;

public class AppBuilder {
    private final int WIDTH = 1400;
    private final int HEIGHT = 750;
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("selectedPreferences.csv",
                    "artsci_timetable.json",
                    "Programs.json"
            );
    private final DistanceDataAccessInterface distanceDataAccessObject =
            new DistanceDataAccessObject();

    private SetPreferencesView setPreferencesView;
    private SetPreferencesViewModel setPreferencesViewModel;
    private DisplayTimetableView displayTimetableView;
    private DisplayTimetableViewModel displayTimetableViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSetPreferencesView() {
        setPreferencesViewModel = new SetPreferencesViewModel();
        setPreferencesView = new SetPreferencesView(setPreferencesViewModel);
        cardPanel.add(setPreferencesView, setPreferencesView.getViewName());
        return this;
    }

    public AppBuilder addDisplayTimetableView() {
        displayTimetableViewModel = new DisplayTimetableViewModel();
        displayTimetableView = new DisplayTimetableView(displayTimetableViewModel);
        cardPanel.add(displayTimetableView, displayTimetableView.getViewName());
        return this;
    }

    public AppBuilder addSetPreferencesUseCases() {
        final AddCourseOutputBoundary addCourseOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel, displayTimetableViewModel);
        final AddCourseInputBoundary addCourseInteractor = new AddCourseInteractor(userDataAccessObject,
                addCourseOutputBoundary);
        final RemoveCourseOutputBoundary removeCourseOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel, displayTimetableViewModel);
        final RemoveCourseInputBoundary removeCourseInteractor = new RemoveCourseInteractor(userDataAccessObject,
                removeCourseOutputBoundary);
        final AddDegreeOutputBoundary addDegreeOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel, displayTimetableViewModel);
        final AddDegreeInputBoundary addDegreeInteractor = new AddDegreeInteractor(userDataAccessObject,
                addDegreeOutputBoundary, userDataAccessObject, addCourseOutputBoundary);
        final RemoveDegreeOutputBoundary removeDegreeOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel, displayTimetableViewModel);
        final RemoveDegreeInputBoundary removeDegreeInteractor = new RemoveDegreeInteractor(userDataAccessObject,
                removeDegreeOutputBoundary);
        final GenerateTimetableOutputBoundary generateTimetableOutputBoundary =
                new SetPreferencesPresenter(viewManagerModel, setPreferencesViewModel, displayTimetableViewModel);

        final SortTimetableInputBoundary sortTimetableInteractor =
                new SortTimetableInteractor(userDataAccessObject, distanceDataAccessObject);

        final GenerateTimetableInputBoundary generateTimetableInteractor =
                new GenerateTimetableInteractor(userDataAccessObject,
                        generateTimetableOutputBoundary,
                        sortTimetableInteractor);

        final LoadTimetableOutputBoundary loadTimetableOutputBoundary =
                new LoadTimetablePresenter(displayTimetableViewModel, viewManagerModel);
        final LoadTimetableInputBoundary loadTimetableInteractor =
                new LoadTimetableInteractor(loadTimetableOutputBoundary);
        final LoadTimetableController loadTimetableController =
                new LoadTimetableController(loadTimetableInteractor);

        // UPDATED: Pass setPreferencesViewModel as last parameter
        final SetPreferencesController setPreferencesController = new SetPreferencesController(addCourseInteractor,
                removeCourseInteractor, addDegreeInteractor, removeDegreeInteractor, generateTimetableInteractor,
                setPreferencesViewModel);
        setPreferencesView.setSetPreferencesController(setPreferencesController);
        setPreferencesView.setLoadTimetableController(loadTimetableController);

        return this;
    }

    public AppBuilder addDisplayTimetableUseCases() {
        final GenerateTimetableOutputBoundary generateTimetableOutputBoundary =
                new SetPreferencesPresenter(viewManagerModel, setPreferencesViewModel, displayTimetableViewModel);
        final SortTimetableInputBoundary sortTimetableInteractor2 =
                new SortTimetableInteractor(userDataAccessObject, distanceDataAccessObject);

        final GenerateTimetableInputBoundary generateTimetableInteractor =
                new GenerateTimetableInteractor(userDataAccessObject,
                        generateTimetableOutputBoundary,
                        sortTimetableInteractor2);

        final ReturnToPrefsOutputBoundary returnToPrefsOutputBoundary = new DisplayTimetablePresenter(viewManagerModel,
                displayTimetableViewModel, setPreferencesViewModel);
        final ReturnToPrefsInputBoundary returnToPrefsInteractor =
                new ReturnToPrefsInteractor(returnToPrefsOutputBoundary);

        final RegenerateTimetableOutputBoundary regenerateTimetableOutputBoundary = new
                DisplayTimetablePresenter(viewManagerModel, displayTimetableViewModel, setPreferencesViewModel);
        final RegenerateTimetableInputBoundary regenerateTimetableInteractor = new
                RegenerateTimetableInteractor(regenerateTimetableOutputBoundary);

        final SaveTimetableViewModel saveTimetableViewModel = new SaveTimetableViewModel();
        final SaveTimetableOutputBoundary saveTimetableOutputBoundary =
                new SaveTimetablePresenter(saveTimetableViewModel);

        final SaveTimetableInputBoundary saveTimetableInteractor =
                new SaveTimetableInteractor(userDataAccessObject, saveTimetableOutputBoundary);

        final SaveTimetableController saveTimetableController =
                new SaveTimetableController(saveTimetableInteractor, displayTimetableViewModel);

        final DisplayTimetableController displayTimetableController = new
                DisplayTimetableController(generateTimetableInteractor, returnToPrefsInteractor,
                regenerateTimetableInteractor);

        displayTimetableView.setDisplayTimetableController(displayTimetableController);
        displayTimetableView.setSaveTimetableController(saveTimetableController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Timetable Builder, but better");
        application.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(setPreferencesView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
