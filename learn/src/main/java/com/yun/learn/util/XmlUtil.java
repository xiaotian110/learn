package com.yun.learn.util;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class XmlUtil {
	    /**
	     * @param args
	     */
	    public static void main1(String[] args) {
	        // 解析books.xml文件
	        // 创建SAXReader的对象reader
	        SAXReader reader = new SAXReader();
	        try {
	            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
	        	Document document = reader.read(new File("src/res/books.xml"));
	            // 通过document对象获取根节点bookstore
	            Element bookStore = document.getRootElement();
	            // 通过element对象的elementIterator方法获取迭代器
	            Iterator it = bookStore.elementIterator();
	            // 遍历迭代器，获取根节点中的信息（书籍）
	            while (it.hasNext()) {
	                Element book = (Element) it.next();//citys
	                if(book.getName().equals("System")) {
	                	continue;
	                }else {
	                	 Iterator itt = book.elementIterator();
	 	                while (itt.hasNext()) {
	 	                    Element bookChild = (Element) itt.next();//city
	 	                   Iterator cityChild=bookChild.elementIterator();
	 	                   while(cityChild.hasNext()) {
	 	                	  Element cityNode = (Element)cityChild.next();//Name
	 	                	  if(cityNode.getStringValue().equals("邯郸市")) {
	 	                		 Element pointersElement= cityNode.getParent().element("Pointers");//Pointers
	 	                		 Iterator pointerIterator= pointersElement.elementIterator();
	 	                		 while(pointerIterator.hasNext()) {
	 	                			 
	 	                			Element pointerItem=(Element)pointerIterator.next();
	 	                			if("武安一中(*)".equals(pointerItem.element("Name").getStringValue())) {
	 	   	 	                    	System.out.println("--节点值：" +pointerItem.element("Name").getStringValue());
	 	                				return;
	 	                			}
	 	                		 }
	 	                	  }else {
	 	                		  continue;
	 	                	  }
	 	                   }
	 	                    

	 	                }
	                }
	                		
	                
	                // 获取book的属性名以及 属性值
	                List<Attribute> bookAttrs = book.attributes();
	                for (Attribute attr : bookAttrs) {
	                    System.out.println("属性名：" + attr.getName() + "--属性值："
	                            + attr.getValue());
	                }
	                Iterator itt = book.elementIterator();
	                while (itt.hasNext()) {
	                    Element bookChild = (Element) itt.next();
	                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
	                }
	                System.out.println("=====结束遍历某一本书=====");
	            }
	        } catch (DocumentException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    public static void main(String[] args) {
	    	  //String xmlContent=HttpUtil.get(monitorUrl);
			 // 创建SAXReader的对象reader
	       SAXReader reader = new SAXReader();
	       try {
	           // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
	           Document document = reader.read("http://121.28.49.85:8080/datas/hour/130000.xml?radn=0.0474545100855836");
	           // 通过document对象获取根节点bookstore
	           Element bookStore = document.getRootElement();
	           // 通过element对象的elementIterator方法获取迭代器
	           Iterator it = bookStore.elementIterator();
	           // 遍历迭代器，获取根节点中的信息
	           while (it.hasNext()) {
	               Element book = (Element) it.next();//citys
	               if(book.getName().equals("System")) {
	               	continue;
	               }else {
	               	 Iterator itt = book.elementIterator();
		                while (itt.hasNext()) {
		                    Element bookChild = (Element) itt.next();//city
		                   Iterator cityChild=bookChild.elementIterator();
		                   while(cityChild.hasNext()) {
		                	  Element cityNode = (Element)cityChild.next();//Name
		                	  if(cityNode.getStringValue().equals("邯郸市")) {
		                		 Element pointersElement= cityNode.getParent().element("Pointers");//Pointers
		                		 Iterator pointerIterator= pointersElement.elementIterator();
		                		 while(pointerIterator.hasNext()) {
		                			Element pointerItem=(Element)pointerIterator.next();//pointer
		                			if("武安一中(*)".equals(pointerItem.element("Name").getStringValue())) {
		                				Element pollsElement=pointerItem.element("Polls");//Polls
		                				Iterator pollsIterator=pollsElement.elementIterator();
		                				double pm2d5=0;
		                				double pm10=0;
		                				while(pollsIterator.hasNext()) {
		                					Element pollItem=(Element)pollsIterator.next();//Poll
		                					if("PM2.5".equals(pollItem.element("Name").getStringValue())) {
		                						pm2d5=Double.parseDouble(pollItem.element("Value").getStringValue())*1000.0;
		                					}else if("PM10".equals(pollItem.element("Name").getStringValue())) {
		                						pm10=Double.parseDouble(pollItem.element("Value").getStringValue())*1000.0;
		                					}
		                					
		                				}
		   	 	                    	
		   	 	                    	System.out.println(pm2d5);
		   	 	                    	System.out.println(pm10);
		                				return;
		                			}
		                		 }
		                	  }else {
		                		  continue;
		                	  }
		                   }
		                    

		                }
	               }
	           }
	       } catch (DocumentException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	    	
	    }
	    
	
}
