package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie) {

        log.info("request={}", request);       // request=org.apache.catalina.connector.RequestFacade@5dffbde5
        log.info("response={}", response);     // response=org.apache.catalina.connector.ResponseFacade@3b8b293f
        log.info("httpMethod={}", httpMethod); // httpMethod=GET
        log.info("locale={}", locale);         // locale=ko_KR
        log.info("headerMap={}", headerMap);   // headerMap={host=[localhost:8080], connection=[keep-alive], 등등}
        log.info("header host={}", host);      // header host=localhost:8080
        log.info("myCookie={}", cookie);       // myCookie=null

        return "ok";
    }
}

/**
 * 1) HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod

 * 2) Locale : Locale 정보를 조회한다.

 * 3) @RequestHeader MultiValueMap<String, String> headerMap : 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
 * ==> MultiValueMap : MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다. (ex. keyA=value1&keyA=value2)

 * 4) @RequestHeader("host") String host : 특정 HTTP 헤더를 조회한다.
 * ==> 속성 필수 값 여부 : required / 기본 값 속성 : defaultValue

 * 5) @CookieValue(value = "myCookie", required = false) String cookie : 특정 쿠키를 조회한다.
 * ==> 속성 필수 값 여부 : required / 기본 값 속성 : defaultValue
 */
