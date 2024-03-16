# Spring Boot Rest Api Example

This is a example Java / Gradle / Spring Boot (version 3.x) application that can be used as a starter for creating a microservice with built-in health check, metrics and much more. I hope it helps you.

## Component

- JDK 17
- Gradle 8.x
- Spring Boot 3.x
- JPA, H2, MapStruct
- QueryDSL 5.0.0
- lombok
- Swagger 3.x
- ResponseEntity
- RestControllerAdvice
- health check
- Spring Boot Security with JWT

## Swagger UI 3.x

http://localhost:8080/swagger-ui/index.html

## health check

http://localhost:8080/actuator/health

## Spring Boot Security with JWT

```bash
POST http://localhost:8080/api/signin
{
  "username": "admin",
  "password": "admin"
}

POST http://localhost:8080/api/signup
{
  "username": "skyer9",
  "password": "abcd1234",
  "nickname": "skyer9"
}

POST http://localhost:8080/api/reissue
{
  "token": "refreshToken"
}

GET http://localhost:8080/api/user
Bearer accessToken

GET http://localhost:8080/api/user/{username}
Bearer accessToken
```

## Database

### MariaDB

```sql
CREATE USER 'myuser'@'localhost' IDENTIFIED BY 'mypass';
CREATE USER 'myuser'@'%' IDENTIFIED BY 'mypass';

CREATE DATABASE myboard;

GRANT ALL PRIVILEGES ON myboard.* TO 'myuser'@'localhost';

FLUSH PRIVILEGES;

USE myboard;

CREATE TABLE `tbl_post` (
    `idx`           bigint(20)    NOT NULL AUTO_INCREMENT,
    `title`         varchar(100)  NOT NULL,
    `content`       varchar(3000) NOT NULL,
    `writer`        varchar(20)   NOT NULL,
    `view_cnt`      int(11)       NOT NULL DEFAULT 1,
    `delete_yn`     tinyint(1)    NOT NULL DEFAULT 0,
    `created_date`  DATETIME      NOT NULL DEFAULT current_timestamp(),
    `modified_date` datetime               DEFAULT NULL,
    PRIMARY KEY (`idx`)
);
```

build.gradle
```groovy
dependencies {
    // implementation 'com.h2database:h2'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
}
```

application.yml
```yaml
spring:
  datasource:
    url: jdbc:mariadb://${MYSQL_HOST:localhost}:3306/myboard
    username: myuser
    password: mypass
    driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
    open-in-view: false
```

### Oracle (v18+)

```sql
// sqlplus
// 사용자명 입력: system
// 비밀번호 입력:
// conn/as sysdba
create user c##myuser identified by mypass;
grant connect, resource, dba to c##myuser;
exit
```

```sql
// sqlplus
// 사용자명 입력: c##myuser
// 비밀번호 입력:
CREATE TABLE tbl_post (
                          idx             NUMBER(20)                          NOT NULL,
                          title           VARCHAR2(100)                       NOT NULL,
                          content         VARCHAR2(3000)                      NOT NULL,
                          writer          VARCHAR2(20)                        NOT NULL,
                          view_cnt        NUMBER(4)       DEFAULT 1           NOT NULL,
                          delete_yn       NUMBER(1)       DEFAULT 0           NOT NULL,
                          created_date    DATE            DEFAULT SYSDATE     NOT NULL,
                          modified_date   DATE            DEFAULT             NULL
);

ALTER TABLE tbl_post ADD CONSTRAINT idx_pk PRIMARY KEY (idx);

CREATE SEQUENCE idx_seq START WITH 1;

COMMIT;

// SID 확인
SELECT instance FROM v$thread;
```

build.gradle
```groovy
dependencies {
    // implementation 'com.h2database:h2'
    implementation 'org.hibernate:hibernate-core:6.4.1.Final'
    implementation 'com.oracle.database.jdbc:ojdbc10:19.22.0.0'
}
```

application.yml
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@${ORACLE_HOST:localhost}:1521:xe
    username: c##myuser
    password: mypass
    driver-class-name: oracle.jdbc.OracleDriver
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
    open-in-view: false
```

Post.java
```java
@Entity
public class Post {
    @Id
    @GeneratedValue(generator="idx_seq")
    @SequenceGenerator(name="idx_seq",sequenceName="idx_seq", allocationSize=1)
    private Long idx;
}
```