package com.board.action;
 
import java.io.PrintWriter;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.board.controller.CommandAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
 
public class CommentUpdateAction implements Action
{
    @Override
    public ActionForward execute(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        // �Ķ���͸� �����´�.
        int comment_num = Integer.parseInt(request.getParameter("comment_num"));
        String comment_content = request.getParameter("comment_content");
        
        CommentDAO dao = CommentDAO.getInstance();
        
        CommentBean comment = new CommentBean();
        comment.setComment_num(comment_num);
        comment.setComment_content(comment_content);
        
        boolean result = dao.updateComment(comment);
        
        response.setContentType("text/html;charset=euc-kr");
        PrintWriter out = response.getWriter();
        
        // ���������� ����� ����������� 1�� �����Ѵ�.
        if(result) out.println("1");
        
        out.close();
        
        return null;
    }
}
 


��ó: https://all-record.tistory.com/146?category=733042 [������ ��� ���]