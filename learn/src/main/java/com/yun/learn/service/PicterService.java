package com.yun.learn.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yun.learn.mapper.PicterMapper;
import com.yun.learn.model.Picter;

@Service
public class PicterService {
	@Resource
	PicterMapper  picterMapper;
	
	
	public  int insert(Picter picter) {
		return picterMapper.insert(picter);
	}
}
