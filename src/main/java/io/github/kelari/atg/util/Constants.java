package io.github.kelari.atg.util;

import com.squareup.javapoet.ClassName;

/**
 * Utility class containing constant values used throughout the application.
 * <p>
 * This class is not meant to be instantiated.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {}

    public static final String PLUGIN_NAME = "KelariApiTestGeneratorPlugin";
    public static final String ATTRIBUTE_CLASS_TEST_BEARER_TOKEN = "bearerToken";
    public static final String WEB_TEST_CLIENT_CLASS_INSTANCE_NAME = "webTestClient";
    public static final String SPRING_BOOT_TEST_CONTEXT_NAME = "webEnvironment";
    public static final String SPRING_BOOT_TEST_CONTEXT_FORMAT = "$T.RANDOM_PORT";
    /**
     * Contains constant values for annotation names related to HTTP request mappings.
     */
    public final class Annotation {
        public static final String REQUEST_MAPPING = "RequestMapping";
        public static final String GET_MAPPING = "GetMapping";
        public static final String POST_MAPPING = "PostMapping";
        public static final String PUT_MAPPING = "PutMapping";
        public static final String DELETE_MAPPING = "DeleteMapping";
        public static final String PATCH_MAPPING = "PatchMapping";
        public static final String HEAD_MAPPING = "HeadMapping";
    }
    public final class ParameterAnnotation {
        public static final String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";
        public static final String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
        public static final String REQUEST_HEADER = "org.springframework.web.bind.annotation.RequestHeader";
        public static final String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
        public static final String COOKIE_VALUE = "org.springframework.web.bind.annotation.CookieValue";
        public static final String MATRIX_VARIABLE = "org.springframework.web.bind.annotation.MatrixVariable";
        public static final String MODEL_ATTRIBUTE = "org.springframework.web.bind.annotation.ModelAttribute";
        public static final String REQUEST_PART = "org.springframework.web.bind.annotation.RequestPart";
    }
    /**
     * Contains constant values for HTTP method names in lowercase.
     */
    public final class HttpMethod {
        /** Represents the HTTP POST method. */
        public static final String POST = "post";
        /** Represents the HTTP GET method. */
        public static final String GET = "get";
        /** Represents the HTTP PUT method. */
        public static final String PUT = "put";
        /** Represents the HTTP DELETE method. */
        public static final String DELETE = "delete";
        /** Represents the HTTP PATCH method. */
        public static final String PATCH = "patch";
        public static final String HEAD = "head";
    }
    public final class StatusMethods{
        public static final String IS_OK = "isOk";
        public static final String IS_CREATED = "isCreated";
        public static final String IS_NO_CONTENT = "isNoContent";
        public static final String IS_BAD_REQUEST = "isBadRequest";
        public static final String IS_UNAUTHORIZED = "isUnauthorized";
        public static final String IS_FORBIDDEN = "isForbidden";
        public static final String IS_NOT_FOUND = "isNotFound";
        public static final String IS_5xx_SERVER_ERROR = "is5xxServerError";
        public static final String DEFAULT = "isEqualTo";
    }
    /**
     * Contains constant values for import statements commonly used in Java tests.
     */
    public static final class Imports {
        // JUnit
        public static final ClassName TEST = ClassName.get("org.junit.jupiter.api", "Test");
        public static final ClassName ORDER = ClassName.get("org.junit.jupiter.api", "Order");
        public static final ClassName TIMEOUT = ClassName.get("org.junit.jupiter.api", "Timeout");
        public static final ClassName DISPLAY_NAME = ClassName.get("org.junit.jupiter.api", "DisplayName");
        public static final ClassName BEFORE_EACH = ClassName.get("org.junit.jupiter.api", "BeforeEach");
        // Spring WebFlux
        public static final ClassName BODY_INSERTERS = ClassName.get("org.springframework.web.reactive.function", "BodyInserters");
        // Spring Web Test Client
        public static final ClassName WEB_TEST_CLIENT = ClassName.get("org.springframework.test.web.reactive.server", "WebTestClient");
        // Spring Autowired
        public static final ClassName AUTOWIRED = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");
        // Spring Boot Test WebClient
        public static final ClassName AUTO_CONFIGURE_WEB_TEST_CLIENT = ClassName.get("org.springframework.boot.test.autoconfigure.web.reactive", "AutoConfigureWebTestClient");
        // Spring Boot Test Context
        public static final ClassName SPRING_BOOT_TEST = ClassName.get("org.springframework.boot.test.context", "SpringBootTest");
        public static final ClassName WEB_ENVIRONMENT = ClassName.get("org.springframework.boot.test.context.SpringBootTest", "WebEnvironment");
        // Spring Framework Classes
        public static final ClassName MULTI_VALUE_MAP = ClassName.get("org.springframework.util", "MultiValueMap");
        public static final ClassName HTTP_ENTITY = ClassName.get("org.springframework.http", "HttpEntity");
        public static final ClassName LINKED_MULTI_VALUE_MAP = ClassName.get("org.springframework.util", "LinkedMultiValueMap");
        public static final ClassName HTTP_HEADERS = ClassName.get("org.springframework.http", "HttpHeaders");
        public static final ClassName RESOURCE_CLASS = ClassName.get("org.springframework.core.io", "Resource");
        public static final ClassName MEDIA_TYPE = ClassName.get("org.springframework.http", "MediaType");
        // Reactor Classes
        public static final ClassName MONO = ClassName.get("reactor.core.publisher", "Mono");
        // Java Util Classes
        public static final ClassName MAP = ClassName.get("java.util", "Map");
    }
}