package io.github.kelari.atg.process.helper;

import io.github.kelari.atg.annotation.ApiTestSpec;
import io.github.kelari.atg.annotation.KelariGenerateApiTest;
import io.github.kelari.atg.annotation.MatcherType;
import io.github.kelari.atg.model.*;
import io.github.kelari.atg.process.AnnotationMetadataExtractor;
import io.github.kelari.atg.util.CompilerLogger;
import io.github.kelari.atg.util.ConditionPipeline;
import io.github.kelari.atg.util.Constants;
import io.github.kelari.atg.util.Predicates;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.util.*;

/**
 * Helper class that provides utility methods for analyzing Java syntax trees
 * and extracting metadata from annotated elements, especially for classes
 * annotated with {@link KelariGenerateApiTest}.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public final class KelariTreeScannerHelper {

    private CompilerLogger compilerLogger;

    private static final KelariTreeScannerHelper INSTANCE = new KelariTreeScannerHelper();

    private KelariTreeScannerHelper() {}

    /**
     * Returns the singleton instance of {@code KelariTreeScannerHelper}.
     *
     * @return singleton instance
     */
    public static KelariTreeScannerHelper getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the {@link CompilerLogger} instance to be used for logging.
     *
     * @param compilerLogger the logger to use
     */
    public void setCompilerLogger(CompilerLogger compilerLogger) {
        this.compilerLogger = compilerLogger;
    }

    /**
     * Creates a {@link ClassTest} object based on the provided element and annotation metadata.
     *
     * @param className   the name of the class
     * @param packageName the package name of the class
     * @param element     the annotated element representing the class
     * @return a fully populated {@link ClassTest} instance
     */
    public ClassTest createClassTest(String className, String packageName, Element element) {
        ClassTest classTest = new ClassTest();
        classTest.setName(className.concat("GeneratedTest"));
        classTest.setPackageName(packageName);

        KelariGenerateApiTest annotation = element.getAnnotation(KelariGenerateApiTest.class);

        element.getAnnotationMirrors().stream().forEach(anno->{
            if (Predicates.IS_REQUEST_MAPPING.test(anno))
                classTest.setPathBase(AnnotationMetadataExtractor.extractUri(anno));
        });

        if (Predicates.HAS_VALID_AUTH_FIELDS.test(annotation)) {
            classTest.setAuthTest(new AuthTest()
                    .authUrl(annotation.authUrl())
                    .username(annotation.username())
                    .password(annotation.password())
                    .parameterTokenName(annotation.parameterTokenName()));
        }

        return classTest;
    }

    /**
     * Processes the parameters of a method element and extracts metadata
     * about each parameter, grouping them by their annotation type.
     *
     * @param methodElement the element representing the method
     * @return metadata for method parameters as {@link ParameterMetadataTest}
     */
    public ParameterMetadataTest processMethodParameters(Element methodElement) {
        ParameterMetadataTest metadata = new ParameterMetadataTest();
        if (methodElement instanceof ExecutableElement) {
            List<? extends VariableElement> params = ((ExecutableElement) methodElement).getParameters();
            for (VariableElement parameter : params) {
                String paramName = parameter.getSimpleName().toString();
                String paramType = parameter.asType().toString();
                boolean hasRelevantAnnotation = false;

                for (AnnotationMirror annotation : parameter.getAnnotationMirrors()) {
                    String annotationType = annotation.getAnnotationType().toString();
                    hasRelevantAnnotation = true;

                    new ConditionPipeline<String>()
                            .when(Constants.ParameterAnnotation.PATH_VARIABLE::equals, ann -> metadata.getPathParams().put(paramName, paramType))
                            .when(Constants.ParameterAnnotation.REQUEST_PARAM::equals, ann -> metadata.getQueryParams().put(paramName, paramType))
                            .when(Constants.ParameterAnnotation.REQUEST_HEADER::equals, ann -> {
                                String headerName = paramName;
                                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                                    String attr = entry.getKey().getSimpleName().toString();
                                    if ("value".equals(attr) || "name".equals(attr)) {
                                        headerName = entry.getValue().getValue().toString();
                                        break;
                                    }
                                }
                                metadata.getHeaderParams().put(headerName, paramType);
                            })
                            .when(Constants.ParameterAnnotation.REQUEST_BODY::equals, ann -> metadata.getBody().put(paramName, paramType))
                            .when(Constants.ParameterAnnotation.COOKIE_VALUE::equals, ann -> metadata.getCookieParams().put(paramName, paramType))
                            .when(Constants.ParameterAnnotation.MATRIX_VARIABLE::equals, ann -> {
                                MatrixParamMetadata matrixParam = new MatrixParamMetadata(paramName, paramType);
                                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                                    String attributeName = entry.getKey().getSimpleName().toString();
                                    Object value = entry.getValue().getValue();
                                    if ("name".equals(attributeName))
                                        matrixParam.setName(value.toString());
                                    else if ("pathVar".equals(attributeName))
                                        matrixParam.setPathVar(value.toString());
                                }
                                metadata.getMatrixParams()
                                        .computeIfAbsent(matrixParam.getPathVar(), k -> new HashMap<>())
                                        .put(matrixParam.getName(), matrixParam.getType());
                            })
                            .when(Constants.ParameterAnnotation.MODEL_ATTRIBUTE::equals, ann -> metadata.getBody().put(paramName, paramType))
                            .when(Constants.ParameterAnnotation.REQUEST_PART::equals, ann -> {
                                metadata.getBody().put(paramName, paramType);
                                metadata.setMultipart(true);
                            })
                            .execute(annotationType);
                }

                if (!hasRelevantAnnotation) {
                    metadata.getBody().put(paramName, paramType);
                }
            }
        }
        return metadata;
    }

    /**
     * Extracts HTTP method, path and method name from a method element
     * and populates a {@link SpecScenariosTest} object accordingly.
     *
     * @param specScenariosTest the object to populate with extracted data
     * @param methodElement     the element representing the annotated method
     */
    public void createSpecScenariosTest(SpecScenariosTest specScenariosTest, Element methodElement){
        specScenariosTest.setMethodName(methodElement.getSimpleName().toString());
        for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
            String annotationName = annotation.getAnnotationType().toString();
            new ConditionPipeline<String>()
                    .when(a -> a.contains(Constants.Annotation.GET_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.GET);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .when(a -> a.contains(Constants.Annotation.POST_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.POST);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .when(a -> a.contains(Constants.Annotation.PUT_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.PUT);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .when(a -> a.contains(Constants.Annotation.DELETE_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.DELETE);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .when(a -> a.contains(Constants.Annotation.PATCH_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.PATCH);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .when(a -> a.contains(Constants.Annotation.HEAD_MAPPING), a -> {
                        specScenariosTest.setHttpMethod(Constants.HttpMethod.HEAD);
                        specScenariosTest.setPathMethod(AnnotationMetadataExtractor.extractUri(annotation));
                    })
                    .execute(annotationName);
        }
    }

    /**
     * Processes the {@link ApiTestSpec} annotation present on the method element
     * and populates the provided {@link SpecScenariosTest} with all defined test cases.
     *
     * @param spec          the test scenario object to populate
     * @param methodElement the method element annotated with {@link ApiTestSpec}
     */
    @SuppressWarnings("unchecked")
    public void processApiTestSpecAndApiTestCase(SpecScenariosTest spec, Element methodElement) {
        for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().toString().equals(ApiTestSpec.class.getCanonicalName())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                    if (entry.getKey().getSimpleName().contentEquals("scenarios")) {
                        @SuppressWarnings("unchecked")
                        List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) entry.getValue().getValue();
                        for (AnnotationValue av : values) {
                            AnnotationMirror scenarioAnnotation = (AnnotationMirror) av.getValue();
                            CaseTest caseTest = new CaseTest();
                            // Map with values defined in the annotation
                            Map<? extends ExecutableElement, ? extends AnnotationValue> presentValues = scenarioAnnotation.getElementValues();
                            // Iterates over all elements of @ApiTestCase
                            for (Element enclosedElement : scenarioAnnotation.getAnnotationType().asElement().getEnclosedElements()) {
                                if (enclosedElement.getKind() != ElementKind.METHOD)
                                    continue;
                                ExecutableElement method = (ExecutableElement) enclosedElement;
                                String name = method.getSimpleName().toString();
                                AnnotationValue value = presentValues.get(method);
                                if (value == null)
                                    value = method.getDefaultValue(); // value default
                                if (value == null) {
                                    compilerLogger.warning("Null value for field: " + name);
                                    continue;
                                }
                                Object fieldValue = value.getValue();
                                switch (name) {
                                    case Constants.AnnotationFileds.DISPLAY_NAME:
                                        caseTest.displayName((String) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.ORDER:
                                        caseTest.order((Integer) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.TIMEOUT:
                                        caseTest.timeout((Integer) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.EXPECTED_STATUS_CODE:
                                        caseTest.expectedStatusCode((Integer) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.REQUIRES_AUTH:
                                        caseTest.requiresAuth((Boolean) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.REPEAT:
                                        caseTest.repeat((Integer) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.ENABLE_LOGGING:
                                        caseTest.enableLogging((Boolean) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.RESPONSE_TIMEOUT_SECONDS:
                                        caseTest.responseTimeoutSeconds((long) fieldValue);
                                        break;
                                    case Constants.AnnotationFileds.JSON_PATHS:
                                        if (fieldValue instanceof List<?> list) {
                                            List<JsonPath> jsonPaths = new ArrayList<>();
                                            for (Object item : list) {
                                                if (item instanceof AnnotationValue annotationValue) {
                                                    AnnotationMirror mirror = (AnnotationMirror) annotationValue.getValue();

                                                    String pathVal = null;
                                                    MatcherType matcherType = null;
                                                    String valueVal = null;
                                                    String matcherClassVal = null;

                                                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> jsonPathEntry : mirror.getElementValues().entrySet()) {
                                                        String key = jsonPathEntry.getKey().getSimpleName().toString();
                                                        Object val = jsonPathEntry.getValue().getValue();

                                                        switch (key) {
                                                            case "path" -> pathVal = (String) val;
                                                            case "type" -> matcherType = MatcherType.valueOf(val.toString());
                                                            case "value" -> valueVal = (String) val;
                                                            case "matcherClass" ->      {
                                                                if (val instanceof DeclaredType declaredType)
                                                                    matcherClassVal = declaredType.toString();
                                                            }
                                                        }
                                                    }
                                                    caseTest.jsonPaths(new JsonPath(pathVal, matcherType, valueVal, matcherClassVal));
                                                }
                                            }
                                        } else
                                            compilerLogger.warning("Unexpected type for jsonPaths: " + fieldValue.getClass());
                                        break;
                                    case Constants.AnnotationFileds.EXPECTED_HEADERS:
                                        if (fieldValue instanceof List<?> list) {
                                            for (Object item : list) {
                                                if (item instanceof AnnotationValue annotationValue) {
                                                    AnnotationMirror mirror = (AnnotationMirror) annotationValue.getValue();
                                                    String headerName = null;
                                                    List<String> headerValues = new ArrayList<>();
                                                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> headerEntry : mirror.getElementValues().entrySet()) {
                                                        String key = headerEntry.getKey().getSimpleName().toString();
                                                        Object val = headerEntry.getValue().getValue();
                                                        if ("name".equals(key) && val instanceof String nameVal)
                                                            headerName = nameVal;
                                                        else if ("value".equals(key)) {
                                                            if (val instanceof List<?> valuesList) {
                                                                for (Object valElement : valuesList) {
                                                                    if (valElement instanceof AnnotationValue avv)
                                                                        headerValues.add(avv.getValue().toString());
                                                                }
                                                            } else if (val instanceof String singleVal)
                                                                headerValues.add(singleVal);
                                                        }
                                                    }
                                                    if (headerName != null && !headerValues.isEmpty())
                                                        caseTest.expectedHeaders(new Header(headerName, headerValues.toArray(new String[0])));
                                                    else
                                                        compilerLogger.warning("Missing name or value(s) in expectedHeader.");
                                                }
                                            }
                                        } else
                                            compilerLogger.warning("Unexpected type for expectedHeaders: " + fieldValue.getClass());
                                        break;
                                    case Constants.AnnotationFileds.EXPECTED_COOKIES:
                                        if (fieldValue instanceof List<?> list) {
                                            for (Object item : list) {
                                                if (item instanceof AnnotationValue annotationValue) {
                                                    AnnotationMirror mirror = (AnnotationMirror) annotationValue.getValue();
                                                    String cookieName = null;
                                                    String cookieValue = null;
                                                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> cookieEntry : mirror.getElementValues().entrySet()) {
                                                        String key = cookieEntry.getKey().getSimpleName().toString();
                                                        String val = cookieEntry.getValue().getValue().toString();
                                                        if ("name".equals(key))
                                                            cookieName = val;
                                                        else if ("value".equals(key))
                                                            cookieValue = val;
                                                    }
                                                    if (cookieName != null && cookieValue != null)
                                                        caseTest.expectedCookies(new Cookie(cookieName, cookieValue));
                                                    else
                                                        compilerLogger.warning("Missing name or value in expectedCookie.");
                                                }
                                            }
                                        } else
                                            compilerLogger.warning("Unexpected type for expectedCookies: " + fieldValue.getClass());
                                        break;
                                    case Constants.AnnotationFileds.DATA_PROVIDER_CLASS_NAME:
                                        if (fieldValue instanceof List<?>) {
                                            List<?> list = (List<?>) fieldValue;
                                            if (!list.isEmpty()) {
                                                Object first = ((AnnotationValue) list.get(0)).getValue();
                                                if (first instanceof String strVal)
                                                    caseTest.dataProviderClassName(strVal);
                                                else
                                                    compilerLogger.warning("Expected String in list for dataProviderClassName, but got: " + first);
                                            }
                                        } else if (fieldValue instanceof String strVal)
                                            caseTest.dataProviderClassName(strVal);
                                          else
                                            compilerLogger.warning("Unexpected type for dataProviderClassName: " + fieldValue.getClass());
                                        break;
                                    default:
                                        compilerLogger.warning("Unknown field: " + name);
                                        break;
                                }
                            }
                            spec.getCaseTestList().add(caseTest);
                        }
                    }
                }
            }
        }
    }

}