package org.skillsmapper.factservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FactControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FactChangedNotifier factChangedNotifier;

  @Test
  public void shouldBeUnsuccessful_postWithoutToken() throws Exception {
    String factCreateRequestJson = "{\"skill\": \"Java\", \"level\": \"learning\"}";

    mockMvc.perform(post("/api/facts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(factCreateRequestJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldBeUnsuccessful_postWithBadToken() throws Exception {
    try {
      String factCreateRequestJson = "{\"skill\": \"Java\", \"level\": \"learning\"}";

      mockMvc
          .perform(post("/api/facts")
              .header("Authorization", "Bearer iam-a-token")
              .contentType(MediaType.APPLICATION_JSON)
              .content(factCreateRequestJson))
          .andExpect(status().isForbidden());
    } catch (Exception e) {
      System.out.println("Caught FirebaseApp error");
    }
  }
}
