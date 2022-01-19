package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        System.out.println("name = " + name);

        //로그 레벨을 정할 수 있다
        log.trace("trace log={}", name);
        log.debug("debug log={}", name); //디버그 (개발서버 같은 데서)
        log.info(" info  log={}", name); //중요한 정보 (비즈니스로직, 운영 등에 중요한 정보)
        log.warn(" warn  log={}", name); //경고
        log.error("error log={}", name); //에러


        return "ok";
    }
}
