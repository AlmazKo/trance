package ru.alexlen.trance;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Almazko
 */
public class EditorComponent implements ProjectComponent {

    private Project project;

    public EditorComponent(Project project) {
        this.project = project;
    }

    public void initComponent()
    {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent()
    {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName()
    {
        return "Editor";
    }

    public void projectOpened()
    {

        // attach toolbar popup (right bottom)
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(this.project);
        if(statusBar == null) {
            return;
        }

//        // clean bar on project open; we can have multiple projects att some time
//        if(statusBar.getWidget(SymfonyProfilerWidget.ID) != null) {
//            statusBar.removeWidget(SymfonyProfilerWidget.ID);
//        }
//
//        if(isEnabled()) {
//            SymfonyProfilerWidget symfonyProfilerWidget = new SymfonyProfilerWidget(this.project);
//            statusBar.addWidget(symfonyProfilerWidget);
//        }
    }

    public void projectClosed()
    {
        // called when project is being closed
    }
}
