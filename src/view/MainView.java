package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.Session;

public class MainView {
//	로그인이 완료되면 보여주는 메인 창임. 마찬가지로 기본생성자를 재정의하자. 
	public MainView() {
//	 	이 클래스도 사용자로부터 값을 입력 받는다. 
		Scanner sc = new Scanner(System.in);
//		여기서 다루는 데이터베이슨느 product이다. product의 데이터베이스를 다룰 DAO 클래스를 만든다. 
		ProductDAO pdao = new ProductDAO();
//		간편하게 사용하기 위한 user변수를 Session에 있는 정보를 담았다. 
		String user = Session.get("login_id");
//		나가기를 하기 전까지 반복시킴
		while(true) {
//			로그인을 하지 않으면 뒤로 보냄, 자바에서 접근점은 main하나라서 상관이 없지만 web브라우저에서는 접근점이 여러점이
//			존재하기 때문에 아래와 같이 명시적으로 접근을 금지시켜야 함. 
//			 login_id 정보가 없으면 null임으로 null일 경우 경고메시지와 break를 검. 
			if(Session.get("login_id") == null) {
				System.out.println("로그인 후 이용하세요");
				break;
			}
//			로그인 정보가 있으면 아래와 같이 상품관련 메시지를 보여줌. choice변수를 만들어 선택 번호를 변수에 담을 것임.
			System.out.println("☆★☆★☆★☆★"+user+"님 어서오세요~☆★☆★☆★☆★\n"
					+ "1. 상품추가\n2. 상품수정\n3. 상품삭제\n"
					+ "4. 내 상품 보기\n5. 상품 검색\n6. 내 정보 수정\n7. 로그아웃");
			int choice = sc.nextInt();
			
			if(choice == 7) {
//				만약 로그아웃을 누르면 로그인 정보를 없애면 됨. 
				System.out.println(user+"님 안녕히가세요~");
//				회원탈퇴가 아닌 로그아웃임으로 현재 Session에 담긴 정보를 null로 바꾸면 됨.
				Session.put("login_id", null);
//				그리고 나감. 
				break;
			}
//			나머지 정수를 입력 받으면 아래와 같이 처리함. 
			switch(choice) {
			case 1:
				//상품추가
//				상품추가는 여러 값을 입력받아야 됨으로 view클래스를 별도로 만드는 것이 좋다.
				new AddProductView();
				break;
			case 2:
				//상품수정
//				마찬가지로 상품을 수정할때 어떤 열을 수정할 지 모르니 값을 입력받는다. 따라서 별도의 클래스를 만든다.
				new ModifyProductView();
				break;
			case 3:
				//상품삭제
//				상품의 리스트를 사져온다.
				System.out.println(pdao.getList());
//				삭제할 상품 번호를 선택하도록 한다.
				System.out.print("삭제할 상품 번호 : ");
//				상품 번호를 입력 받고 입력받은 값을
				int prodnum = sc.nextInt();
//				상품 제거 메소드로 넘겨준다. 
				if(pdao.removeProduct(prodnum)) {
					System.out.println(prodnum+"번 상품 삭제 성공!");
				}
				else {
					System.out.println("알 수 없는 오류 / 다음에 다시 시도해 주세요.");
				}
				break;
			case 4:
				//내 상품 보기
				System.out.println("===========내가 올린 상품 목록===========");
				System.out.println(pdao.getList());
				System.out.println("===================================");
				break;
			case 5:
				//상품 검색
				System.out.println("검색어를 입력하세요 : ");
				sc = new Scanner(System.in);
				String keyword = sc.nextLine();
				System.out.println(pdao.search(keyword));
//				System.out.println("자세히 볼 상품번호 : ");
//				prodnum = sc.nextInt();
				new ProductInfoView();
//				자세히 볼 상품번호를 선택했다면 그 번호에 해당하는 상품 정보를 띄워주는
//				ProductInfoView 만들기!
//				1. 좋아요 누르기
//				2. 판매자 연락처 보기
//				3. 돌아가기 
				break;
			case 6:
				//내 정보 수정
//				내 정보를 수정하기 위해서는 여러 정보를 입력받기 때문에 마찬가지로 view클래스를 만든다.
				new MyInfoView();
				break;
			}
		}
	}
}









