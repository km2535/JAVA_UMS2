package dao;

import java.util.ArrayList;
import java.util.HashSet;

import dto.ProductDTO;

public class ProductDAO {
//	데이터베이스와 통신할 준비
	DBConnection conn = null;
//	기본생성자를 재정의하여 객체가 생성될때 마다 데이터베이스와 연결해준다.ㅣ
	public ProductDAO() {
		conn = new DBConnection("database/ProductTable.txt");
	}
//	사용자가 마지막으로 입력한 상품 번호를 리턴해줘야한다. 
	public int getLastNum() {
//		이미 만들어진 메소드로 lastPK를 이용한다,
		String lastPK = conn.lastPK();
//		만약 입력된 키가 없다면 0을 리턴하면 되고(0+1을 하여 1부터 시작할 것이다.)
		if(lastPK == null) {
			return 0;
		}
//		입력된 키가 있다면 정수타입으로 형변환하여 마지막 값을 리턴한다.
		else {
			return Integer.parseInt(lastPK);
		}
	}
//	추가 메소드를 만든다.
	public boolean addProduct(ProductDTO product) {
//		데이터베이스의 insert메소드를 이용하여 넘기는데 매개변수의  toString()메소드형태로 값을 넘긴다.
		return conn.insert(product.toString());
	}
//	상품을 모두 보여줄 메소드를 만든다  
	public String getList() {
//		상품을 가져오긴 하는데 로그인된 유저가 넣은 상품만 불러와야 한다. 
//		따라서 매개변수를 다음과 같이 넣는다. 
		HashSet<String> rs = conn.select(6,Session.get("login_id"));
//		가져온 값을 원하는 정보만 담기위해 result를 만듬.
		String result = "";
		//1. 구찌 지우개 : 10000원(재고 : 3개)
		//2. 루이비똥 연필 : 10000원(재고 : 3개) 
//		HashSet을 반복문을 돌려서 값을 받아냄. 
		for(String line : rs) {
//			line에는 상품의 한줄이 담겨있을 것이고 탭으로 분리하여 배열에 담아준다. 
			String[] datas = line.split("\t");
//			해당 배열의 들어있는 값들을 result에 String.format형식에 맞추어 넣는다. 
			result += String.format("%s. %s : %s원(재고 : %s개)\n", datas[0],datas[1],datas[2],datas[3]);
		}
//		그 결과를 리턴하면 된다.
		return result;
	}
//	상품을 없앨 메소드를 정의한다. MainView 클래스에서 전달 받은 prodnum을 데이터베이스 연결 클래스의 delete 메소드를 이용한다.
	public boolean removeProduct(int prodnum) {
//		해당 메소드는 String을 받기때문에 문자열로 형변환한다.
		return conn.delete(prodnum+"");
	}
//	3가지 정보를 받아온다. 수정할 상품번호, 수정할 내용, 새로운 내용
	public boolean modifyProduct(int prodnum, int choice, String newData) {
//				상품번호 상품명 상품가격	재고	설명		좋아요 판매자
		//		1	바나나	1000	10	맛좋은 바나나	 0	apple
//		가격은 2번방으로 선택한 번호의 + 1, 재고랑 설명도 마찬가지임, 
//		prodnum은 문자열로 형변환 시켜서 받도록 하자.
		return conn.update(prodnum+"", choice+1, newData);
	}
//	상품을 모두 제거하는 메소드, 회원 탈퇴 시 필요함. 
	public void removeAll() {
//		해당 유저가 올린 상품을 모두 가져온다
		HashSet<String> rs = conn.select(6,Session.get("login_id"));
		for(String line : rs) {
//			가져온 상품들을 모두 삭제한다. 
			conn.delete(line.split("\t")[0]);
		}
	}

	public String search(String keyword) {
		HashSet<String> rs = conn.select();
		ArrayList<String> result = new ArrayList<String>();
		for (String line : rs) {
			String[] datas = line.split("\t");
			if(datas[1].contains(keyword)) {
				result.add(line);
			}
		}
		String resultMsg = "\""+keyword+"\"로 검색된 결과 \n";
		if(result.size() ==0) {
			resultMsg += "해당하는 상품이 없습니다.\n";
		}else {
			for (String line : result) {
				String[] datas = line.split("\t");
				resultMsg += String.format("%s. %s : %s원(재고 : %s개) - %s\n", 
						datas[0],datas[1],datas[2],datas[3], datas[6]);
			}
		}
		return resultMsg;
	}
	
	public void search(int prodnum) {
		HashSet<String> hi = conn.select(0, prodnum+"");
		String result = "";
		for (String line : hi) {
			String[] datas = line.split("\t");
				result += String.format("상세 내용 : %s", datas[4]);
		}
		
		System.out.println(result);
	}
	
	
//	prodnum은 사용자로부터 int타입으로 매개변수를 받는다.
	public boolean likecnt(int prodnum) {
//		search 메소드와 동일하게 conn.select를 이용하여 HashSet타입으로 만든다.
		HashSet<String> hi = conn.select(0, prodnum+"");
//		그 결과를 result에 담을 것이다.
		String result = "";
//		기존에 좋아요 갯수를 가져와서 그 좋아요 갯수에서 1개를 추가하는 방식으로 만들 것이다.
//		기존의 좋아요를 가지고 오기위해 아래와 같이 만든다. 
//		편의상 방식은 search와 동일하게 가져왔다. 
		for (String line : hi) {
			String[] datas = line.split("\t");
//			좋아요는 5번방에 들어있다. 
				result += String.format("%s", datas[5]);
		}
//		result는 문자열 타입으로 정수타입으로 형변환시키고 1을 더한 값을 likecnt에 할당한다.
		int likecnt = Integer.parseInt(result) + 1;
//		상품을 수정하기 위해 update메소드를 이용했다.
//		수정되는 상품번호는 prodnum이고 수정할 자리는 좋아요 자리인 5번이다.
//		좋아요가 추가된 likecnt를 newData매개변수 자리에 두면 된다.
		return conn.update(prodnum+"", 5, (likecnt+""));
	}
	
	// 상품번호를 받는 메소드임.
	public String searchPhone(int prodnum) {
//		상품 정보를 가져오는 코드는 동일함.
		HashSet<String> hi = conn.select(0, prodnum+"");
//		해당 사용자id를 result에 담을 것임.
		String result = "";
//		상품에 대한 정보를 가져온다.(search와 동일)
		for (String line : hi) {
			String[] datas = line.split("\t");
//			사용자 id는 6번방에 있다. 
				result += String.format("%s", datas[6]);
		}
//		사용자 id를 return 시킨다. String타입 메소드로 변경함.
		return result;
	}
	
	
	
}

















