package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.UserDAO;

public class ProductInfoView {
	public ProductInfoView() {
		Scanner sc = new Scanner(System.in);
		ProductDAO pdao = new ProductDAO();
		UserDAO udao = new UserDAO();
		System.out.println("자세히 볼 상품번호 : ");
//		sc = new Scanner(System.in);
		int prodnum = sc.nextInt();
//		번호를 입력하면 해당되는 product를 자세히 보여줌 
		pdao.search(prodnum);
		boolean flag = true;
		while(flag) {
			System.out.println("1. 좋아요\n2. 판매자 연락처\n3. 돌아가기");
			int choice = sc.nextInt();
			switch(choice) {
			case 1:
				if(pdao.likecnt(prodnum)) {
					System.out.println("좋아요 감사");
				}else {
					System.out.println("알 수 없는 오류");
				}
				break;
			case 2:
				String userid = pdao.searchPhone(prodnum);
				udao.contactSeller(userid);
				break;
			case 3 :
				System.out.println("돌아갑니다.");
				flag = false;
				break;
			default:
				System.out.println("번호를 다시 선택해주세요");
				break;
		  }
		}
	}
}
