package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // HttpServletRequest 를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);

        // 문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환한다.
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username{}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    /**
     * @RequestBody
     * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환 (view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        // @RequestBody 를 사용해서 HTTP 메시지에서 데이터를 꺼내고 messageBody 에 저장한다
        log.info("messageBody={}", messageBody);

        // 문자로된 JSON 데이터인 messageBody 를 objectMapper 를 통해서 자바 객체로 변환한다
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username{}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * @RequestBody 생략 불가능 (@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content- type: application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data) {

        // [ @RequestBody 객체파라미터 ]
        // @RequestBody HelloData data : @RequestBody에 직접 만든 객체를 지정할 수 있다.
        // HttpEntity, @RequestBody 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 문자나 객체 등으로 변환해준다.
        // HTTP 메시지 컨버터는 문자뿐만 아니라 JSON 도 객체로 변환해주는데, 방금 V2에서 했던 작업을 대신 처리해준다.

        // [ @RequestBody 는 생략 불가능 ]
        // HelloData 에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어 버린다.
        // HelloData data ==> @ModelAttribute HelloData data
        // 따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.

        log.info("username{}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> data) {

        HelloData helloData = data.getBody();

        log.info("username{}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {

        // @RequestBody  요청 : json 요청 -> HTTP 메시지 컨버터 -> 객체(HelloData)
        // @ResponseBody 응답 : 객체(HelloData) -> HTTP 메시지 컨버터 -> json 응답

        log.info("username{}, age={}", data.getUsername(), data.getAge());
        return data;
    }
}
