package com.yun.learn.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yun.learn.model.Picter;
@Mapper
public interface PicterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picter picter);

    int insertSelective(Picter record);

    Picter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Picter record);

    int updateByPrimaryKey(Picter record);
}