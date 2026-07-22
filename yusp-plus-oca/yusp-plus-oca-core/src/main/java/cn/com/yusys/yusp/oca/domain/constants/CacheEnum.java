package cn.com.yusys.yusp.oca.domain.constants;

/**
 * oca中缓存名称和key的枚举
 *
 * @author yusys
 */
public enum CacheEnum {

    /**
     * dict
     */
    DICT("dict","dictInfo"),

    /**
     * org
     */
    ORG("org","orgInfo"),

    /**
     * message
     */
    MSG("message","messageInfo");

    private String cacheName;

    private String key;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    CacheEnum(String cacheName, String key) {
        this.cacheName = cacheName;
        this.key = key;
    }
}
