package web.jsp15.mvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 서블릿 컨테이너에 처음 요청들어오면 자동으로 객체 생성되고 서버가 stop될때까지 살아있다. (마치 싱글턴처럼 사용됨)
public class MemberTestController extends HttpServlet{

	// Map (key=uri, value=uri맞는 Bean클래스 객체)
	// service -> 요청 -> Map에 키로 던져 value=Bean클래스 -> actionBean
	
	// key = uri, value = uri에 맞는 Bean 클래스의 생성된 객체들을 저징해놓을 저장소
	private Map beansMap = new HashMap(); //객체생성
	


	//#1  init : 오버라이딩 : Bean 클래스들을 미리 로딩 시켜놓고 service에서 요청에 따라 필요한거 가져다 써라 ~~ 
	@Override //init 메서드
	public void init(ServletConfig config) throws ServletException {
		String path = config.getInitParameter("propertiesPath");//param-name :propertiesPath 과 동일하게
		System.out.println("path :" + path);//경로가 나옴
		
		//Properties 클래스로 Properties 파일 정보 로드 시키기			 util 패키지에 있는 Properties  / io 패키지에 있는 InputStream
		InputStream is = null; // Properties 타입의 외부파일 가져올때 사용
		Properties p = null; //Properties파일 로드 시켜주는 기능을 위해 사용
		try {
			p = new Properties(); //Map타입
			is = new FileInputStream(path); // 외부파일 가져올때 스트림 생성, 생성자 인수로 파일저장경로 던져주기.
			
			//로드 시키기(key, value)
			p.load(is); // (key.value) 형식으로 만들어줌	*load : 외부파일을 스트림으로 읽은것을 로드시킴
			System.out.println(p);
			
			
			Iterator it = p.keySet().iterator(); //반복자 얻어오기 key만꺼내오기  
											 	//p.keySet : 모든 키를 담은 Set<K> 컬렉션 리턴 / ineratior(반복자) : 요소를 순차적으로 검색할때 사용하면 편리함
			
			while(it.hasNext()) {	//hasNext : 접근할 다음번쨰 요소가 있으면 true
				String key = (String)it.next();		//next : 다음번째 요소 리턴
				String value = p.getProperty(key); 	//키 값 가지고 value를 꺼내주기
				System.out.println("key : " + key);
				System.out.println("value : " + value);
				
				//Properties 파일은 원본 그대로 유지
				//beansMap에 uri경로 = key, Bean 클래스 객체 = value 정리해서 저장
				Class c = Class.forName(value); // Bean 클래스 경로명(문자열)으로 해당 클래스 찾아 리턴받기 //텍스트를 class형식으로 객체생성해서 바꾸고 (beansMap)에  key value형식으로 저장
				Object obj = c.newInstance();	// 해당 클래스로 객체 생성 new xxxBean()과 동일
				beansMap.put(key, obj);			// map에 저장
			}
			System.out.println("beansMap : " + beansMap);
			
			//		p 	= key = uri(String) / value = Bean (String)
			//beansMap	= key = uri(String) / value = Bean(객체생성된거)
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI();
		System.out.println("uri : " + uri);
		
		//http://localhost:8080/web/main.git -> main.jsp
		//http://localhost:8080/web/index.git -> index.jsp
		/*
			//uri에 따른 처리
			String view="";
			if(uri.equals("/web/main.git")) {
				// 처리 : DTO, DAO 등등 : jsp페이지에 섞여있던 자바 코드가 다 이곳으로 이동, jsp 페이지는 결과만 보여준다.
				view="/jsp15/main.jsp";
			}else if(uri.equals("/web/index.git")) {
				// 비즈니스 로직처리
				view="/jsp15/index.jsp";
			}// 단점 -> if-else 페이지 개수만큼 else if 존재 코드가 많고 복잡해진다.->비지니스 로직처리를 Model 클래스로 분리
		*/
		
//		String view ="";
//		if(uri.equals("/web/loginForm.git")) { 1번쨰
//			//LoginFormBean lfb = new LoginFormBean(); init으로 한번씩 만들어둠
//			//view = lfb.actionBean(request, response);
//		}else if(uri.equals("/web/loginPro.git")) {
//			//비지니스 로직 처리
//			//LoginProBean lpb = new LoginProBean(); init으로 한번씩 만들어둠
//			//view = lpb.actionBean(request, response);
//		}
		
		//Map map = new HashMap();  저장 put(key,value); 값 꺼내기 .get(key); 하나씩 모두 접근 .keySet().inerator(); 반복문
		
		//uri = key값 주고 value = Bean클래스 객체 꺼내기 -> 이유는 요청에 맞는 actionBean메서드 호출하고 맞는 view주소 리턴 받기 위해
		Object obj = beansMap.get(uri);//2번째
		String view ="";
		SuperBean sb = null;
		if(obj instanceof SuperBean) {
			sb = (SuperBean)obj; // 형변환할건데 부모타입으로 받아서
			view = sb.actionBean(request, response); //모델한테 위임 : 비즈니스 로직 처리하고 이동할 view 리턴해줘~~
		}
		
		
		//jsp페이지 만들고, Bean 클래스 만들고, -> properties 파일에 한줄 추가(주의점! Bean클래스를 먼저 만들고 properties에 추가해야한다.)
		
		
		
		request.getRequestDispatcher(view).forward(request, response);
	}
}
