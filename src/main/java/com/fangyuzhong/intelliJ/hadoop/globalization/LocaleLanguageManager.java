package com.fangyuzhong.intelliJ.hadoop.globalization;

import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectLifecycleListener;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 定义本地化语言管理类
 * Created by fangyuzhong on 17-7-29.
 */
public class LocaleLanguageManager  implements ApplicationComponent
{

    ResourceBundle resourceBundle=null;

    /**
     * 获取本地化语言管理
     * @return
     */
    public static LocaleLanguageManager getInstance()
    {
        return  ApplicationManager.getApplication().getComponent(LocaleLanguageManager.class);
    }


    /**
     * 获取本地化资源对象
     * @return
     */
    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }


    /**
     * 设置本地化资源
     * @param locale
     */
    public void setResourceBundle(Locale locale)
    {
        resourceBundle = ResourceBundle.getBundle("HadoopNavigator", locale);
    }

    /**
     * 组件初始化 获取系统默认的语言
     */
    public void initComponent()
    {
        //获取当前操作系统的语言环境，默认加载
        Locale localeDefault = Locale.getDefault();
        setResourceBundle(localeDefault);
        EventUtil.subscribe(null, ProjectLifecycleListener.TOPIC, this.projectLifecycleListener);
    }

    public void disposeComponent()
    {
    }

    @NotNull
    public String getComponentName()
    {
        return  "HadoopNavigator.LocaleLanguageManager";
    }


    /**
     * 语言改变监听事件处理
     */
    private ProjectLifecycleListener projectLifecycleListener = new ProjectLifecycleListener.Adapter()
    {
        Project project;
        public void projectComponentsInitialized(@NotNull Project project)
        {
            this.project=project;
            EventUtil.subscribe(project,null, LanguageSettingsListener.TOPIC, this.languageSettingsListener);
        }

        private LanguageSettingsListener languageSettingsListener = new LanguageSettingsListener()
        {
            @Override
            public void LanguageChanged(Locale locale)
            {
                resourceBundle = ResourceBundle.getBundle("HadoopNavigator", locale);
                final ConnectionBundle connectionBundle = ConnectionManager.getInstance(project).getConnectionBundle();
                for(ConnectionHandler connectionHandler :connectionBundle.getConnectionHandlers())
                {
                    if (connectionHandler != null)
                    {
                        connectionHandler.getObjectBundle().refreshTreeChildren();
                    }
                }
                //通知相关组件，需要调整界面语言
                EventUtil.notify(project, UpdateLanguageListener.TOPIC).UpdateLanguage();
            }
        };
    };
}
