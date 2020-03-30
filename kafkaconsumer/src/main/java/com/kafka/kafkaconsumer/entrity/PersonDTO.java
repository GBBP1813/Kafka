package com.kafka.kafkaconsumer.entrity;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonDTO implements Serializable {

    private String name;

    private Integer age;

}
