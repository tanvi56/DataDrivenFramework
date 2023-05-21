package com.datadriven.framework.utils;

public class TempReadData {
//Not part of Framework
	public static void main(String[] args) {
	
		ReadExcelDataFile read = new ReadExcelDataFile(System.getProperty("user.dir")+"\\src\\main\\resources\\testData\\TestData.xlsx");
		int rows = read.getRowCount("credentials");
		System.out.println(rows);
	}
}
