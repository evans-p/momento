package gr.evansp.momento.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.dto.AssetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

/**
 * Integration tests for {@link CDNController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TestCDNController extends AbstractIntegrationTest {

  MockMultipartFile file = new MockMultipartFile("file", "Yosuke.png", "image/png", PNG_IMAGE);

  /**
   * {@link MockMvc}.
   */
  @Autowired MockMvc mockMvc;

  @Test
  public void testFaultyUrl_english() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/faulty").file(file))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content().bytes("{\"message\":\"No content found at the specified URL.\"}".getBytes()));
  }

  @Test
  public void testFaultyUrl_greek() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/faulty")
                .file(file)
                .header("Accept-Language", "el-GR"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .bytes(
                    "{\"message\":\"Δε βρέθηκε περιεχόμενο στο συγκεκριμένο URL.\"}".getBytes()));
  }

  /**
   * Test for {@link CDNController#upload(MultipartFile)}
   *
   * @throws Exception
   * 		Exception
   */
  @Test
  public void testUpload_validationErrorEnglish() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/cdn/v1/assets/upload")
                .file(new MockMultipartFile("file", "Yosuke.jpg", "image/png", PNG_IMAGE)))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .bytes(
                    "{\"messages\":{\"type.mismatch\":\"Content type and file suffix do not match.\"}}"
                        .getBytes()));
  }

  /**
   * Test for {@link CDNController#upload(MultipartFile)}
   *
   * @throws Exception
   * 		Exception
   */
  @Test
  public void testUpload_validationErrorGeek() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/cdn/v1/assets/upload")
                .file(new MockMultipartFile("file", "Yosuke.jpg", "image/png", PNG_IMAGE))
                .header("Accept-Language", "el-GR"))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .bytes(
                    "{\"messages\":{\"type.mismatch\":\"Διαφορά μεταξύ του τύπου περιεχομένου του αρχείου και της επέκτασης του.\"}}"
                        .getBytes()));
  }

  /**
   * Test for {@link CDNController#upload(MultipartFile)}
   *
   * @throws Exception
   * 		Exception
   */
  @Test
  public void testUpload_ok() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/cdn/v1/assets/upload").file(file))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Test for {@link CDNController#getFile(String)}.
   *
   * @throws Exception
   * 		Exception
   */
  @Test
  public void testGetFile_ok() throws Exception {
    MvcResult result =
        mockMvc
            .perform(MockMvcRequestBuilders.multipart("/cdn/v1/assets/upload").file(file))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    AssetDto asset = mapper.readValue(result.getResponse().getContentAsString(), AssetDto.class);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/cdn/v1/assets/" + asset.fileName()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.IMAGE_PNG));
  }

  /**
   * Test for {@link CDNController#getFile(String)}.
   *
   * @throws Exception
   * 		Exception
   */
  @Test
  public void testGetFile_fileNotFound() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/cdn/v1/assets/3f361ed5-39c9-4e71-a5c3-778faaf1bdf3-ab0b1b8f.png"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
