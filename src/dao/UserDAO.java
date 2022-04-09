package dao;

import java.util.HashSet;

import dto.UserDTO;

public class UserDAO {
//	데이터베이스와 통신하기 위한 클래스임. 
	DBConnection conn = null;
//	기본생성자를 재정의하여 데이터베이스에 접근하도록한다.
	public UserDAO() {
		conn = new DBConnection("database/UserTable.txt");
	}
//	받아온 객체는 DTO타입의 user로 불리언 타입메소드임
	public boolean join(UserDTO user) {
//		데이터베이스의 접근한 객체의 insert메소드를 활용하여 받아온 객체의 문자열을 넘겨준다.
		return conn.insert(user.toString());
	}
// 	중복검사 메소드, 매개변수는 String 타입의 userid를 받는다.
	public boolean checkDup(String userid) {
//		HashSet타입의 객체를 생성하여 정보를 가져온다.(select를 이용하기 위해서는 HashSet타입으로 사용)
		HashSet<String> rs = conn.select(0,userid);
//		가져온 객체의 size가 0인지 돌려주면 된다. 아이디가 없어야 사용가능, 있으면 불가.
		return rs.size() == 0;
	}

//	LoginView에서 두개의 매개변수, 아이디, 비밀번호를 받아와서 로그인 여부를 확인
	//							apple		 abcd1234
	public boolean login(String userid, String userpw) {
//		데이터베이스에 접근하는데 select메소드를 이용함, userid가 동일한 데이터베이스 행을 가져옴. 
		HashSet<String> rs = conn.select(0, userid);
		if(rs.size() == 1) {
//			해쉬셋으로 가져온 객체가 값이 존재하면 아래를 실행, 
			for(String line : rs) {
//				아래의 문자열을 가져오게 된다. 
				//line : "apple	abcd1234	김사과	10	01012341234	서울시 강남구 역삼동"
				//"문자열1".split("문자열2") : "문자열1" 을 "문자열2" 기준으로 나누기
				// 						나뉘어진 문자열들이 담겨있는 배열 return
				//{"apple","abcd1234","김사과","10","01012341234","서울시 강남구 역삼동"}
//				split은 위와 같은 배열을 리턴하는데 1번방에 비밀번호가 있다. 이 비밀번호와 일치하는지 여부를 확인 
				if(line.split("\t")[1].equals(userpw)) {
					//로그인 성공
//					Session.datas.put("login_id",userid);
//					일치하면 Session에 로그인 정보(아이디)를 넣는다. 
					Session.put("login_id", userid);
					return true;
//					true를 리턴
				}
			}
		}
//		일치하는 아이디가 없으면 바로 false를 리턴.
		return false;
	}
//	유저 정보 메소드임. 
	public String myInfo() {
//		유저 정보를 가져오는데 해당 login_id를 이용한다ㅣ. 
		HashSet<String> rs = conn.select(0, Session.get("login_id"));
//		유저 정보를 원하는 값으로 리턴하기 위해 result를 정의함.
		String result = "";
//		가져온 정보는 행으로 된 정보임 원하는 정보로 바꿔보자.
		for(String line : rs) {
//			마찬가지로 datas에 유저 정보들을 배열로 담는다. 
			String[] datas = line.split("\t");
//			각 배열 정보를 이용하여 아래와 같이 result에 추가한다.
			result += "======"+datas[2]+"님의 회원정보======\n";
			result += "아이디 : "+datas[0]+"\n"; 
			result += "비밀번호 : "+datas[1]+"\n"; 
			result += "핸드폰 번호 : "+datas[4]+"\n"; 
			result += "주소 : "+datas[5]+"\n"; 
		}
//		result를 리턴한다.
		return result;
	}
//	수정하기 위한 메소드임, 
	public boolean modifyUser(int col, String newData) {
//		업데이트 메소드를 이용하여 유저 정보를 수정한다ㅣ.
		return conn.update(Session.get("login_id"), col, newData);
	}
//	사용자 정보를 확인하는 메소드 
	public boolean checkPw(String userpw) {
//		비밀번호를 매개변수로 입력 받고 login_id에 해당되는 유저 정보를 받는다.
		HashSet<String> rs = conn.select(0,Session.get("login_id"));
		for(String line : rs) {
//			가져온 유저정보와 데이터베이스의 유저정보가 일치하는지 확인한다.
			return line.split("\t")[1].equals(userpw);
		}
//		일치하지 않으면 false를 리턴한다.
		return false;
	}
//	회원 탈퇴 메소드임.
	public boolean leaveId() {
//		로그인 로그인 정보를 null로 만들면 delete에 인자를 넘겨줄 수없기 때문에 아래와 같이 백업을 하고 작업한다.
		String userid = Session.get("login_id");
		Session.put("login_id", null);
		return conn.delete(userid);
	}
	
//	외부로부터 사용자 id를 전달받는 메소드 
	public void contactSeller(String userid) {
//		conn.select에 userid를 넘겨주어 해당되는 정보를 HashSet타입으로 받는다.
		HashSet<String> rs = conn.select(0, userid);
//		마찬가지로 해당 전화번호를 result로 받을 것이다. 
		String result = "";
//		해당 유저 정보도 1개에 불과하지만 HashSet으로 받았기에 foreach로 정보를 가져왔다.
		for (String line : rs) {
//			datas 배열에 유저 정보를 담고
			String[] datas = line.split("\t");
//			전화번호가 있는 배열방을 result에 담는다.
			result += "연락처 : "+datas[4]+"\n"; 
		}
//		해당 정보를 바로 출력했다. 
		System.out.println(result);
	}
}










