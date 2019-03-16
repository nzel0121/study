# Search (검색) Module 만들기 
일전에 gradle multi module 상태에서 fares 를 생성하였으니 이제 search를 만들어본다.

##  Modele 추가
1. project > new > Module > Gradle 선택 , Java 체크 > next
2. ArtifactId 에 'search' > next > finish
이렇게되면 fares 때와 동일하게 setting.gradle 에 Module 이 추가됨.
```gradle
rootProject.name = '[project name]'
include 'fares'
include 'search'
```

3. build.gradle 설정
dependencies 하위로 **compile project(':search')** 를 추가한다.
```gradle
plugins {
    id 'java'
}

dependencies {
    compile project(':fares')
    compile project(':search')
}
```

## Submodule (search) build.gradle 설정
사용기술 
* spring-boot
* srping-boot-web
* spring-boot-starter-data-jpa
* h2 database

build.gradle 내용 
```gradle
buildscript {
    ext {
        springBootVersion = '2.0.7.RELEASE'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'terry.study.microservice2e'
sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    compile('org.springframework.boot:spring-boot-starter-amqp')
    runtime('com.h2database:h2')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

## Search Project 코드 작성
1. Spring Boot 기본 Application 작성 
    1. pacakge com.brownfield.pss.search
    2. Application.java 생성
    3. @SpringBootApplication 어노테이션 붙임
    ```java
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        }
    }
    ```
    4. 서버기동 후 8080 으로 잘 뜨는지 확인
> 혹 기존 fares 를 기동중에 있다면 기동을 종료하고 Search 를 시킨다.  
각각의 모듈이 기본포트(8080)을 사용하는 부분은 이후 두 모듈을 같이 띄울때 수정한다.

2. Entity 생성  
책만 읽은 사람들이 Search 모듈의 Entity를 모두 이해할 거라 생각하지 않는다.  
Entity에 대한 설명이 조금 필요한데. **3개의 Entity**를 만들 게 될 것이다. 
* Entity
    * Flight  
    비행번호 , 출발지 , 도착지 , 비행일자 , 운임(아래객체) , 인벤토리(아래아래객체)
    Flight 객체는 Fare, Inventory 객체를 OneToOne 으로 가지고 있을 것이다. 
    * Fares 
    Id 와 운임, 통화 를 가지고 있다.
    * Inventory  
    Id 와 count를 가지고 있다.

    1. package com.brownfield.pss.search.entity
    2. Entity 생성 객체연결을 위해 Fares , Inventory 부터 만들도록 함.
        1. Fare  
        Fares.java > @Entity 붙여주고 , Id 로 사용할 필드에 @Id @GeneratedValue 붙여주고 column 을 fare_id 와 연결하기위해 **@Column(name="fare_id")** 를 추가로 연결
        ```java
        @Entity
        public class Fares {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "fare_id")
        private Long id;
        
        private String fare;
        private String currency;

        ....

        ```
        > setter,getter, constructor 는 설명에서 생략함.(실제론 만들어야함! 아님 Lombok쓰셔도..되요 - 근데 난 지금 기존소스와 동일하게 추가적인 의존성을 넣고 싶지 않다.)  
        constructor 는 Id 어노테이션이 붙은 필드를 제외하고 생성한다.

        2. Inventory  
        Inventory.java > @Entity 붙여주고 , Id 로 사용할 필드에 @Id @GeneratedValue 붙여주고 column 을 inv_id 와 연결하기위해 **@Column(name="inv_id")** 를 추가로 연결
        ```java
        @Entity
        public class Inventory {

            @Id
            @GeneratedValue(strategy=GenerationType.AUTO)
            @Column(name = "inv_id")
            long id;
            
            int count;

        ....
        
        ```
        3. Flight  
        Flight.java 에 @Entity 붙여주고 , Id 로 사용할 필드에 @Id @GeneratedValue 붙여준다.  
        위에서 생성한 Fare 와 Inventory 를 1:1 관계로 가지게 설정하기위해 @OneToOne 으로 연결한다. 
        ```java
        @Entity
        public class Flight {

            @Id
            @GeneratedValue(strategy=GenerationType.AUTO)
            long id;
            
            String flightNumber;
            String origin;
            String destination;
            String flightDate;
            
            @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name="fare_Id")
            Fares fares;

            @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name="inv_Id")
            Inventory inventory;

            ....

        ```
3. Repository 생성
    1. pacakge com.brownfield.pss.search.repository
    2. FlightRepository.java(interface) extends JpaRepository<T,ID>
    3. 정상적으로 위에 생성된 Entity가 저장되는지 보기위해 Application 에서 CommandLineRunner 의 run 메소드로 FlightRepository.saveAㅣㅣ() 이 실행되는지 확인해보겠다. 
        1. Application 에 implements CommandLineRunner 를 하고 run 메소드를 implement 한다. 
        ```java
        public class Application implements CommandLineRunner {

            @Override
            public void run(String... args) throws Exception {
                // 여기에 Flight객체를 생성해서 saveAll한다. 
            }

            ...
        ```
        2. run 메소드에서 repository 를 이용해서 entity를 저장하기위해 repository 를 @Autowired 해준고 저장한다. 
        ```java
        @Autowired
        FlightRepository flightRepository;

        @Override
        public void run(String... args) throws Exception {
            List<Flight> flights = Arrays.asList(
                    new Flight("BF100", "SEA", "SFO", "22-JAN-18", new Fares("100", "USD"), new Inventory(100)),
                    new Flight("BF101", "NYC", "SFO", "22-JAN-18", new Fares("101", "USD"), new Inventory(100)),
                    new Flight("BF105", "NYC", "SFO", "22-JAN-18", new Fares("105", "USD"), new Inventory(100)),
                    new Flight("BF106", "NYC", "SFO", "22-JAN-18", new Fares("106", "USD"), new Inventory(100)),
                    new Flight("BF102", "CHI", "SFO", "22-JAN-18", new Fares("102", "USD"), new Inventory(100)),
                    new Flight("BF103", "HOU", "SFO", "22-JAN-18", new Fares("103", "USD"), new Inventory(100)),
                    new Flight("BF104", "LAX", "SFO", "22-JAN-18", new Fares("104", "USD"), new Inventory(100))
            );
            flightRepository.saveAll(flights);
        }
        ```
        3. 서버기동시 run메소드가 실행됨으로 서버가 정상기동됐다면 되고있다고 보고 진행함. (개발시였다면 확인하고 넘어가야함. 테스트던, 로그던..)
    4. FlightRepository 에서 Flight 목록 , Flight 단건 조회를 위해 메소드를 2개 생성한다. JPA 의 기능을 활용하기위해 메소드 이름을 관례만든 것이다. findBy[필드명]And[필드명]
    ```java
    List<Flight> findByOriginAndDestinationAndFlightDate(String origin,String destination, String flightDate);

	Flight findByFlightNumberAndFlightDate(String flightNumber, String flightDate);
    ```
4. Controller 생성  
 화면에서 넘어와서 component 로 넘겨줄 DTO 를 만들기위해 컴포넌트보다 먼저 컨트롤러를 생성한다. 중간부분에 Component 가 없어서 나는 에러는 5. Component생성 을 하면 사라지니 그냥 진행하도록 한다.
    1. pacakge com.brownfield.pss.search.controller
    2. SearchRestController.java 를 만들고  @RestController, @RequestMapping("/search")을 붙여준다. ( 소스상에는 CORS 때문에 @CrossOrigin 이 붙어있으나. 그 부분은 외부호출이 시작될때 설명하며 붙이도록 진행)
    ```java
    @RestController
    @RequestMapping("/search")
    class SearchRestController {

    ```
    2. Component를 @Autowired 한다.
    ```java
    @Autowired
    private SearchComponent searchComponent;
    ```
    3. PostMapping 을 하나 만들어준다.  
    >본책에서 @RequestMapping 을 사용하여 method = 의 형태를 띔 , @PostMapping 으로 대체 가능함. 
    ```java
    @RequestMapping(value="/get", method = RequestMethod.POST)
	List<Flight> search(@RequestBody SearchQuery query){
		System.out.println("Input : "+ query);
		return searchComponent.search(query);
	}
    ```
    4. 저렇게 코딩하고 나면 파라미터로 받고 있는 SearchQuery가 에러가 나게됨. DTO 객체를 만들어준다. 
    > 당연히. Contructor , setter , getter 생성해줘야 사용 가능하다.
    ```java
    public class SearchQuery {
        String origin;
        String destination;
        String flightDate;

    ...
    ```
5. Component생성 1 
4, 5 를 왔다 갔다하면 진행했어야 하는데 단순히 책의 소스를 멀티모듈로 만들어가는 과정임으로 순차적으로 소스를 작성해 나감.
    1. pacakge com.brownfield.pss.search.component
    2. SearchComponent.java 를 @Component 로 만든다. 
    ```java
    @Component
    public class SearchComponent {
    ```
    3. Repository를 이용해서 Flight 목록 등을 가져오는 로직을 위해 @Autowired FightRepository 한다. 
    ```java
    @Autowired
    private FlightRepository flightRepository;
    ```
    4. Flight목록을 조회하여 Inventory가 없는 데이터를 제외 후 반환하는 메소드를 생성
    ```java
    public List<Flight> search(SearchQuery query){
		List<Flight> flights= flightRepository.findByOriginAndDestinationAndFlightDate(
										query.getOrigin(),
										query.getDestination(),
										query.getFlightDate()); 
		List<Flight> searchResult = new ArrayList<Flight>();
		searchResult.addAll(flights);
		flights.forEach(flight -> {
			flight.getFares();
			int inv = flight.getInventory().getCount();
			if(inv < 0) {
				searchResult.remove(flight);
			}
		});
		return searchResult; 
	}
    ```
    5. Inventory 를 업데이트 하는 메소드 생성 
    ```java
    public void updateInventory(String flightNumber, String flightDate, int inventory) {
		logger.info("Updating inventory for flight "+ flightNumber + " innventory "+ inventory); 
		Flight flight = flightRepository.findByFlightNumberAndFlightDate(flightNumber,flightDate);
		Inventory inv = flight.getInventory();
		inv.setCount(inventory);
		flightRepository.save(flight); 
	}
    ```
    > 책 내 내용상 Inventory의 값을 Queue를 이용하여 업데이트 할 때 사용함.

6. Component생성 2  
RabbitMQ 를 이용하여 비동기로 외부 Queue와 연결되는 컴포넌트를 생성한다.
    1. pacakge com.brownfield.pss.search.component
    2. Reciever.java 를 @Component 로 만든다. 
    ```java
    @Component
    public class Receiver {
    ```
    3. 로직중 위에서 만든 updateInventory를 호출하기위해 @Autowired SearchComponent 한다.
    ```java
    @Autowired
    SearchComponent searchComponent;
    ```
    4. Queue 를 @Bean 으로 등록한다.  
    외부 큐를 사용하기위해서 Bean으로 등록 
    ```java
    @Bean
	Queue queue() {
		return new Queue("SearchQ", false);
	}
    ```
    5. RabbitMQ를 구독(Listen)하기 위한 @RabbitListner 작업 
    ```java
    @RabbitListener(queues = "SearchQ")
    public void processMessage(Map<String,Object> fare) {
       searchComponent.updateInventory((String)fare.get("FLIGHT_NUMBER"),(String)fare.get("FLIGHT_DATE"),(int)fare.get("NEW_INVENTORY"));
    }
    ```

7. 서버기동 및 확인 1. rabbit설치 후 확인  
자... 다 됐다. 서버를 띄워보자! ---> 에러가 날것이다. 왜? Rabbit Server가 없으니까..  
RabbitMQ 설치 및 구동을 해야한다.

    1.  https://www.rabbitmq.com/download.html 에서 받고
    2. RabbitMQ 실행 

    ```sh
    $ ./rabbitmq-server
    ```
    3. 어플리케이션에서 포트설정 등을 해줘야하는데 spring-boot-starter-amqp 에서 dafault port 로 5672를 사용중에 있어 에러가 나지 않을 것이다.
    4. 어플리케이션을 기동하면 정상기동은 될 것이고 아래 로그처럼 무설정상태로 rabbit server 와 연결된 것을 알 수 있다.
    ```
    [cTaskExecutor-1] o.s.a.r.c.CachingConnectionFactory       : Created new connection: rabbitConnectionFactory#1c240cf2:0/SimpleConnection@149235a8 [delegate=amqp://guest@127.0.0.1:5672/, localPort= 64878]
    ```

8. 멀티모듈 상에서 Application 띄우기  
에러가 없다는 전제 하에 어플리케이션을 2개 생성했으니 두개를 모두 기동해보자.
    1. search 모듈에 있는 Application 을 시작 한다. 
    ```
    com.brownfield.pss.search.Application    : Started Application in 21.227 seconds (JVM running for 27.431)
    ```
    2. fares 모듈에 있는 Application 을 시작 한다.  
    어라? 실패?? 이유는? 아래와 같다. 8080포트가 already be in use라고 포트를 바꾸라고 함. 그럼 포트를 설정해보자
    ```
    ***************************
    APPLICATION FAILED TO START
    ***************************

    Description:

    The Tomcat connector configured to listen on port 8080 failed to start. The port may already be in use or the connector may be misconfigured.

    Action:

    Verify the connector's configuration, identify and stop any process that's listening on port 8080, or configure this application to listen on another port.
    ```
    3. 포트설정 with application.properties 
        1. fares 모듈  
        위치는 resources/application.properties를 생성하여 server.port=8080 를 입력해준다.
        ```
        server.port=8080
        ```
        > 책(chapter6)에서 말하는 것과 다르며, 책도 소스와 다름. 임의로 8090로 진행
        2. search 모듈
        위치는 resources/application.properties를 생성하여 server.port=8090 를 입력해준다.
        ```
        server.port=8090
        ```
    > 앞으로 만들 다른 모듈에서도 서버포트를 모두 다르게 설정해줘야 함.