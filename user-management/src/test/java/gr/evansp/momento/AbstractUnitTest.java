package gr.evansp.momento;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.core.io.ClassPathResource;

public class AbstractUnitTest {

  protected static final String VALID_TOKEN = loadValidToken();

  private static String loadValidToken() {
    ClassPathResource resource = new ClassPathResource("util/jwt-token.txt");
    try {
      return resource.getContentAsString(Charset.defaultCharset());
    } catch (IOException e) {
      return null;
    }
  }
}
