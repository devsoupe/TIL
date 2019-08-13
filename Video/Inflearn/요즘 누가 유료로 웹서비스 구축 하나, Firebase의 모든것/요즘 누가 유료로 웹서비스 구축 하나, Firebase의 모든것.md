# 요즘 누가 유료로 웹서비스 구축 하나, Firebase의 모든것

https://www.inflearn.com/course/firebase-server#

다음의 내용은 `요즘 누가 유료로 웹서비스 구축 하나, Firebase의 모든것`를 보고 주관적으로 간략하게 정리한 내용입니다. 제대로된 학습을 원하신다면 강의 수강을 권장해 드립니다.

---

## 웹 서비스

### 강의 소개

* Firebase를 이용해서 웹 호스팅, 데이터베이스 관리, 스토리지 관리, 서버사이드 작업을 한번에 어떻게 할 수 있는지 알려준다.
* 리눅스 서버 구입, 웹 호스팅 의뢰 등 직접 해야했던 많은 부분을 Firebase 을 사용해 편리하게 사용할 수 있다.
* 우리나라는 웹 프론트, 백엔드, DB 개발에 대한 롤이 명확해서 기존 방식을 고수하다보니 Firebase를 잘 모르는 측면이 있다.
* Firebase에 기반한 웹 서비스, Hosting, Auth(로그인), Real Time Database, Storage, 서버사이드 작업을 할 수 있는 Cloud Function, 웹 서비스를 운영하기 위한 기타 Library를 소개한다.
* 개발환경은 Mac, Chrome, VisualStudio Code, npm으로 구성한다.

### 웹서비스에 대한 이해

* 웹플랫폼은 제공되는 URL이 있고 크롬, 익스플로러, 파이어폭스 같은 다양한 디바이스 브라우저를 통해 접근가능하게 만든 어플리케이션이다.
* 웹앱은 앱처럼 느껴지지만 위 웹플랫폼을 감싸고 있는 앱이라고 볼 수 있다.
* 웹서비스는 HTML에서 스타일리쉬한 모습을 표현하기 위해 CSS 스타일 언어 추가되고 동적인 반응을 표현하기위해 JavaScript가 생겨나 HTML을 제어하게 된다.
* 최근 Angular, React, Vue 등등 새로운 프레임워크가 생겨나서 프론트엔드를 표현하지만 그 끝을 따라가보면 결국엔 JavaScript로 변환되어 표현되어진다.
* 웹에서의 JavaScript는 일반 어플리케이션의 C언어 같은 위치를 차지하고 있다.
* 데이터 관리가 없는 웹은 HTML 그 이상도 그 이하도 아니다.
* 데이터 관리를 위해 DB 서버에 접근해야 되는데 웹에서 DB 서버에 접근할 수 있는 구조가 아니기 때문에 중간다리 역할이 필요하게 되어 서버(BackEnd) 개념이 도입되게 된다.
* 서버는 Java 혹은 Node.js로 만들어지게 되고 웹과도 통신을 해야 하므로 Tomcat 어플리케이션이 구동되어 진다.
* 일련의 라이프 사이클이 만들어지게 되고 [웹 프론트] <-- ajax/axios 통신 --> [어플리케이션 서버] <-- DB라이브러리 --> [DB 서버]와 같은 웹 서비스 구조가 만들어지게 된다.

### AWS / IDC / Cloud(Firebase) 비교 이해

* 초기 스타트업 서비스에서는 굳이 AWS를 구축할 필요가 없다고 생각한다.
* IDC는 Internet Data Center로 물리적인 Application, Database, Storage Machine를 제공해준다. 
* IDC를 사용하게 되면 직접 콘솔로 접근하여 관리, 배포를 해줘야 하므로 관리 포인트가 많다.
* AWS는 Amazon Web Services로 IDC와 비슷하지만 각각의 Machine을 논리적인 플러그인 방식으로 제공하므로 필요에 따라 쉽게 추가하거나 빼거나하는게 가능한 서비스이다.
* AWS도 필요한 부분에 대해 직접 관리가 필요하지만 콘솔이 아닌 웹 페이지에서 쉽게 관리를 할 수 있다.
* Cloud Firebase는 선택의 권한이 없고 이미 Google이 Application, Database, Storage Machine들을 세팅해 놓은 상태이다.
* Cloud Firebase는 관리도 직접 Google이 하게 되고 배포만 관리하면 된다.

---

## Firebase Hosting

### Hosting #1 : Hosting 이해 및 custom 도메인 연결

* 사이트를 호스팅 하기 위해서는 npm install -g firebase-tools 명령으로 Firebase CLI 툴을 설치해야 한다.
* 배포를 하기 위해서는 firebase login -> firebase init -> firebase deploy를 해야한다.

#### 1. firebase-tools 설치과정

```txt
$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
$ brew install node
$ brew link node

버전확인

$ node -v
$ npm -v

cli 설치

$ mkdir firebase-tests
$ cd firebase-test
$ npm install -g firebase-tools
```

#### 2. deply 과정

```txt
$ firebase login (브라우저에서 로그인)
$ firebase init 
- Hosting: Configure and deploy Firebase Hosting sites (Hosting으로 이동 후 Space로 선택 후 Enter)
- 프로젝트 이름인 firebase-test으로 이동 후 Enter
- What do you want to use as your public directory? (그냥 Enter)
- Configure as a single-page app (rewrite all urls to /index.html)? (그냥 Enter)
$ cd public (templated.co 사이트에서 정적인 페이지를 다운받아 public 폴더안에 복사)
$ firebase deploy (명령 후 firebase 콘솔에서 배포됨을 확인)
```

* 위 과정을 통해 기본적이 정적인 페이지 Hosting을 완료했다. 

---

## Firebase Auth

### Auth #1 : 회원가입 (로그인)

* Firebase는 인증기능을 제공해준다.
* 인증에는 이메일, 전화, 소셜등 여러가지 방법을 가지고 있다.
* 인증 기능을 사용할때 주의할점은 사용할 도메인을 승인된 도메인에 추가시켜줘야 한다.
* 기능을 사용하기 위해 [문서]( https://firebase.google.com/docs/web/setup?authuser=0#config-web-app)를 참고하여 몇가지 설정을 해줘야 한다.

#### 1. 필요한 기능 스크립트 추가

> index.html
 
```html
<body>
  ...
  <!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-app.js"></script>

  <!-- Add Firebase products that you want to use -->
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-auth.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-database.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-firestore.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-messaging.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-functions.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-storage.js"></script>
  ...
<html>
```

#### 2. Firebase 앱 등록 및 초기화

```txt
1. Firebase 콘솔로 이동
2. 왼쪽 상단 Project Overview 옆 아이콘을 누르고 프로젝트 설정을 누름
3. 일반 탭에서 내 앱을 웹앱으로 등록하고 '연결된 Firebase 호스팅 사이트'는 이미 만든 호스팅을 선택함
```

> auth.js

```js
// Your web app's Firebase configuration
var firebaseConfig = {
  apiKey: "AIzaSyCM4k3cT6jMLjlApFKdq0UghBN6g6eX-nY",
  authDomain: "fir-tests-6f8f8.firebaseapp.com",
  databaseURL: "https://fir-tests-6f8f8.firebaseio.com",
  projectId: "fir-tests-6f8f8",
  storageBucket: "",
  messagingSenderId: "1098700087057",
  appId: "1:1098700087057:web:eecfc8555c7dc0d7"
};
```

#### 3. auth 테스트

```txt
1. auth.js 파일에 아래 내용 추가함
2. index.html에 auth.js 추가함
3. 로컬에서 index.html을 실행해보면 Firebase가 정상적으로 initialize 되나 제대로된 로그인이 되지 않아 콘솔에 err no yet이 출력됨
```

> auth.js

```js
// Your web app's Firebase configuration
var firebaseConfig = {
  apiKey: "AIzaSyCM4k3cT6jMLjlApFKdq0UghBN6g6eX-nY",
  authDomain: "fir-tests-6f8f8.firebaseapp.com",
  databaseURL: "https://fir-tests-6f8f8.firebaseio.com",
  projectId: "fir-tests-6f8f8",
  storageBucket: "",
  messagingSenderId: "1098700087057",
  appId: "1:1098700087057:web:eecfc8555c7dc0d7"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

$(document).ready(function ($) {
  firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
      console.log('log', user);
    } else {
      console.log('err', 'not yet')
    }
  });
});
```

> index.html

```html
<body>
  ...
  <!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-app.js"></script>

  <!-- Add Firebase products that you want to use -->
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-auth.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-database.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-firestore.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-messaging.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-functions.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-storage.js"></script>

  <script src="assets/js/auth.js"></script>
  ...
<html>
```

#### 4. 정상적인 log 유저정보 출력

### Auth #2 : 회원가입 (신규가입)

### Auth #2 : 회원가입 (Social 로그인 연동)

---

## Firebase Real-Time DB

### Database #1 : Firebase DB 이해

### Database #2 : Rule 정의 및 Insert

### Database #3 : Query

### Database #4 : Update

### Database #5 : Delete

---

## Firebase Storage

### Storage #1 : Storage 이해 및 이미지 업로드

### Firebase Cloud 함수

---

## Cloud 함수 #1 : Cloud 함수 이해 및 외부 API송신

### Cloud 함수 #2 : 이메일 송신 및 Cron job 소개

---

## 부가 기능

### 부가 기능 #1 : 무료 Javascript 암호화, 무료 웹앱
