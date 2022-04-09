# JAVA - 18

## 종합예제

### 상품검색 부분 추가하기

먼저 상품 자세히 보기는 기존에 만든 pdao.search메소드를 이용해서 사용자가 검색한 keyword를 넘겨주어 그 결과를 출력하도록 하였다.
상품을 자세히 보려면 어떻게 해야 될까??
상품보기에서 우리가 원하는 문자열대로 출력하기 위해 String.format을 이용해서 data들을 출력했었다.
마찬가지로 우리가 원하는 정보를 출력하기 위해서는 새로운 메소드를 생성해야 될 것이다. 여기서 끝나지 않고 사용자가 해당 상품번호를 선택했다면 해당 상품에대해서 좋아요와 판매자연락처보기 돌아가기 기능을 추가하고 싶다면 어떻할까?

어쨎든 우리는 상품검색이라는 선택창에 많은 기능을 넣고 싶어한다.
또한 사용자로부터 선택을 추가적으로 요구하고 있다.
그래서 클래스를 별도로 만들어 사용자에게 선택을 요구하고 선택에 맞는 결과값을 출력할 것이다.

```java
case 5:
		//상품 검색
		System.out.println("검색어를 입력하세요 : ");
		sc = new Scanner(System.in);
		String keyword = sc.nextLine();
		System.out.println(pdao.search(keyword));
	//아래와 같이 새로운 클래스를 정의해보자.
		new ProductInfoView();
```

#### new ProductInfoView();

해당 클래스의 기능은 사용자로부터 자세히 볼 상품의 번호를 받아서 그 번호에 해당하는 상품의 상세 설명을 출력하는 것이다.
따라서 상세 설명을 출력하는 기능을 넣어보자.

```java
package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.UserDAO;

public class ProductInfoView {
	public ProductInfoView() {
    //사용자로부터 입력받은 값을 가지기 위해 스캐너를 이용,
		Scanner sc = new Scanner(System.in);
     //우리는 상품 데이터를 활용해야 된다. 따라서 상품 데이터를 통신하는 클래스인
     //ProductDAO를 객체화한다.
		ProductDAO pdao = new ProductDAO();
     // 자세히 볼 상품번호를 입력 받는다.
		System.out.println("자세히 볼 상품번호 : ");
      //변수로 번호를 입력받는다.
     	int prodnum = sc.nextInt();
//		번호를 입력하면 해당되는 product를 자세히 보여주는 메소드를 실행한다.
		pdao.search(prodnum);
	}
}
```

pdao.search메소드를 구현해보자. 우리는 기존 메소드명과 동일한 메소드가 있지만 매개변수의 타입을 int타입으로 받음으로써 overloading 할 것이다.
또한 편의상 메소드 사용 시 바로 출력하도록 하였다.

---

#### ProductDAO에서 search메소드 정의

```java
//의도한데로 매개변수를 int타입으로 받는다.
public void search(int prodnum) {
//데이터 베이스 통신을 해야한다. 데이터베이스와 통신하는 conn변수와 select메소드를 이용한다.
//상품번호는 0번째에 있고 해당 번호를 String타입으로 넘겨주면
//HashSet에 데이터베이스 한줄이 들어가게 된다.
		HashSet<String> hi = conn.select(0, prodnum+"");
        //우리가 원하는 정보를 담을 수 있는 result변수를 만든다.
		String result = "";
        //물론 선택하는 번호는 중복되는 경우는 없지만 HashSet 타입의 정보를
        //쉽게 가져올 수 있는 foreach문을 사용했다.
		for (String line : hi) {
        //datas 배열에 데이터 한줄에 대한 정보를 넣었다.
			String[] datas = line.split("\t");
            //우리가 원하는 상세 내용은 datas의 4번방에 있다.
				result += String.format("상세 내용 : %s", datas[4]);
		}
		//result에 누적연산자를 이용해 값을 넣었고 그 값을 그대로 출력한다.
		System.out.println(result);
	}
```

---

다시 ProductInfoView로 돌아오자.
이제 그 상품에 대해 좋아요와 판매자 연락처, 돌아가기 기능을 구현하면된다.
먼저 사용자가 입력할 수 있도록

1. 좋아요
2. 판매자 연락처
3. 돌아가기
   위와 같은 형태로 보여주자

```java
System.out.println("1. 좋아요\n2. 판매자 연락처\n3. 돌아가기");
```

이제 사용자로부터 선택한 값을 변수에 할당하자

```java
int choice = sc.nextInt();
```

우리는 여기서 생각할 수 있다, 좋아요를 누르고 판매자 연락처를 누르면 이전화면으로 돌려 보내야 되는가? 아니면 좋아요도 누르고 판매자 연락처를 알 수 있을까??
그래서 while문으로 해당 질문을 반복해서 보여 줄 수 있도록 하자

```java
while(true) {
	System.out.println("1. 좋아요\n2. 판매자 연락처\n3. 돌아가기");
	int choice = sc.nextInt();
}
```

이제 사용자가 입력한 값에 따라 case별로 다른 기능을 구현하도록 하기위해 switch문을 사용한다.
또한 아래 case들이 의도치 않게 실행되는 것을 막기위해 break;를 각 case별로 작성하고 while 탈출을 막기위해 true가 아닌 불리언 타입의 일종의 체크포인트가 필요하다.
case3일 경우에만 while문을 탈출시키기 위해 flag라는 변수에 true를 할당하고 case3에서 flag에 false를 할당하여 탈출 시킬 것이다!

```java
// while문안에서 작성하면 무한반복임.
boolean flag = true;
while(true) {
	System.out.println("1. 좋아요\n2. 판매자 연락처\n3. 돌아가기");
	int choice = sc.nextInt();
		switch(choice) {
			case 1:
				//좋아요
				break;
			case 2:
				//판매자 연락처
				break;
			case 3 :
       //돌아가는 메시지와 함께 flag를 fasle로 만들어 while문을 탈출시킨다.
				System.out.println("돌아갑니다.");
				flag = false;
				break;
			default:
           		//다시 선택하도록 유도
				System.out.println("번호를 다시 선택해주세요");
				break;
		 }
}
```

자, 이제 좋아요를 만들어 보자.
사용자가 1번을 누르면 좋아요가 실행되고 실제 데이터베이스에 저장되어 있는 상품의 정보도 수정되어야 한다.
어떤 상품이 수정되는지 알려면 상품번호를 알아야 될 것이다.
우리는 while문 위에서 상세 정보를 보여주기 위해 사용자로부터 해당 상품번호를 받았다. 이렇게 받아온 상품정보는 prodnum이라는 변수에 할당되어 있으며 이를 활용하자.

상품 데이터베이스에 접근하려면 pdao를 이용해야 되고 pdao에 likecnt 메소드를 새롭게 정의하도록 하자.

```java
switch(choice) {
	case 1:
    //	likecnt메소드는 위에서 받아온 변수를 매개변수로 할당하고 있다.
		pdao.likecnt(prodnum);
		break;
        ...생략
```

#### ProductDAO에서 likecnt메소드 정의

```java
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
```

좋아요를 성공여부를 확인하기 위해 boolean타입으로 선언하였다.
다시 ProductInfoView로 돌아와서 case1을 다음과 같이 수정한다.

#### ProductInfoView

```java
boolean flag = true;
while(true) {
	System.out.println("1. 좋아요\n2. 판매자 연락처\n3. 돌아가기");
	int choice = sc.nextInt();
		switch(choice) {
			case 1:
				//좋아요
                if(pdao.likecnt(prodnum)) {
                // 성공 시 다음과 같은 메시지 출력
					System.out.println("좋아요 감사");
				}else {
                //실패 시 다음과 같은 메시지 출력
					System.out.println("알 수 없는 오류");
				}
				break;
			case 2:
				//판매자 연락처
				break;
			case 3 :
       //돌아가는 메시지와 함께 flag를 fasle로 만들어 while문을 탈출시킨다.
				System.out.println("돌아갑니다.");
				flag = false;
				break;
			default:
           		//다시 선택하도록 유도
				System.out.println("번호를 다시 선택해주세요");
				break;
		 }
}
```

---

이제 판매자 전화번호를 출력하는 기능을 구현하도록 하자
먼저 해당 상품의 정보를 살펴보자
만약 상품의 데이터 접근하면 다음과 같은 정보를 알 수 있다.

{_상품번호, 상품명, 가격, 재고, 상세설명, 좋아요, 판매자id_}

또한 유저 데이터에 접근하면 다음과 같은 정보를 알 수 있다.

{_id, pw, 이름, 나이, 전화번호, 주소_}

위와 같은 정보를 활용해서 판매자의 전화번호를 알아내면 된다.
먼저 특정 상품의 상품번호를 알 수 있다.

상품번호를 이용하여 판매자id를 가져오고 다시 판매자 id를 활용하여 전화번호에 접근하면 쉽게 문제를 해결 할 수 있다.
이제 유저 데이터베이스도 접근하니 UserDAO도 객체화하여 사용하자

```java
package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.UserDAO;

public class ProductInfoView {
	public ProductInfoView() {
		Scanner sc = new Scanner(System.in);
		ProductDAO pdao = new ProductDAO();
        // 유저정보에 접근해서 전화번호를 알아낼 것이다.
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
            	// 먼저 특정 상품에 해당되는 유저id를 알아내는 메소드를 만들자
                //이 메소드는 pdao에서 알 수 있다.
                //또한 우리는 prodnum 변수에 특정 상품의 상품번호를 알고있다.
                //따라서 메소드의 매개변수로 넘겨주자
				 pdao.searchPhone(prodnum);
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

```

#### productDAO

여기서 해당 상품번호에 해당되는 사용자정보를 가져오는 메소드를 정의한다.

```java
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
```

#### ProductInfoView

사용자 정보를 ProductInfoView에서 활용할 수 있도록 변수에 할당하자
변수에 할당한 userid를 다시 udao에 넘겨주어 해당 판매자의 전화번호를 알아내자.

```java
...생략
case 2:
// 메소드로 가져온 판매자 id를 userid 변수에 할당함.
	String userid = pdao.searchPhone(prodnum);
    // 해당 userid를 다시 udao에 넘겨주어 메소드를 통해 전화번호를 알아낸다.
	udao.contactSeller(userid);
	break;
...
```

#### UserDAO

전화번호를 가져오는 메소드를 정의하자

```java
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
```

> **ProductInfoView 전체 코드**

```java
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
```

## 개선사항

1. 반복되는 코드
2. 많이 사용되지 않고 있는 변수
3. 사용자가 잘못 입력 했을 경우에 대한 경우의 수 부족
