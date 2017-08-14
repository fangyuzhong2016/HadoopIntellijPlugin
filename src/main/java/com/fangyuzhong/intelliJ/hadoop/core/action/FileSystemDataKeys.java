package com.fangyuzhong.intelliJ.hadoop.core.action;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.util.Key;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public interface FileSystemDataKeys
{
     static final DataKey<ConnectionBundleSettingsForm> CONNECTION_BUNDLE_SETTINGS
             = DataKey.create("HadoopNavigator.ConnectionSettingsEditor");
     static final Key<String> ACTION_PLACE_KEY = Key.create("HadoopNavigator.ActionPlace");
     static final Key<Boolean> PROJECT_SETTINGS_LOADED_KEY
             = Key.create("HadoopNavigator.ProjectSettingsLoaded");
}
