package com.yun.learn.controller;

import java.io.BufferedInputStream; 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.yun.learn.model.Bcar;
import com.yun.learn.service.BcarService;
import com.yun.learn.util.CommonHelper;
import com.yun.learn.util.ExcelExport;
import com.yun.learn.util.ExcelUtil;
import com.yun.learn.util.JsonResult;
import com.yun.learn.util.Page;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@Controller
@RequestMapping("car")
public class BcarController {
	@Resource
	BcarService bcarService;

	// 分页，查询所有，模糊查询
	@RequestMapping("select")
	@ResponseBody
	public JsonResult select(String current, Bcar bcar) {
		JsonResult jsonResult = new JsonResult();
		Map map = new HashMap();
		int count = bcarService.count();
		Page page = new Page(current, count, "10");
		bcar.setPage(page);
		List<Bcar> bcars = bcarService.queryAll(bcar);
		map.put("bcars", bcars);
		map.put("pageInfo", page);
		map.put("countPage", page.getCurrentPage()); // 总页数
		jsonResult.setData(map);
		return jsonResult;

	}

	// 导出excel
	@RequestMapping("excel")
	@ResponseBody
	public ModelAndView excel(HttpServletResponse response) {
		ExcelExport excelExport = new ExcelExport();
		List<Bcar> bcars = bcarService.excelAll();
		excelExport.setSheetName("汽车");
		excelExport.addColumnInfo("设备id", "deviceId");
		excelExport.addColumnInfo("车类型", "carType");
		excelExport.addColumnInfo("车排号", "carNum");
		excelExport.addColumnInfo("司机编号", "driverNum");
		excelExport.addColumnInfo("司机姓名", "driverName");
		excelExport.addColumnInfo("司机电话", "driverPhone");
		excelExport.setDataList(bcars);
		return CommonHelper.getExcelModelAndView(excelExport);

	}

	// 导出excel2
	@RequestMapping("excel2")
	@ResponseBody
	public JsonResult excel2(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		List<Bcar> list = bcarService.excelAll();
		// excel标题
		String[] title = { "车辆类型", "车牌号", "司机编号", "司机姓名", "司机电话" };
		// excel文件名
		String fileName = "汽车信息.xls";
		// sheet名
		String sheetName = "汽车和司机信息";
		String[][] content = null;
		content = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			content[i] = new String[title.length];
			Bcar bcar = list.get(i);
			Object object = null;
			if (bcar.getCarType() == null) {
				object = "";
				content[i][0] = object.toString();
			} else {
				content[i][0] = bcar.getCarType().toString();
			}
			if (bcar.getCarNum() == null) {
				object = "";
				content[i][1] = object.toString();
			} else {
				content[i][1] = bcar.getCarNum().toString();
			}
			if (bcar.getDriverNum() == null) {
				object = "";
				content[i][2] = object.toString();
			} else {
				content[i][2] = bcar.getDriverNum().toString();
			}
			if (bcar.getDriverName() == null) {
				object = "";
				content[i][3] = object.toString();
			} else {
				content[i][3] = bcar.getDriverName().toString();
			}
			if (bcar.getDriverPhone() == null) {
				object = "";
				content[i][4] = object.toString();
			} else {
				content[i][4] = bcar.getDriverPhone().toString();
			}

		}
		// 创建HSSFWorkbook
		HSSFWorkbook hs = ExcelUtil.getHSSFWorkbook(fileName, title, content, null);
		// 响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			hs.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void setResponseHeader(HttpServletResponse response, String style) {
		try {
			try {
				style = new String(style.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + style);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 导出excel
	@RequestMapping("/export")
	private void export(Bcar bcar,HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Bcar> list = bcarService.excelAll();
		String tempFileName = request.getSession().getServletContext().getRealPath("/") + "resources/excelTemplate";
		System.out.println(tempFileName);
		List values = new ArrayList();
		List time = new ArrayList();
		List deviceNum = new ArrayList();
		Map beans = new HashMap();
		Date date = new Date();
		SimpleDateFormat simpl = new SimpleDateFormat("yyyyMMddHHmmss");
		String currntTime = simpl.format(date);
		tempFileName += "/yq_hour.xlsx";

		// 导出列表名
		String fileName = "烟气排放连续监测小时平均值报表.xls";

		values.add(bcar.getId());
		time.add(bcar.getCarType());
		deviceNum.add(bcar.getCarNum());

		beans.put("values", values);
		beans.put("time", time);
		beans.put("deviceNum", deviceNum);
		// 文件名称统一编码格式
		fileName = URLEncoder.encode(fileName, "utf-8");

		// 生成的导出文件
		File destFile = File.createTempFile(fileName, ".xls");

		// transformer转到Excel
		XLSTransformer transformer = new XLSTransformer();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			// 将数据添加到模版中生成新的文件
			transformer.transformXLS(tempFileName, beans, destFile.getAbsolutePath());
			// 将文件输入
			InputStream inputStream = new FileInputStream(destFile);
			// 设置response参数，可以打开下载页面
			response.reset();
			// 设置响应文本格式
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			// 将文件输出到页面
			ServletOutputStream out = response.getOutputStream();
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// 根据读取并写入
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 使用完成后关闭流
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
			}

		}
	}

}
