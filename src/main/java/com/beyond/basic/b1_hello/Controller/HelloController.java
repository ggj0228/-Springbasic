package com.beyond.basic.b1_hello.Controller;

import org.springframework.ui.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Controller 어노테이션 안에 있는 Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http req를 분석하고, http res를 생성
@Controller
// 클래스 차원에 url매핑 시에 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {

    //get요청의 case들
    // case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody있을 떼

    @GetMapping("") // 아래 메서드에 대한 서버의 엔드포인트를 설정한 것
    //ResponseBody가 없고 리턴 타입이 String인 경우에 서버는 templates폴더 밑에 helloworld.html을 찾아서 화면을 return
    @ResponseBody
    public String helloWorld() {
        return "helloworld";
    }

    // case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello hellojJson() throws JsonProcessingException {
        Hello h1 = new Hello("ong", "hong@naver.com");
        // 직접 json으로 직렬화를 할 필요없이 return 타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }


    // case3.parameter 방식을 통해 사용자로부터 값을 수신
    // parameter의 형식 : /member?name=hongildong

    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name")String inputName) {
        System.out.println(inputName);
        Hello h1 = new Hello("망고", "fnksan@naver.com");
        // {name: 사용자가 넣어온 이름, email : 아무거나}

        return h1;
    }

    // case 4. pathvariable방식을 통해 사용자로부터 값을 수신
    // pathvariable의 형식 : /member/1
//    pathvariable 방식은 url을 통해 자원의 구조를 명확하게 표현할 떄 사용(좀 더 restful함)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {
//        별도의 형변환 없이도, 매개변수에 타입지정시 자동형변환 시켜줌.
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "ok";
    }

    // case 5. parameter 2개 이상 형
    // /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name")String inputName,
                         @RequestParam(value = "email")String inputEmail) {
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }

    // case 6. parameter가 많아질 경우, 데이터바인딩을 통해 input값 처리
    // 데이터바인딩: param을 사용하여 객체로 생성해줌.
    // ?name=hong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    public String param3(Hello hello) {
    // @ModelAttribute를 써도되고, 안 써도 되지만, 명시적으로 param형식의 데이터를 받겠다라는 것
      public String param3(@ModelAttribute Hello hello) {

        System.out.println(hello);
        return "ok";
    }


    // case 7. 서버에서 화면을 return, 사용자로부터 넘어오는 input값을 활용하여 동적인 화면 생성.
    // 서버에서 화면(+데이터)를 랜더링해주는 ssr방식(csr에서 서버는 데이터만)
    // mvc(model, view, controller)패턴이라고도 함
    @GetMapping("/model-param")
    public String modelParam(@RequestParam(value = "id")Long inputId, Model model) {

        // model객체는 데이터를 화면에 전달해주는 역할
        // name이라는 키에 hongildong이라는 value를 key:value 형식으로 화면에 출력
        if(inputId == 1) {
            model.addAttribute("name","hongildong");
            model.addAttribute("email","hongildong@naver.com");

        } else if (inputId == 2) {
            model.addAttribute("name","hongildong2");
            model.addAttribute("email","hongildong2@naver.com");
        }
        return "helloworld2";
    }
/* *******************************************************************************************************/
    // post요청의 case 2가지 :  url임코딩 방식 또는 multipart-formdata, json
    // case 1. text만 있는 form-data방식
    // 형식 : body부에 name=xxx&email=xxx
    @GetMapping("/form-view")
    public String formView() {
        return "form-view";
    }
    @PostMapping("/form-view")
    @ResponseBody
    // get요청에 url에 파라미터방식과 동일한 뎅이터 형식이므로, RequestParam 또는 데이터 바인딩 방식으로 가능
    public String formViewPost(@ModelAttribute Hello hello) {
        System.out.println(hello);
        return "ok";
    }
/************************************************************************************/
    // case 2-1. text와 file이 있는 form-data형식(순수 html로 제출)
    @GetMapping("/form-file-view")
    public String formFileView() {
        return "form-file-view";
    }
    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam (value = "photo")MultipartFile photo
                                   ) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }
/********************************************************************************************************/
    // case 2-2. text와 file이 있는 form-data형식(json 제출)
    @GetMapping("/axios-file-view")
    public String axiosFileView() {
    return "axios-file-view";
    }

/*****************************************************************************************************************/
    // case 3. text와 멀티file이 있는 form-data형식(js로 제출)
    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView() {
    return "axios-multi-file-view";
    }
    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam (value = "photos") List<MultipartFile> photos
    ) {
        System.out.println(hello);
        for (int i = 0; i < photos.size(); i++) {
            System.out.println(photos.get(i).getOriginalFilename());
        }
        return "ok";
    }

/*******************************************************************************************************/
    // case 4. json데이터 전송
    @GetMapping("/axios-json-view")
    public String axiosJsonView() {
        return "axios-json-view";
    }
    @PostMapping("/axios-json-view")
    @ResponseBody
    // RequestBody : json형식으로 데이터가 들어올 때 객체로 자동 파싱
    public String axiosJsonViewPost(@RequestBody Hello hello) {
        System.out.println(hello);
    return "ok";
    }

/******************************************************************************************************/
    // case 5. 증첩된 json 데이터 처리
    @GetMapping("/axios-nested-json-view")
    public String axiosNestedJsonView() {
    return "axios-nested-json-view";
    }
    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    public String axiosNestedJsonViewPost(@RequestBody Student student) {
        System.out.println(student);
        return "ok";
    }
/***********************************************************************************************************/
    // case 6. json과 file을 같이 처리할 때 : text구조가 복잡하여 피치못하게 json을 써야하는 경우.
    // 데이터 형식 : hello={name: "xx", emailL: "xx"}&photo="이미지.jpg"}
    // 결론은 단순 json구조가 아닌, multipart-formdata구조 안에 json을 넣는 구조.
    @GetMapping("/axios-json-file-view")
    public String axiosJsonFileView() {
    return "/axios-json-file-view";
    }
    @PostMapping("/axios-json-file-view")
    @ResponseBody
    public String axiosJsonFileViewPost(
            // json과 file을 함께 처리할 때 RequestPart를 일반적으로 활용
            @RequestPart("hello") Hello hello,
            @RequestPart("photo") MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }


}
