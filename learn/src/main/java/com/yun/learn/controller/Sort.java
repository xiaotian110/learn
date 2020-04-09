package com.yun.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("a")
public class Sort {

	public static void main(String[] args) {
		int[]  sorts = {2,7,34,53,4,5};
		for (int i = 0; i < sorts.length; i++) {
			for (int j = 0; j < sorts.length-i; j++) {
				if (sorts[j]>sorts[j+1]) {
					int temp = sorts[j+1];
					sorts[j]=sorts[j+1];
					sorts[j+1]= temp;
				}
			}
		}
		for (int i1 : sorts) {
			System.out.println(i1);
		}
	}

}
