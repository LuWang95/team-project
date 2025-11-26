package Generator.View;

import Generator.InterfaceAdapter.ViewManagerModel;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Handles switching between multiple views (panels) with CardLayout.
 * Listens to ViewManagerModel for view change events.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            String viewName = (String) evt.getNewValue();
            switchToView(viewName);
        }
    }

    /**
     * Switches the visible card to the view with the given name.
     * Ready for easy extension (animations, hooks, etc.).
     */
    public void switchToView(String viewName) {
        cardLayout.show(views, viewName);
        // Additional extensible features (e.g., hooks, transitions) could go here.
    }
}
