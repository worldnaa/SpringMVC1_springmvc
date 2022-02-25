package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Controller : 반환값이 String이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
 * @RestController : 반환값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다. (실행결과로 ok 메세지 받음)
 */
@Slf4j
@RestController
public class LogTestController {

//  private final Logger log = LoggerFactory.getLogger(getClass()); // getClass() 대신 클래스명 써도 됨

    @RequestMapping("/log-test")
    public String logTest(){

        String name = "Spring";

        // 사용하지 말아야 할 방식 
        System.out.println("name = " + name);
        log.info(" trace  log = " + name); // 더하기 연산이 일어나서, trace를 사용 안 하는데도, 리소스를 사용하게 되어 비효율적
        
        // 아래 순서로 로그 레벨을 정할 수 있고, 실행시 info/warn/error 만 보여진다
        // trace/debug 로그를 보고 싶으면, application.properties 에서 얼만큼의 로그를 보여줄 지 정할 수 있다
        // 실무에선 로컬에 trace부터 / 개발에 debug부터 / 운영에 info 부터 많이 한다

        log.trace("trace log={}", name);
        log.debug("debug log={}", name); //디버그 (개발서버 같은 데서)
        log.info(" info  log={}", name); //중요한 정보 (비즈니스로직, 운영 등에 중요한 정보)
        log.warn(" warn  log={}", name); //경고
        log.error("error log={}", name); //에러

        return "ok";
    }
}

// 로그가 출력되는 포멧 : 시간, 로그레벨, 프로세스 ID, 쓰레드명, 클래스명, 로그메시지
// ex) 2022-02-25 13:33:01.277  INFO 26920 --- [nio-8080-exec-1] hello.springmvc.basic.LogTestController  :  trace  log=Spring