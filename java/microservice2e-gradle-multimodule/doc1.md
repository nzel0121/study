# 마이크로서비스 2nd edition gradle multi module 스타일 변경
스프링5.0 마이크로서비스 2/e 는 책은 좋은 것 같은데 소스가 간간히 공부에 방해가되는 부분이 있다.  
방해되는 목록
* chapter6 기준으로 service가 5개 인데 책을 읽기만 하고 boot 실행을 java -jar 로 하는 경우는 상관없으나 내부적으로 흐름을 본다던지 하려면 IDE 에서 project 를 여러개 띄워야함. ( chapter7 로 가면 11개로 늘어남. )
* chapter6 에서는 boot 2.0 을 기준으로 작성 후 chapter7 에서 cloud를 구현하며 boot 1.5.2 로 버전이 내려감.( sping-cloud 는 Dalston 을 사용 )
* chapter6 과 chapter7에서 사용되는 각 Microservice에 대한 port가 다름.

해보려는 것
1. Gradle 의 Multi Module 를 이용하여 한 소스내에서 모든 서비스를 기동 시켜서 IDE 하나에서 모든 소스를 보자.
2. chapter6 을 기준 ( spring-boot2 ) 에 맞는 spring-cloud 로 설정한다. ( Dalston 을 사용하면 안 맞는 부분이 있음) 
3. chapter6 을 소스를 기준으로 점진적으로 변경해 가며 복습을 한다. ( 개인적인 이유 )

# Gradle 프로젝트 생성 
##  new project.. 
1. file > new Project > Gradle > java 체크 > next
2. GroupId , ArtfactId 적고 > next
3. Use auto-import 체크 > next 

## Fares(운임) modele 추가 
1. Project Tree( Command + 1 ) 에서 Project 상단에서 > new > Module > Gradle > java 체크 > next 
2. ArtfactId 적고 > next ( 구지 그룹아이디가 inherit 이 아닌경우 GroupId 도 변경 )
-> 이렇게되면 setting.gradle 에 Module 이 추가됨.
```gradle
rootProject.name = '[project name]'
include 'fares'
```

## build.gradle 설정
다 지우고 아래와 같이 작성 
```gradle
plugins {
    id 'java'
}

dependencies {
    compile project(':fares')
}
```

## Submodule (fares) build.gradle 설정
사용기술 
* spring-boot
* srping-boot-web
* hal-browser
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
    compile('org.springframework.boot:spring-boot-starter-actuator')
    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    compile('org.springframework.data:spring-data-rest-hal-browser')
    runtime('com.h2database:h2')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

> 각각의 build.gradle 내 내용은 잘모르는 상태 이나. maven을 아는상태에서는 저 형태로 설정 후 dependencies 내 내용을 Scope 에 맞게 추가하면된다. 
향후 추가적으로 dependency를 추가하는 상황에 대해 정리한다.


### Fares Project 코드 작성
1. Spring Boot 기본 Application 작성 
    1. pacakge com.brownfield.pss.fares
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
2. Entity 생성
    1. package com.brownfield.pss.fares.entity
    2. Fare.java > @Entity 붙여주고 , Id 로 사용할 필드에 @Id , @GeneratedValue 붙여줌
    ```java
    @Entity
    public class Fare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String flightNumber;
    private String flightDate;
    private String fare;
    ```
    > setter,getter, constructor 는 생략함.  
    constructor 는 Id를 제외하고 생성
3. Repository 생성
    1. pacakge com.brownfield.pss.fares.repository
    2. FaresRepository.java(interface) extends JpaRepository<T,ID>
    3. flightNumber 와 flightDate 로 조회가 가능한 메소드'getFareByFlightNumberAndFlightDate' 생성 
    ```java
    public interface FaresRepository extends JpaRepository<Fare,Long> {
        Fare getFareByFlightNumberAndFlightDate(String flightNumber, String flightDate);
    }
    ```
4. Component 생성 ( repository 를 이용해서 Fare를 가져올 비즈니스 )
    1. pacake com.brownfield.pss.fares.component
    2. FaresComponent.java 
        1. @Autowired FaresRepository ( 데이터 조회해야되니까! )
        2. Fare getFare(String flightNumber, String flightDate) 메소드 생성 ( 항공번호와 항공시간으로 조회하는 로직 - 단 로직없음!^^)
    ```java
    @Component
    public class FaresComponent {

        private final FaresRepository faresRepository;

        public FaresComponent(FaresRepository faresRepository) {
            this.faresRepository = faresRepository;
        }

        public Fare getFare(String flightNumber, String flightDate) {
            return faresRepository.getFareByFlightNumberAndFlightDate(flightNumber, flightDate);
        }
    }
    ```
5. Controller 생성
    1. pakage com.brownfield.pss.fares.controller
    2. FaresController.java
        1. @RestController ( 왜하는지 설명하지않겠음 )
        2. @RequestMapping("/fares) ( 왜하는지 설명하지 않겠음.)
        3. @Autowired FaresComponent - 컨트롤러로 요청이 들어오면 getFare호출해서 데이터 조회를 하기위해서
        4. @RequestMapping Fare getFare(...) 메소드 생성 - endpoint 생성
    ```java
    @RestController
    @RequestMapping("/fares")
    public class FaresController {

        private final FaresComponent faresComponent;

        public FaresController(FaresComponent faresComponent){
            this.faresComponent = faresComponent;
        }

        @RequestMapping("/get")
        Fare getFare(@RequestParam(value = "flightNumber") String flightNumber, @RequestParam(value = "flightDate") String flightDate) {
            return faresComponent.getFare(flightNumber, flightDate);
        }
    }
    ```
    > 이 상태로는 조회가 불가함. 이유: 데이터가 없음, 데이터를 먼저 넣고 데이터넣은것으로 조회가되는 Test 를 작성해서 하면 좋겠지만.. 현재 목적이 multimodule 로 소스를 합치는 것이기 떄문에 클래스 생성등의 순서가 일하는 순서와 맞지 않을 수 있음
6. CommandRunner 를 이용하여 Fare 저장하기
    1. Application 에 implements CommandLineRunner 를 한다. 
    ```java
    public class Application implements CommandLineRunner {
    ```
    2. CommandRunner.run 을 implement() 한다. 
        1. 임의에 Fare 객체를 생성자를 통해 생성
        2. 생성된 Fare 들을 repo.save() 를 통해 저장한다. 
    ```java
    @Override
    public void run(String... args) throws Exception {
        Fare[] fares = {
                new Fare("BF100","22-JAN-18", "101"),
                new Fare("BF101","22-JAN-18", "101"),
                new Fare("BF102","22-JAN-18", "102"),
                new Fare("BF103","22-JAN-18", "103"),
                new Fare("BF104","22-JAN-18", "104"),
                new Fare("BF105","22-JAN-18", "105"),
                new Fare("BF106","22-JAN-18", "106")};
        List<Fare> list = Arrays.stream(fares).collect(Collectors.toList());
        list.forEach(fare -> faresRepository.save(fare));
    }
    ```
7. Fare 테스트
    1. Application 을 실행 ( boot 실행하는 방법은 설명안함! )
    2. Browser 에서 http://localhost:8080/fares/get?flightNumber=BF100&flightDate=22-JAN-18 을 호출 > 아래와 같이 나오면 성공
    ```json
    {"id":1,"flightNumber":"BF100","flightDate":"22-JAN-18","fare":"101"}
    ```