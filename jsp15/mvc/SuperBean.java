package web.jsp15.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//interface로 부모클래스 super 만들어 준다
public interface SuperBean {

	public String actionBean (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;//세미콜론
	
	
	
}
