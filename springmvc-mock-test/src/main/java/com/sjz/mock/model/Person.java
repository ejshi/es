package com.sjz.mock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person implements java.io.Serializable{

    private String name;

    private Integer age;
    /**
     * 1：男，0：女
     */
    private Integer sex;

}
