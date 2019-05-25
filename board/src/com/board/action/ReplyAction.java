/**
 * ���� �ۼ� �ϰ� �����ͺ��̽��� �ִ� Action
 */
package com.board.action;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.board.controller.CommandAction;
 
public class ReplyAction implements CommandAction {
 
    public String requestPro(HttpServletRequest request,
 
    HttpServletResponse response) throws Throwable {
    	
    	request.setCharacterEncoding("euc-kr");
    	//����� ������ �Է� �޾� ������ ����
    	String subject = request.getParameter("subject");
    	String content = request.getParameter("content");
    	String email = request.getParameter("email");
    	
    	String id = null;
    	Class.forName("com.mysql.jdbc.Driver");
    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	Statement stmt=null;
    	
    	try{
    		HttpSession session = request.getSession();
    		//������ �о� �α��� ���°� �ƴϸ� �α��� â���� �̵�
        	id = (String) session.getAttribute("id");
    		if( id == null){
    			return "loginerror.jsp";
    		}
    		
    		String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
    		          // +
						//		"useUnicode=true&characterEncoding = euc-kr";
			String dbUser = "scott";
			String dbPass = "1234";
			int currval =0;
			conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
			stmt = conn.createStatement();
			String sql = "select num from board";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				currval = rs.getInt("num");
			}
			System.out.println(currval);
			sql = "insert into board values(NULL,?,?,?,?,now(),0,?,0,0)";
      		pstmt = conn.prepareStatement(sql);     				
    				pstmt.setString(1, id);
    				pstmt.setString(2, subject);
    				pstmt.setString(3, content);
    				pstmt.setString(4, email);
    				pstmt.setInt(5, currval+1);
    				//���� ����
    				pstmt.executeUpdate();	
        	
    	} catch(SQLException ex){
			
		}finally{
    		if(pstmt != null) try{pstmt.close();} catch(SQLException ex){}
    		if(conn != null) try{conn.close();} catch(SQLException ex){}
    		}
    	
 
        return "reply.jsp";
 
    }
 
}