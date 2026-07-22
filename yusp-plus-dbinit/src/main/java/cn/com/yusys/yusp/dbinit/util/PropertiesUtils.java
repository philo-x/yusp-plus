package cn.com.yusys.yusp.dbinit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.*;
import java.util.Properties;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/3 16:59
 */
public class PropertiesUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);
    private static Properties property = new Properties();

    static {
        try {
            InputStream in = getOuterConf();

            if(null == in){
                in = getCurrentConf();
            }
            if(null == in){
                in = getCurrent();
            }
            if (null == in) {
                in = PropertyUtils.class.getResourceAsStream("/dbinit.properties");
            }
            property.load(in);
        } catch (IOException e) {
            log.error("read properties file error,", e);
        }
    }
    /**
     * 根据参数名称获取参数值
     * @param key 参数名
     * @return 参数值
     * @author zhanyq
     * @date 2021-09-08 15:47
     */
    public static String get(String key) {
        return property.getProperty(key);
    }

    /**
     * 获取外部的dbinit.properties文件流
     * @return 返回外部的dbinit.properties文件流
     * @author zhanyq
     * @date 2021-09-08 15:47
     */
    private static InputStream getOuterConf() {
        String parentPath = PathUtils.getParentPath();
        try {
            return new FileInputStream(parentPath + "conf" + File.separator + "dbinit.properties");
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * 获取jar包同级conf下的dbinit.properties文件流
     * @return 返回jar包同级conf下的dbinit.properties文件流
     * @author zhanyq
     * @date 2021-09-08 15:47
     */
    private static InputStream getCurrentConf() {
        String currentPath = PathUtils.getCurrentPath();
        try {
            return new FileInputStream(currentPath + "conf" + File.separator + "dbinit.properties");
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * 获取jar包同级的dbinit.properties文件流
     * @return 返回jar包同级的dbinit.properties文件流
     * @author zhanyq
     * @date 2021-09-08 15:47
     */
    private static InputStream getCurrent() {
        String currentPath = PathUtils.getCurrentPath();
        try {
            return new FileInputStream(currentPath + "dbinit.properties");
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
