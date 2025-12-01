package app;

import java.awt.*;

import javax.swing.*;

import generator.dataaccess.FileUserDataAccessObject;
import generator.interfaceadapter.*;
import generator.interfaceadapter.display_timetable.DisplayTimetableController;
import generator.interfaceadapter.display_timetable.DisplayTimetablePresenter;
import generator.interfaceadapter.display_timetable.DisplayTimetableViewModel;
import generator.interfaceadapter.set_preferences.SetPreferencesController;
import generator.interfaceadapter.set_preferences.SetPreferencesPresenter;
import generator.interfaceadapter.set_preferences.SetPreferencesViewModel;
import generator.usecase.add_course.AddCourseInputBoundary;
import generator.usecase.add_course.AddCourseInteractor;
import generator.usecase.add_course.AddCourseOutputBoundary;
import generator.usecase.add_degree.AddDegreeInputBoundary;
import generator.usecase.add_degree.AddDegreeInteractor;
import generator.usecase.add_degree.AddDegreeOutputBoundary;
import generator.usecase.generate_timetable.GenerateTimetableInputBoundary;
import generator.usecase.generate_timetable.GenerateTimetableInteractor;
import generator.usecase.generate_timetable.GenerateTimetableOutputBoundary;
import generator.usecase.regenerate_timetable.RegenerateTimetableInputBoundary;
import generator.usecase.regenerate_timetable.RegenerateTimetableInteractor;
import generator.usecase.regenerate_timetable.RegenerateTimetableOutputBoundary;
import generator.usecase.remove_course.RemoveCourseInputBoundary;
import generator.usecase.remove_course.RemoveCourseInteractor;
import generator.usecase.remove_course.RemoveCourseOutputBoundary;
import generator.usecase.remove_degree.RemoveDegreeInputBoundary;
import generator.usecase.remove_degree.RemoveDegreeInteractor;
import generator.usecase.remove_degree.RemoveDegreeOutputBoundary;
import generator.usecase.return_to_prefs.ReturnToPrefsInputBoundary;
import generator.usecase.return_to_prefs.ReturnToPrefsInteractor;
import generator.usecase.return_to_prefs.ReturnToPrefsOutputBoundary;
import generator.view.*;
import generator.interfaceadapter.save_timetable.SaveTimetableController;
import generator.interfaceadapter.save_timetable.SaveTimetablePresenter;
import generator.interfaceadapter.save_timetable.SaveTimetableViewModel;

import generator.usecase.save_timetable.SaveTimetableInputBoundary;
import generator.usecase.save_timetable.SaveTimetableInteractor;
import generator.usecase.save_timetable.SaveTimetableOutputBoundary;

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
        final GenerateTimetableInputBoundary generateTimetableInteractor =
                new GenerateTimetableInteractor(userDataAccessObject, generateTimetableOutputBoundary);
        final SetPreferencesController setPreferencesController = new SetPreferencesController(addCourseInteractor,
                removeCourseInteractor, addDegreeInteractor, removeDegreeInteractor, generateTimetableInteractor);
        setPreferencesView.setSetPreferencesController(setPreferencesController);
        return this;
    }

    public AppBuilder addDisplayTimetableUseCases() {
        // Existing generate timetable wiring
        final GenerateTimetableOutputBoundary generateTimetableOutputBoundary =
                new SetPreferencesPresenter(viewManagerModel, setPreferencesViewModel, displayTimetableViewModel);
        final GenerateTimetableInputBoundary generateTimetableInteractor =
                new GenerateTimetableInteractor(userDataAccessObject, generateTimetableOutputBoundary);

        // Existing return-to-prefs wiring
        final ReturnToPrefsOutputBoundary returnToPrefsOutputBoundary = new DisplayTimetablePresenter(viewManagerModel,
                displayTimetableViewModel, setPreferencesViewModel);
        final ReturnToPrefsInputBoundary returnToPrefsInteractor =
                new ReturnToPrefsInteractor(returnToPrefsOutputBoundary);

        // Existing regenerate wiring
        final RegenerateTimetableOutputBoundary regenerateTimetableOutputBoundary = new
                DisplayTimetablePresenter(viewManagerModel, displayTimetableViewModel, setPreferencesViewModel);
        final RegenerateTimetableInputBoundary regenerateTimetableInteractor = new
                RegenerateTimetableInteractor(regenerateTimetableOutputBoundary);

        final SaveTimetableViewModel saveTimetableViewModel = new SaveTimetableViewModel();
        final SaveTimetableOutputBoundary saveTimetableOutputBoundary =
                new SaveTimetablePresenter(saveTimetableViewModel);

        // NOTE: userDataAccessObject must implement SaveTimetableDataAccessInterface
        final SaveTimetableInputBoundary saveTimetableInteractor =
                new SaveTimetableInteractor(userDataAccessObject, saveTimetableOutputBoundary);

        final SaveTimetableController saveTimetableController =
                new SaveTimetableController(saveTimetableInteractor, displayTimetableViewModel);

        // Existing display timetable controller
        final DisplayTimetableController displayTimetableController = new
                DisplayTimetableController(generateTimetableInteractor, returnToPrefsInteractor,
                regenerateTimetableInteractor);

        displayTimetableView.setDisplayTimetableController(displayTimetableController);
        // NEW: give the view the save controller as well
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
