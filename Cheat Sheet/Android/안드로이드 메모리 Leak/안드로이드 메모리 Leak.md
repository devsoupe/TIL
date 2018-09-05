# 안드로이드 메모리 릭```Leak```

안드로이드에서는 일반적으로 액티비티```Activity```나 컴포넌트```Component```들을 정적```static``` 변수, 싱글턴```singleton``` 객체의 변수에 전달하여 할당하는 경우나  내부 클래스와 익명 클래스에서 핸들러```Handler```, ```AsyncTask```, 스레드```Thread```, 타이머```Timer```등을 사용하는 경우에 메모리 릭을 유발할 수 있다.

기본적으로 자바에서 일반적인 객체는 강한 참조```strong reference```형태를 가진다. 보통은 가비지 컬렉션```Garbage Collection, 이하 GC``` 대상을 선정하는 과정에서 ```reachable```, ```unreachable```로 나뉘게 되는데 루트 셋 ```root set```으로부터 시작된 참조 사슬에 포함이 되어 있으면 ```reachable``` 객체, 아니면 ```unreachable```로 나뉘게 된다. 메모리에서 사라져야 될 상황에  어떠한 이유로 인해 객체가 ```reachable``` 상태로 유지되면 가비지 컬랙션의 대상이 되지 못하고 메모리 릭이 발생하게 된다.

```java
/*
* 안드로이드 핸들러에서 메모리 릭을 유발하는 코드
*/


public class LeakActivity extends Activity {

  // inner class (Handler)
  private final Handler handler = new Handler() {

    @Override
    public void handlerMessage(Message msg) {
      
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handler.postDelayed(new Runnable() { // anonymous class (Runnable)

      @Override
      public void run() {

      }
    }, 60000);

    finish()
  }
}
```

- ```LeakActivity```의 ```onCreate```에서 ```Handler```로 1분 뒤 어떤 작업을 하도록 요청하고 바로 Activity가 종료되도록 ```finish()``` 함수를 수행

- Java에서 ```non-static inner class```의 경우 ```outer class```에 대한 ```reference```를 가지게 됨. 위에서 ```Handler```는 ```LeakActivity```에 대한 ```reference```를 가지고 main thread에서 생성되었기 때문에 main thread의 Looper 및 Message Queue에 바인딩 됨

- ```finish()``` 함수로 ```LeakActivity```는 종료되었지만 Message Queue에 delay된 작업이 남아있게 되어 ```Handler```가 ```LeakActivity```를 계속 참조하므로 LeakActivity는 GC의 대상이 되지 않음. 즉, 메모리 Leak 발생

- 메모리 Leak이 발생되지 않기 위해서는 ```Handler```를 ```non-static inner class``` 대신 외부 클래스를 참조하지 않는 ```static inner class```로 선언을 해야함. ```Handler```가 더이상 ```Activity```를 참조하고 있지 않으므로, ```Activity```의 생명주기에 따라 적절하게 소멸되며 ```Handler``` 내부에서 GC를 통해 소멸된 ```Activity``` 메서드나 변수등 리소스를 참조해야 될 경우을 대비해 Activity의 참조 변수를 ```WeakReference```로 래핑해서 사용하도록 함

> 메모리 Leak에 대한 수정안

```java
public class NonLeakActivity extends Activity {
  private NonLeakHandler handler = new NonLeakHandler(this);
  
  // static inner class (Activity 참조를 위해 클래스 상속)
  private static final class NonLeakHandler extends Handler {
    private final WeakReference<NonLeakActivity> ref;
    
    public NonLeakHandler(NonLeakActivity act) {
      ref = new WeakReference<>(act);  
    }
    
    @Override
    public void handleMessage(Message msg) {
      NonLeakActivity act = ref.get();
      if (act != null) {
        
      }
    }
  }
  
  // static inner class
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

- ```Runnable```도 ```Anonymous class```에서 ```static inner class```로 변경 함. ```Anonymous class```도 ```non-static inner class```와 마찬가지로 ```outer class```에 대한 ```reference```를 가지게 되기 때문

- 즉, ```Inner class```, ```Anonymous class``` 모두 ```Activity```의 생명주기와 동일하게 생성 및 종료가 보장된다면 ```non-static inner class```로 정의해도 되지만, 아닌 경우는 ```static inner class``` 정의해야 된다는 것