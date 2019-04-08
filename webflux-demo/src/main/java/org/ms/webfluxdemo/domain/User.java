package org.ms.webfluxdemo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author Zhenglai
 * @since 2019-04-09 01:18
 */
@Document(collection = "user")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private int age;
}
