package gr.evansp.momento.validator;

import static org.junit.jupiter.api.Assertions.*;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidUpdateUserProfileDto;
import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.service.JwtService;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit tests for {@link UserProfileDtoValidator}.
 */
@SpringBootTest
class TestUpdateUserProfileDtoValidator extends AbstractUnitTest {

  @Autowired JwtService service;

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_nullUserProfileDto() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper = new ProfileDtoWrapper(null);

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());

      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.input"),
          violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_emptyUserProfileDto() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper = new ProfileDtoWrapper(new UpdateUserProfileDto(null, null, null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.cannot.be.empty"),
          violationsMap.get("{first.name.cannot.be.empty}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.cannot.be.empty"),
          violationsMap.get("{last.name.cannot.be.empty}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_blankFirstNameLastName() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("", "      ", null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.cannot.be.empty"),
          violationsMap.get("{first.name.cannot.be.empty}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.cannot.be.empty"),
          violationsMap.get("{last.name.cannot.be.empty}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_tooLongFirstNameLastName() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("a".repeat(1000), "b".repeat(1000), null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.too.long"),
          violationsMap.get("{first.name.too.long}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.too.long"),
          violationsMap.get("{last.name.too.long}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_exactly511LengthFirstNameLastName() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("a".repeat(511), "b".repeat(511), null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_invalidCharactersFirstNameLastName1() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  "John <script>alert('XSS')</script> Doe",
                  "John <script>alert('XSS')</script> Doe",
                  null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.contains.invalid.characters"),
          violationsMap.get("{first.name.contains.invalid.characters}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.contains.invalid.characters"),
          violationsMap.get("{last.name.contains.invalid.characters}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_invalidCharactersFirstNameLastName2() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  "John <a href='https://example.com'>Doe</a>",
                  "John <a href='https://example.com'>Doe</a>",
                  null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.contains.invalid.characters"),
          violationsMap.get("{first.name.contains.invalid.characters}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.contains.invalid.characters"),
          violationsMap.get("{last.name.contains.invalid.characters}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_invalidCharactersFirstNameLastName3() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  "John <table><tr><td>Doe</td></tr></table>",
                  "John <table><tr><td>Doe</td></tr></table>",
                  null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.contains.invalid.characters"),
          violationsMap.get("{first.name.contains.invalid.characters}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.contains.invalid.characters"),
          violationsMap.get("{last.name.contains.invalid.characters}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_invalidCharactersFirstNameLastName4() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  "John <style>body{color:red;}</style> Doe",
                  "John <style>body{color:red;}</style> Doe",
                  null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.contains.invalid.characters"),
          violationsMap.get("{first.name.contains.invalid.characters}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.contains.invalid.characters"),
          violationsMap.get("{last.name.contains.invalid.characters}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_invalidCharactersFirstNameLastName5() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto("John <b>Doe</b>", "John <b>Doe</b>", null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());

      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("first.name.contains.invalid.characters"),
          violationsMap.get("{first.name.contains.invalid.characters}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("last.name.contains.invalid.characters"),
          violationsMap.get("{last.name.contains.invalid.characters}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_okCharactersFirstNameLastName() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("John Doe", "John Doe", null));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_emptyProfileUrl() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("John Doe", "John Doe", ""));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());
      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.url"), violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_blankProfileUrl() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("John Doe", "John Doe", "    "));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());
      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.url"), violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_tooLongProfileUrl() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(new UpdateUserProfileDto("John Doe", "John Doe", "a".repeat(2048)));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(2, violations.size());
      Map<String, String> violationsMap =
          violations.stream()
              .collect(
                  Collectors.toMap(
                      ConstraintViolation::getMessageTemplate, ConstraintViolation::getMessage));

      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.url"), violationsMap.get("{invalid.url}"));
      assertEquals(
          VALIDATION_MESSAGES.getString("url.too.long"), violationsMap.get("{url.too.long}"));
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_okGoogle() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      JwtTokenInfo tokenInfo = service.extractUserProfileInfo(VALID_GOOGLE_TOKEN);
      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  tokenInfo.firstName(), tokenInfo.lastName(), tokenInfo.profilePictureUrl()));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_okKeycloak() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      JwtTokenInfo tokenInfo = service.extractUserProfileInfo(VALID_KEYCLOAK_TOKEN);
      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  tokenInfo.firstName(), tokenInfo.lastName(), tokenInfo.profilePictureUrl()));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_okFacebook() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      JwtTokenInfo tokenInfo = service.extractUserProfileInfo(VALID_FACEBOOK_TOKEN);
      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  tokenInfo.firstName(), tokenInfo.lastName(), tokenInfo.profilePictureUrl()));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  /**
   * Test for {@link UserProfileDtoValidator#isValid(UpdateUserProfileDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_okLinkedIn() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      JwtTokenInfo tokenInfo = service.extractUserProfileInfo(VALID_LINKED_IN_TOKEN);
      ProfileDtoWrapper wrapper =
          new ProfileDtoWrapper(
              new UpdateUserProfileDto(
                  tokenInfo.firstName(), tokenInfo.lastName(), tokenInfo.profilePictureUrl()));

      Set<ConstraintViolation<ProfileDtoWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  @Setter
  @Getter
  private static class ProfileDtoWrapper {

    @ValidUpdateUserProfileDto private UpdateUserProfileDto profileDto;

    public ProfileDtoWrapper(UpdateUserProfileDto profileDto) {
      this.profileDto = profileDto;
    }
  }
}
