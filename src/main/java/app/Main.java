package app;

import javax.swing.*;

public class Main {
    /**
     * Builds the Java Swing Panel.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addSetPreferencesView()
                .addDisplayTimetableView()
                .addSetPreferencesUseCases()
                .addDisplayTimetableUseCases()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

