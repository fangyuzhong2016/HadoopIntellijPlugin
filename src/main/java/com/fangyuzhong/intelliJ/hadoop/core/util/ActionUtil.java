package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ActionUtil
{
    public static final AnAction SEPARATOR = Separator.getInstance();

    public static ActionToolbar createActionToolbar(String place, boolean horizontal, String actionGroupName)
    {
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup actionGroup = (ActionGroup) actionManager.getAction(actionGroupName);
        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    public static ActionToolbar createActionToolbar(String place, boolean horizontal, ActionGroup actionGroup)
    {
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    public static ActionToolbar createActionToolbar(String place, boolean horizontal, AnAction... actions)
    {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (AnAction action : actions)
        {
            if (action == SEPARATOR)
            {
                actionGroup.addSeparator();
            } else
            {
                actionGroup.add(action);
            }
        }
        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    @Nullable
    public static Project getProject(AnActionEvent e)
    {
        return (Project) e.getData(PlatformDataKeys.PROJECT);
    }

    /**
     * @deprecated
     */
    public static Project getProject()
    {
        DataContext dataContext = (DataContext) DataManager.getInstance().getDataContextFromFocus().getResult();
        return (Project) PlatformDataKeys.PROJECT.getData(dataContext);
    }

    public static Project getProject(Component component)
    {
        return (Project) PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(component));
    }

    public static void registerDataProvider(JComponent component, DataProviderSupplier dataProviderSupplier)
    {
        DataProvider dataProvider = dataProviderSupplier.getDataProvider();
        if (dataProvider != null)
        {
            DataManager.registerDataProvider(component, dataProvider);
        }
    }

    public static  boolean  TestHdfsConnect(Project project,ConnectionHandler connectionHandler)
    {
        boolean isSuccessConnection=false;
        String messgae = connectionHandler.getConnectionInfo().toString()+ LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.TESTCONNECTIONERRORINFOR);
        try
        {
            isSuccessConnection = connectionHandler.createTestConnection();
        }
        catch (IOException ex)
        {
            isSuccessConnection=false;
            messgae=messgae+System.getProperty("line.separator")+ex.getMessage();
        }
        if(!isSuccessConnection)
        {
            MessageUtil.showWarningDialog(project,
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE)
                    ,messgae);
            connectionHandler.disconnect();
        }
        return isSuccessConnection;
    }
}
