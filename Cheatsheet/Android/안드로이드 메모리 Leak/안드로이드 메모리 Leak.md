# 안드로이드 메모리 릭`Leak`

## 내부 클래스`inner class`와 익명 클래스`anonymous class`

안드로이드에서 일반적으로 액티비티`Activity`나 컴포넌트`Component`들을 정적`static` 변수나 싱글턴`singleton` 객체안의 변수에 전달하여 할당하는 경우, 내부 클래스와 익명 클래스에서 핸들러`Handler`, `AsyncTask`, 스레드`Thread`, 타이머`Timer`등을 사용하여 오랜시간 걸리는 작업을 진행하는 경우에 메모리 릭을 유발할 수 있다.

기본적으로 자바의 일반 객체는 강한 참조`strong reference`형태를 가지는데 그에 기반하여 가비지 컬렉션`Garbage Collection, 이하 GC`은 그 대상을 선정하는 과정에서 `reachable`, `unreachable`로 구분하게 된다. 루트 셋`root set`으로부터 시작된 참조 사슬에 포함이 되어 있으면 `reachable` 객체 아니면 `unreachable`로 나누는 것이다. 메모리에서 사라져야 될 상황에 어떠한 이유로 인해 객체가 `reachable` 상태로 유지되면 가비지 컬랙션의 대상이 되지 못해 힙에 계속 남게 되고 메모리 릭을 유발하게 된다.

```java
/*
* 안드로이드 핸들러에서 메모리 릭을 유발하는 코드
*/

public class LeakActivity extends Activity {

  // 내부 클래스
  private final Handler handler = new Handler() {

    @Override
    public void handlerMessage(Message msg) {
      
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handler.postDelayed(new Runnable() { // 익명 클래스

      @Override
      public void run() {

      }
    }, 60000);

    finish()
  }
}
```

`LeakActivity`의 `onCreate` 함수에서 핸들러로 1분 뒤 어떤 작업을 하도록 요청하고 바로 액티비티가 종료 되도록 `finish()` 함수를 호출하게 한다.

자바에서는 비정적 내부 클래스`non-static inner class`의 경우 외부 클래스에 대한 참조`reference`를 가지게 되는데, 위에서 핸들러는 `LeakActivity`에 대한 참조를 가지고 메인 스레드에서 생성되었기 때문에 메인 스레드의 루퍼`Looper` 및 메시지 큐`Message Queue`에 바인딩 되게 되어 계속 참조하게 된다.

`finish()` 함수로 인해 `LeakActivity`는 종료되었지만 메시지 큐에 지연`delay`된 작업이 남아있게 된 핸들러가 `LeakActivity`를 계속 참조하므로 `LeakActivity`는 액티비티가 종료 되었음에도 가비지 컬랙션의 대상이 되지 못해 메모리 릭이 발생하게 되는 것이다.

메모리 릭이 발생되지 않기 위해서는 핸들러를 비정적 내부 클래스 대신 외부 클래스를 참조하지 않는 정적 내부 클래스`static inner class`로 선언을 해야한다. 그러면 핸들러가 더이상 외부 클래스를 참조하고 있지 않게 되므로, 액티비티의 생명주기에 따라 적절하게 소멸되게 된다.

핸들러 내부에서 가비지 컬랙션을 통해 소멸된 액티비티의 메서드나 변수등 관련 리소스를 참조해야 할 경우가 있는데 안전하게 사용하기 위해서는 액티비티의 참조 변수를 약한 참조`WeakReference` 변수로 래핑시켜 액티비티의 상태에 따라 잘못된 참조가 되지 않도록 해줘야 한다.

```java

/**
* 메모리 릭에 대한 수정안
*/
public class NonLeakActivity extends Activity {
  private NonLeakHandler handler = new NonLeakHandler(this);
  
  // 내부 클래스 -> 정적 내부 클래스 (액티비티 참조 변수를 가지기 위해 클래스 상속)
  private static final class NonLeakHandler extends Handler {
    private final WeakReference<NonLeakActivity> ref;
    
    public NonLeakHandler(NonLeakActivity act) {
      ref = new WeakReference<>(act);  
    }
    
    @Override
    public void handleMessage(Message msg) {
      NonLeakActivity act = ref.get();
      if (act != null) { // 액티비티가 유지된 경우에만 리소스 접근 작업을 함
        
      }
    }
  }
  
  // 익명 클래스 -> 정적 내부 클래스
  private static final Runnable runnable = new Runnable() {

    @Override
    public void run() {

    }
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    handler.postDelayed(runnable, 60000);
    finish();
  }
}
```

익명 클래스도 비정적 내부 클래스와 마찬가지로 외부 클래스에 대한 참조를 가지게 되기 때문에 익명 클래스로 사용했던 `Runnable`도 정적 내부 클래스로 변경한다. 내부 클래스, 익명 클래스 모두 액티비티의 생명주기와 동일하게 생성 및 종료가 보장되지 않는다면 정적 내부 클래스로 정의해야 메모리 릭을 방지할 수 있는 것이다.