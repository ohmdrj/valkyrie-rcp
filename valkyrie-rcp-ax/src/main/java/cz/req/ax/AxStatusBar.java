package cz.req.ax;

import cz.req.ax.view.AxView;
import cz.req.ax.view.AxWidgetView;
import cz.req.ax.widget.AxWidget;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.support.DefaultStatusBar;
import org.valkyriercp.application.support.StatusBarProgressMonitor;
import org.valkyriercp.widget.Widget;

import javax.swing.*;

/**
 * AxStatusBar
 *
 * @author Ondrej Burianek
 */
public class AxStatusBar extends DefaultStatusBar {
    public AxStatusBar() {
        System.out.println("cz.req.ax.AxStatusBar.AxStatusBar");
    }

    @Override
    protected JComponent createControl() {
        JComponent control = super.createControl();
        control.setBorder(null);
        return control;
    }

    @Override
    protected StatusBarProgressMonitor createStatusBarProgressMonitor() {
        return new AxProgressMonitor();
    }

    public void setLocked(boolean locked) {
        try {
            AxApplicationWindow window = (AxApplicationWindow) AxApp.applicationConfig().windowManager().getActiveWindow();
            PageComponent comp1 = window.getPage().getActiveComponent();
            if (comp1 instanceof AxWidgetView) {
                Widget widget = ((AxWidgetView) comp1).getWidget();
                if (widget instanceof AxWidget) {
                    ((AxWidget) widget).setLocked(locked);
                }
            } else if (comp1 instanceof AxView) {
                PageComponentContext comp2 = ((AxView) comp1).getContext();
                System.out.println("comp2 = " + comp2);
                //((AxWidget) comp2).setLocked(locked);
            } else {
                window.setLocked(locked);
            }
        } catch (Exception ex) {
            System.err.println("Cannot lock/unlock application window: " + ex.getMessage());
        }
    }

    public class AxProgressMonitor extends StatusBarProgressMonitor {

        @Override
        protected JProgressBar createProgressBar() {
            JProgressBar progressBar = super.createProgressBar();
            progressBar.setBorderPainted(false);
            return progressBar;
        }

        @Override
        public void taskStarted(String name, int totalWork) {
            setLocked(true);
            super.taskStarted(name, totalWork);
        }

        @Override
        public void done() {
            super.done();
            setLocked(false);
        }

    }
}
