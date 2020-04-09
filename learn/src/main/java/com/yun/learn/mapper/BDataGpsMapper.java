package com.yun.learn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yun.learn.model.BDataGps;
@Mapper
public interface BDataGpsMapper {
    int delete();

    int insert(BDataGps record);

    int insertSelective(BDataGps record);

    BDataGps selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BDataGps record);

    int updateByPrimaryKey(BDataGps record);
    
    List<BDataGps>  queryAll();
    
}