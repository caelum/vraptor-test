## vraptor-test

Um plugin do VRaptor que permite a criação rápida e fácil de testes.

# instalação

Para instalar use:

	vraptor install vraptor-test
	
Ou adicione no seu pom:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-test</artifactId>
			<version>1.0.0</version>
			<scope>test</scope>
		</dependency>
		
Ou ainda simplesmente copie os jars necessários para seu path.
		
# Criando o teste

Crie um teste que inicia o servidor uma única vez:

	public interface Server {
		private static final RealVRaptor VRAPTOR = new RealVRaptor(new File("src/main/webapp")); 
	}
	
	public class IndexTest implements Server {
	
		@Test
		public void shouldHaveTheProductsListAvailable() {
			assertEquals(200, VRAPTOR.at("/products").get().getCode());
		}
	}
	
O método VRAPTOR.at retorna uma instância do Restfulie Client, então você pode utilizá-lo
para acessar o body, headers etc, como qualquer cliente HTTP.
Note que testes de sistema acessam o sistema por completo e irão renderizar sua view (o que
pode ser ou não de seu interesse), além de carregar o web.xml por completo.

# Rodando os testes

Para rodar os testes basta utilizar o JUnit em sua IDE ou ainda a ferramenta escolhida durante
a criação de seu projeto com o scaffold.