package com.yun.learn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yun.learn.model.Bcar;
@Mapper
public interface BcarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bcar record);

    int count();

    Bcar selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bcar record);

    int updateByPrimaryKey(Bcar record);
    
    List<Bcar> queryAll(Bcar bcar);
    
    List<Bcar> excelAll();
}