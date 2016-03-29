## vraptor-test

A VRaptor plugin that allows you to quickly create integration or system tests. 

This plugin currently works only with VRaptor 4.x versions. For support to
VRaptor 3, check vraptor-test 1.x versions (being developed in vraptor3 branch:
https://github.com/caelum/vraptor-test/tree/vraptor3).

**Warning: this plugin works only with Java <= 7**

# installing

Add to your pom:


```xml
<dependency>
	<groupId>br.com.caelum.vraptor</groupId>
	<artifactId>vraptor-test</artifactId>
	<version>4.0.0-SNAPSHOT</version>
	<scope>test</scope>
</dependency>
```
		
Or simply copy all jars to your classpath.
		
# creating a system wide test

To create a test with vraptor-test you only need to create a junit test class
that extends our base class, `VRaptorIntegration`: 
```java
public class IntegrationTest extends VRaptorIntegration {

	@Test
	public void shouldIncludeAtrributeInResult() throws Exception {
		VRaptorTestResult result = navigate().get("/test/test").execute();
	}
}
```

This test case will execute a request to `/test/test` url in your application
and return a `VRaptorTestResult` object. Note that this test will really execute the code of your
controller.

To add headers, sometimes for authentication, for example:
```java
@Test
public void shouldPassObjectHeaders() {
	String name = "Authorization";
	String value = "Bearer 123";
	VRaptorTestResult result = navigate().post("/test/test11", new Parameters().addHeader(name, value)).execute();
}
```
This is the result:
```http
POST /test/test11 HTTP/1.1
Host: http://myhost/test/test11
Accept: application/json, text/javascript, */*; q=0.01
Authorization: Bearer 123
Referer: http://myhost/test/test11
Accept-Encoding: gzip, deflate, sdch
```

The `VRaptorTestResult` object allow you to verify things that
happened during the request:
```java
@Test
public void shouldIncludeAtrributeInResult() throws Exception {
	VRaptorTestResult result = navigate().get("/test/test").execute();
	result.wasStatus(200).isValid();
	assertEquals("vraptor", result.getObject("name"));
}
```

In the second line of the test, we verify that the http status code of the
request was 200 and that there was no validation errors (included in vraptor's
`Validator` class). If any of this two conditions had failed, the test would
fail imediatly (you don't need to write any assert).

In the third line, we verify if a attribute was included in the result (in a
`result.include` in the controller being tested, for example). The
`result.getObject` method returns the object included or null if it wasn't
included.

You can also add parameters to be sent in the request:
```java
@Test
public void shouldIncludeAtrributeInResult() throws Exception {
	VRaptorTestResult result = navigate().post("/tasks", 
	    Parameters.initWith("task.description", "Task description").add("task.name", "Task name")
	    .execute();
	Task task = result.getObject("task"));
	assertEquals("Task description", task.getDescription());
	assertEquals("Task name", task.getName());
}
```

vraptor-test also compile and execute jsp files. You can get the final output
written to the response from the result object:
```java
@Test
public void shouldCompileAndExecuteAJsp() {
	VRaptorTestResult result = navigate().post("/task/1").execute();
	String html = result.getResponseBody();
	assertEquals("<h1>Task name</h1>", html);
}
```

For other examples, check the vraptor-test tests :-) 

https://github.com/caelum/vraptor-test/blob/master/src/test/java/br/com/caelum/vraptor/test/VRaptorNavigationSimpleScenariosTest.java
