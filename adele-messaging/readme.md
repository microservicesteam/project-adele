**Possible configuration of listeners**

```java
@Configuration
public class EventListenerConfig {

    @Autowired
    ReservationRequestedEventListener rEventListener;

    @Autowired
    ReservationCancelledEventListener bEventListener;


    @PostConstruct
    public void init(){
        rEventListener.addConsumer(e -> System.out.println(e.eventType() + " 1"));
        rEventListener.addConsumer(e -> System.out.println(e.eventType() + " 2"));
        bEventListener.addConsumer(e -> System.out.println(e.eventType() + " 3"));
    }
}
```
