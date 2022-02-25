package hello.springmvc.basic.request;

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
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        // Stream은 바이트 코드라서 문자로 바꿀 때는 어떤 인코딩을 지정해서 바꿀지 명시해줘야 한다

        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    /**
     * 스프링 MVC 는 다음 파라미터를 지원한다
     * InputStream(Reader)  : HTTP 요청 메시지 바디의 내용을 직접 조회
     * OutputStream(Writer) : HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    /**
     * HttpEntity : HTTP header, body 정보를 편라하게 조회
     * - 메시지 바디 정보를 직접 조회
     * - 요청 파라미터를 조회하는 기능과 관계 없음 (@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * HttpEntity 는 응답에서도 사용 가능
     * - 메시지 바디 정보 직접 반환
     * - 헤더 정보 포함 가능
     * - view 조회 X
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<>("ok");
    }

    /**
     * @RequestBody
     * - 메시지 바디 정보를 직접 조회 (@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 메시지 바디 정보 직접 반환 (view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {

        log.info("messageBody={}", messageBody);
        return "ok";
    }
}

// @RequestBody
// ==> @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.
// ==> 참고로 헤더 정보가 필요 하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
// ==> 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam, @ModelAttribute 와 전혀 관계없다.

// 요청파라미터 vs HTTP 메시지바디
// 요청 파라미터를 조회하는 기능         : @RequestParam , @ModelAttribute
// HTTP 메시지 바디를 직접 조회하는 기능 : @RequestBody

// @ResponseBody
// ==> @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
// ==> 물론 이 경우에도 view 를 사용하지 않는다.
