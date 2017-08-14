package com.fangyuzhong.intelliJ.hadoop.core.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.setting.SettingsUtil;
import org.jdom.Element;

/**
 * 配置工具类
 * Created by fangyuzhong on 17-7-21.
 */
public class ConfigurationUtil
        extends SettingsUtil
{
    /**
     * 写入配置的抽象方法
     * @param element XML元素
     * @param configuration 配置
     */
    public static void writeConfiguration(Element element, Configuration configuration)
    {
        String elementName = configuration.getConfigElementName();
        if (elementName != null)
        {
            Element childElement = new Element(elementName);
            element.addContent(childElement);
            configuration.writeConfiguration(childElement);
        }
    }

    /**
     * 读取配置的抽象方法
     * @param element XML元素
     * @param configuration 配置类
     */
    public static void readConfiguration(Element element, Configuration configuration)
    {
        String elementName = configuration.getConfigElementName();
        if (elementName != null)
        {
            Element childElement = element.getChild(elementName);
            if (childElement != null)
            {
                configuration.readConfiguration(childElement);
            }
        }
    }
}
