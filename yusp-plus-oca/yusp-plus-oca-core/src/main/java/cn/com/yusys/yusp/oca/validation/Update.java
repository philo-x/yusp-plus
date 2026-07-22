package cn.com.yusys.yusp.oca.validation;

import jakarta.validation.groups.Default;
/**
 * 参数校验分组:更新
 * 若不继承Default，实体参数校验时将不校验未分组字段
 * @author terry
 * @date 2021/6/28 11:27:00
 */
public interface Update  extends Default {
}
