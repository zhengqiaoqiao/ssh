package com.qiao.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {
	//获取目录中的文件列表
	public static List<File> getAllFile(String fileAbsolutePath, List<File> rs) {
		if(rs == null)
			return null;
		
		File file = new File(fileAbsolutePath);
		if(!file.exists())
			return null;
		
		File[] list = file.listFiles();

		for (int i = 0; i < list.length; i++) {
			// 判断是否为文件夹
			File subFile = list[i];
			if (!subFile.isDirectory()) {
				rs.add(subFile);
			}else{
				String path = subFile.getAbsolutePath();
				getAllFile(path, rs);
			}
		}
		return rs;
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFile(String filePath, int start, int size) {
    	ArrayList<String> list = new ArrayList<String>();
        File file = new File(filePath);
        LineNumberReader reader = null;
        try {
        	System.out.println("开始读取文件！");
            reader = new LineNumberReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	if(reader.getLineNumber()<start)
            		continue;
            	else if(list.size()<size) {
            		list.add(tempString);
            	}else {
            		break;
            	}
            }
            System.out.println("读取文件完成！");
           
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取文件失败！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
        return list;
    }
    /**
     * 拷贝文件
     */
    public static File copyFile(String sourcePath, String targetPath) {
    	File targetFile = null;
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
        	System.out.println("开始拷贝文件！");
        	File sourceFile = new File(sourcePath);
        	targetFile = new File(targetPath);
        	if(!sourceFile.exists()){
        		System.out.println("拷贝文件失败：源文件不存在！");
        		return null;
        	}
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            System.out.println("拷贝文件完成！");
        } catch(Exception e){
        	System.out.println("拷贝文件失败！");
        	e.printStackTrace();
        }finally {
            // 关闭流
        	try {
	            if (inBuff != null)
	                inBuff.close();
	            if (outBuff != null)
	                outBuff.close();
        	} catch(Exception e){
            	e.printStackTrace();
            }
        }
        return targetFile;
    }
    
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static int getFileRowNum(String filePath) {
    	int total = 0;
        File file = new File(filePath);
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            // 一次读入一行，直到读入null为文件结束
            while ((reader.readLine()) != null) {
            	total++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
        return total;
    }
    
}