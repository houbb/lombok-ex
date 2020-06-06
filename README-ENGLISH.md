# lombok-ex

> [中文文档](README.md)

[lombok-ex](https://github.com/houbb/lombok-ex) is a compile-time annotation framework similar to lombok.

Mainly add some common tools that lombok does not implement, and you will use yourself.

There is no loss in annotation performance at compile time. One annotation does everything, no third-party dependencies.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lombok-ex/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lombok-ex)
[![Build Status](https://www.travis-ci.org/houbb/lombok-ex.svg?branch=master)](https://www.travis-ci.org/houbb/lombok-ex)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lombok-ex/badge.svg?branch=master)](https://coveralls.io/github/houbb/lombok-ex?branch=master)

## Creative purpose

-Added missing annotations of lombok to facilitate daily development and use.

-The source code of lombok is basically unreadable, it should be encrypted.

-Provide a basis for improving the performance of other annotation-related frameworks, and consider replacing them with compile-time annotations later.

## Features

- `@Serial`

- `@Util`

- `@ToString`

- `@Sync`

- `@Modifiers`

## CHANGE LOG

[CHANGE LOG](CHANGE_LOG.md)

# Quick Start

## Require

jdk1.7+

maven 3.x+

- enable annotation process。

If you use editor，please checked【enable annotation process】

## maven import

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lombok-ex</artifactId>
    <version>0.0.8</version>
    <scope>provided</scope>
</dependency>
```

- Gradle import

```
compile group: 'com.github.houbb', name: 'lombok-ex', version: '0.0.8'
```

# @Serial annotation

- User.java

We can use `@Serial` like this:

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

## compile

use maven command

```
$   mvn clean install
```

## result

see the User.class, content is as following：

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

# @Util annotation

## usage

```java
@Util
public class StringUtil {

    public static boolean isEmpty(final String string) {
        return null == string || "".equals(string);
    }

}
```

## result

```java
public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }
}
```

# @ToString annotation

## usage

```java
import com.github.houbb.lombok.ex.annotation.ToString;

@ToString
public class ToStringTest {
}
```

## result

ps: This is dependent on FastJson, you need import it by yourself.

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

## Config Type

`@ToString` can also config the toString() generate type.

### java code

```java
@ToString(ToStringType.CONCAT)
public class ToStringConcatTest {

    private String name;

    private int age;

    private int[] ints;

}
```

### class result

```java
import java.util.Arrays;

public class ToStringConcatTest {
    private String name;
    private int age;
    private int[] ints;

    public ToStringConcatTest() {
    }

    public String toString() {
        return "ToStringConcatTest{name=" + this.name + ", age=" + this.age + ", ints=" + Arrays.toString(this.ints) + "}";
    }
}
```

# @Sync

## Usage

```java
@Sync
public void syncTest() {
    System.out.println("sync");
}
```

## result

```java
public synchronized void syncTest() {
    System.out.println("sync");
}
```

# @Modifiers

## Intro

You can modify the access level of classes, methods, and fields with `@Modifiers`.
             
There may be no particularly good application scenarios for the time being, depending on the user's own use.
             
Note: Do not engage in something that is difficult to understand, as easy as possible for users to understand.

## Usage

`@Modifiers` has appendMode attr，default is true。

If set to false, you can directly change the modifier to the one specified by the user.

```java
import com.github.houbb.lombok.ex.annotation.Modifiers;
import com.github.houbb.lombok.ex.constant.Flags;

@Modifiers(Flags.FINAL)
public class ModifiersTest {

    @Modifiers(Flags.VOLATILE)
    private int value;

    @Modifiers(Flags.SYNCHRONIZED)
    public static void syncTest() {
        System.out.println("sync");
    }

}
```

## result

```java
public final class ModifiersTest {
    private volatile int value;

    public ModifiersTest() {
    }

    public static synchronized void syncTest() {
        System.out.println("sync");
    }
}
```

# Road-map

- [ ] Config and Optimize

- [ ] `@AutoLog` implements

- [ ] `@Equals` `@HashCode` `@EqualsAndHashCode` implements 

- [ ] `@NotNull` for argument check, like [valid](https://github.com/houbb/valid)

- [ ] `@Async` async execute [async](https://github.com/houbb/async)

- [ ] [bean-mapping](https://github.com/houbb/bean-mapping), [sensitive](https://github.com/houbb/sensitive)

- [ ] AST basic framework

Idea: parse the text directly through AST, and then rebuild the class file through AST combined with jdk utils.