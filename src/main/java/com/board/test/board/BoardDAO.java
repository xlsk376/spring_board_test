package com.board.test.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDAO {

	@Autowired
	DataSource dataSource;
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public void finallyClose() {
		try {
			if (pstmt != null) { pstmt.close();}
			if (conn != null) { conn.close(); }
			if (rs != null) { rs.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getAllCount() {
		int count = 0;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "SELECT COUNT(*) FROM board";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			finallyClose();
		}
		
		return count;
	}
	
	public ArrayList<Board> getAllBoard(int start, int count) {

		ArrayList<Board> list = new ArrayList<Board>();

		try {
			conn = dataSource.getConnection();
			
			/*
				MySQL LIMIT 사용법
				
				SELECT * FROM board LIMIT 5;		// 인덱스가 0번째에서부터 5개 가져오기
				SELECT * FROM board LIMIT 4, 10;	// 인덱스가 4번째에서부터 10개 가져오기
			 */
			
			String sql = "SELECT * FROM board ORDER BY ref DESC, re_level LIMIT ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, count);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Board bean = new Board();
				
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				System.out.println(rs.getDate(6));
				bean.setReg_date(rs.getDate(6).toString());		// Date타입을 String타입으로 변환
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			finallyClose();
		}
		
		return list;
	}
	
	public Board getOneBoard(int num) {
		Board bean = new Board();

		try {
			conn =  dataSource.getConnection();	
			
			String readsql = "UPDATE board SET readcount=readcount+1 WHERE num=?";
			pstmt = conn.prepareStatement(readsql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			String sql = "SELECT * FROM board WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			finallyClose();
		}
		
		return bean;
	}
	
	public void insertBoard(Board board) {

		int ref = 0;
		
		try {
			conn = dataSource.getConnection();
			
			String refSql = "SELECT MAX(ref) FROM board";
			pstmt = conn.prepareStatement(refSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ref = rs.getInt(1) + 1;
			}
			
			String sql = "INSERT INTO board (writer, email, subject, "
					+ "password, reg_date, ref, re_step, re_level, "
					+ "readcount, content) VALUES(?, ?, ?, ?, now(), ?, 1, 1, 0, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getWriter());
			pstmt.setString(2, board.getEmail());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setString(6, board.getContent());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			finallyClose();
		}
	}
	
	public void reWriteBoard(Board bean) {
		
		int ref = bean.getRef();
		int re_step = bean.getRe_step();
		int re_level = bean.getRe_level();
		
		int num = 0;
		
		try {
			conn = dataSource.getConnection();	
			
			String levelsql = "UPDATE board SET re_level=re_level+1 WHERE ref=? and re_level > ?";
			pstmt = conn.prepareStatement(levelsql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, re_level);
			pstmt.executeUpdate();		
			
			String sql = "INSERT INTO board (writer, email, subject, password, "
					+ "reg_date, ref, re_step, re_level, readcount, content) "
					+ "VALUES (?,?,?,?,now(),?,?,?,0,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);				
			pstmt.setInt(6, re_step + 1);		
			pstmt.setInt(7, re_level + 1);
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			finallyClose();
		}
	}
	
}
