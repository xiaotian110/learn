package com.yun.learn.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yun.learn.model.BDataGpsHistory;
@Mapper
public interface BDataGpsHistoryMapper {
    int deleteByPrimaryKey();

    int insert(BDataGpsHistory bDataGpsHistory);

    int insertSelective(BDataGpsHistory record);

    BDataGpsHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BDataGpsHistory record);

    int updateByPrimaryKey(BDataGpsHistory record);
}