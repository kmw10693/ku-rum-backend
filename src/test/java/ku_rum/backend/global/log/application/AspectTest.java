package ku_rum.backend.global.log.application;

import ku_rum.backend.global.log.domain.ApiLog;
import ku_rum.backend.global.log.domain.repository.ApiLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AspectTest {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void reset() {
        apiLogRepository.deleteAllInBatch();
    }

    @Test
    @WithMockUser
    public void testGetLog() throws Exception {
        // given
        // when
        mockMvc.perform(get("/log-test/200ok?name=minu&message=hello")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        ApiLog log = apiLogRepository.findAll().get(0);
        assertThat(log.getRequest()).isEqualTo("name=minu, message=hello");
        assertThat(log.getResponseStatus()).isEqualTo(200);
        assertThat(log.getRequestUrl()).isEqualTo("http://localhost/log-test/200ok");
        assertThat(log.getRequestMethod()).isEqualTo("GET");
        assertThat(log.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(log.getResponse()).isEqualTo("{name=minu, message=hello}");
    }

    @Test
    @WithMockUser
    public void testBadRequestLog() throws Exception {
        // given
        // when
        mockMvc.perform(get("/log-test/400bad?name=minu&message=hi")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        ApiLog log = apiLogRepository.findAll().get(0);
        assertThat(log.getRequest()).isEqualTo("name=minu, message=hi");
        assertThat(log.getResponseStatus()).isEqualTo(400);
        assertThat(log.getRequestUrl()).isEqualTo("http://localhost/log-test/400bad");
        assertThat(log.getRequestMethod()).isEqualTo("GET");
        assertThat(log.getClientIp()).isEqualTo("127.0.0.1");
    }

    @Test
    @WithMockUser
    public void testInternalServerErrorLog() throws Exception {
        // given
        // when
        mockMvc.perform(get("/log-test/500error?name=minu&message=yes")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        ApiLog log = apiLogRepository.findAll().get(0);
        assertThat(log.getRequest()).isEqualTo("name=minu, message=yes");
        assertThat(log.getResponseStatus()).isEqualTo(500);
        assertThat(log.getRequestUrl()).isEqualTo("http://localhost/log-test/500error");
        assertThat(log.getRequestMethod()).isEqualTo("GET");
        assertThat(log.getClientIp()).isEqualTo("127.0.0.1");
    }

    @Test
    @WithMockUser
    public void testPostLog() throws Exception {
        // given
        // when
        mockMvc.perform(post("/log-test/post?message=hello")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"minu\"}"));

        // then
        ApiLog log = apiLogRepository.findAll().get(0);
        assertThat(log.getRequest()).isEqualTo("message=hello, body={name=minu}");
        assertThat(log.getResponseStatus()).isEqualTo(200);
        assertThat(log.getRequestUrl()).isEqualTo("http://localhost/log-test/post");
        assertThat(log.getRequestMethod()).isEqualTo("POST");
        assertThat(log.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(log.getResponse()).isEqualTo("{name=minu, message=hello}");
    }
}
