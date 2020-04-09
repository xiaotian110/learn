package com.yun.learn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yun.learn.mapper.BDataGpsHistoryMapper;
import com.yun.learn.mapper.BDataGpsMapper;
import com.yun.learn.model.BDataGps;
import com.yun.learn.model.BDataGpsHistory;

@Service
public class BDataGpsService {
	@Resource
	BDataGpsMapper bDataGpsMapper;
	@Resource
	BDataGpsHistoryMapper  bDataGpsHistoryMapper;
	
	public List<BDataGps>  queryAll(){
		return bDataGpsMapper.queryAll();
	}
	
	public int insert(BDataGpsHistory bDataGpsHistory) {
		return bDataGpsHistoryMapper.insert(bDataGpsHistory);
	}
	
	public int delete() {
		return bDataGpsMapper.delete();
	}

}
