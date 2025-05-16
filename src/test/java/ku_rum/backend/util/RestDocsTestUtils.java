package ku_rum.backend.util;


import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RestDocsTestUtils {

    public static ResultMatcher[] expectCommonSuccess() {
        return new ResultMatcher[] {
                (ResultMatcher) jsonPath("$.code").value("200"),
                (ResultMatcher) jsonPath("$.status").value("OK"),
                (ResultMatcher) jsonPath("$.message").value("OK")
        };
    }
}
