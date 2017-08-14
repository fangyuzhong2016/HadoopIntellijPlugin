
# IntelliJ plugin for Hadoop

Hadoop-Intellij-Plugin is a plugin on Intellij IDEA. Implemented access and related operations on the Hadoop file system on Intellij IDEA. This includes reading the list of files on the Hadoop file system for display, creating directories in the Hadoop file system, deleting directories, downloading or uploading files, viewing file contents, running Job jobs, supporting international language settings, and so on. Similar plugin with hadoop-eclipse-plugin. Intellij-Plugin-Hadoop interface is roughly as follows:

![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/1.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/2.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/3.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/4.jpg)

## Feature
- 1, simple. The plugin is easy to use and supports Window and Linux.
- 2, can support the configuration of multiple Hadoop file system access.
- 3, support multi-language switching (currently only supports English and Chinese, such as the need to support other languages, the need to make language packs).

## How to compile and install ？
- 1、Source code:  
Get the latest source code from https://github.com/fangyuzhong2016/HadoopIntellijPlugin.
- 2、Source compile   
①, The current Intellij plugin for hadoop source code using maven to compile and package, so before compiling to ensure that the installation of JDK1.8 and maven3 or above    
②, Intellij plugin for hadoop plug-in based on IntelliJ IDEA Ultimate 2017.2 version of the development, so need to install IntelliJ IDEA Ultimate 2017 or later  
③, Ready Hadoop related jar package (compilation step is not necessary)    
④, Enter the source directory ../HadoopIntellijPlugin/ modify the pom.xml file, the main modified hadoop version and IntelliJ IDEA installation path, set as follows:  
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/5.jpg)
⑤, the implementation of mvn order:  
First execute   
mvn clean  
Then execute   
mvn assembly:assembly    
After the completion of the compiler in the ... / target / HadoopIntellijPlugin-1.0.zip that is the plug-in installation package, and then installed to IntelliJ

## 3、Intellij plugin for hadoop development environment settings
   As the current use of Maven management Intellij plug-in development more difficult, so if you want to use Intellij modify Intellij plugin for hadoop plug-in code, directly based on the source code to create the Plugin project is not possible, according to IDEA plug-in development model for code engineering settings The
   First create a plugin project in Intellij IDEA, such as HadoopIntellijPlugin, directory organization:
   
 ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/6.jpg)
    Then, the source directory in the ../src/main/java/com folder, copy the development of the plug-in project source ... / src folder, then the source directory .... / src / main / Java / resources folder to the development plug-in project source .... / resources directory, and in the directory, create a lib folder, the Hadoop related jar package, copy in. Finally, set the development of the plug-in project HadoopIntellijPlugin configuration.    
    ①, set the plug-in project expansion of the lib, the hadoop related jar package introduced:
    ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/7.png)
    ②、Plug some UI interface, the use of the IDEA GUI Design drag and drop the design, the interface element to save the corresponding class xml, the compilation process is used in the IDEA library for the compiler, not javac compiler, so in the compiler Process, set the GUI Designer source code generation, if you do not do this step in the development environment in the debug run, no problem, the entire UI interface code from the IDEA framework for dynamic generation insert, but packaged installation, will prompt interface The control failed to instantiate. Set the GUI Designer source code generation, in fact, those UI interface is generated xml file static Java code, insert the source file. In the IDEA settings, make the following settings:
     ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/8.png)
     
## Intellij plugin for hadoop plugin configuration and source code
- 1、 Plug-in source code, the source code organization is as follows:  
    ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/9.jpg)    
      ①, core package, the core package for the plug-in project, the public component library, including the generic UI interface, multi-threaded operation, Hadoop connection set base class, Hadoop file system general operation class, plug-in project settings generic class and other tools    
      ②, fsconnection package, Hadoop file system connection implementation class and connection related configuration implementation class     
      ③, fsobject package, file system object class implementation (for HDFS is the directory tree and file tree node organization to achieve)   
      ④, fsbrowser package, the main interface to achieve plug-ins, including the HDFS file system to read the relevant data display, file system object creation, download, delete, upload and other operations    
      ⑤, globalization package, plug-in multi-language support class⑥, options package, plugin set class⑦, mainmenu package, plug-in main menu operation class     
      
- 2、Plug-in configuration instructions
The plugin is configured in the / resources / directory, including HadoopNavigator_en_US.properties, HadoopNavigator_en_US.properties, plugin.xml.
The HadoopNavigator_en_US.properties file is the English language configuration for the plug-in interface
HadoopNavigator_en_US.properties file for the plug-in interface, the Chinese language configuration
The current plug-in interface language only supports Simplified Chinese and English, other languages, need to make their own language pack. The initial default language for the system is the default language for the operating system. Plugin.xml for the plugin's configuration file

![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/10.jpg)

## Contact information and related design documents
- 1, plugin source address: https://github.com/fangyuzhong2016/HadoopIntellijPlugin
- 2, plug-in related design: http://www.fangyuzhong.com
- 3, plug the author contact: QQ: 906328924
- 4, plug-in development QQ group: 421116017

![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/11.png)

# --------------------------------------------------------------------------------------------------------------------------------------

# IntelliJ plugin for Hadoop

Hadoop-Intellij-Plugin 是Intellij IDEA 上的一款插件。实现了在Intellij IDEA 上的Hadoop文件系统的访问和相关操作。该操作包括读取Hadoop文件系统上文件列表进行展示、可以在Hadoop文件系统中创建目录、删除目录；下载或者上传文件；查看文件内容；运行Job作业、支持国际化语言设置等等。类似与 hadoop-eclipse-plugin的插件。Intellij-Plugin-Hadoop 的界面大致如下：

![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/1.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/2.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/3.jpg)
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/4.jpg)

## 特性
- 1、简单。 该插件易于使用，支持Window和Linux。
- 2、可支持配置多个Hadoop文件系统的访问。
- 3、支持多语言切换(目前只支持英文和中文，如需要支持其他语言，需要制作语言包)。


## 编译和安装
- 1、源码获取。
       从 https://github.com/fangyuzhong2016/HadoopIntellijPlugin 获取最新的源码。

- 2、源码编译

    ①、目前 Intellij plugin for hadoop 的源码使用maven 进行编译和打包，因此在编译之前请确保安装JDK1.8和 maven3 以上版本
    ②、 Intellij plugin for hadoop 插件基于 IntelliJ IDEA  Ultimate 2017.2 版本进行开发的，因此需要安装  IntelliJ IDEA  Ultimate 2017 以上版本
    ③、准备好Hadoop的相关的jar包（编译步骤不是必须的）
    ④、进入源码目录  ../HadoopIntellijPlugin/   修改  pom.xml 文件，主要修改hadoop的版本和IntelliJ IDEA 安装的路径，设置如下：
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/5.jpg)
  ⑤、 执行mvn 命令：
  先执行
   
        mvn clean ，
    
    然后执行 
    
        mvn assembly:assembly 
    
    编译完成后 在.../target/ HadoopIntellijPlugin-1.0.zip   即为该插件的安装包，然后安装到 IntelliJ 中即可


- 3、Intellij plugin for hadoop 开发环境设置
  由于，目前使用Maven 管理Intellij 的插件开发比较困难，因此，如果要使用Intellij 修改 Intellij plugin for hadoop  插件代码，直接基于源码创建Plugin工程是不可以的，需要按照IDEA插件开发的模式进行代码工程的设置。
  首先在Intellij IDEA 中创建 一个插件项目，如HadoopIntellijPlugin ，目录组织方式：
 ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/6.jpg)
  
    然后，把源码目录中的   ../src/main/java/com  文件夹，拷贝到 开发的插件工程 源码 .../src文件夹下，再把  源码目录中的 ..../src/main/java/resources  文件夹拷贝到 开发插件工程源码 ..../resources  目录下，并且在该目录下，创建lib 文件夹，把Hadoop的相关jar包，拷贝进来。最后，设置开发的插件工程 HadoopIntellijPlugin配置。
    
    ①、设置插件工程扩展的lib，把hadoop 的相关jar 包引入：
     ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/7.png)
      
    ②、插件有些UI界面，使用的是IDEA的 GUI Design 进行拖拽设计的，界面元素保存对应类的xml 中，编译过程中使用的是 IDEA的库进行的编译的，并不是 javac 编译器，因此在编译过程中，设置 GUI Designer 的源码生成方式，如果不做这步的设置，在开发环境中进行 debug 运行，没有问题，整个UI界面代码由IDEA框架进行动态生成插入，但打包安装后，会提示界面控件未能实例化。设置 GUI Designer 的源码生成方式，其实就是将那些UI界面的xml 文件生成静态的 Java代码，插入源文件中。在IDEA设置中，进行如下设置：
     ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/8.png)


## Intellij plugin for hadoop 插件配置和源码的相关说明
- 1、 插件的源码说明，源码组织如下：
     ![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/9.jpg)


        ①、core 包，为插件项目的核心包，公共组件库，包括了 通用UI界面、多线程操作、Hadoop连接设置基类、Hadoop文件系统通用操作类、插件项目设置通用类和其他工具类
        ②、fsconnection 包，Hadoop文件系统连接实现类和连接相关配置实现类
        ③、fsobject 包，文件系统对象类的实现（对于HDFS来讲就是 目录树和文件树节点的组织方式的实现）
        ④、fsbrowser包，插件的主界面实现，包括读取HDFS文件系统相关数据进行展示、文件系统对象的创建、下载、删除、上传和其他一些操作
        ⑤、globalization包，插件多语言支持类
        ⑥、options 包，插件设置类
        ⑦、mainmenu包， 插件主菜单操作类
  
  
- 2、插件配置相关说明
插件配置在.../resources/目录下，包括HadoopNavigator_en_US.properties、HadoopNavigator_zh_CN.properties  、plugin.xml。
HadoopNavigator_en_US.properties  文件为插件界面的英文语言配置
HadoopNavigator_zh_CN.properties 文件为插件界面的中文语言配置
目前插件界面的语言只支持 简体中文和英文，其他的语言，需要自行制作语言包。系统初始默认的语言为操作系统默认的语言。plugin.xml 为插件的配置文件
![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/10.jpg)

## 联系方式和相关设计文档
- 1、插件源码地址： https://github.com/fangyuzhong2016/HadoopIntellijPlugin
- 2、插件相关设计： http://www.fangyuzhong.com
- 3、插件作者联系方式：QQ:906328924
- 4、插件开发QQ群：421116017

![](https://github.com/fangyuzhong2016/HadoopIntellijPlugin/blob/master/img-folder/11.png)








