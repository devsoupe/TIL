# 안드로이드 프로그래밍 Next Step

## 1. 안드로이드 프레임워크

### 안드로이드 아키텍처 개요

안드로이드 아키텍처는 크게 애플리케이션, 애플리케이션 프레임워크, 라이브러리/안드로이드 런타임, 리눅스 커널로 이루어진다. 

애플리케이션 계층은 선탑재된 기본 앱(시스템 권한 사용), 일반 앱으로 나뉘고 애플리케이션 프레임워크 스택 위에서 동작한다. 

애플리케이션 프레임워크는 파일 네이밍`Naming`이 매니저`Manager` 형태로 구현되어 있고, 대부분이 자바로 구성되어 있으나 하드웨어 제어나 빠른 속도를 위해 JNI를 연결해서 네이티브 C/C++ 코드를 사용하기도 한다. `ActivityManager, ResourceManager, TelephonyManager, LocationManager...` 애플리케이션 프레임워크의 각종 Manager에서 서버 기능은 별도 프로세스인 서버 시스템`system_server`에서 동작하고 앱 프로세스는 씬 클라이언트`thin clinet`에서 실행된다. 앱 프로세스는 컴포넌트 탐색, 액티비티 스택 관리, 서비스 목록 유지, ANR 처리 등을 직접 처리하지 않고, 서버 시스템 프로세스에 위임해 실제로는 컴포넌트 실행 등 최소한의 역할만 담당한다. 이러한 역할로 보면 시스템 서버는 여러 앱을 통합해서 관리하는 '통합 문의 채널'이 된다. 애플리케이션 프레임워크의 여러 매니저들는 시스템 서비스 형태로 존재하는데 접근시에는 컨텍스트`Context`의 `getSystemService(String name)` 메서드를 사용해야 한다. 시스템 서버는 별도 프로세스에서 실행되므로 앱(씬 클라이언트)에서 시스템 서비스에 접근할때는 프로세스간 통신인 `Binder IPC`가 필요하다.

안드로이드 런타임은 달빅 가상머신(롤리팝부터 달빅 대신 아트`ART`)과 코어 라이브러리로 구성되어 있고, 런타임은 레지스트 기반의 가상 머신으로 자바 가상 머신보다 명령이 단순하고 빠르다.

안드로이드의 커널은 리눅스 커널을 기반으로 불필요한 것은 제거하고 필요한 부분들은 기능을 확장 패치했다. 확장 패치한 기능 중 `Binder IPC`는 프로세스 간 통신에 사용 하는데 앱 프로세스에는 바인더 스레드`Binder Thread`라는 네이티브 스레드 풀이 있고 최대 16개 까지 생성되어 있어 다른 프로세스에서 `Binder IPC` 통신할때 이 스레드 풀을 통해 접근하여 사용하도록 되어있다. `DDMS`의 `Binder_1`, `Binder_2`와 같은 이름의 스레드가 바인더 스레드에 속한다.

### 프레임워크 소스

- 안드로이드에서 기반이 되는 자바 버전은 프로요까지 자바5, 젤리빈/킷캣/롤리팝까지 자바6, 마시멜로까지 자바7, 누가/오레오까지 자바8

### 안드로이드 버전

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

- 안드로이드 앞자리 숫자가 바뀌는 버전 11(허니콤), 14(아이스크림 샌드위치), 21(롤리팝), 23(마시멜로), 24(누가), 26(오레오)

- 안드로이드 버전 지정은 AndroidManifest.xml에서 uses-sdk 항목 중 android:minSdkVersion, android:targetSdkVersion을 기재하면됨. 현재는 대부분 안드로이드 스튜디오를 사용하므로 build.gradle에서 두 항목을 오버라이드 해서 많이 사용

- targetSdkVersion은 반드시 지정하는게 좋음. 지정하지 않으면 minSdkVersion과 동일한 값으로 지정됨. targetSdkVersion을 지정한다는 것은 해당 버전까지는 테스트해서 앱을 실행하는 데 문제가 없고, 그 버전까지는 호환성 모드를 쓰지 않음

- 호환성 모드는 안드로이드 버전이 올라가더라도 앱의 기존 동작이 바뀌는 것을 방지하기 위한 것으로 호환성 모드로 동작되게 두는 것 보다는 targetSdkVersion을 높여 쓰는 것이 단말의 최신 기능을 쓸 수 있게 되기 때문에 권장됨

  - AsyncTask 병렬/순차 실행 : 허니콤 이전 버전에선 AsyncTask 태스크가 병렬 실행. 허니콘 부터는 순차 실행. targetSdkVersion이 10 이하이면 안드로이드 버전이 높다고 해도 병렬 실행하게됨

  - 메인 스레드 상에서 네트워크 통신 : API 9 까지 메인 스레드 상에서 네트워크 통신 허용. 그 이후는 NetworkOnMainThreadException 발생

  - 하드웨어 가속 : GPU를 가지고 View에서 Canvas에 그리는 작업. 허니콤에서 처음 시작되었고 targetSdkVersion이 14 이상이면 디폴트 옵션임

  - 앱 위젯 기본 패딩 : targetSDKVersion이 14 이상일 때는 앱 위젯에 기본 패딩이 존재. 기존에는 셀의 사이즈를 가득 채움

  - 명시적 인텐트로 서비스 시작 : targetSDKVersion이 21 이상일 때는 startService(), bindService() 메서드를 실행할 때 명시적 인테트를 사용해야 함. 암시적 인텐트를 사용하면 예외발생. 20 이하이면 문제 없이 동작

- compileSdkVersion은 컴파일 시에 어느 버전의 android.jar를 사용할지 정하는 것. ```<sdk>/platforms/android-[버전]``` compileSdkVersion은 디폴트 값이 없으므로 반드시 지정해야 함.

- targetSdkVersion은 런타임 시에 비교해서 호환성 모드로 동작하기 위한 값이고, compileSdkVersion은 컴파일 시에 사용할 버전을 정하는 것임. 규칙은 없으나 compileSdkVersion은 targetSdkVersion과 동일하거나 그 이상으로 정함

- compileSdkVersion을 높은 버전으로 정하고 컴파일해서 만든 앱이, 낮은 버전의 단말에서 설치되어 동작될때 높은 버전에만 있는 클래스나 메서드가 호출될때 크래시가 발생되므로 버전을 체크하는 코드를 사용하게 됨

- 메서드 마다 Build.VERSION_SDK_INT를 확인해 분기하는 코드 보다는 support 패키지의 -Compat 클래스를 사용하는게 좋음 ```ViewCompat, ActivityCompat, WindowCompat, NotifiationCompat, AsyncTaskCompat, SharedPreferencesCompat.EditorCompat...```

```java
if (Build.VERSION.SDK_INT >= 9) {
  listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
}
```

> ViewCompat을 쓰면 간단해짐

```java
ViewCompat.setOverScrollMode(listView, ViewCompat.OVER_SCROLL_NEVER);
```

- support-v4에 호환 메서드가 있으면 그것을 먼저 사용하고 없을때는 별도로 작성. 버전마다 동작이 달라지도록 코드를 작성할 때는 ViewCompat 클래스의 구조를 활용 ```if 문으로 버전을 체크하지 않고, 정적 초기화 블록에서 if 문으로 버전을 체크 후 사용할 클래스를 지정하는 방식```

## 2. 메인 스레드와 Handler

### UI 처리를 위한 메인 스레드

### Looper 클래스

### Message와 MessageQueue

### Handler 클래스

### UI 변경 메커니즘

### ANR

## 3. 백그라운드 스레드

### HandlerThread 클래스

### 스레드 풀 사용

### AynsTask 클래스

## 4. Context

- Context는 앱을 개발할때 어디서든 항상 만남. Context가 없으면 액티비티 시작, 브로드캐스트 발생, 서비스 시작, 리소스에 접근 할 수도 없음. Context는 여러 컴포넌트의 상위 클래스 이면서 Context를 통해 여러 컴포넌트가 연결되므로 Context를 자세히 살펴볼 필요가 있음

- Context는 추상 클래스로 메서드 구현이 거의 없고 상수, 추상 메서드 정의로 이루어짐 (구현체는 ContextImpl)

- Activity, Service, Application -> ContextWrapper -> Context (BroadcastReceiver, ContentProvider는 Context를 상속받지 않음)

- ContextWrapper는 Context를 래핑한 클래스로 실제 전달되는 것은 Context의 여러 메서드를 구현한 ContextImpl 인스턴스임

- Activity, Service, Application 컴포넌트는 각각 생성한 ContextImpl를 하나씩 래핑하고 있고, getBaseContext()는 각각 ContextImpl 인스턴스를 리턴하고 getApplicationContext()는 1개 뿐인 어디서나 동일한 Application 인스턴스를 리턴함

- ContextImpl의 메서드는 기능별로 헬퍼, 퍼미션, 시스템 서비스 접근 관련 3개의 그룹으로 나뉨

  - 헬퍼 : 앱 패키지 정보, 내/외부 파일, SharedPreferences, 데이터베이스

  - 퍼미션 : Activity, BroadcastReceiver, Service 컴포넌트 시작 메서드, 퍼미션 체크 메서드. 이들 메서드는 system_server 프로세스의 ActivityManagerService(시스템 서비스)의 메서드를 다시 호출함

  - 시스템 서비스 접근 : ContextImpl의 정적 초기화 블록에서 클래스가 최초 로딩될 때 시스템 서비스를 매핑함. 후엔 Context 클래스에 XXX_SERVICE 상수 형태로 정의된 값을 전달 인자로 하여 getSystemService()를 호출하면 시스템 서비스를 가져다 쓸 수 있음 ```getSystemService(Context.ALARM_SERVICE)```

  <!--
  ```mermaid
  classDiagram
  Context <|-- ContextWrapper
  Context <|-- ContextImpl
  ContextWrapper <|-- Activity
  ContextWrapper <|-- Service
  ContextWrapper <|-- Application
  ContextImpl <-- ContextWrapper
  Context
  ```
  -->
  
  - Activity, Service, Application는 ContextImpl을 직접 상속하지 않고 ContextWrapper를 통한 구성형태로 ContextImpl 기능을 호출함. 이를 통해 ContextImpl의 변수가 노출되지 않고 ContextImpl의 공개 메서드만 호출하게 됨

- 사용 가능한 Context 종류 (Activity 코드에서 Context를 쓰는 방법)

  - Activity 인스턴스 자신(this)

  - getBaseContext()를 통해 가져오는 ContextImpl 인스턴스

  - getApplicationContext()를 통해 가져오는 Application 인스턴스 : Activity의 getApplication() 메서드로 가져오는 인스턴스와 같음

  - 위 3개의 인스턴스가 모두 다르기 때문에 함수로 캐스팅 하면 안됨. ClassCastException 발생

  > View의 Context는 어디서 온걸까?

  ```java
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
  ```
  
  - View 클래스는 생성자에 Context가 전달되어야 하는데 Activity에서 쓸 수 있는 3가지 Context 중 View와 연관이 깊은 Activity가 전달된 것을 알 수 있음

## 5. 액티비티

- 앱에서 화면의 기본단위가 되고 가장 많이 쓰이는 컴포넌트임

- 다른 컴포넌트와 마찬가지로 AndroidManifest.xml에 선언되어야 함

- 내부에 UI 액션이 많고 로직이 많으면 액티비티를 고려하고, 팝업 형식으로 뜬다면 커스텀 레이아웃 다이얼로그나 DialogFragment, PopupWindow로 대체하는게 좋음. 기준을 단순하게 하자면 독립적인 화면은 액티비티가 적합하고, 종속적인 화면으로 보인다면 다른것을 쓰는게 좋음.

- 액티비티에서는 setContentView() 메서드로 메인 뷰를 화면에 표시. setContentView()를 실행하지 않고, 로직에 따라 분기해서 다른 액티비티를 띄우는 용도로 사용하기도 함. (UI 없는 액티비티가 됨)

> Intent의 스킴(scheme)에 따라 화면 분기하는 관문(Front controller) 역할로서의 액티비티

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
```

### 생명주기

### 구성 변경

### 태스크

### <activity-alias> 선언

## 6. 서비스

### 스타티드 서비스

### 바운드 서비스

## 7. 콘텐트 프로바이더

### SQLite

### 콘텐트 프로바이더

### SQLite/ContentProvider 관련 팁

## 8. 브로드캐스트 리시버

### 브로드캐스트 리시버 구현

### 브로드캐스트 리시버 등록

### 오더드 브로드캐스트

### 스티키 브로드캐스트

### LocalBroadcastManager 클래스

### 앱 위젯

## 9. Application

### 앱 초기화

### Application 콜백

### 프로세스 분리

## 10. 시스템 서비스

### 시스템 서비스 기본

### dumpsys 명령어

### 시스템 서비스 이슈

## 11. 구현 패턴

### 싱글톤 패턴

- 싱글톤을 잘못 사용하면 메모리 누수 가능성이 있음. 꼭 필요한 곳에서만 사용하는게 좋음

- 싱글톤에 액티비티 Context를 전달한 경우 싱글톤 생명주기동안 참조로 남아서 액티비티를 종료했음에도 GC 대상이 되지 않아 메모리에 계속 남는 문제가 생길 수 있음

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

- ScheduleActivity 실행 후 Back 키로 종료하면 어떤 코드가 액티비티 메모리 Leak이 생길까?

  - (1) 전달 받은 액티비티의 Context를 넘겨줌 (액티비티를 종료해도 Context가 Singleton에서 참조되고 있어 GC 대상이 되지 않아 메모리 릭이 발생함)
  
  - (2) 전달 받은 액티비티에서 getApplicationContext()로 Application Context를 가져온 다음 넘겨줌 (액티비티의 Context를 넘긴것이 아니므로 액티비티 종료시 메모리 릭이 발생하지 않음)

- 싱클톤은 사용하는 쪽에서 규칙을 강제하면 안되고, 싱글톤 내에서 getApplicationContext()와 같이 얻은 결과를 사용하는 것이 나음

### 마커 인터페이스

- 메서드 선언이 없는 인터페이스. 표식(marking) 용도로 인터페이스를 사용. 예) Serializable

- 복잡한 조건문이 필요한 로직이나 파라미터가 많은 메서드에서 사용 가능함

- 마커 인터페이스는 마커 어노테이션으로 대체 가능함

### Fragment 정적 생성

- Fragment를 생성할때 값을 전달하는 경우가 많은데, Fragment에 세터(setter)로 값을 전달하는 방식보다는,정적 메서드로 Fragment를 생성하면서 값을 전달하는 패턴이 많이 사용됨

- 정적 메서드는 Bundle에 전달받은 데이터를 넣고, 생성한 Fragment에 setArguments를 사용해 값을 넣은 다음 return 해주게 되는데 값을 유지 할 수 있기 때문에 구성변경(Configuration Change)와 액티비티 강제 종료에 대응이 가능함

  - 액티비티의 onSaveInstanceState()가 호출될때 Fragment는 setArguments 함수를 통해 전달받은 값을 가지고 있는 FragmentState 값들을 saveAllState() 호출로 저장해주기 때문임

  - Fragment의 onSaveInstanceState()를 오버라이드해서 사용할 수 있지만 프레임워크에서 제공하는 기능을 피할 이유는 없음