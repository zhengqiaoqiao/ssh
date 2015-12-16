package com.qiao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;


public class ExcelUtil {
	
	/**
	 * 获得单元格中的内容
	 * 
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("#");
		Object result = null;
		if (cell != null) {
			int cellType = cell.getCellType();
			switch (cellType) {
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					result = df.format(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					result = cell.getCellFormula();
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					result = null;
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					result = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					result = null;
					break;
			}
		}
		return result;
	}

	public static HSSFSheet getSheet(String filePath, int sheetId){
		HSSFSheet sheet = null;
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(sheetId);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	public static List<Object[]> readExcel(InputStream s, int sheetId){
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			HSSFWorkbook wb = new HSSFWorkbook(s);
			HSSFSheet sheet = wb.getSheetAt(sheetId);
			//迭代行
	        for (Iterator<Row> riter = (Iterator<Row>) sheet.rowIterator(); riter.hasNext();) {
	            Row row = riter.next();
	            // 获得本行中单元格的个数
				int columnCount = row.getLastCellNum();
				Object[] rowData = new Object[columnCount];
	            //迭代列
	            for (Iterator<Cell> citer = (Iterator<Cell>) row.cellIterator(); citer.hasNext();) {
                    Cell cell = citer.next();
					int colNum = cell.getColumnIndex();
					// 获得指定单元格中数据
					Object cellStr = getCellValue(cell);
					rowData[colNum] = cellStr;
	            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void copeCellValue(Cell hssfCell1, Cell hssfCell2){
		if(hssfCell1.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
			hssfCell2.setCellValue(hssfCell1.getBooleanCellValue());
		}else if(hssfCell1.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
			hssfCell2.setCellValue(hssfCell1.getNumericCellValue());
		}else{
			hssfCell2.setCellValue(hssfCell1.getStringCellValue());
		}
	}

	public static void editExcel(String path, List<String[]> list){
		FileOutputStream fos = null;
		FileInputStream fis = null;
		HSSFWorkbook wb = null;
		try {
			System.out.println("开始编辑Excel文件！");
	        fis = new FileInputStream(path);
			wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);
			int total = 0;
			HSSFCellStyle style = wb.createCellStyle();
			for(int i=0;i<list.size();i++){
				String[] data = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				for(int j=0;j<data.length;j++){
					String value = data[j];
					if(value.equals("")||value==null)
						continue;
					Cell cell = row.createCell(j);
					cell.setCellValue(value);
					cell.setCellStyle(style);
				}
				total++;
			}
			fos = new FileOutputStream(path);
			wb.write(fos);
			System.out.println("编辑Excel文件完成！");
			System.out.println("本文件总共有"+(total)+"条记录");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("编辑Excel文件失败！");
			e.printStackTrace();
		} finally {
			try {
				if(wb!=null)
					wb.close();
				if(fis!=null)
					fis.close();
				if(fos!=null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
	}
	
	
	public static void copeExcel(String sourcePath, String targetPath){
		FileOutputStream fos = null;
		FileInputStream fis = null;
		HSSFWorkbook wwb = null;
		HSSFWorkbook rwb = null;
		try {
			// 创建Excel的工作书册 Workbook,对应到一个excel文档
	        wwb = new HSSFWorkbook();
	        // 创建Excel的工作sheet,对应到一个excel文档的tab
	        HSSFSheet sheet = wwb.createSheet("Sheet1");
	        fis = new FileInputStream(sourcePath);
	        rwb = new HSSFWorkbook(fis);
			HSSFSheet rdsheet = rwb.getSheetAt(0);			
			//迭代行
	        for (Iterator<Row> iter = (Iterator<Row>) rdsheet.rowIterator(); iter.hasNext();) {
	            Row row = iter.next();
	            int rowNum = row.getRowNum();
	            // 创建Excel的sheet的一行
                HSSFRow row1 = sheet.createRow(rowNum);
	            //迭代列
	            for (Iterator<Cell> iter2 = (Iterator<Cell>) row.cellIterator(); iter2.hasNext();) {
                    Cell cell = iter2.next();
                    int colNum = cell.getColumnIndex();
                    // 创建一个Excel的单元格
                    HSSFCell cell1 = row1.createCell(colNum);
                    copeCellValue(cell,cell1);
                	HSSFCellStyle style = wwb.createCellStyle();
                	style.cloneStyleFrom(cell.getCellStyle());
                    cell1.setCellStyle(style);
	            }
	        }
			fos = new FileOutputStream(targetPath);
			wwb.write(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(wwb!=null)
					wwb.close();
				if(rwb!=null)
					rwb.close();
				if(fos!=null)
					fos.close();
				if(fis!=null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
	}
}
