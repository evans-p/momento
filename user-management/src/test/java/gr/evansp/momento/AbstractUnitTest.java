package gr.evansp.momento;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.core.io.ClassPathResource;

public class AbstractUnitTest {

  protected static final String VALID_GOOGLE_TOKEN = loadValidToken("util/google-jwt-token.txt");

  protected static final String VALID_KEYCLOAK_TOKEN =
      loadValidToken("util/keycloak-jwt-token.txt");

  protected static final String VALID_FACEBOOK_TOKEN =
      loadValidToken("util/facebook-jwt-token.txt");

  protected static final String VALID_LINKED_IN_TOKEN =
      loadValidToken("util/linkedin-jwt-token.txt");

  /**
   * Validation Messages.
   */
  protected static final ResourceBundle VALIDATION_MESSAGES =
      ResourceBundle.getBundle("ValidationMessages", Locale.getDefault());

  private static String loadValidToken(String path) {
    ClassPathResource resource = new ClassPathResource(path);
    try {
      return resource.getContentAsString(Charset.defaultCharset());
    } catch (IOException e) {
      return null;
    }
  }
}
