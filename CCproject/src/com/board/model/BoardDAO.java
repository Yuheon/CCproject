package com.board.model;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.board.model.BoardBean;

public class BoardDAO 
{
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    
    private static BoardDAO instance;
    
    private BoardDAO(){}
    public static BoardDAO getInstance(){
        if(instance==null)
            instance=new BoardDAO();
        return instance;
    }
    
    // �������� �����´�.
    public int getSeq()
    {
        int result = 1;
        
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
            
            // ������ ���� �����´�. (DUAL : ������ ���� ������������ �ӽ� ���̺�)
            String sql = "SELECT BOARD_NUM FROM board";   
            pstmt = conn.prepareStatement(sql);
            // ���� ����
            rs = pstmt.executeQuery();
            
            if(rs.next())    result = rs.getInt(1);
 
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        close();
        return result;    
    } // end getSeq
    
    // �� ����
    public boolean boardInsert(BoardBean board)
    {
        boolean result = false;
        int currval = 0;
        System.out.println("ss");
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        	
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
            conn.setAutoCommit(false);
            
            Statement stmt = conn.createStatement();
			String sql = "select board_num from board";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				currval = rs.getInt("board_num");
			}
			
            sql = "INSERT INTO board (BOARD_NUM, BOARD_ID, BOARD_SUBJECT, BOARD_CONTENT, BOARD_FILE"
            		+ ", BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_COUNT, BOARD_DATE) VALUES(NULL,?,?,?,?,?,?,?,?,now())";
            // ������ ���� �۹�ȣ�� �׷��ȣ�� ���
 
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, board.getBoard_id());
            pstmt.setString(2, board.getBoard_subject());
            pstmt.setString(3, board.getBoard_content());
            pstmt.setString(4, board.getBoard_file());
            pstmt.setInt(5,currval+1);
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.setInt(8, 0);
            
            int flag = pstmt.executeUpdate();
            if(flag > 0){
               result = true;
               conn.commit(); 
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        close();
        return result;    
    } // end boardInsert();
    
 // �۸�� ��������
    public ArrayList<BoardBean> getBoardList(HashMap<String, Object> listOpt)
    {
        ArrayList<BoardBean> list = new ArrayList<BoardBean>();
        String opt = (String)listOpt.get("opt"); // �˻��ɼ�(����, ����, �۾��� ��..)
        String condition = (String)listOpt.get("condition"); // �˻�����
        int start = (Integer)listOpt.get("start"); // ���� ��������ȣ
        System.out.println(opt);
        System.out.println(condition);
        System.out.println(start);
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
            // �۸�� ��ü�� ������ ��
            if(opt == null)
            {
                // BOARD_RE_REF(�׷��ȣ)�� �������� ���� �� ������ �׷��ȣ�� ����
                // BOARD_RE_SEQ(�亯�� ����)�� ������������ ���� �� �Ŀ�
                // 10���� ���� �� ȭ�鿡 �����ִ�(start��° ���� start+9����) ����
                // desc : ��������, asc : �������� ( ���� ���� )
            	String sql = "select * from (select BOARD_NUM, BOARD_ID, BOARD_SUBJECT"
                		+ ", BOARD_CONTENT, BOARD_FILE, BOARD_COUNT, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_DATE "
                		+ "FROM (select * from board order by BOARD_RE_REF desc, BOARD_RE_SEQ asc)A)B where BOARD_NUM>=? and BOARD_NUM<=?";

            	pstmt = conn.prepareStatement(sql);
                	pstmt.setInt(1, start);
                	pstmt.setInt(2, start+9);
                
            }
            else if(opt.equals("0")) // �������� �˻�
            {
            	
            	String sql = "select * from (select BOARD_NUM, BOARD_ID, BOARD_SUBJECT"
            			+ ", BOARD_CONTENT, BOARD_FILE, BOARD_DATE, BOARD_COUNT, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ "
            			+ "FROM (select * from board where BOARD_SUBJECT like ? order BY BOARD_RE_REF desc, BOARD_RE_SEQ asc)A)B "
            			+ "where BOARD_NUM>=? and BOARD_NUM<=?";
             
                pstmt = conn.prepareStatement(sql);
                	pstmt.setString(1, "%"+condition+"%");
                	pstmt.setInt(2, start);
                	pstmt.setInt(3, start+9);
                
            }
            else if(opt.equals("1")) // �������� �˻�
            {
            	String sql = "select * from (select BOARD_NUM, BOARD_ID, BOARD_SUBJECT"
            			+ ", BOARD_CONTENT, BOARD_FILE, BOARD_DATE, BOARD_COUNT, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ "
            			+ "FROM (select * from board where BOARD_CONTENT like ? order BY BOARD_RE_REF desc, BOARD_RE_SEQ asc)A)B"
            			+ "where BOARD_NUM>=? and BOARD_NUM<=?"; 
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%"+condition+"%");
                pstmt.setInt(2, start);
                pstmt.setInt(3, start+9);
            }
            else if(opt.equals("2")) // ����+�������� �˻�
            {
            	String sql = "select * from (select BOARD_NUM, BOARD_ID, BOARD_SUBJECT"
            			+ ", BOARD_CONTENT, BOARD_FILE, BOARD_DATE, BOARD_COUNT, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ "
            			+ "FROM (select * from board where BOARD_SUBJECT like ? OR BOARD_CONTENT like ? "
            			+ "order BY BOARD_RE_REF desc, BOARD_RE_SEQ asc)A)B where BOARD_NUM>=? and BOARD_NUM<=? ";
            
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%"+condition+"%");
                pstmt.setString(2, "%"+condition+"%");
                pstmt.setInt(3, start);
                pstmt.setInt(4, start+9);
                
            }
            else if(opt.equals("3")) // �۾��̷� �˻�
            {
            	String sql = "select * from (select BOARD_NUM, BOARD_ID, BOARD_SUBJECT"
            			+ ", BOARD_CONTENT, BOARD_FILE, BOARD_DATE, BOARD_COUNT , BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ "
            			+ "FROM (select * from board where BOARD_ID like ? order BY BOARD_RE_REF desc, BOARD_RE_SEQ asc)A)B "
            			+ "where BOARD_NUM>=? and BOARD_NUM<=?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%"+condition+"%");
                pstmt.setInt(2, start);
                pstmt.setInt(3, start+9);
            }
            
            rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                BoardBean board = new BoardBean();
                board.setBoard_num(rs.getInt("BOARD_NUM"));
                board.setBoard_id(rs.getString("BOARD_ID"));
                board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
                board.setBoard_content(rs.getString("BOARD_CONTENT"));
                board.setBoard_file(rs.getString("BOARD_FILE"));
                board.setBoard_count(rs.getInt("BOARD_COUNT"));
                board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
                board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
                board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
                board.setBoard_date(rs.getDate("BOARD_DATE"));

                list.add(board);
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        close();
        return list;
    } // end getBoardList
    
    // ���� ������ �������� �޼���
    public int getBoardListCount(HashMap<String, Object> listOpt)
    {
        int result = 0;
        String opt = (String)listOpt.get("opt"); // �˻��ɼ�(����, ����, �۾��� ��..)
        String condition = (String)listOpt.get("condition"); // �˻�����
        
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
            
            if(opt == null)    // ��ü���� ����
            {
            	String sql = "select count(*) from board";
                pstmt = conn.prepareStatement(sql);
                
                // StringBuffer�� ����.
                sql = null;
            }
            else if(opt.equals("0")) // �������� �˻��� ���� ����
            {
            	String sql = "select count(*) from board where BOARD_SUBJECT like ?";
                pstmt = conn.prepareStatement(sql);
                               
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, '%'+condition+'%');
                
                sql = null;
            }
            else if(opt.equals("1")) // �������� �˻��� ���� ����
            {
            	String sql = "select count(*) from board where BOARD_CONTENT like ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, '%'+condition+'%');
                sql = null;
            }
            else if(opt.equals("2")) // ����+�������� �˻��� ���� ����
            {
            	String sql = "select count(*) from board where BOARD_SUBJECT like ? or BOARD_CONTENT like ?";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, '%'+condition+'%');
                pstmt.setString(2, '%'+condition+'%');
                sql = null;
            }
            else if(opt.equals("3")) // �۾��̷� �˻��� ���� ����
            {
            	String sql = "select count(*) from board where BOARD_ID like ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, '%'+condition+'%');
                sql = null;
            }
            
            rs = pstmt.executeQuery();
            if(rs.next())    result = rs.getInt(1);
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        close();
        return result;
    } // end getBoardListCount


    // DB �ڿ�����
    private void close()
    {
        try {
            if ( pstmt != null ){ pstmt.close(); pstmt=null; }
            if ( conn != null ){ conn.close(); conn=null;    }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    } // end close()
}


