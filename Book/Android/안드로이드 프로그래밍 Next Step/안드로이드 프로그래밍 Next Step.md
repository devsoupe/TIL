# 안드로이드 프로그래밍 Next Step

## 1. 안드로이드 프레임워크

### 안드로이드 아키텍처 개요

- 애플리케이션, 애플리케이션 프레임워크, 라이브러리/안드로이드 런타임, 리눅스 커널로 이루어짐
- 애플리케이션은 선탑재된 기본 앱(시스템 권한 사용), 일반 앱으로 나뉨. 애플리케이션 프레임워크 스택 위에서 동작
- 애플리케이션 프레임워크는 Manager 형태로 구현되어 있고, 대부분이 자바로 구성되어 있으나 하드웨어 제어나 빠른 속도를 위해 JNI를 연결해서 네이티브 C/C++ 코드를 사용하기도 함 ```ActivityManager, ResourceManager, TelephonyManager, LocationManager...```
- 애플리케이션 프레임워크의 각종 Manager에서 서버 기능은 별도 프로세스인 sysetm_server에서 앱 프로세스는 thin clinet에서 실행됨. 앱 프로세스는 컴포넌트 탐색, 액티비티 스택 관리, 서비스 목록 유지, ANR 처리 등을 직접하지 않고 서버인 server_system 프로세스에 위임하고 컴포넌트 실행 등 최소한의 역할만 담당. sysetm_server는 여러 앱을 통합해서 관리하는 '통합 문의 채널'임
- 애플리케이션 프레임워크의 여러 Manager는 시스템 서비스 형태로 존재함. 접근시 Context의 getSystemService(String name) 메서드를 사용. sysetm_server라는 별도 프로세스에서 실행되므로 앱에서 시스템 서비스에 접근할때 Binder IPC 프로세스 통신이 필요
- 안드로이드 런타임은 달빅 가상머신(롤리팝부터 달빅 대신 ART)과 코어 라이브러리로 구성됨. 런타임은 레지스트 기반의 가상 머신으로 자바 가상 머신보다 명령이 단순하고 빠름
- 안드로이드의 커널은 리눅스 커널을 기반으로 불필요한 것은 제거하고 필요한 부분들은 기능을 확장 패치함. 확장 패치한 기능 중 Binder IPC는 프로세스 간 통신에 사용. 앱 프로세스에는 Binder Thread라는 네이티브 스레드 풀이 있고 최대 16개 까지 생성됨. 다른 프로세스에서 Binder IPC 통신할때 이 스레드 풀을 통해 접근함 ```DDMS의 Binder_1, Binder_2와 같은 이름의 스레드가 Binder Thread에 속함```

### 프레임워크 소스

- 안드로이드에서 기반이 되는 자바 버전은 프로요까지 자바5, 젤리빈까지 자바6, 마시멜로까지 자바7, 누가/오레오까지 자바8

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
오레오 | 26 | 8.0

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

## 5. 액티비티

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
- 싱글톤에 Activity Context를 전달한 경우 싱글톤 생명주기동안 참조로 남아서 Activity를 종료했음에도 GC 대상이 되지 않아 메모리에 계속 남는 문제가 생길 수 있음
- 누수 검증 테스트 (싱글톤 코드는 support-v4에 포함된 LocalBroadcastManager 구현 방식을 그대로 차용함) 
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
> (1) 전달 받은 Activity의 Context를 넘겨줌 (2) 전달 받은 Activity에서 getApplicationContext()로 Application Context를 가져온 다음 넘겨줌

> ScheduleActivity 실행 후 Back 키로 종료하면 어떤 코드가 Activity 메모리 Leak이 생길까? 

- 싱클톤은 사용하는 쪽에서 규칙을 강제하면 안되고, 싱글톤 내에서 getApplicationContext()와 같이 얻은 결과를 사용하는 것이 나음

### 마커 인터페이스

### Fragment 정적 생성