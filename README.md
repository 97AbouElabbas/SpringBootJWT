# Restful API with JWT token based Authentication
JSON Web Token (JWT) is an Internet standard for creating data with optional signature and/or optional encryption whose payload holds JSON that asserts some number of claims.
![JWT](https://drive.google.com/uc?export=view&id=1V-gPjbrpXrL1yI03DgOxzT38Dq40MUpF)
## application.properties
````
# Oracle settings
# JDBC URL of the database.
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:ORCL
# Login username of the database.
spring.datasource.username=HR
# Login password of the database.
spring.datasource.password=HR

# Hibernate additional native properties to set on the JPA provider.
# To support multiple sessions
spring.jpa.properties.hibernate.current_session_context_class=thread
````
## Database table
````
CREATE TABLE HR.USERS
(
  ID        NUMBER(4),
  USERNAME  VARCHAR2(255),
  PASSWORD  VARCHAR2(255)
)
````
## Maven Dependences
````
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</dependency>
<dependency>
	<groupId>com.oracle.database.jdbc</groupId>
	<artifactId>ojdbc8</artifactId>
	<scope>runtime</scope>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-test</artifactId>
	<scope>test</scope>
</dependency>
````
## Testing
Testing can be done by send POST request containing user JSON object in request body
````
http://localhost:8080/authentication
````
````
{
    "username":"admin",
    "password":"admin"
}
````
It returns JWT token 
````
{
    "jwt_TOKEN": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYxMTA4Mzk2MywiaWF0IjoxNjExMDQ3OTYzfQ.WTOMvfL4QxIFK0anxXvZkVjzFrYH_uHCTQCMZik8dac"
}
````
Then you can access the rest of APIs by adding the JWT token on http header in Authorization variable.
