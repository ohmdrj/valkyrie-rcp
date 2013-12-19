package cz.req.ax.dialog;

import org.jdesktop.swingx.JXFrame;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.WindowManager;
import org.valkyriercp.util.DialogFactory;

/**
 * AxDialogFactory
 *
 * @author Ondrej Burianek
 */
@Component
public class AxDialogFactory extends DialogFactory {

    public JXFrame getParentWindow() {
        WindowManager windowManager = applicationConfig.windowManager();
        if (windowManager == null || windowManager.getActiveWindow() == null) {
            return null;
        }
        return windowManager.getActiveWindow().getControl();
    }

    public void showMessageDialog(String id) {
        super.showMessageDialog(getParentWindow(), id, null, OK_OPTION);
    }

    public void showWarningDialog(String id) {
        super.showWarningDialog(getParentWindow(), id, null, OK_OPTION);
    }
}
