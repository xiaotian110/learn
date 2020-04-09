package com.yun.learn.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yun.learn.model.BDataGps;
import com.yun.learn.model.BDataGpsHistory;
import com.yun.learn.service.BDataGpsService;
import com.yun.learn.util.JsonResult;

@Controller
@RequestMapping("clear")
public class BDataGpsController {
	@Resource
	BDataGpsService bDataGpsService;
	
	
	@RequestMapping("a")
	@ResponseBody
	public JsonResult clear(BDataGpsHistory bDataGpsHistory) {
		JsonResult jsonResult = new JsonResult();
		List<BDataGps> list = bDataGpsService.queryAll();
		if (list!=null) {
			for (BDataGps bDataGps2 : list) {
				bDataGpsHistory.setDeviceId(bDataGps2.getDeviceId());
				bDataGpsHistory.setId(bDataGps2.getId());
				bDataGpsHistory.setCreateTime(bDataGps2.getCreateTime());
				bDataGpsHistory.setLat(bDataGps2.getLat());
				bDataGpsHistory.setLng(bDataGps2.getLng());
				bDataGpsService.insert(bDataGpsHistory);
			}
			bDataGpsService.delete();
		}else {
			jsonResult.setMsg("没有数据");
		}
		return jsonResult;
		
	}
	

}
