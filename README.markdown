## vraptor-test

A VRaptor plugin that allows you to quickly create integration or system tests.

# installing

You can install it by doing:

	vraptor install vraptor-test
	
Or add to your pom:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-test</artifactId>
			<version>1.0.0</version>
			<scope>test</scope>
		</dependency>
		
Or simply copy all jars to your classpath.
		
# Creating a system wide test

Create a test case such as the following, that starts the server just once:

	public interface Server {
		private static final RealVRaptor VRAPTOR = new RealVRaptor(new File("src/main/webapp")); 
	}
	
	public class IndexTest implements Server {
	
		@Test
		public void shouldHaveTheProductsListAvailable() {
			assertEquals(200, VRAPTOR.at("/products").get().getCode());
		}
	}
	
The VRAPTOR.at method returns a Restfulie client instance, so you can use it as a client
HTTP api to access your vraptor instance. Note that system wide tests will render your view
files and fully process the web.xml configuration.

# Running the tests
		
To run your tests, use the build tool you have chosen with vraptor-scaffold or simply use JUnit in your IDE.
