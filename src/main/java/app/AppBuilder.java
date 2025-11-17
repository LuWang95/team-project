package app;

import Generator.DataAccess.FileUserDataAccessObject;
import Generator.InterfaceAdapter.*;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableController;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetablePresenter;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesController;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesPresenter;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;
import Generator.UseCase.add_course.AddCourseInputBoundary;
import Generator.UseCase.add_course.AddCourseInteractor;
import Generator.UseCase.add_course.AddCourseOutputBoundary;
import Generator.UseCase.generate_timetable.GenerateTimetableInputBoundary;
import Generator.UseCase.generate_timetable.GenerateTimetableInteractor;
import Generator.UseCase.generate_timetable.GenerateTimetableOutputBoundary;
import Generator.UseCase.remove_course.RemoveCourseInputBoundary;
import Generator.UseCase.remove_course.RemoveCourseInteractor;
import Generator.UseCase.remove_course.RemoveCourseOutputBoundary;
import Generator.UseCase.add_degree.AddDegreeInputBoundary;
import Generator.UseCase.add_degree.AddDegreeInteractor;
import Generator.UseCase.add_degree.AddDegreeOutputBoundary;
import Generator.UseCase.remove_degree.RemoveDegreeInputBoundary;
import Generator.UseCase.remove_degree.RemoveDegreeInteractor;
import Generator.UseCase.remove_degree.RemoveDegreeOutputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsInputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsInteractor;
import Generator.UseCase.return_to_prefs.ReturnToPrefsOutputBoundary;
import Generator.View.*;


import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("selectedPreferences.csv", "artsci_timetable.json");

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
                addDegreeOutputBoundary);
        final RemoveDegreeOutputBoundary removeDegreeOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel, displayTimetableViewModel);
        final RemoveDegreeInputBoundary removeDegreeInteractor = new RemoveDegreeInteractor(userDataAccessObject,
                removeDegreeOutputBoundary);
        final GenerateTimetableOutputBoundary generateTimetableOutputBoundary
                = new SetPreferencesPresenter(viewManagerModel, setPreferencesViewModel, displayTimetableViewModel);
        final GenerateTimetableInputBoundary generateTimetableInteractor
                = new GenerateTimetableInteractor(userDataAccessObject, generateTimetableOutputBoundary);
        SetPreferencesController setPreferencesController = new SetPreferencesController(addCourseInteractor,
                removeCourseInteractor, addDegreeInteractor, removeDegreeInteractor, generateTimetableInteractor);
        setPreferencesView.setSetPreferencesController(setPreferencesController);
        return this;
    }

    public AppBuilder addDisplayTimetableUseCases() {
        final GenerateTimetableOutputBoundary generateTimetableOutputBoundary
                = new SetPreferencesPresenter(viewManagerModel, setPreferencesViewModel, displayTimetableViewModel);
        final GenerateTimetableInputBoundary generateTimetableInteractor
                = new GenerateTimetableInteractor(userDataAccessObject, generateTimetableOutputBoundary);
        final ReturnToPrefsOutputBoundary returnToPrefsOutputBoundary = new DisplayTimetablePresenter(viewManagerModel,
                displayTimetableViewModel, setPreferencesViewModel);
        final ReturnToPrefsInputBoundary returnToPrefsInteractor
                = new ReturnToPrefsInteractor(returnToPrefsOutputBoundary);
        DisplayTimetableController displayTimetableController
                = new DisplayTimetableController(generateTimetableInteractor, returnToPrefsInteractor);
        displayTimetableView.setDisplayTimetableController(displayTimetableController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Timetable Builder");
        application.setMinimumSize(new Dimension(600, 400));
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(setPreferencesView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
