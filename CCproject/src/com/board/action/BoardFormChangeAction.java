package com.board.action;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.common.action.Action;
import com.common.action.ActionForward;
 
/**
 * 화면 전환을 처리하는 Action
 *
 */
public class BoardFormChangeAction implements Action
{
    private String form = "main.jsp?contentPage=";
    private String path;
    
    /**
     * 명령어로부터 다음 이동할 페이지 경로를 생성한다.
     * @param command 명령어
     */
    public void setCommand(String command){
        int idx = command.indexOf(".");
        path = command.substring(0, idx)+".jsp";
    }
 
    @Override
    public ActionForward execute(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ActionForward forward = new ActionForward();
        
        forward.setRedirect(false);
        
        // 메인화면일 경우 MainForm.jsp만 경로로 지정한다.
        if(path.equals("main.jsp"))
            forward.setNextPath(path);
        else
            forward.setNextPath(path);
        
        return forward;
    }
}


