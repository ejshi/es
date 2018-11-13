package com.sjz.mock.controller;

import com.google.common.collect.Lists;
import com.sjz.mock.model.Person;
import com.sjz.mock.model.ResponseCodeEnum;
import com.sjz.mock.model.ResponseJson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;
import java.util.List;

@RequestMapping("/person")
@RestController
public class PersonController {
    private static List<Person> personDBList = Lists.newArrayList();

    @PostConstruct
    public void initData(){
        personDBList.add(Person.builder().name("zhangsan").age(20).sex(1).build());
        personDBList.add(Person.builder().name("lisi").age(30).sex(1).build());
        personDBList.add(Person.builder().name("xiaohua").age(18).sex(0).build());
    }

    @GetMapping(path = "/list")
    public ResponseJson<List<Person>> list(){

        return new ResponseJson<>(true, ResponseCodeEnum.REQUEST_SUCCESS, personDBList);
    }

    /**
     * contentType：application/x-www-form-urlencoded (form表单提交)
     * @param person
     * @return
     */
    @PostMapping(path = "/add")
    public ResponseJson<Boolean> add(Person person){
        if(person == null){
            return new ResponseJson<>(false,ResponseCodeEnum.REQUEST_FAIL,"数据不能为空");
        }

        System.out.println("===========" + person.toString());

        if(StringUtils.isEmpty(person.getName())){
            return new ResponseJson<>(false,ResponseCodeEnum.REQUEST_FAIL,"姓名不能为空");
        }

        personDBList.add(person);

        return new ResponseJson<>(true,ResponseCodeEnum.REQUEST_SUCCESS,true);
    }

    /**
     * contentType：application/json;charset=UTF-8
     * @param person
     * @return
     */
    @PostMapping(path = "/addRequestBody")
    public ResponseJson<Boolean> addRequestBody(@RequestBody Person person){


        if(person == null){
            return new ResponseJson<>(false,ResponseCodeEnum.REQUEST_FAIL,"数据不能为空");
        }

        System.out.println("================"+person.toString());

        if(StringUtils.isEmpty(person.getName())){
            return new ResponseJson<>(false,ResponseCodeEnum.REQUEST_FAIL,"姓名不能为空");
        }

        personDBList.add(person);

        return new ResponseJson<>(true,ResponseCodeEnum.REQUEST_SUCCESS,true);
    }





    @GetMapping(path = "/delete/{name}")
    public ResponseJson<Boolean> delete(@PathVariable(value = "name") String name){

        for (Person person : personDBList) {
            if(person.getName().equals(name)){
                personDBList.remove(person);
                break;
            }
        }

        return new ResponseJson<>(true,ResponseCodeEnum.REQUEST_SUCCESS,"删除成功");
    }

    @GetMapping(path = "/get")
    public ResponseJson<Person> get(@RequestParam(value = "name") String name){
        Person p = null ;
        for (Person person : personDBList) {
            if(person.getName().equals(name)){
                p = person;
                break;
            }
        }

        if(p == null){
            return new ResponseJson<>(false,ResponseCodeEnum.REQUEST_FAIL, name+"数据不存在");
        }
        return new ResponseJson<>(true,ResponseCodeEnum.REQUEST_SUCCESS,p);
    }
}
