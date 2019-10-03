# lombok-ex

lombok-ex 是一款类似于 lombok 的编译时注解框架。

主要补充一些 lombok 没有实现，且自己会用到的常见工具。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lombok-ex/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lombok-ex)
[![Build Status](https://www.travis-ci.org/houbb/lombok-ex.svg?branch=master)](https://www.travis-ci.org/houbb/lombok-ex)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lombok-ex/badge.svg?branch=master)](https://coveralls.io/github/houbb/lombok-ex?branch=master)

## 特性

- `@Serial` 支持

## 变更日志

[变更日志](doc/CHANGE_LOG.md)

# 快速开始

## 准备工作

jdk1.7+

maven 3.x+

- 编译器启用编译时注解功能。

如  idea 启用 `enable annotation process`

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lombok-ex</artifactId>
    <version>0.0.1</version>
    <scope>provided</scope>
</dependency>
```

## 例子

- User.java

我们定义一个简单的 pojo，使用 `@Serial`

```java
package com.github.houbb.lombok.test.model;

import com.github.houbb.lombok.ex.annotation.Serial;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
@Serial
public class User {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```

## 编译

直接使用 maven 命令编译

```
$   mvn clean install
```

## 编译结果

查看对应的 User.class 文件，内容如下：

```java
package com.github.houbb.lombok.test.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final Long serialVersionUID = 1L;
    private String name;

    public User() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
