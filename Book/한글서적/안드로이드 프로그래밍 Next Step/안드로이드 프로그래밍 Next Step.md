# 안드로이드 프로그래밍 Next Step 요약

http://www.yes24.com/24/goods/41085242?scode=032&OzSrank=1

다음의 내용은 `안드로이드 프로그래밍 Next Step`을 읽고 주관적으로 간략하게 정리한 내용입니다. 제대로된 학습을 원하신다면 책을 읽을 것을 권장해 드립니다.

---

## 1. 안드로이드 프레임워크

### 1.1. 안드로이드 아키텍처 개요

#### 1.1.1. 애플리케이션

#### 1.1.2. 애플리케이션 프레임워크

#### 1.1.3. 안드로이드 런타임

#### 1.1.4. 라이브러리

#### 1.1.5. 리눅스 커널

### 1.2. 프레임워크 소스

### 1.3. 안드로이드 버전

#### 1.3.1. 호환성 모드

#### 1.3.2. 호환성 모드 동작 예시

#### 1.3.3. 단말 버전 체크

---

## 2. 메인 스레드와 Handler

### 2.1. UI 처리를 위한 메인 스레드

### 2.2. Looper 클래스

### 2.3. Message와 MessageQueue

### 2.4. Handler 클래스

#### 2.4.1. Handler 생성자

#### 2.4.2. Handler 동작

#### 2.4.3. Handler 용도

#### 2.4.4. Handler의 타이밍 이슈

### 2.5. UI 변경 메커니즘

### 2.6 ANR

#### 2.6.1. ANR 타임아웃

#### 2.6.2. 프레임워크에서 ANR 판단

---

## 3. 백그라운드 스레드

### 3.1. HandlerThread 클래스

### 3.2. 스레드 풀 사용

#### 3.2.1. ThreadPoolExecutor 클래스

#### 3.2.2. ScheduledThreadPoolExecutor 클래스

#### 3.2.3. Executors 클래스

### 3.3. AynsTask 클래스

#### 3.3.1. 백그라운드 스레드와 UI 스레드 구분

#### 3.3.2. 액티비티 종료 시점과 불일치

#### 3.3.3. AsyncTask 취소

#### 3.3.4. 예외 처리 메서드 없음

#### 3.3.5. 병렬 실행 시 doInBackground() 실행 순서가 보장되지 않음

---

## 4. Context

* Context는 앱을 개발할때 어디서든 항상 만나게 되는 객체이다.
* Context가 없으면 Activity 시작, 브로드캐스트 발생, Service 시작, 리소스에 접근 할 수도 없다.
* Context는 여러 컴포넌트의 상위 클래스 이면서 Context를 통해 여러 컴포넌트가 연결되므로 Context에 대해 자세히 살펴보는것이 컴포넌트를 이해하는데 도움이 된다.
* Context는 추상 클래스로 메서드 구현이 거의 없고 상수, 추상 메서드 정의로 이루어져 있다.
* Context 하위 클래스는 ContextWrapper이고 ContextWrapper를 상속한 것은 Activity, Service, Application이다. BroadcastReceiver, ContentProvider는 Context를 상속하지 않는다.
* ContextWrapper 클래스는 그 이름처럼 Context를 래핑한 ContextWrapper(Context base) 생성자를 가지고 있다.

```java
Context mBase;

public ContextWrapper(Context base) {
  mBase = base;
}

protected void attachBaseContext(Context base) {
  if (mBase != null) {
    throw new IllegalStateException("Base context already set");
  }
  mBase = base;
}

public Context getBaseContext() {
  return mBase;
}

...

@Override
public Context getApplicationContext() {
  return mBase.getApplicationContext();
}

...

@Override
public void startActivity(Intent intent) {
  mBase.startActivity(intent);
}

...

@Override
public void sendBroadcast(Intent intent) {
  mBase.sendBroadcast(intent);
}

...

@Override
public Resources getResources() {
  return mBase.getResources();
}
```

* 생성자와 attachBaseContext 함수에 전달하는 base는 Context의 여러 함수를 직접 구현한  ContextImpl 인스턴스 이다.
* ContextWrapper의 여러 함수는 base의 함수를 그대로 다시 호출한다.
* ContextImpl은 싱글턴이 아닌 Activity, Service, Application 컴포넌트별로 각각 생성한 ContextImpl을 하나씩 래핑하고 있다.
* getApplicationContext() 함수는 Application 인스턴스를 리턴하는 것으로 Application은 앱에 1개 밖에 없고 어디서나 동일한 인스턴스를 반환한다.
* ContextImple의 함수는 기능별로 헬퍼, 퍼미션, 시스템 서비스 접근 관련한 3개의 그룹으로 나뉜다.

```txt
* 헬퍼 : 앱 패키지 정보를 제공하거나 내/외부 파일, SharedPreferences, DB를 사용하기 위한 함수이다.
* 퍼미션 : Activity, BoradcastReceiver, Service와 같은 컴포넌트를 시작하는 함수와 퍼미션을 체크하는 함수가 있다. 이들 함수는 system_server 프로세스의 ActivityManagerService의 함수를 다시 호출한다.
* 시스템 서비스 접근 : ActivityManagerService를 포함한 시스템 서비스에 접근하는 함수로 getSystemService() 함수가 있다. 시스템 서비스는 Context 클래스에 XXX_SERVICE와 같이 상수명으로 모두 매핑되어 있고, Context가 전달된다면 어디서든지 getSystemService(Context.ALARM_SERVICE)와 같이 시스템 서비스를 가져다 쓸 수 있다.
```

```txt
* Context 관련 클래스 다이어그램

+---------------+
| <<abstract>>  |
|   Context     |
+---------------+
        △
        |---------------------------------+
        |                                 |
+----------------+                 +-------------+
|                |                 |             |
| ContextWrapper | --------------➤ | ContextImpl |  
|                |                 |             |
+----------------+                 +-------------+  
        △
        |---------------+------------------+
        |               |                  |
+------------+   +------------+    +---------------+
|            |   |            |    |               |
|  Activity  |   |  Service   |    |  Application  |  
|            |   |            |    |               |
+------------+   +------------+    +---------------+

* Activity, Service, Application은 ContextImpl를 직접상속하지 않고 구성을 이용하여 ContextImpl 함수를 호출하는 형태임을 알 수 있다.
* ContextImpl 변수는 노출되지 않고 필요한 함수만 ContextWrapper롤 통해 공개하게 된다.
```

* 상황에 따라 사용가능한 Context는 여러개가 있을 수 있다. Activity로 예를 들자면 `1) Activity 인스턴스 자신(this), 2) getBaseContext()를 통해 가져오는 ContextImpl 인스턴스, 3) getApplicationContext()를 통해 가져오는 Application 인스턴스` 3가지 형태의 Context를 사용할 수 있다.
* 3개의 인스턴스가 다르기 때문에 캐스팅을 함부로 하면 안 된다. getBaseContext()로 가져온 것을 Activity로 캐스팅하면 ClassCastException이 발생한다.
* View 클래스를 보면 Context가 들어가는데 그 Context는 어디서 온 것일까?

```java
@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.simple_text);
  statusView = (TextView) findViewById(R.id.status);

  Log.d(TAG, "1st=" + (statusView.getContext() == this)); // (1)
  Log.d(TAG, "2nd=" + (statusView.getContext() == getBaseContext())); // (2)
  Log.d(TAG, "3rd=" + (statusView.getContext() == getApplicationContext())); // (3)
  Log.d(TAG, "4th=" + (statusView.getContext() == getApplication())); // (4)
}
```

* 1만 true가 나온다. View 클래스 생성자에 Context 전달되어야 하는데 3가지 Context가 모두 전달 가능하나 View와 연관이 깊은 Activity가 전달된 것으로 이해를 할 수 있다.
* setContentView() 함수에서 사용하는 LayoutInflater에 Activity 인스턴스가 전달되고 View 생성자에도 동일하게 Activity 인스턴스가 전달된다.

---

## 5. 액티비티

### 5.1. 생명주기

#### 5.1.1 액티비티 생명주기 다이어그램

#### 5.1.2 생명주기 메서드 호출 시점

#### 5.1.3 액티비티 시작 메서드

#### 5.1.4 액티비티 전환 시 생명주기 메서드 호출

#### 5.1.5 생명주기 메서드 사용 시 주의사항

### 5.2. 구성 변경

#### 5.2.1 리소스 반영

#### 5.2.2 구성 변경으로 인한 액티비티 재시작

#### 5.2.3 프레임워크 소스 확인

#### 5.2.4 구성 한정자

#### 5.2.5 데이터 복구

#### 5.2.6 android:configChanges 속성

#### 5.2.7 Configuration 클래스의 변수 확인

### 5.3. 태스크

#### 5.3.1 태스크 상태

#### 5.3.2 dumpsys 명령어로 태스크 확인

#### 5.3.3 taskAffinity 속성

#### 5.3.4 태스크 속성 부여

### 5.4. <activity-alias> 선언

---

## 6. 서비스

---

## 7. 콘텐트 프로바이더

* 콘텐트 프로바이더는 외부 프로세스에 데이터를 제공하는 표준 인터페이스이다.
* 콘텐트 프로바이더에서는 주로 데이터 소스로 SQLite를 사용한다.

### 7.1. SQLite

* SQLite는 로컬 DB로써 속도가 그렇게 빠르지는 않고 개인적으로 쓸정도이다. 네이티브 라이브러리에 포함되어 있고 프레임워크를 거쳐서 접근하고 사용한다.

#### `db 내용 확인`

* SQLite db파일은 /data/data/패키지/databases에 저장된다.
* 단말에서는 일반적으로 db파일에 직접 접근하거나 쿼리를 실행할 수 없다.
* 개발시 에뮬레이터에서 `1) sqlite shell에서 쿼리를 실행 2) adb pull을 통해 db 파일을 가져와서 SQLite Database Browser 같은 툴로 데이터를 확인하고 쿼리를 실행`와 같은 방법으로 db를 확인할 수 있다.

#### 7.1.1 sqlite shell

#### 7.1.2 DB 락 문제

#### 7.1.3 SQLiteOpenHelper 클래스

### 7.2. 콘텐트 프로바이더

#### 7.2.1 로컬 프로세스에서 콘텐트 프로바이더 적용 기준

#### 7.2.2 콘텐트 프로바이더 예제

#### 7.2.3 배치 실행

### 7.3. SQLite/ContentProvider 관련 팁

#### 7.3.1 쿼리 실행 확인

#### 7.3.2 콘텐트 프로바이더 예외 확인

---

## 8. 브로드캐스트 리시버

---

## 9. Application

* Application도 Activity나 Service와 마찬가지로 ContextWrapper를 상속한다.
* Application은 단독으로 시작하지 않는다.
* 앱 프로세스가 떠있지 않은 상태에서 다른 컴포넌트 실행을 요청 받으면 앱 프로세스가 생성되고 Application이 먼저 시작되고 이후 컴포넌트가 시작된다.
* Activity, Service, BroadcastReceiver, ContentProvider 어느 것이든지 외부에서 실행을 요청받는 경우 Application이 시작되어 있다면 컴포넌트를 바로 시작한다. 아닌경우 Application이 먼저 시작하고 해당 컴포넌트를 시작한다.
* Application의 onCreate 함수보다 ContentProvider의 onCreate() 함수가 먼저 실행되는 부분은 주의할 필요가 있다.
* Context만 전달된다면 getApplicationContext() 함수로 언제든지 Application 객체를 가져올 수 있다.
* Activity에서는 getApplication() 함수를 사용해서 가져올 수 있다.

### 9.1. 앱 초기화

* Application은 다른 컴포넌트보다 먼저 실행되기 때문에 Application의 onCreate() 함수에서 앱을 위한 초기화 작업을 실행한다.
* onCreate() 함수는 가능한 한 빨리 끝나야 한다. onCreate() 함수에서 시간이 오래 걸린다면 검은 화면이 오래 보이거나 화면이 늦게 뜰 수 있다. UI 블로킹을 최소화하기 위해 스레드 작업을 하는것이 좋다.
* 백 키를 통해서 Activity를 모두 다 벗어나도 프로세스가 종료되는 것은 아니다. 이 후 앱 재 실행시 Application의 onCreate() 함수는 실행되지 않는다.
* Application은 프로세스에 항상 유지되는 인스턴스이다. 따라서 전역적인 앱 상태를 저장하기에 좋은 조건을 가지고 있다.
* 규칙만 잘 정한다면 특정 객체를 싱글톤 패턴 대신 Application을 통해 가져와서 사용할 수 있다.
* Application에 공유한 데이터는 시스템이 메모리 부족으로 인해 프로세스를 종료하거나 마시멜로 환경에서 앱의 퍼미션을 추가하거나 제거할때 Application이 재시작되며 onCreate() 함수를 다시 호출 하게 되는데 이럴때 모두 데이터가 사라져 버릴 수 있다.
* 결론적으로 Application에서 저장할 데이터는 사라져버려도 문제가 없고 남아있으면 좋은 캐시같은 데이터만 공유하도록 하는게 좋다.

### 9.2. Application 콜백

* Application에서는 앱 초기화 작업 때문에 onCreate() 함수만 오버라이드해서 사용하는 경우가 많다.
* ComponentCallbacks2 인터페이스에는 onConfigurationChanged, onLowMemory, onTrimMemory 3개의 함수가 존재한다.
* Application, Activity, Service, ContentProvider, 프레그먼트 모두 ComponentCallbacks2 인터페이스를 구현하고 있다.
* Application의 onTerminated() 함수는 에뮬레이터에서만 동작하고 실제 단말에서는 동작하지 않는다.

#### 9.2.1. ComponentCallback2 인터페이스

* 구성이 변경되면 Application, Activity, Service, 컨텐트 프로바이더 순으로 onConfigurationChanged() 함수가 호출된다.
* Application의 onConfigutationChanged()가 가장 먼저 실행되므로, 구성 변경시 반드시 필요한 작업은 여기서 작업한다. 예로 캘린더 앱에서 언어 변경시 휴일 정보를 새로 업데이트 해야 된다면 Application의 onConfigurationChanged() 함수에서 startService()를 실행하여 Service에서 API를 통해 휴일 정보를 업데이트 하면 된다.
* 시스템 메모리가 부족해 백그라운드 프로세스가 강제 종료될때 onLowMemory() 함수가 호출된다.

#### 9.2.2. Application에 등록하는 콜백

### 9.3. 프로세스 분리

#### 9.3.1. 프로세스 분리가 필요한 때

#### 9.3.2. 분리된 프로세스에서 Application은 새로 시작

#### 9.3.3. 프로세스 분리 시 주의할 점

---

## 10. 시스템 서비스

## 11. 구현 패턴



<!--
 즉 Activity, Service, 켕이션 컴포넌트는 각각 전달받은 `ContextImpl`를 래핑하고 있고, `getBaseContext()`는 각각 `ContextImpl` 인스턴스를 리턴하고 `getApplicationContext()`는 어디서나 1개로 동일한 `ContextImpl`의 어플리케이션 Context를 리턴한다.

`ContextImpl`의 함수는 기능별로 헬퍼, 퍼미션, 시스템 Service 접근 관련 3개의 그룹으로 나뉜다.

* 헬퍼`Helper` : 앱 패키지 정보, 내/외부 파일, `SharedPreferences`, 데이터베이스 정보를 제공한다.

* 퍼미션`Permission` : Activity, 브로드케스트 리시버, Service 컴포넌트 시작 함수, 퍼미션 체크 함수를 제공한다. 이들 함수는 `system_server` 프로세스의 Activity 메니저 Service`ActivityManagerService(시스템 Service)` 함수를 다시 호출한다.

* 시스템 Service 접근 : `ContextImpl`의 정적 초기화 블록에서 클래스가 최초 로딩될 때 시스템 Service를 매핑한다. 후엔 Context 클래스에 `XXX_SERVICE` 상수 형태로 정의된 값을 전달 인자로 하여 `getSystemService(`)를 호출하면 시스템 Service를 가져다 쓸 수 있다. `getSystemService(Context.ALARM_SERVICE)`
  
* Activity, Service, 어플리케이션은 `ContextImpl`을 직접 상속하지 않고 `ContextWrapper`를 통한 구성 형태로 `ContextImpl` 기능을 호출한다. 이를 통해 `ContextImpl`의 변수가 노출되지 않고 `ContextImpl`의 공개 함수만 호출할 수 있게 된다.

사용 가능한 Context 종류(Activity 코드에서 Context를 쓰는 방법)는 다음과 같다.

* Activity 인스턴스 자신 : Activity는 Context를 상속받은 `ContextWrapper`를 상속받으므로 자신`this`이 Context가 된다.

* `getBaseContext()` : 외부에서 전달받아 저장해놨던 `ContextImpl` 인스턴스도 사용 가능한 Context가 된다.

* `getApplicationContext()` : Activity의 `getApplication()` 메서드로 가져온 어플리케이션 인스턴스와 같은데 어플리케이션 인스턴스도 `ContextWrapper`를 상속받았으므로 Context가 된다.

* 위 3개의 인스턴스가 모두 다른 객체`(this, mBase, Application)`로 잘못 캐스팅시 ClassCastException 발생하게 되므로 함부로 캐스팅 하면 안된다.    -->

<!-- ```java
// View의 Context는 어디서 온걸까?

@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.simple_text);
  statusView = (TextView) findViewById(R.id.status);
  Log.d(TAG, "1st=" + (statusView.getContext() == this)); // (1) true
  Log.d(TAG, "2nd=" + (statusView.getContext() == getBaseContext())); // (2) false
  Log.d(TAG, "3rd=" + (statusView.getContext() == getApplicationContext())); // (3) false
  Log.d(TAG, "4th=" + (statusView.getContext() == getApplication())); // (4) false
}

// View 클래스는 생성자에 Context가 전달되어야 하는데 Activity에서 쓸 수 있는 3가지 Context 중 View와 연관이 깊은 Activity가 전달된 것을 알 수 있음
```
-->

<!-- 애플리케이션 계층은 선탑재된 기본 앱(시스템 권한 사용), 일반 앱으로 나뉘고 애플리케이션 프레임워크 스택 위에서 동작한다. 애플리케이션 프레임워크는 네이밍`Naming`이 매니저`Manager` 형태로 구현되어 있고, 대부분이 자바로 구성되어 있으나 하드웨어 제어나 빠른 속도를 위해 JNI를 연결해서 네이티브 C/C++ 코드를 사용하기도 한다. `ActivityManager, ResourceManager, TelephonyManager, LocationManager...` 애플리케이션 프레임워크의 각종 매니저에서 서버 기능은 별도 프로세스인 서버 시스템`system_server`에서 동작하고 앱 프로세스는 씬 클라이언트`thin clinet`에서 실행된다. 앱 프로세스는 컴포넌트 탐색, Activity 스택 관리, 서비스 목록 유지, ANR 처리 등을 직접 처리하지 않고, 서버 시스템 프로세스에 위임해 실제로는 컴포넌트 실행 등 최소한의 역할만 담당한다. 이러한 역할로 보면 시스템 서버는 여러 앱을 통합해서 관리하는 '통합 문의 채널'이 된다. 애플리케이션 프레임워크의 여러 매니저들는 시스템 서비스 형태로 존재하는데 접근시에는 Context`Context`의 `getSystemService(String name)` 메서드를 사용해야 한다. 시스템 서버는 별도 프로세스에서 실행되므로 앱(씬 클라이언트)에서 시스템 서비스에 접근할때는 프로세스간 통신인 `Binder IPC`가 필요하다. -->

<!-- 안드로이드 런타임은 달빅 가상머신(롤리팝부터 달빅 대신 아트`ART`)과 코어 라이브러리로 구성되어 있고, 런타임은 레지스트 기반의 가상 머신으로 자바 가상 머신보다 명령이 단순하고 빠르다.

안드로이드의 커널은 리눅스 커널을 기반으로 불필요한 것은 제거하고 필요한 부분들은 기능을 확장 패치했다. 확장 패치한 기능 중 `Binder IPC`는 프로세스 간 통신에 사용 하는데 앱 프로세스에는 바인더 스레드`Binder Thread`라는 네이티브 스레드 풀이 있고 최대 16개 까지 생성되어 있어 다른 프로세스에서 `Binder IPC` 통신할때 이 스레드 풀을 통해 접근하여 사용하도록 되어있다. `DDMS`의 `Binder_1`, `Binder_2`와 같은 이름의 스레드가 바인더 스레드에 속한다. -->

<!-- 안드로이드에서 기반이 되는 자바 버전은 프로요까지 자바5, 젤리빈/킷캣/롤리팝까지 자바6, 마시멜로까지 자바7, 누가/오레오까지 자바8에 해당된다. -->

<!-- 

코드네임 | API 레벨 | 안드로이드 버전
---|---|---
프로요 | 8 | 2.2
진저브레드 | 9, 10 | 2.2, 2.3
허니콤 | 11, 12, 13 | 3.0, 3.1, 3.2
아이스크림 샌드위치 | 14, 15 | 4.0 ~ 4.0.2, 4.0.3 ~ 4.0.4
젤리빈 | 16, 17,18 | 4.1, 4.2, 4.3
킷켓 | 19, 20 | 4.4 ~ 4.4.2, 4.4.3 ~ 4.4.4
롤리팝 | 21, 22 | 5.0, 5.1
마시멜로 | 23 | 6.0
누가 | 24 | 7.0
오레오 | 26, 27 | 8.0

안드로이드 앞자리 숫자가 바뀌는 버전은 11(허니콤), 14(아이스크림 샌드위치), 21(롤리팝), 23(마시멜로), 24(누가), 26(오레오)이다. 

안드로이드 버전 지정은 안드로이드 매니페스트`AndroidManifest.xml`에서 `uses-sdk` 항목 중 `android:minSdkVersion`, `android:targetSdkVersion`을 기재하면 된다. 현재는 대부분 안드로이드 스튜디오를 사용하므로 `build.gradle`에서 두 항목을 오버라이드 해서 많이 사용한다. `targetSdkVersion`을 지정하지 않으면 `minSdkVersion`과 동일한 값으로 지정되므로 반드시 지정하는게 좋다. `targetSdkVersion`을 지정한다는 것은 해당 버전까지는 테스트해서 앱을 실행하는 데 문제가 없고, 그 버전까지는 호환성 모드를 쓰지 않겠다는 뜻이 된다.

호환성 모드는 안드로이드 버전이 올라가더라도 앱의 기존 동작이 바뀌는 것을 방지하기 위한 것으로 호환성 모드로 동작되게 두는 것 보다는 `targetSdkVersion`을 높여 쓰는 것이 단말의 최신 기능을 쓸 수 있게 되기 때문에 더 권장된다.

* `AsyncTask` 병렬/순차 실행 : 허니콤 이전 버전에선 `AsyncTask` 태스크`Task`가 병렬로 실행되는데 허니콤 부터는 순차적으로 실행된다. 그러므로 `targetSdkVersion`이 10 이하이면 안드로이드 버전이 높다고 해도 병렬 실행하게 된다.

* 메인 스레드 상에서 네트워크 통신 : API 9 까지 메인 스레드 상에서 네트워크 통신 허용했으나 그 이후는 `NetworkOnMainThreadException` 발생한다.

* 하드웨어 가속 : `GPU`를 가지고 뷰`View`에서 캔버스`Canvas`에 그리는 작업을 말하며 허니콤에서 처음 시작되었고 `targetSdkVersion`이 14 이상이면 디폴트 옵션이다.

* 앱 위젯 기본 패딩`Padding` : 기존에는 셀의 사이즈를 가득 채웠으나 `targetSDKVersion`이 14 이상 부터는 앱 위젯에 기본 패딩이 존재한다. 

* 명시적 인텐트로 서비스 시작 : `targetSDKVersion`이 21 이상일 때는 `startService()`, `bindService()` 메서드를 실행할 때 명시적 인테트를 사용해야 한다. 암시적 인텐트를 사용하면 예외발생하게 된다. 20 이하이면 암시적 인텐트도 문제 없이 동작한다.

* compileSdkVersion은 컴파일 시에 어느 버전의 android.jar를 사용할지 정하는 것을 의미하고 `<sdk>/platforms/android-[버전]` compileSdkVersion은 디폴트 값이 없으므로 반드시 지정해야 한다.

* targetSdkVersion은 런타임 시에 비교해서 호환성 모드로 동작하기 위한 값이고, compileSdkVersion은 컴파일 시에 사용할 버전을 정하는 것이다. 규칙은 없으나 compileSdkVersion은 targetSdkVersion과 동일하거나 그 이상으로 정하는게 좋다.

* compileSdkVersion을 높은 버전으로 정하고 컴파일해서 만든 앱이, 낮은 버전의 단말에서 설치되어 동작될때 높은 버전에만 있는 클래스나 메서드가 호출될때 크래시가 발생되므로 버전을 체크하는 코드를 사용해야 한다.

* 메서드 마다 Build.VERSION_SDK_INT를 확인해 분기하는 코드 보다는 support 패키지의 -Compat 클래스를 사용하는게 좋다. `ViewCompat, ActivityCompat, WindowCompat, NotifiationCompat, AsyncTaskCompat, SharedPreferencesCompat.EditorCompat...`

  ```java
  if (Build.VERSION.SDK_INT >= 9) {
    listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
  }

  // 위 코드는 ViewCompat을 쓰면 간단해지며 버전 분기코드를 작성하지 않아도 된다.
  ViewCompat.setOverScrollMode(listView, ViewCompat.OVER_SCROLL_NEVER);
  ```

  support-v4에 호환 메서드가 있으면 그것을 먼저 사용하고 없을때만 별도로 작성한다. 버전마다 동작이 달라지도록 코드를 작성할 때는 ViewCompat 클래스의 구조를 활용하는게 좋다. `if 문으로 버전을 체크하지 않고, 정적 초기화 블록에서 if 문으로 버전을 체크 후 사용할 클래스를 지정하는 방식`

-->
<!--
## 5. Activity

- 앱에서 화면의 기본단위가 되고 가장 많이 쓰이는 컴포넌트임

- 다른 컴포넌트와 마찬가지로 AndroidManifest.xml에 선언되어야 함

- 내부에 UI 액션이 많고 로직이 많으면 Activity를 고려하고, 팝업 형식으로 뜬다면 커스텀 레이아웃 다이얼로그나 DialogFragment, PopupWindow로 대체하는게 좋음. 기준을 단순하게 하자면 독립적인 화면은 Activity가 적합하고, 종속적인 화면으로 보인다면 다른것을 쓰는게 좋음.

- Activity에서는 setContentView() 메서드로 메인 뷰를 화면에 표시. setContentView()를 실행하지 않고, 로직에 따라 분기해서 다른 Activity를 띄우는 용도로 사용하기도 함. (UI 없는 Activity가 됨)

> Intent의 스킴(scheme)에 따라 화면 분기하는 관문(Front controller) 역할로서의 Activity

```xml
<activity android:name=".FrontControllerActivity">
  <intent-filter>
    <action android:name="android.intent.action.VIEW">
    <category android:name="android.intent.category.DEFAULT">
    <data android:scheme="doc">
    <data android:scheme="xls">
    <data android:scheme="ppt">
```

```java
public class FrontControllerActivity extends Activity {

  private static final String WORD_SCHEME = "doc"
  private static final String EXCEL_SCHEME = "xls"
  private static final String POWERPOINT_SCHEME = "ppt"

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)

    Uri uri = getIntet().getData();
    if (uri == null) {
      Toast.makeText(this, "Uri does not exist!!", Toast.LENGTH_LONG).show();
    } else {
      // (1)
      switch (uri.getScheme()) {
        case WORD_SCHEME:
          startActivity(new Intent(this, WordActivity.class));
          break;
        case EXCEL_SCHEME:
          startActivity(new Intent(this, ExcelActivity.class));
          break;
        case POWERPOINT_SCHEME:
          startActivity(new Intent(this, PowerPointActivity.class));
          break;
      }
      finish();
    }
  }
}
``` -->

<!-- - 싱글톤을 잘못 사용하면 메모리 누수 가능성이 있음. 꼭 필요한 곳에서만 사용하는게 좋음

- 싱글톤에 Activity Context를 전달한 경우 싱글톤 생명주기동안 참조로 남아서 Activity를 종료했음에도 GC 대상이 되지 않아 메모리에 계속 남는 문제가 생길 수 있음

> 누수 검증 테스트 (싱글톤 코드는 support-v4에 포함된 LocalBroadcastManager 구현 방식을 그대로 차용함)

```java
public class CalendarManager {

  private static final Object lock = new Object();
  private static final CalendarManager instance;

  public static CalendarManager getInstance(Context context) {
    synchronized (lock) {
      if (instance == null) {
        instance = new CalendarManager(context); // (1)
        instance = new CalendarManager(context.getApplicationContext()); // (2)
      }
      return instance;
    }
  }

  private Context context;

  private CalendarManager(Context context) {
    this.context = context;
  }

  public String getText() {
    return context.getString(R.string.hello_world);
  }
}
```

```java
public class ScheduleActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final TextView textView = new TextView(this);
    text.setText("first run");
    setContentView(textView);
    CalendarManager manager = CalendarManager.getInstance(this);
  }
}
```

- ScheduleActivity 실행 후 Back 키로 종료하면 어떤 코드가 Activity 메모리 Leak이 생길까?

  - (1) 전달 받은 Activity의 Context를 넘겨줌 (Activity를 종료해도 Context가 Singleton에서 참조되고 있어 GC 대상이 되지 않아 메모리 릭이 발생함)
  
  - (2) 전달 받은 Activity에서 getApplicationContext()로 Application Context를 가져온 다음 넘겨줌 (Activity의 Context를 넘긴것이 아니므로 Activity 종료시 메모리 릭이 발생하지 않음)

- 싱클톤은 사용하는 쪽에서 규칙을 강제하면 안되고, 싱글톤 내에서 getApplicationContext()와 같이 얻은 결과를 사용하는 것이 나음

### 마커 인터페이스

- 메서드 선언이 없는 인터페이스. 표식(marking) 용도로 인터페이스를 사용. 예) Serializable

- 복잡한 조건문이 필요한 로직이나 파라미터가 많은 메서드에서 사용 가능함

- 마커 인터페이스는 마커 어노테이션으로 대체 가능함

### Fragment 정적 생성

- Fragment를 생성할때 값을 전달하는 경우가 많은데, Fragment에 세터(setter)로 값을 전달하는 방식보다는,정적 메서드로 Fragment를 생성하면서 값을 전달하는 패턴이 많이 사용됨

- 정적 메서드는 Bundle에 전달받은 데이터를 넣고, 생성한 Fragment에 setArguments를 사용해 값을 넣은 다음 return 해주게 되는데 값을 유지 할 수 있기 때문에 구성변경(Configuration Change)와 Activity 강제 종료에 대응이 가능함

  - Activity의 onSaveInstanceState()가 호출될때 Fragment는 setArguments 함수를 통해 전달받은 값을 가지고 있는 FragmentState 값들을 saveAllState() 호출로 저장해주기 때문임

  - Fragment의 onSaveInstanceState()를 오버라이드해서 사용할 수 있지만 프레임워크에서 제공하는 기능을 피할 이유는 없음 -->