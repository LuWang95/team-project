package app;

import Generator.DataAccess.FileUserDataAccessObject;
import Generator.InterfaceAdapter.*;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesController;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesPresenter;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;
import Generator.UseCase.add_course.AddCourseInputBoundary;
import Generator.UseCase.add_course.AddCourseInteractor;
import Generator.UseCase.add_course.AddCourseOutputBoundary;
import Generator.UseCase.remove_course.RemoveCourseInputBoundary;
import Generator.UseCase.remove_course.RemoveCourseInteractor;
import Generator.UseCase.remove_course.RemoveCourseOutputBoundary;
import Generator.View.*;


import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("selectedCourses.csv");

    private SetPreferencesView setPreferencesView;
    private SetPreferencesViewModel setPreferencesViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSetPreferencesView() {
        setPreferencesViewModel = new SetPreferencesViewModel();
        setPreferencesView = new SetPreferencesView(setPreferencesViewModel);
        cardPanel.add(setPreferencesView, setPreferencesView.getViewName());
        return this;
    }

    public AppBuilder addAddCourseUseCase() {
        final AddCourseOutputBoundary addCourseOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel);
        final AddCourseInputBoundary addCourseInteractor = new AddCourseInteractor(userDataAccessObject,
                addCourseOutputBoundary);
        final RemoveCourseOutputBoundary removeCourseOutputBoundary = new SetPreferencesPresenter(viewManagerModel,
                setPreferencesViewModel);
        final RemoveCourseInputBoundary removeCourseInteractor = new RemoveCourseInteractor(userDataAccessObject,
                removeCourseOutputBoundary);
        SetPreferencesController setPreferencesController = new SetPreferencesController(addCourseInteractor,
                removeCourseInteractor);
        setPreferencesView.setSetPreferencesController(setPreferencesController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("sdjkfsjdkdjfksjkdfk");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(setPreferencesView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
