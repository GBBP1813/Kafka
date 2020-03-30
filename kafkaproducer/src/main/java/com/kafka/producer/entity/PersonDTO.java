package com.kafka.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class PersonDTO implements Serializable {

    private String name;

    private Integer age;

}
