package web.jsp15.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Bean 클래스 : loginForm 용
public class LoginFormBean implements SuperBean{
	
	//String id = request.getParameter("id");
	//String pw = request.getParameter("pw");
	//DAO
	//session.set~

	//리턴타입 String : 이동할 jsp view 경로 리턴해줄것임.
	//매개변수와 throws는 Controller의 서비스 메서드와 동일하게 -> 정상적인 비지니스 로직 처리를 하려면 request, response 정보가 필요함
	//일반 클래스에서는 서블릿이 아니기 때문에 자동으로 채워주지 못하기 때문에

	public String actionBean (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		System.out.println("loginFormBean 실행 !!!!");
		
		return "/jsp15/loginForm.jsp";//이동할 jsp 페이지의 경로 리턴
	}
	
}
