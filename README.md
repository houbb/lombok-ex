# lombok-ex

lombok-ex 是一款类似于 lombok 的编译时注解框架。

主要补充一些 lombok 没有实现，且自己会用到的常见工具。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lombok-ex/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lombok-ex)
[![Build Status](https://www.travis-ci.org/houbb/lombok-ex.svg?branch=master)](https://www.travis-ci.org/houbb/lombok-ex)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lombok-ex/badge.svg?branch=master)](https://coveralls.io/github/houbb/lombok-ex?branch=master)

## 创作目的

- 补充 lombok 缺失的注解，便于日常开发使用。

- lombok 的源码基本不可读，应该是加密处理了。

- 为其他注解相关框架提升性能提供基础，后期考虑替换为编译时注解。

## 特性

- `@Serial` 支持

（1）类实现序列化接口

（2）类生成 serialVersionUID 字段，字段的值可以通过 value() 属性指定。

- `@Util` 支持

（1）类设置为 final

（2）类构造器私有化

- `@ToString` 支持

（1）自动生成 toString() 方法

（2）默认为 JSON 实现，后期将添加拓展

## 变更日志

[变更日志](CHANGE_LOG.md)

# 快速开始

## 准备工作

jdk1.7+

maven 3.x+

- 编译器启用编译时注解功能。

如  idea 启用编译时注解，勾选【enable annotation process】

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lombok-ex</artifactId>
    <version>0.0.4</version>
    <scope>provided</scope>
</dependency>
```

## 例子

- User.java

我们定义一个简单的 pojo，使用 `@Serial`

```java
package com.github.houbb.lombok.test.model;

import com.github.houbb.lombok.ex.annotation.Serial;

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

# @Util 使用案例

## 注解使用

```java
@Util
public class StringUtil {

    public static boolean isEmpty(final String string) {
        return null == string || "".equals(string);
    }

}
```

## 效果

```java
public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }
}
```

# @ToString 注解

## 简介

`@ToString` 注解在类上使用，可以默认生成 toString() 方法

例子:

```java
import com.github.houbb.lombok.ex.annotation.ToString;

@ToString
public class ToStringTest {
}
```

## 效果

编译后的 class 文件信息：

```java
import com.alibaba.fastjson.JSON;

public class ToStringTest {
    public ToStringTest() {
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
```

# 后期 Road-map

- [ ] `@AutoLog` 实现完善

- [ ] `@NotNull` 等参数校验，可以单独一个项目

- [ ] `@Sync` 等同步异步

- [ ] `@Equals` `@HashCode` `@EqualsAndHashCode` 等内置方法重载 

- [ ] bean-mapping, sensitive 等优化

- [ ] AST 基础框架

思路：通过 AST 直接解析文本，然后通过 AST 结合 jdk utils 重新构建 class 文件。