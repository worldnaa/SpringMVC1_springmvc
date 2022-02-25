package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어 넣으면, view 조회X
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // request.getParameter() : 단순히 HttpServletRequest 가 제공하는 방식으로 요청 파라미터 조회
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);
        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용 - 파라미터 이름으로 바인딩
     * @ResponseBody 추가 - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     */
    @ResponseBody // 문자를 Http 응답 메시지에 넣어서 반환 (@RestController 랑 같은 효과)
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    // 요청 파라미터 이름과 변수명이 같으면 ("username") 생략가능
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    // String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
    public String requestParamV4(String username, int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam.required
     * 1) /request-param            -> username 은 필수인데, 없으므로 예외 발생
     * 2) /request-param?username=  -> 빈문자로 통과 (주의!)
     * 3) /request-param            -> int age 의 경우, null 을 int 에 입력하는 것은 불가능,
     * 따라서 Integer 변경해야 함 (또는 다음에 나오는 defaultValue 사용)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username, //required=true  : 없으면 에러 but "" 은 가능
//          @RequestParam(required = false) int age) {      //required=false : 없어도 됨 ==> 500에러 발생
            @RequestParam(required = false) Integer age) {  //required=false : 없어도 됨 ==> ok

            //int a = null;     ==> X (int에는 null이 들어갈 수 없다)
            //Integer b = null; ==> O (객체에는 null이 들어갈 수 있다)

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * defaultValue : 파라미터에 값이 없는 경우 defaultValue 를 사용하면 기본값을 적용할 수 있다
     * defaultValue 는 빈 문자의 경우에도 설정한 기본값이 적용  ==> /request-param?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamRequired(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
            //defaultValue 는 ""으로 들어올 경우도 처리해준다
            //결과 : username=guest, age=-1
        
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam Map, MultiValueMap
     * @RequestParam Map : Map(key=value)
     * @RequestParam MultiValueMap : MultiValueMap(key=[value1, value2, ...]
     * ex) (key=userIds, value=[id1, id2])
     * 파라미터의 값이 1개가 확실하다면 Map을 사용해도 되지만, 그렇지 않다면 MultiValueMap을 사용하자
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    /**
     * @ModelAttribute 사용
     * model.addAttribute(helloData) 코드도 함께 자동 적용 됨
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {

        // 스프링 MVC 는 @ModelAttribute 가 있으면 다음을 실행한다.
        // 1) HelloData 객체를 생성한다.
        // 2) 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다.
        // 3) 그리고 해당 프로퍼티의 setter 를 호출해서 파라미터의 값을 입력(바인딩) 한다.
        // ex) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.

        // 객체에 getUsername(), setUsername() 메서드가 있으면, 이 객체는 username 이라는 프로퍼티를 가지고 있다.
        // username 프로퍼티의 값을 변경하면 setUsername()이 호출되고, 조회하면 getUsername()이 호출된다.

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData); //lombok 안에 ToString이 있다

        return "ok";
    }

    /**
     * @ModelAttribute 생략 가능
     * String, int 같은 단순 타입                      ==> @RequestParam
     * argument resolver 로 지정해둔 타입 외 나머지 전부 ==> @ModelAttribute
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
}
