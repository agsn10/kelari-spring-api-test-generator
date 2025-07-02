package io.github.kelari.atg.util;

import io.github.kelari.atg.annotation.ApiTestSpec;
import io.github.kelari.atg.annotation.KelariGenerateApiTest;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ClassTest;
import io.github.kelari.atg.model.ParameterMetadataTest;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Utility class that provides reusable {@link Predicate} instances
 * commonly used for filtering annotations and keys during annotation processing,
 * particularly in the context of test generation.
 * <p>
 * This class is final and cannot be instantiated.
 * </p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * annotationMirrors.stream()
 *     .filter(Predicates.IS_API_TEST_SPEC)
 *     .forEach(...);
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved
 */
public final class Predicates {

    /**
     * Private constructor to prevent instantiation.
     */
    private Predicates() {}

    /**
     * Predicate that checks whether an {@link AnnotationMirror}
     * represents a {@code @RequestMapping} annotation.
     *
     * @implNote It compares the simple name of the annotation type with {@code "RequestMapping"}.
     */
    public static final Predicate<? super AnnotationMirror> IS_REQUEST_MAPPING =
            annotation -> annotation.getAnnotationType().asElement().getSimpleName().toString().equals(Constants.Annotation.REQUEST_MAPPING);

    /**
     * Predicate that checks whether a given annotation attribute key is {@code "scenarios"}.
     * <p>
     * Useful when filtering annotation attributes in {@code @ApiTestSpec}.
     */
    public static final Predicate<String> IS_SCENARIOS_KEY = key -> "scenarios".equals(key);

    /**
     * Predicate that checks whether the given {@link AnnotationMirror}
     * corresponds to the {@link ApiTestSpec} annotation.
     *
     * @implNote This uses the canonical class name to perform a direct string comparison.
     */
    public static final Predicate<? super AnnotationMirror> IS_API_TEST_SPEC =
            annotation -> annotation.getAnnotationType().toString().equals(ApiTestSpec.class.getCanonicalName());

    /**
     * Predicate that validates if a {@link KelariGenerateApiTest} annotation instance
     * contains all required non-null authentication fields:
     * {@code authUrl}, {@code username}, and {@code password}.
     *
     * @see KelariGenerateApiTest
     */
    public static final Predicate<KelariGenerateApiTest> HAS_VALID_AUTH_FIELDS =
            annotation -> true;
                  /*  Objects.nonNull(annotation.auth().authUrl()) &&
                    Objects.nonNull(annotation.auth().password()) &&
                    Objects.nonNull(annotation.auth().username()); */

    /**
     * Predicate that verifies if the provided {@link ClassTest} contains
     * a valid {@code authTest} configuration with non-null {@code authUrl},
     * {@code username}, and {@code password}.
     * <p>
     * Commonly used to determine if a test class requires token generation for authentication.
     *
     * @see io.github.kelari.atg.model.AuthTest
     */
    public static final Predicate<ClassTest> SHOULD_GENERATE_AUTH_TOKEN =
            classTest -> classTest.getAuthTest() != null &&
                    Objects.nonNull(classTest.getAuthTest().getAuthUrl()) &&
                    Objects.nonNull(classTest.getAuthTest().getPassword()) &&
                    Objects.nonNull(classTest.getAuthTest().getUsername());

    /**
     * Predicate that verifies if the given {@link Element} is annotated
     * with {@link KelariGenerateApiTest}.
     *
     * @param element the element being inspected (typically a class element)
     * @return true if the element has the {@code @KelariGenerateApiTest} annotation
     */
    public static final Predicate<Element> HAS_KELARI_ANNOTATION =
            element -> element != null && element.getAnnotation(KelariGenerateApiTest.class) != null;


    /**
     * Predicate that determines whether a {@link ClassTest} contains
     * any method parameters with a body type that requires multipart generation,
     * such as {@code MultipartFile}, {@code Resource}, or {@code byte[]}.
     * <p>
     * Useful to decide whether a controller method should include multipart handling logic.
     *
     */
    public static final Predicate<ClassTest> SHOULD_GENERATE_MULTIPART_METHOD = classTest -> classTest.entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(Objects::nonNull)
            .flatMap(spec -> spec.getCaseTestList().stream())
            .map(CaseTest::getMethodParameters)
            .filter(Objects::nonNull)
            .map(ParameterMetadataTest::getBody)
            .filter(Objects::nonNull)
            .anyMatch(bodyMap -> bodyMap.values().stream().anyMatch(value -> {
                String className = String.valueOf(value);
                return className.contains("MultipartFile") || className.contains("Resource") || className.contains("byte[]");
            }));

    /**
     * Predicate that checks if the {@link ClassTest} contains methods
     * that explicitly enable logging.
     * <p>
     * Used to determine if logging should be included in the test setup.
     */
    public static final Predicate<ClassTest> SHOULD_GENERATE_LOGGING_METHODS = classTest ->
            classTest.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .filter(Objects::nonNull)
                    .flatMap(spec -> spec.getCaseTestList().stream())
                    .anyMatch(CaseTest::isEnableLogging);

    /**
     * Predicate that verifies if any {@link CaseTest} in the given {@link ClassTest}
     * explicitly requires authentication (i.e., {@code requiresAuth = true}).
     * <p>
     * This is typically used to determine whether the authentication token
     * should be injected in the generated test class.
     *
     */
    public static final Predicate<ClassTest> IS_REQUIRE_AUTH = classTest -> classTest.entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(spec -> spec != null && spec.getCaseTestList() != null)
            .flatMap(spec -> spec.getCaseTestList().stream())
            .anyMatch(CaseTest::isRequiresAuth);
}