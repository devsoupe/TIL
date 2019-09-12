# JAVA 웹 개발 마스터 올인원 패키지

https://www.fastcampus.co.kr/dev_online_jvweb/

다음의 내용은 `JAVA 웹 개발 마스터 올인원 패키지`를 보고 주관적으로 간략하게 정리한 내용입니다. 제대로된 학습을 원하신다면 강의 수강을 권장해 드립니다.

---

## 스프링 부트 프로젝트 2 (레스토랑 정보 예약 사이트 만들기)

### 01. 강의 소개 및 활용법

* 강의의 목적이 뭔지 다음엔 어떻게 될지를 관찰하고 생각해보도록 한다.
* 원하는 결과가 안나오고 오류가 날때는 외부에서 원인을 찾지 말고 '뭔가 잘못했겠지'라는 격억을 기억하도록 한다.
* 무엇을 했고 뭐가 어려웠고 다음엔 어떻게 해야할지를 고민해보도록 한다. 
* 배운것을 따라하는 훈련을 하고 문제를 해결하고 노트나 블로그에 기록하는 습관을 들이도록 한다.

### 02. 무엇을 만들 것인가?

* 프로그래밍이란 무엇인가? 프로그래밍이란 문제를 해결하는 것이다.
* 문제란 우리의 불편함에서 출발하고 불편함은 특정 분야에 대한 것이기도 한다.
* 이런 분야들을 도메인이라 부르고 전문적인 지식들이 필요하게 된다.
* 올바른 문제찾기는 대단히 어렵고 정말 어렵고 올바른 문제를 찾아 잘 해결하게 된다면 대박을 터트리게 되는 것이다.
* 이 강의는 '오늘 뭐 먹지?'라는 문제를 해결하려고 한다.
* '왜 고민하는가?'를 찾게 된다면 '무엇이 문제인가?'에 대한 답을 쉽게 찾을 수 있다.

```txt
[고민하는 이유]

1. 우리는 주변에 어떤 가게가 있는지 잘 모르지만 매일 같은 곳에 가는 건 싫어한다.
2. 어떤 가게가 좋은지 잘 모르고 기존에 방문한 사람들은 뭐라고 말했는지 알고싶다.
3. 예전에 봐둔 가게가 있었는데 기억이 나질 않고 가보고 싶은 곳을 기록하고 싶다.
4. 가고 싶다고 해도 지금 가면 자리가 있는지 모르겠고 전화보단 온라인으로 예약을 하고 싶다.
```

* 고민의 이유를 찾고 사용자 입장에서 해결 방법을 찾아 기능을 서술하는 것을 사용자 스토리라고 한다.
* (사용자)는 (가치)를 위해 (기능)을 할 수 있다.

```txt
[사용자는 누구인가?]

1. 서비스를 이용하는 고객
2. 고객에게 자기의 가게를 알리고 싶은 사장님
3. 고객과 가게 사장님께 이 서비스를 사용하도록 하길 원하는 관리자

[가치와 기능]

고객은 '뭘 먹고 싶은지 발견(가치)'할 수 있도록 '가게 목록을 볼 수(기능)' 있다.
고객은 '정확히 먹고 싶은게 뭔지 확인(가치)'하기 위해 '가게의 메뉴를 볼 수(기능)' 있다.
고객은 '좋은 가게인지 알 수(가치)' 있도록 '평점을 확인(기능)'할 수 있다.
고객은 '선택의 폭을 좁히기 위해(가치)' '가게 목록을 분류(기능)'에 따라 볼 수 있다.
고객은 '나와 남을 위해(가치)' 가게에 '평점과 리뷰(기능)'를 남길 수 있다.
고객은 '나중에 찾아보기 쉽도록(가치)' 가게를 '즐겨찾기에 추가(기능)'할 수 있다.
고객은 '가게에서 기다리지 않기(가치)' 위해 가게에 '인원, 메뉴를 예약(기능)'할 수 있다.
...

가게는 '관심 있는 고객을 받기(가치)' 위해 '예약 요청을 볼 수(기능)' 있다.
가게는 '더 나은 고객 서비스(가치)'를 위해 '예약 요청에 응답(기능)'할 수 있다.
...

관리자는 '고객이 서비스를 쓸 수(가치)' 있도록 '가게 정보를 등록(기능)'할 수 있다.
...

기타 등등
```

* 누가 어떤 가치를 위해서 어떤 기능을 사용할 수 있다라는 사용자 스토리는 서비스를 이해하고 같은 목표를 바라보는데 도움이 된다.
* 인간의 욕심은 끝이 없기에 '요구사항 변경은 반복된다는 사실'을 기억해야 한다.
* 요구사항이 바뀌는 것은 서비스를 향상시키려는 의도가 있으므로 좋은 것으로 받아들이고 변경을 적용하기 위해 계획을 잘 세워야 한다.

### 03. 어떻게 만들 것인가?

* 도메인 모델 - 우리가 해결하려는 각 문제에서 쓰이는 개념들을 정리하고 거기에 필요한 것들을 알아본다.

```txt
1. Restaurant (가게)
2. Menu Item (메뉴)
3. User (사용자)
4. Favorite (즐겨찾기)
5. Review (리뷰)
6. Reservation (예약)
```

* 시스템 아키텍처 - 우리가 제공하는 서비스는 프로그램으로만 이루어져있지 않고 소프트웨어를 구동할 인프라 하드웨어도 포함한다.
* 시스템 아키텍처를 올바르게 하기 위해서는 제약조건이 필요하다.

```txt
1. 서비스는 홈페이지를 통해 제공한다. (Web - Html, CSS)
2. 모바일 환경에서도 서비스를 제공한다.
3. 서로 다른 부분을 Front-end로 공통인 부분을 Back-end로 분리한다.
```

* 시스템 아키텍처에는 여러개의 계층으로 나눠서 구성하는 Multi-tier Architecture라는 방법이 있다.
* 가장 흔하게 쓰이는 방법은 3-tier Architecture이다. <p>
  | 이름 | 내용 | 용어 | 기술 | 
  |:--------|:--------|:--------|:--------|
  | Presentation | 사용자와 소통하는 부분 | Front-end | HTML/CSS/JS |
  | Business | 사용자가 요청했거나, 지금 해야할 일들을 처리하는 부분 | Back-end | REST API |
  | Data Source | 처리한 결과가 저장되는 부분 | Database | DBMS |
* 이번 강의에서는 Back-end에 집중한다.
* Bussiness에 대해 좀더 알아보면 이 계층에는 Layered Architecture가 사용된다. <p>
  | Layered Architecture |
  |:--------|
  | UI Layer |
  | Application Layer |
  | Domain Layer |
  | Infrastructure Layer |
  * 각 레이어는 바로 아래, 혹은 그 보다 아래있는 레이어에 의존한다.
  * 아래 있는 레이어는 위에 있는 레이어를 쓸 수 없다.
  * UI 레이어는 Application, Domain, Infrastructure 레이어를 사용하지만 Domain 레이어는 UI 레이어를 사용하지 않는다.
  * 이런 상관관계는 프로그램의 복잡도를 낮추기 위함이다. <p>
* 기술은 Java, Spring Boot, REST API를 사용한다.

### 04. Hello world

* [Spring Initializr](https://start.spring.io/) 사이트에서 간단한 정보만 입력하면 스프링 프로젝트를 시작할 수 있다.
* 선택한 정보는 다음과 같다.

```txt
Project - Gradle Project
Language - Java
Spring Boot - 2.1.8

Project Metadata
  Group - com.perelandrax
  Artifact - eatgo
  Options
    Name - eatgo
    Description - Eat Go Project
    Package Name - com.perelandrax.eatgo
    Packaging - Jar
    Java - 8

Dependencies 
  Spring Web
  Spring Boot DevTools
  Lombok    
```

* HELP.md를 README.md로 변경한다.
* Spring Initializr로 만든 프로젝트는 단일 모듈 프로젝트로 생성되나 멀티 모듈 프로젝트로 변경하여 사용하도록 한다. (eatgo-api 디렉토리 생성 후 src, build.gradle를 생성한 폴더로 옮기고 setting.gradle에 include 'eatgo-api'를 추가하면 된다)
* localhost:8080으로 접속시 Hello, world가 출력되도록 한다.

```txt
1. Layered Architecture에서 Presentation Layer를 추가하기 위해 interfaces 패키지를 추가한다.
2. WelcomeController를 추가하고 hello()함수를 추가해 GetMapping으로 URL("/")를 연결한다.
3. DevTools는 컴파일만 다시하게 되면 브라우저에 반영되도록 HotReload를 지원한다.
```

### 05. Test Driven Development

* 테스트 주도 개발의 의미를 목표 주도 개발로 변경하여 이해해 보도록 한다.
* 목표를 완료 했다는것을 어떻게 알 수 있는가? 바로 테스트를 해보는 것이다. 
* 테스트에는 우리가 달성해야할 목표가 정해져 있고 그게 통과가 되면 목표를 달성한 것이고 통과 하지 못했다면 목표를 달성하지 못한것이다.
* 테스트를 한다는 것은 사용자 입장에서 써본다는 의미도 들어있으므로 사용자 중심 개발이라고 표현할 수도 있다.
* 먼저 테스트를 작성함을 통해 제품 코드 없이 인터페이스를 중심으로 개발하게 되므로 인터페이스 중심 개발이라고도 할 수 있다.
* TDD를 하는 이유는 목표를 달성하기 위함이고 목표를 달성한다는 의미는 올바르게 작동하는 코드를 만든다는 뜻이된다. 또한 테스트 하기 좋은 코드를 만들게 되므로 깔끔한 코드를 작성하게 된다.
* 한마디로 정의해 TDD의 목표는 올바르게 작동하는 깔끔한 코드를 작성하는 것이다.

```txt
TDD의 목표를 어떻게 달성해야 할까?

1. 우선 올바르게(테스트를 통과하는) 작동하는 코드를 작성한다.
2. 깔끔한 코드를 위해 내부 구현을 리펙토링 한다.
```
  > TDD Cycle

  | 과정 | 내용 |
  |:--------|:--------|
  | Red | 결과를 기대하는 실패하는 테스트 코드 먼저 작성 (제품 코드가 없으므로 당연히 실패) |
  | Green | 빠르게 테스트가 통과할만큼의 제품코드만 작성하여 테스트를 통과시킴 |
  | Refactoring | 테스트는 그대로인 상태에서 제품 코드를 좀더 나은 코드 깔끔한 코드로 변경 |

* 도메인 모델 만들기 

```txt
1. domain 패키지 생성
2. domain 패키지 안에 Restaurant 클래스 생성
3. RestaurantTests 테스트 클래스 생성 (JUnit4)
```

### 06. REST API

* 다양한 환경을 지원하려면 어떻게 해야 할까?
* 서로 다른 Front-end를 통해 다양한 환경을 지원하고 대시 공통으로 사용하는 부분은 하나의 Back-end로 제공한다.
* Back-end는 어떻게 만들어야 하나? 이때 바로 REST API를 사용하게 된다.
* REST(REpresentational State Transfer) 표현상태를 전달하는 것을 의미하고 다르게 이야기하면 Resource를 처리함을 뜻한다.
* Resource를 처리하는 방법은 CRUD로 Resource를 Create, Read, Update, Delete하는 것을 의미하고 HTTP의 POST, GET, PUT/PATCH, DELETE와 대응된다.
* Resource를 지정할때 URI(Uniform Resource Identifier) 식별자를 사용하고 또는 URL(Uniform Resource Locator) 지시자를 사용하기도 한다.
* Resource는 크게 Collection, Member 두가지로 구분할 수 있고 Collection은 Read(List), Create를 할 수 있고 Memeber에 대해서는 Read(Detail), Update, Delete를 할 수 있다.
* Restaurant 사례로 Collection, Memeber에 대해 알아보도록 한다.
* Restaurant의 Collection은 http://host/restaurants 이와 같이 표현할 수 있다.
* Restaurant의 개별적인 Resource Member에 대해서는 http://host/restaurants/1 이렇게 표현한다.
* Resource에 대해 정보를 받거나 넘길때 JSON(JavaScript Object Notation) 포멧을 사용한다.

```txt
개별표현 (Member Resource)

{
  "id":2019,
  "name":"식당",
  "address":"골목"
}

목록표현 (Collection Resource)

[
  {
    "id":2001,
    "name":"오디세이",
    "address":"우주"
  },
  {
    "id":2019,
    "name":"식당",
    "address":"골목"
  }
]
```

* eatgo-api 프로젝트에서 제공할 API를 REST API에 맞춰 표현해보도록 한다.

  |  | 내용 |
  |:--------|:--------|
  | 가게 목록 | GET /restaurants |
  | 가게 상세 | GET /restaurants/{id} |
  | 가게 추가 | POST /restaurants |
  | 가게 수정 | PATCH(PUT) /restaurants/{id} | 
  | 가게 삭제 | DELETE /restaurants/{id} | 

### 07. 가게 목록

* 가게 목록을 만들어 보도록 한다.
* GET /restaurants에 대한 응답을 만들어 주면 된다.

```txt
결과 JSON

[
  {
    "id":2001,
    "name":"오디세이",
    "address":"우주"
  },
  {
    "id":2019,
    "name":"식당",
    "address":"골목"
  }
]

실제로는 Image URL등과 같은 추가 정보가 많이 필요하지만 이 시점에는 간단히 구현하도록 한다.
```


* GET /restaurants에 대한 처리를 할 수 있는 RestaurantController를 만들도록 한다.
* RestaurantControllerTest를 만들고 /restaurants 요청에 대해 기본적인 응답이 Ok인 상황을 구현한다.
* id, name, address 응답에 대한 테스트를 확인한다.
* 요청 결과가 객체에 정의된 변수명:값 JSON 형태로 나오는지 브라우저에서 확인한다.

 ### 08. 가게 상세 1

* 가게 상세를 만들어 보도록 한다.
* GET /restaurants/{id}에 대한 응답을 만들어 주면 된다.

```txt
결과 JSON

{
  "id":2019,
  "name":"식당",
  "address":"골목"
}
```

* GetMapping에 가변적인 값은 {name}과 같이 선언해준다.
* 가변 값이 들어오는 부분은 함수의 파라미터 이고 PathVariable("name") 형 변수이름 형태로 사용한다.
* 목록, 상세에서도 리스트를 사용하게 되는데 이는 중복 코드이고 중복 코드를 없애는 리펙토링을 통해 Repository 형태로 처리하도록 한다.

### 09. 가게 상세 2

* Repository를 이용해서 중복된 코드를 제거하는 방법을 알아보도록 한다.
* interfaces 패키지의 presentation 레이어에 해당하는 *Controller들은 로직이 들어있으면 안되고 비지니스 로직이 담긴 도메인 Repository와의 중간다리 역할만 해야 한다.
* 예제의 RestaurantRepository에서는 간단히 사용하기 위해 데이터가 담긴 그릇을 List를 이용했지만 DB같은걸로 변경해서 사용할 수 있고 그렇게 되면 더욱 많은 데이터를 핸들링 하는것이 가능해진다.

### 10. 의존성 주입

* 의존성 주입에 Dependency Injection에 대한 내용이다.
* 기존에 아무런 설명없이 사용했던 @Autowired에 대해 좀더 알아보도록 한다.
* 의존성이란 무엇인가? 의존 관계를 의미하고 의존 관계란 둘 이상의 객체가 협력하는 방법을 의미한다.
* A, B라는 객체가 있고 A는 B에 의존해서 어떤작업을 한다고 하는 것은 A는 B가 있어야 되고 B를 사용하게 된다는 의미이다.
* A가 B에 의존한다는 뜻은 A가 B를 사용한다는 것이다. 이런 의존은 B의 변화가 A에 영향을 주게된다.
* B가 변화를 계속 하게 된다면 지속적으로 A에 영향을 주게 되므로 관리가 필요하다.
* 우리의 사례에서는 RestaurantController는 RestaurantRepository에 의존한다는것을 알 수 있다.

```txt
RestaurantController에서 RestaurantRepository를 생성하는 역할을 담당
RestaurantController에서 생성된 RestaurantRepository 객체를 맴버변수에 연결
```

* 생성과 연결을 다른객체에서 하면 어떨까? 이것이 Spring IoC Container가 하는 역할이다.
* Dependency Injection은 이런 의존관계를 스프링을 이용해서 관리하는 것을 의미한다.
* 스프링에서는 이런 역할을 수행하기 위해 @Component, @Autowired 어노테이션을 지원한다.
* @Component 어노테이션을 붙인 RestrauantRespository를 @WebMvcTest 어노테이션을 붙인 테스트에서 사용할 수 없어 직접 주입해줘야 한다. (@SpyBean을 사용하면 원하는 객체 주입 가능)
* 의존성 주입의 장점은 객체를 원하는 상태로 다양하게 변경해서 주입가능하다는 것이다.
* 의존성 주입은 강하게 연결되어 있는 객체의 관계를 좀더 느슨하게 변경할 수 있다.





