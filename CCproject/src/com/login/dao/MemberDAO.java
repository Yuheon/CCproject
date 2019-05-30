package com.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

 
import com.login.beans.MemberBean;

public class MemberDAO {
	private static MemberDAO instance;
    private MemberDAO(){}
    
    //�̱��� ����
    public static MemberDAO getInstance(){
        if(instance == null ) instance = new MemberDAO();
        return instance;
    }
    
    // ȸ������ �޼���
    public void joinMember(MemberBean bean) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // ����
        	Class.forName("com.mysql.jdbc.Driver");
            String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);

        	String sql = "INSERT INTO jspdb VALUES(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
	            // MemberBean�� ��� ���� ������ �������� �����Ѵ�.
	            pstmt.setString(1, bean.getId());
	            pstmt.setString(2, bean.getPw());
	            pstmt.setString(3, bean.getName());
	            pstmt.setString(4, bean.getGender());
	            pstmt.setString(5, bean.getEmail());
	            // ��������
	            pstmt.executeUpdate();
 
        } catch (Exception sqle) {
            try {
                conn.rollback(); // ������ �ѹ�
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new RuntimeException(sqle.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    } // end joinMember()
    
 // �α��ν� ���̵�, ��й�ȣ üũ �޼���
    // ���̵�, ��й�ȣ�� ���ڷ� �޴´�.
    public int loginCheck(String id, String pw) 
    {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
 
        String dbPW = ""; // db���� ���� ��й�ȣ�� ���� ����
        int x = -1;
 
        try {
            // ���� - ���� �Էµ� ���̵�� DB���� ��й�ȣ�� ��ȸ�Ѵ�.
        	Class.forName("com.mysql.jdbc.Driver");
        	String jdbcDriver = "jdbc:mysql://127.0.0.1/jspdb";
          	String dbUser = "scott";
          	String dbPass = "1234";
        	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
        	String sql = "SELECT PASSWORD FROM jspdb WHERE ID=?";
            pstmt = conn.prepareStatement(sql);
            	pstmt.setString(1, id);
            rs = pstmt.executeQuery();
 
            if (rs.next()) // �Է��� ���̵� �ش��ϴ� ��� �������
            {
                dbPW = rs.getString("password"); // ����� ������ �ִ´�.
 
                if (dbPW.equals(pw)) 
                    x = 1; // �Ѱܹ��� ����� ������ ��� ��. ������ ��������
                else                  
                    x = 0; // DB�� ��й�ȣ�� �Է¹��� ��й�ȣ �ٸ�, ��������
                
            } else {
                x = -1; // �ش� ���̵� ���� ���
            }
            return x;
 
        } catch (Exception sqle) {
            throw new RuntimeException(sqle.getMessage());
        } finally {
            try{
                if ( pstmt != null ){ pstmt.close(); pstmt=null; }
                if ( conn != null ){ conn.close(); conn=null;    }
            }catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
    } // end loginCheck()


}

