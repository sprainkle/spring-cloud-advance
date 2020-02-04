package com.sprainkle.licence.constant;

import com.sprainkle.spring.cloud.advance.common.core.constant.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * licence type
 *
 * @author sprainkle
 * @date 2019/5/3
 */
@Getter
@ToString
@AllArgsConstructor
public enum LicenceTypeEnum implements Dictionary {

	USER("user", "user"),
	CORE_PROD("core-prod", "core-prod"),
    ;

    private String code;
    private String name;

	/**
	 * code -> {@link LicenceTypeEnum}, 允许返回空
	 * @param code
	 * @return
	 */
	public static LicenceTypeEnum parseOfNullable(String code) {
		if (code != null) {
			for (LicenceTypeEnum e : values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * code -> name, 允许返回空
	 * @param code
	 * @return
	 */
	public static String parseOfNameNullable(String code) {
		LicenceTypeEnum enable = parseOfNullable(code);
		return enable == null ? null : enable.getName();
	}

}
