package com.yun.learn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yun.learn.mapper.BcarMapper;
import com.yun.learn.model.Bcar;

@Service
public class BcarService {
	@Resource
	BcarMapper bcarMapper;
	
	public List<Bcar> queryAll(Bcar bcar){
		return bcarMapper.queryAll(bcar);
	}
	
	public int count() {
		return bcarMapper.count();
	}
	
	public List<Bcar> excelAll(){
		return bcarMapper.excelAll();
		
	}

}
