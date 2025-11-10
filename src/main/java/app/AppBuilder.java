package app;

import Generator.InterfaceAdapter.*;
import Generator.InterfaceAdapter.add_course.AddCourseViewModel;
import Generator.View.*;


import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private AddCourseView addCourseView;
    private AddCourseViewModel addCourseViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addAddCourseView() {
        addCourseViewModel = new AddCourseViewModel();
        addCourseView = new AddCourseView(addCourseViewModel);
        cardPanel.add(addCourseView, addCourseView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("sdjkfsjdkdjfksjkdfk");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(addCourseView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
