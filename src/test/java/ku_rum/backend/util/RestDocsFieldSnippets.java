package ku_rum.backend.util;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RestDocsFieldSnippets {

    public static List<FieldDescriptor> COMMON_RESPONSE_FIELDS = List.of(
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (OK)"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
    );

    public static List<FieldDescriptor> COMMON_RESPONSE_FIELDS_WITH_DATA = List.of(
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (OK)"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
            fieldWithPath("data").type(JsonFieldType.VARIES).description("응답 데이터")
    );

    /**
     * 공통 응답 필드 + data 내부 필드를 합쳐 리턴합니다.
     * @param dataFields data 내부 필드들 (예: data[].id, data[].name)
     */
    public static List<FieldDescriptor> withDataFields(List<FieldDescriptor> dataFields) {
        return Stream.concat(COMMON_RESPONSE_FIELDS_WITH_DATA.stream(), dataFields.stream())
                .collect(Collectors.toList());
    }
}
