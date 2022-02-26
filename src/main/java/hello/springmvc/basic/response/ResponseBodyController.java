package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @Controller 대신 @RestController 사용 시 해당 컨트롤러에 모두 @ResponseBody 가 적용된다.
// 따라서 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 데이터를 입력한다.
// 이름 그대로 Rest API(HTTP API) 를 만들 때 사용하는 컨트롤러이다.
// @ResponseBody 는 클래스 레벨에 두면 전체 메서드에 적용되는데, @RestController 안에 @ResponseBody 가 적용되어 있다.

@Slf4j
//@Controller
//@ResponseBody
@RestController // @Controller + @ResponseBody 한 것!
public class ResponseBodyController {

    // HttpServletResponse 객체를 통해서 HTTP 메시지 바디에 직접 ok 응답 메시지를 전달한다.
    // ==> response.getWriter().write("ok")
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    // HttpEntity : HTTP 메시지의 헤더, 바디 정보를 가지고 있다.
    // ResponseEntity : HttpEntity 를 상속 받았고, HTTP 응답 코드를 설정할 수 있다.
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    // @ResponseBody : view 를 사용하지 않고 HTTP 메시지 컨버터를 통해 HTTP 메시지를 직접 입력할 수 있다.
    // ResponseEntity 도 동일한 방식으로 동작한다.
    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    // ResponseEntity 를 반환한다. HTTP 메시지 컨버터를 통해서 JSON 형식으로 변환되어서 반환된다.
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    // @ResponseStatus() : 응답 코드 설정 가능 (동적 변경은 불가)
    // 프로그램 조건에 따라서 동적으로 변경하려면 ResponseEntity 를 사용하면 된다.
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
}
