package com.qiao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.qiao.model.pojo.User;

@Component
public class JdbcUtil {
	@Resource(name="dataSource")
	DataSource dataSource;
	
	/**
	 * 获取 数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public void close(Connection conn, Statement stm, ResultSet rs) {
		try {
			if(rs!=null)
				rs.close();
			if(stm!=null)
				stm.close();
			if(conn!=null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public void close(Connection conn, Statement stm) {
		try {
			if(stm!=null)
				stm.close();
			if(conn!=null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
	}
	
	public List<Map<String, Object>> select(String sql) {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			conn = this.getConnection();
			pstm = conn.prepareStatement(sql);
			rs=pstm.executeQuery();
			list = convertList(rs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			close(conn,pstm,rs);
		}
		return list;
	}
	
	public int update(String sql) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = this.getConnection();
			pstm = conn.prepareStatement(sql);
			count=pstm.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			close(conn,pstm);
		}
		return count;
	}
}
