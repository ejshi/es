package com.sjz.mock.controller;

import com.alibaba.fastjson.JSON;
import com.sjz.mock.model.Person;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class PersonControllerTest {

//参考网址：https://www.cnblogs.com/0201zcr/p/5756642.html

//    perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
//    get:声明发送一个get请求的方法。
//      MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables)：根据uri模板和uri变量值得到一个GET请求方式的。另外提供了其他的请求的方法，如：post、put、delete等。
//    param：添加request的参数，如上面发送请求的时候带上了了pcode = root的参数。假如使用需要发送json数据格式的时将不能使用这种方式，可见后面被@ResponseBody注解参数的解决方法
//    andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确（对返回的数据进行的判断）；
//    andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台（对返回的数据进行的判断）；
//    andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理（对返回的数据进行的判断）；

    @Autowired
    private MockMvc mvc;

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void helloWorldTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/helloworld"))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("hello world"));  //测试接口返回内容
    }

    /**
     * 列表
     * @throws Exception
     */
    @Test
    public void personListTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/person/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
    }

    /**
     * get数据
     * @throws Exception
     */
    @Test
    public void getPersonTest() throws Exception {
        String contentAsString = mvc.perform(MockMvcRequestBuilders.get("/person/get").param("name", "zhangsan"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //返回的数据是否含有“zhangsan”数据
//                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("zhangsan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is("zhangsan"))) // 判断返回数据结果name的值为zhangsan
                .andReturn().getResponse().getContentAsString();

        System.out.println(contentAsString);
    }

    /**
     * 新增/修改
     * contentType：application/x-www-form-urlencoded (form表单提交)
     * @param person
     * @return
     */
    @Test
    public void addPersonTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/person/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //制定请求文本类型
                .param("name", "zhaoliu2").param("age", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //请求成功，返回的数据结构
                // {"result":true,"code":"1000","msg":"请求成功","errors":{},"data":true,"total":null}
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Is.is(true))) //判断result结果为true
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true))) // 判断返回数据结果data的值为true
//                .andDo(MockMvcResultHandlers.print()) //打印执行结果
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
    }


    /**
     * 新增/修改
     * contentType：application/json;charset=UTF-8
     * @param person
     * @return
     */
    @Test
    public void addPersonBodyTest() throws Exception {

        /******* ======= 造数据-begin ======= **/
        Person p = new Person();
        p.setName("zhangsan123");
        p.setAge(50);
        p.setSex(1);
        String personJson = JSON.toJSONString(p);
        /******* ======= 造数据-end ======= **/

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/person/addRequestBody")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(personJson) //{"age":50,"name":"zhangsan123","sex":1}
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                //请求成功，返回的数据结构
                // {"result":true,"code":"1000","msg":"请求成功","errors":{},"data":true,"total":null}
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Is.is(true))) //判断result结果为true
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true))) // 判断返回数据结果data的值为true
//                .andDo(MockMvcResultHandlers.print()) //打印执行结果
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
    }

    /**
     * 测试文件下载
     * @throws Exception
     */
    @Test
    public void downLoadFile() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/io/download"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();


        //获取数据流中的字节数组
        byte[] contentAsByteArray = response.getContentAsByteArray();

        //定义一个文件（文件后缀和你要测试的文件下载后缀一致即可），将字节数组写到你指定的文件中
        File file = new File("d:\\test1.jpg");
        try(OutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(contentAsByteArray);
            outputStream.flush();
        }

        //response.getBufferSize()获取流大小不准确，可以通过response.getContentAsByteArray().length
        System.out.println("下载的文件大小==========="+contentAsByteArray.length);
        System.out.println("========== 下载完成 ==========");
    }
}
