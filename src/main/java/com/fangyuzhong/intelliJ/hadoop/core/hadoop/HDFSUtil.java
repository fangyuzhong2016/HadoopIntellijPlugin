package com.fangyuzhong.intelliJ.hadoop.core.hadoop;
import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.util.FormatUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.AclStatus;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import static org.apache.hadoop.fs.FileSystem.getLocal;
import static org.apache.hadoop.io.IOUtils.closeStream;

/**
 * HDFS文件操作工具类
 * Created by fangyuzhong on 17-7-27.
 */
public class HDFSUtil
{


    /*
     定义文件读取的缓冲区长度
     */
    private static final int BUFFERSIZE=4096;

    /**
     * 判断目录是否存在
     * @param strPath 目录路径
     * @param fileSystem 文件系统
     * @return 存在返回true，否则返回false
     */
    public static boolean dirExists(String strPath,FileSystem fileSystem)
    {
        try
        {
            return fileSystem.getFileStatus(new Path(strPath)).isDirectory();
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    /**
     * 判断文件是否存在
     * @param strPath 文件路径
     * @param fileSystem 文件系统
     * @return 存在返回true，否则返回false
     */
    public static boolean fileExists(String strPath,FileSystem fileSystem)
    {
        try
        {
            return fileSystem.getFileStatus(new Path(strPath)).isFile();
        }
        catch (Exception ex)
        {
            return false;
        }
    }


    /**
     *获取指定目录或者文件的ACL权限对象
     * @param fileSystem
     * @param strDirPath
     * @param strUserName
     * @return
     */
    public static FsAction getDirFileActionByUser(FileSystem fileSystem,String strDirPath,String strUserName)
    {
        Path path = new Path(strDirPath);
        try
        {
            AclStatus aclStatus= fileSystem.getAclStatus(path);
            if(aclStatus.getOwner().equals(strUserName))
            {
               return aclStatus.getPermission().getUserAction();
            }
            List<AclEntry> aclEntries= aclStatus.getEntries();
            for(AclEntry aclEntry:aclEntries)
            {
                if(aclEntry.getType()== AclEntryType.USER)
                {
                    if(aclEntry.getName().equals(strUserName))
                    {
                       return aclEntry.getPermission();
                    }
                }
            }
            return  aclStatus.getPermission().getOtherAction();
        }
        catch (IOException ex)
        {
            //如果出现异常，或许是ACL权限没有配置。默认给出所有权限
            return  FsAction.ALL;
           // return  null;
        }
    }

    /**
     * 获取文件系统的相关信息
     *
     * @param fileStatus
     * @return
     */
    public synchronized static Map<String, String> getFileSystemInformation(FileStatus fileStatus)
    {
        Map<String, String> fileSystemInformations = new TreeMap();
        ResourceBundle resourceBundle = LocaleLanguageManager.getInstance().getResourceBundle();
        if (fileStatus.isDirectory())
        {
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYPATH), fileStatus.getPath().toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYOWNER), fileStatus.getOwner());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYPERMISSION), fileStatus.getPermission().toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYGROUP), fileStatus.getGroup());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYMODIFICATIONTIME), new Timestamp(fileStatus.getModificationTime()).toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.DIRECTORYACCESSTIME), new Timestamp(fileStatus.getAccessTime()).toString());
        } else
        {
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEPATH), fileStatus.getPath().toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILELEN), FormatUtil.sizeFormatNum2String(fileStatus.getLen()));
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEREPLICATION), new Short(fileStatus.getReplication()).toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEBLOCKSIZE), FormatUtil.sizeFormatNum2String(fileStatus.getBlockSize()));
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEOWNER), fileStatus.getOwner());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEGROUP), fileStatus.getGroup());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEPERMISSION), fileStatus.getPermission().toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEMODIFICATIONTIME), new Timestamp(fileStatus.getModificationTime()).toString());
            fileSystemInformations.put(resourceBundle.getString(LanguageKeyWord.FILEACCESSTIME), new Timestamp(fileStatus.getAccessTime()).toString());

        }
        return fileSystemInformations;
    }


    /**
     * @param strhdfspath       HDFS中选中的单个文件路径
     * @param strLocalPath      下载要保存到本地的文件路径
     * @param fileSystem        HDFS文件系统对象
     * @param progressIndicator IDEA进度对象
     * @return 是否下载成功
     * @throws IOException
     */
    @Deprecated
    public synchronized static boolean copyFile(String strhdfspath, String strLocalPath, FileSystem fileSystem,
                                                ProgressIndicator progressIndicator) throws IOException
    {
        OutputStream out = null;
        InputStream in = null;
        try
        {
            progressIndicator.setIndeterminate(false);//确定进度
            String fileName = strhdfspath.trim().substring(strhdfspath.lastIndexOf("/") + 1);
            //事先获取文件的大小
            long fileSize = fileSystem.getFileStatus(new Path(strhdfspath)).getLen() / 1024L;
            int pageSize = (int) Math.ceil(fileSize / 100.00);//进度条进度设置100
            in = fileSystem.open(new Path(strhdfspath), 0);
            out = new FileOutputStream(strLocalPath + "/" + fileName);
            PrintStream ps = out instanceof PrintStream ? (PrintStream) out : null;
            byte[] buf = new byte[HDFSUtil.BUFFERSIZE];
            long count = 0;
            int progressCount = 0;
            for (int bytesRead = in.read(buf); bytesRead >= 0; bytesRead = in.read(buf))
            {
                count++;
                if (count % pageSize == 0)
                {
                    progressCount++;
                    progressIndicator.setIndeterminate(false);
                    progressIndicator.setFraction(progressCount * 0.1);
                }
                out.write(buf, 0, bytesRead);
                if (ps != null && ps.checkError())
                {
                    throw new IOException("Unable to write to output stream.");
                }
            }
            if (true)
            {
                out.close();
                out = null;
                in.close();
                in = null;
            }
        } finally
        {
            if (true)
            {
                closeStream(out);
                closeStream(in);
            }
        }
        return true;
    }


    /**
     * 下载单个文件
     *
     * @param strhdfspath       HDFS中选中的单个文件路径
     * @param strLocalPath      下载要保存到本地的文件路径
     * @param fileSystem        HDFS文件系统对象
     * @param progressIndicator IDEA进度对象
     * @param project           IDEA工程
     */
    @Deprecated
    public synchronized static void copyFile(String strhdfspath, String strLocalPath, FileSystem fileSystem,
                                             ProgressIndicator progressIndicator, Project project)
    {
        boolean isSuccess = false;
        try
        {
            isSuccess = copyFile(strhdfspath, strLocalPath, fileSystem, progressIndicator);

        } catch (Exception ex)
        {
            isSuccess = false;

        }
        if (!isSuccess)
        {
            MessageUtil.showErrorDialog(project, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNFAILED));
        } else
        {
            MessageUtil.showInfoDialog(project, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNSUCCESS));

        }
    }


    /**
     * 将文件或目录拷贝到本地系统(即下载目录或文件)
     * @param src 远程的文件或者目录
     * @param dst 本地的文件或者目录
     * @param conf 系统配置
     * @param overwrite 是否覆盖
     * @param srcFileSystem 源系统对象
     * @param progressIndicator 进度对象
     *
     */
    public synchronized static boolean  copyToLocalFile(String src,String dst,Configuration conf,boolean overwrite,
                                           FileSystem srcFileSystem,ProgressIndicator progressIndicator)
    {
        boolean isSuccess=false;
        try
        {
            FileSystem dstFileSystem = getLocal(conf);//获取目标(本地文件系统)
            FileStatus fileStatus = srcFileSystem.getFileStatus(new Path(src));
            isSuccess= copyFile(srcFileSystem,fileStatus,dstFileSystem,new Path(dst),
                    overwrite,progressIndicator,LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNPROGRESSTEXT));
        }
        catch (Exception ex)
        {
            LoggerFactory.createLogger(HDFSUtil.class).error("Error",ex);
        }
        return isSuccess;
    }



    /**
     * 将本地文件或者目录拷贝到目标系统中（HDFS）
     * @param src 源路径（本地路径）
     * @param dst 目标路径（HDFS路径）
     * @param conf 配置
     * @param overwrite 是否覆盖
     * @param dstFileSystem 目标文件系统（HDFS）
     * @param progressIndicator 进度对象
     * @return
     */
    public synchronized static boolean copyFromLocalFile(String src,String dst,Configuration conf,boolean overwrite,
                                                         FileSystem dstFileSystem,ProgressIndicator progressIndicator)
    {
        boolean isSuccess=false;
        try
        {
            FileSystem srcFileSystem = getLocal(conf);//获取本地选中的目录或者文件
            FileStatus fileStatus = srcFileSystem.getFileStatus(new Path(src));
            isSuccess= copyFile(srcFileSystem,fileStatus,dstFileSystem,new Path(dst),
                    overwrite,progressIndicator,LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPPROGRESSTEXT));
        }
        catch (Exception ex)
        {

        }
        return isSuccess;
    }


    /**
     *
     * @param srcFS
     * @param srcStatus
     * @param dstFS
     * @param dst
     * @param overwrite
     * @param progressIndicator
     * @param strProgressText
     * @return
     * @throws IOException
     */
    public synchronized static boolean copyFile(FileSystem srcFS, FileStatus srcStatus,
                               FileSystem dstFS, Path dst,
                               boolean overwrite,ProgressIndicator progressIndicator,String strProgressText) throws IOException
    {
        Path src = srcStatus.getPath();
        dst = checkDest(src.getName(), dstFS, dst, overwrite);
        if (srcStatus.isDirectory())
        {
            checkDependencies(srcFS, src, dstFS, dst);
            if (!dstFS.mkdirs(dst))
            {
                return false;
            }
            FileStatus contents[] = srcFS.listStatus(src);
            int fileCount= (int)Math.ceil(contents.length/100.00);
            int k=0;
            String dir = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPEDIRECTORY);
            for (int i = 0; i < contents.length; i++)
            {
                if(i%fileCount==0)
                {
                    k++;
                    progressIndicator.setIndeterminate(false);
                    progressIndicator.setFraction(k * 0.01);
                    String strMessage = String.format("%s：%s",strProgressText+dir,contents[i].getPath().getParent().toString());
                    progressIndicator.setText(strMessage);
                }
                copyFile(srcFS, contents[i], dstFS,
                        new Path(dst, contents[i].getPath().getName()), overwrite, progressIndicator,strProgressText);
            }
        } else
        {
            InputStream in = null;
            OutputStream out = null;
            try
            {
                in = srcFS.open(src);
                out = dstFS.create(dst, overwrite);
                PrintStream ps = out instanceof PrintStream ? (PrintStream) out : null;
                byte[] buf = new byte[HDFSUtil.BUFFERSIZE];
                long fileSize=srcStatus.getLen();
                int bufCount =(int) Math.ceil(fileSize / (HDFSUtil.BUFFERSIZE*1.00));//缓冲区个数
                int count =0;
                int showProgressCount= (int)Math.ceil(bufCount/100.00);//计算多少个缓冲区后，显示进度条的一个刻度
                int k=0;
                String strFile=LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPEFILE);
                for (int bytesRead = in.read(buf); bytesRead >= 0; bytesRead = in.read(buf))
                {
                    count++;
                    if (count % showProgressCount == 0)
                    {
                        k++;
                        progressIndicator.setIndeterminate(false);
                        progressIndicator.setFraction(k * 0.01);
                        progressIndicator.setText2(strProgressText+strFile+"("+k+"%)："+src.toString());
                    }
                    out.write(buf, 0, bytesRead);
                    if (ps != null && ps.checkError())
                    {
                        throw new IOException("Unable to write to output stream.");
                    }
                }
                out.close();
                out = null;
                in.close();
                in = null;
            } catch (IOException e)
            {
                IOUtils.closeStream(out);
                IOUtils.closeStream(in);
                throw e;
            }
        }
        return true;
    }


    /**
     *
     * @param srcName
     * @param dstFS
     * @param dst
     * @param overwrite
     * @return
     * @throws IOException
     */
    private synchronized static Path checkDest(String srcName, FileSystem dstFS, Path dst,
                                  boolean overwrite) throws IOException
    {
        FileStatus sdst;
        try
        {
            sdst = dstFS.getFileStatus(dst);
        } catch (FileNotFoundException e)
        {
            sdst = null;
        }
        if (null != sdst)
        {
            if (sdst.isDirectory())
            {
                if (null == srcName)
                {
                    throw new IOException("Target " + dst + " is a directory");
                }
                return checkDest(null, dstFS, new Path(dst, srcName), overwrite);
            } else if (!overwrite)
            {
                throw new IOException("Target " + dst + " already exists");
            }
        }
        return dst;
    }

    /**
     *
     * @param srcFS
     * @param src
     * @param dstFS
     * @param dst
     * @throws IOException
     */
    private synchronized static void checkDependencies(FileSystem srcFS, Path src,
                                          FileSystem dstFS, Path dst)
            throws IOException
    {
        if (srcFS == dstFS)
        {
            String srcq = src.makeQualified(srcFS).toString() + Path.SEPARATOR;
            String dstq = dst.makeQualified(dstFS).toString() + Path.SEPARATOR;
            if (dstq.startsWith(srcq))
            {
                if (srcq.length() == dstq.length())
                {
                    throw new IOException("Cannot copy " + src + " to itself.");
                } else
                {
                    throw new IOException("Cannot copy " + src + " to its subdirectory " +
                            dst);
                }
            }
        }
    }
}
