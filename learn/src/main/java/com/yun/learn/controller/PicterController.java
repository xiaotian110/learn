package com.yun.learn.controller;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yun.learn.model.Picter;
import com.yun.learn.service.PicterService;
import com.yun.learn.util.JsonResult;



@Controller
@RequestMapping("file")
public class PicterController {
	@Resource
	private PicterService picterService;
	
	
	
	//信息添加文件上传
	@RequestMapping("insert")
	@ResponseBody
	public JsonResult insert(Picter picter,MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException {
		JsonResult jsonResult = new JsonResult();
			String contentType="";
			String suffixName="";
			String path = request.getSession().getServletContext().getRealPath("/src/main/webapp/recourse/images/");
			String fileName = "" + System.currentTimeMillis() + (int)(Math.random()*1000) + file.getOriginalFilename();
			if (file != null) {
				//生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				// 文件类型(可以判断如果不是图片，禁止上传)
				contentType = file.getContentType();
				// 文件后缀名
				suffixName = contentType.substring(contentType.indexOf("/") + 1);
				if (suffixName.equals("jpg")) {
					suffixName="jpg";
				}else if (suffixName.equals("png")) {
					suffixName="png";
				}
				/*// 文件后缀名
				suffixName = contentType.substring(contentType.indexOf("/") + 1);
				if (suffixName.equals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					suffixName="xlsx";
				}else if (suffixName.equals("vnd.ms-excel")) {
					suffixName="xls";
				}*/
			File file2 = new File(path+"."+fileName);
			if( !file2.getParentFile().exists()) {
	            // 如果目标文件所在的目录不存在，则创建父目录
	            file2.getParentFile().mkdirs();
	        }
			Date date = new Date();
			picter.setName(request.getParameter("name"));
			picter.setTime(date);
			picter.setPic(path+"."+fileName);
			try {
				file.transferTo(file2);
				picterService.insert(picter);
				jsonResult.setData("success"); 
			} catch (Exception e) {
				jsonResult.setData("msg");
			}
			         
        }				
		return jsonResult;
		
		
	}
	//单文件下载
	@RequestMapping("updown")
	@ResponseBody
	public JsonResult updown(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException {
		JsonResult jsonResult = new JsonResult();
		String path = request.getSession().getServletContext().getRealPath("/src/main/webapp/recourse/images/");
		File file = new File(path);
		File[] files = file.listFiles();
		String name = files[0].getName();//随机获取一个文件，实际中按需编写代码
		System.out.println("文件的名字："+name);
		response.addHeader("content-disposition", "attachment;filename="+name);
		FileUtils.copyFile(files[0], response.getOutputStream());	
		return jsonResult;
		
		
	}
	//多个文件上传
	@RequestMapping(value="/upload3",produces="text/html;charset=utf-8")
	@ResponseBody
	private String upload3(@RequestParam("file")CommonsMultipartFile[] partFiles,HttpServletRequest request) {
	         InputStream inputStream = null;    
	         try {
	             String path = request.getServletContext().getRealPath("/upload");
	             String name = request.getParameter("name");	            
	             for (int i = 0; i < partFiles.length; i++) {
                 String filename = partFiles[i].getOriginalFilename();	                
	                 File file = new File(path+"/"+filename);
	                 inputStream = partFiles[i].getInputStream();
	                 FileUtils.copyInputStreamToFile(inputStream, file);
	             }
	             if(inputStream!=null){
	                inputStream.close();
	             }
	             return "文件上传成功！";
	         } catch (Exception e) {
             e.printStackTrace();
	             return "文件上传失败！";
	        } 
	     }
	//多个文件下载
	
	
	
	/**
	 * 上传多个文件或图片上传在项目路径下的img文件夹在
	 * ！！！！！！重新部署项目实效,因为文件夹被删除
	 * @param files
	 * @param request
	 * @return
	 */
	public static List<String> upfiles(CommonsMultipartFile files[],HttpServletRequest request){
				List<String> list = new ArrayList<String>();  
		        // 获得项目的路径  
		        ServletContext sc = request.getSession().getServletContext();  
		        // 上传位置  
		        String path = sc.getRealPath("/img") + File.separatorChar; // 设定文件保存的目录  
		        File f = new File(path);  
		        if (!f.exists())  
		            f.mkdirs();  
		      
		        for (int i = 0; i < files.length; i++) {  
		            // 获得原始文件名  
		            String fileName = files[i].getOriginalFilename();  
		            System.out.println("原始文件名:" + fileName);  
		            // 新文件名  
		            String newFileName = UUID.randomUUID() + fileName;  
		            if (!files[i].isEmpty()) {  
		                try {  
		                    FileOutputStream fos = new FileOutputStream(path  
		                            + newFileName);  
		                    InputStream in = files[i].getInputStream();  
		                    int b = 0;  
		                    while ((b = in.read()) != -1) {  
		                        fos.write(b);  
		                    }  
		                    fos.close();  
		                    in.close();  
		                } catch (Exception e) {  
		                    e.printStackTrace();  
		                }  
		            }  
		            System.out.println("上传图片到:" + path + newFileName);  
		            list.add("img/"+newFileName);
		        }  
		       return list;
	}
	/**
	 * 上传一个文件或图片
	 * 上传多个文件或图片上传在项目路径下的img文件夹在
	 * ！！！！！！重新部署项目实效,因为文件夹被删除
	 * @param file
	 * @param request
	 * @return
	 */
	public static String upfile(CommonsMultipartFile file,HttpServletRequest request){
		 // 获得项目的路径  
        ServletContext sc = request.getSession().getServletContext();  
        // 上传位置  
        String path = sc.getRealPath("/img") + File.separatorChar; // 设定文件保存的目录  
        File f = new File(path);  
        if (!f.exists())  
            f.mkdirs();  
            // 获得原始文件名  
            String fileName = file.getOriginalFilename();  
            System.out.println("原始文件名:" + fileName);  
            // 新文件名  
            String newFileName = UUID.randomUUID() + fileName;  
            if (!file.isEmpty()) {  
                try {  
                    FileOutputStream fos = new FileOutputStream(path  
                            + newFileName);  
                    InputStream in = file.getInputStream();  
                    int b = 0;  
                    while ((b = in.read()) != -1) {  
                        fos.write(b);  
                    }  
                    fos.close();  
                    in.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            System.out.println("上传图片到:" + path + newFileName);  
            return "img/"+newFileName;
	}
	
	
	
	/**
	 * 下载
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 */
	public static void downFile(HttpServletRequest request,  
            HttpServletResponse response,String filename) {  
        // 得到要下载的文件名  
        String fileName = filename.substring(4);
        try {  
            fileName = new String(fileName.getBytes("iso8859-1"), "UTF-8");  
            // 获取上传文件的目录  
            ServletContext sc = request.getSession().getServletContext();  
            // 上传位置  
            String fileSaveRootPath = sc.getRealPath("/img");   
              
            System.out.println(fileSaveRootPath + "\\" + fileName);  
            // 得到要下载的文件  
            File file = new File(fileSaveRootPath + "\\" + fileName);  
              
            // 如果文件不存在  
            if (!file.exists()) {  
                request.setAttribute("message", "您要下载的资源已被删除！！");  
                System.out.println("您要下载的资源已被删除！！");  
                return ;  
            }  
            // 处理文件名  
            String realname = fileName.substring(fileName.indexOf("_") + 1);  
            // 设置响应头，控制浏览器下载该文件  
            response.setHeader("content-disposition", "attachment;filename="  
                    + URLEncoder.encode(realname, "UTF-8"));  
            // 读取要下载的文件，保存到文件输入流  
            FileInputStream in = new FileInputStream(fileSaveRootPath + "\\" + fileName);  
            // 创建输出流  
            OutputStream out = response.getOutputStream();  
            // 创建缓冲区  
            byte buffer[] = new byte[1024];  
            int len = 0;  
            // 循环将输入流中的内容读取到缓冲区当中  
            while ((len = in.read(buffer)) > 0) {  
                // 输出缓冲区的内容到浏览器，实现文件下载  
                out.write(buffer, 0, len);  
            }  
            // 关闭文件输入流  
            in.close();  
            // 关闭输出流  
            out.close(); 
        } catch (Exception e) {  
        } 
    } 	


}
