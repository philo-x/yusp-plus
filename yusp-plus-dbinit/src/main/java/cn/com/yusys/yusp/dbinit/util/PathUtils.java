package cn.com.yusys.yusp.dbinit.util;

import java.io.File;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/8 15:22
 */
public class PathUtils {

    /**
     * 获取jar包的上级目录
     * @return 获取jar包的上级目录
     * @author zhanyq
     * @date 2021-09-08 15:52
     */
    public static String getParentPath() {
        String curPath = System.getProperty("user.dir");
        String parent = curPath.substring(0, curPath.lastIndexOf(File.separator));
        return parent + File.separator ;
    }

    /**
     * 获取jar包目录
     * @return 获取jar包目录
     * @author zhanyq
     * @date 2021-09-08 15:52
     */
    public static String getCurrentPath(){
        String curPath = System.getProperty("user.dir");
        return curPath + File.separator;
    }

}
