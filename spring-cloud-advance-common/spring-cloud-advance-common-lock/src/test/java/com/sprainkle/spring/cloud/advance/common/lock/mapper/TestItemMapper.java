package com.sprainkle.spring.cloud.advance.common.lock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sprainkle.spring.cloud.advance.common.lock.entity.TestItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@Mapper
public interface TestItemMapper extends BaseMapper<TestItem> {

    int decreaseStock(@Param("id") Long id);

}
