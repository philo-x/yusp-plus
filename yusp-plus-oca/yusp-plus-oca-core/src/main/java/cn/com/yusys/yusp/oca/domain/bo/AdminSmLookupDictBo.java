package cn.com.yusys.yusp.oca.domain.bo;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 数据字典实体，修改保存使用
 *
 * @author xufy1@yusys.com.cn
 * @date 2021-01-15 18:39
 */

public class AdminSmLookupDictBo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 字典类别主键id
     */
    private String lookupItemId;

    /**
     * 父字典类别id
     */
    private String upLookupItemId;
    /**
     * 字典类别code码
     */
    @NotBlank(message = "lookupCode can not be empty!")
    private String lookupCode;
    /**
     * 字典类别名称
     */
    @NotBlank(message = "lookupName can not be empty!")
    private String lookupName;
    /**
     * 字典类别分类标识id
     */
    @NotBlank(message = "lookupTypeId can not be empty!")
    private String lookupTypeId;

    private String lookupTypeName;

    List<LookupItemBo> lookupItemBos;

    public AdminSmLookupDictBo() {
    }

    public String getLookupItemId() {
        return this.lookupItemId;
    }

    public String getUpLookupItemId() {
        return this.upLookupItemId;
    }

    public @NotBlank(message = "lookupCode can not be empty!") String getLookupCode() {
        return this.lookupCode;
    }

    public @NotBlank(message = "lookupName can not be empty!") String getLookupName() {
        return this.lookupName;
    }

    public @NotBlank(message = "lookupTypeId can not be empty!") String getLookupTypeId() {
        return this.lookupTypeId;
    }

    public String getLookupTypeName() {
        return this.lookupTypeName;
    }

    public List<LookupItemBo> getLookupItemBos() {
        return this.lookupItemBos;
    }

    public void setLookupItemId(String lookupItemId) {
        this.lookupItemId = lookupItemId;
    }

    public void setUpLookupItemId(String upLookupItemId) {
        this.upLookupItemId = upLookupItemId;
    }

    public void setLookupCode(@NotBlank(message = "lookupCode can not be empty!") String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public void setLookupName(@NotBlank(message = "lookupName can not be empty!") String lookupName) {
        this.lookupName = lookupName;
    }

    public void setLookupTypeId(@NotBlank(message = "lookupTypeId can not be empty!") String lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public void setLookupTypeName(String lookupTypeName) {
        this.lookupTypeName = lookupTypeName;
    }

    public void setLookupItemBos(List<LookupItemBo> lookupItemBos) {
        this.lookupItemBos = lookupItemBos;
    }


    public static class LookupItemBo {

        public LookupItemBo() {
        }

        /**
         * 字典类别主键id
         */
        private String lookupItemId;
        /**
         * 字典代码
         */
        @NotBlank(message = "lookupItemCode can not be empty!")
        private String lookupItemCode;
        /**
         * 字典名称
         */
        @NotBlank(message = "lookupItemName can not be empty!")
        private String lookupItemName;

        public String getLookupItemId() {
            return this.lookupItemId;
        }

        public @NotBlank(message = "lookupItemCode can not be empty!") String getLookupItemCode() {
            return this.lookupItemCode;
        }

        public @NotBlank(message = "lookupItemName can not be empty!") String getLookupItemName() {
            return this.lookupItemName;
        }

        public void setLookupItemId(String lookupItemId) {
            this.lookupItemId = lookupItemId;
        }

        public void setLookupItemCode(@NotBlank(message = "lookupItemCode can not be empty!") String lookupItemCode) {
            this.lookupItemCode = lookupItemCode;
        }

        public void setLookupItemName(@NotBlank(message = "lookupItemName can not be empty!") String lookupItemName) {
            this.lookupItemName = lookupItemName;
        }

    }
}
