package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {

        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {

        // String 을 반환하는 경우 - View or HTTP 메시지
        // @ResponseBody 가 없으면 response/hello 로 뷰 리졸버가 실행 되어서 뷰를 찾고 렌더링한다.
        // @ResponseBody 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는 문자가 입력된다.
        // 다음 경로의 뷰 템플릿이 렌더링되며 실행 : templates/response/hello.html

        model.addAttribute("data", "hello!!");
        return "response/hello";
    }

    @RequestMapping("/response/hello") //컨트롤러 경로 이름과, 뷰의 논리적 이름이 같을 경우(권장X)
    public void responseViewV3(Model model) {

        // @Controller 를 사용하고, HttpServletResponse, OutputStream(Writer) 같은
        // HTTP 메시지 바디를 처리하는 파라미터가 없으면 요청 URL 을 참고해서 논리 뷰 이름으로 사용한다.
        // 요청 URL: /response/hello
        // 실행 : templates/response/hello.html

        model.addAttribute("data", "hello!!!");
    }
}
