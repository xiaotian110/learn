package com.yun.learn.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yun.learn.model.Banner;
@Mapper
public interface BannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Banner record);

    int insertSelective(Banner record);

    Banner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Banner record);

    int updateByPrimaryKey(Banner record);
}