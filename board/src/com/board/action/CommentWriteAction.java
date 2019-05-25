package com.board.action;
 
import java.io.PrintWriter;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.board.beans.board;
import com.board.beans.comment;
import com.board.controller.CommandAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
 
public class CommentWriteAction  implements CommandAction
{
    @Override
    public String requestPro(HttpServletRequest request,
    	    HttpServletResponse response) throws Throwable {
    	request.setCharacterEncoding("euc-kr");
    	comment comment = new comment();
    	int comment_board = Integer.parseInt(request.getParameter("comment_board"));
        String comment_id = request.getParameter("comment_id");
        String comment_content = request.getParameter("comment_content");
        
    	Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs;
        
        
    	 // �������� �����´�.
        int result = 1;
        /*try {
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
		          // +
					//		"useUnicode=true&characterEncoding = euc-kr";
            String dbUser = "scott";
            String dbPass = "1234";
            conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
                
                // ������ ���� �����´�. (DUAL : ������ ���� ������������ �ӽ� ���̺�)
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COMMENT_SEQ.NEXTVAL FROM DUAL");
     
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery(); // ���� ����
     
            if (rs.next())    result = rs.getInt(1);
     
         } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
         }
        */
        comment.setComment_num(result);    // ��� ��ȣ�� ������������
        comment.setComment_board(comment_board);
        comment.setComment_id(comment_id);
        comment.setComment_content(comment_content);
           
         response.setContentType("text/html;charset=euc-kr");
         PrintWriter out = response.getWriter();
         out.println("1");
         out.close();
        
        // ��� ���
        try {
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
		          // +
					//		"useUnicode=true&characterEncoding = euc-kr";
            String dbUser = "scott";
            String dbPass = "1234";
            conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
     
                // �ڵ� Ŀ���� false�� �Ѵ�.
            conn.setAutoCommit(false);
            String sql  ="INSERT INTO BOARD_COMMENT(COMMENT_NUM, COMMENT_BOARD,"
            		+ "COMMENT_ID, COMMENT_DATE, COMMENT_PARENT, COMMENT_CONTENT)"
            		+ "values(NULL,?,?,now(),?,?)";
            pstmt = conn.prepareStatement(sql);
            //pstmt.setInt(1, comment.getComment_num());
            pstmt.setInt(1, comment.getComment_board());
            pstmt.setString(2, comment.getComment_id());
            pstmt.setInt(3, comment.getComment_parent());
            pstmt.setString(4, comment.getComment_content());
                
            int flag = pstmt.executeUpdate();
            if(flag > 0){
                conn.commit(); // �Ϸ�� Ŀ��
            }
                
        } catch (Exception e) {
            try {
                conn.rollback(); // ������ �ѹ�
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } 
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
            
        
        // ��� ��� ��������
        ArrayList<comment> list = new ArrayList<comment>();
            
        try {
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
		          // +
					//		"useUnicode=true&characterEncoding = euc-kr";
            String dbUser = "scott";
            String dbPass = "1234";
            conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
                
                /* ����� ������ ó���� �ϰ�ʹٸ� �� ������ ����ϸ� �ȴ�.
                 * SELECT * FROM
                 *            (SELECT  ROWNUM AS rnum,
                 *                   data.*
                 *             FROM
                 *                   (SELECT LEVEL,
                 *                           COMMENT_NUM,
                 *                             COMMENT_BOARD,
                 *                           COMMENT_ID,
                 *                           COMMENT_DATE,
                 *                           COMMENT_PARENT,
                 *                           COMMENT_CONTENT
                 *                    FROM BOARD_COMMENT
                 *                    WHERE COMMENT_BOARD = ?
                 *                   START WITH COMMENT_PARENT = 0
                 *                   CONNECT BY PRIOR COMMENT_NUM = COMMENT_PARENT) 
                 *              data)
                 *    WHERE rnum>=? and rnum<=? ;
                 */
           String sql = "SELECT LEVEL, COMMENT_NUM, COMMENT_BOARD,COMMENT_ID, COMMENT_DATE,COMMENT_PARENT, COMMENT_CONTENT"
           		+ "FROM BOARD_COMMENT WHERE COMMENT_BOARD = ?"
           		+ "START WITH COMMENT_PARENT = 0"
           		+ "CONNECT BY PRIOR COMMENT_NUM = COMMENT_PARENT";
            pstmt.setInt(1, comment_board);

            pstmt = conn.prepareStatement(sql);
                
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                comment comment1 = new comment();
                comment1.setComment_level(rs.getInt("LEVEL"));
                comment1.setComment_num(rs.getInt("COMMENT_NUM"));
                comment1.setComment_board(rs.getInt("COMMENT_BOARD"));
                comment1.setComment_id(rs.getString("COMMENT_ID"));
                comment1.setComment_date(rs.getDate("COMMENT_DATE"));
                comment1.setComment_parent(rs.getInt("COMMENT_PARENT"));
                comment1.setComment_content(rs.getString("COMMENT_CONTENT"));
                list.add(comment1);
            }
                    
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        try {
            if ( pstmt != null ){ pstmt.close(); pstmt=null; }
            if ( conn != null ){ conn.close(); conn=null;    }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        return "content.jsp";
    }

}


