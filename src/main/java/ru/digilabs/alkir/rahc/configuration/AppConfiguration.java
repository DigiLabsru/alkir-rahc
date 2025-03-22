package ru.digilabs.alkir.rahc.configuration;

import com._1c.v8.ibis.admin.client.AgentAdminConnectorFactory;
import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.service.RacService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Configuration
@EnableRetry
public class AppConfiguration {

    @Bean
    IAgentAdminConnectorFactory adminConnectorFactory() {
        return new AgentAdminConnectorFactory();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    @Scope(value = SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    RacService sessionScopedRacService(RacService racService) {
        return racService;
    }

    @Bean
    public GroupedOpenApi jsonrpcApi(List<JsonRpcController> services) {

        return GroupedOpenApi.builder()
            .group("jsonrpc")
            .addOperationCustomizer(this::customizeOperation)
            .addOpenApiCustomizer(openApi -> customizeOpenApi(services, openApi))
            .build();
    }

    private Operation customizeOperation(Operation operation, HandlerMethod handlerMethod) {
        if (!JsonRpcController.class.isAssignableFrom(handlerMethod.getBeanType())) {
            return operation;
        }

        String description = "";
        if (StringUtils.isNotEmpty(operation.getDescription())) {
            description += operation.getDescription();
            description += "\n\n";
        }

        description += "json-rpc method: " + handlerMethod.getMethod().getName();
        var parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length > 0) {
            description += "\n\n";
            description += "json-rpc params: ";

            var parametersDescription = new StringJoiner(", ");
            for (Parameter parameter : parameters) {
                String parameterName = parameter.getName();
                if (Optional.class.isAssignableFrom(parameter.getType())) {
                    parameterName += "?";
                }
                parametersDescription.add(parameterName);
            }

            description += parametersDescription.toString();
        }
        operation.description(description);
        return operation;
    }

    private void customizeOpenApi(List<JsonRpcController> services, OpenAPI openApi) {
        services.stream().map(JsonRpcController::getClass).forEach(controllerClass -> {
            var basePath = basePath(controllerClass);

            var operation = operation(controllerClass, openApi);
            var pathItem = new PathItem().post(operation);

            openApi.path(basePath, pathItem);
        });
    }

    private String basePath(Class<? extends JsonRpcController> controllerClass) {
        var jsonRpcServiceAnnotation = AnnotationUtils.findAnnotation(controllerClass, JsonRpcService.class);
        Objects.requireNonNull(jsonRpcServiceAnnotation);

        return jsonRpcServiceAnnotation.value();
    }

    private Operation operation(Class<? extends JsonRpcController> controllerClass, OpenAPI openApi) {
        var operation = new Operation();

        addTags(controllerClass, operation);
        addSecurityRequirement(controllerClass, operation);
        addRequestBody(controllerClass, operation, openApi);
        addResponses(controllerClass, operation, openApi);

        return operation;
    }

    private void addTags(Class<? extends JsonRpcController> controllerClass, Operation operation) {
        var tagAnnotation = AnnotationUtils.findAnnotation(controllerClass, Tag.class);
        Objects.requireNonNull(tagAnnotation);
        var tagsItem = tagAnnotation.name();

        operation.addTagsItem(tagsItem);
    }

    private void addSecurityRequirement(Class<? extends JsonRpcController> controllerClass, Operation operation) {
        var securityRequirementAnnotation = AnnotationUtils.findAnnotation(controllerClass, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);

        if (securityRequirementAnnotation != null) {
            var securityItem = new SecurityRequirement()
                .addList(securityRequirementAnnotation.name());
            operation.addSecurityItem(securityItem);
        }
    }

    private void addRequestBody(Class<? extends JsonRpcController> controllerClass, Operation operation, OpenAPI openApi) {
        var requestSchema = requestSchema(controllerClass, openApi);

        MediaType mediaType = new MediaType()
            .schema(requestSchema);

        var content = new Content()
            .addMediaType(APPLICATION_JSON_VALUE, mediaType);

        RequestBody requestBody = new RequestBody()
            .content(content);

        operation.requestBody(requestBody);
    }

    private void addResponses(Class<? extends JsonRpcController> controllerClass, Operation operation, OpenAPI openApi) {
        var responseSchema = responseSchema(controllerClass, openApi);

        var responseMediaType = new MediaType()
            .schema(responseSchema);

        var responseContent = new Content()
            .addMediaType(APPLICATION_JSON_VALUE, responseMediaType);

        var apiResponse = new ApiResponse()
            .content(responseContent);

        var responses = new ApiResponses()
            .addApiResponse("200", apiResponse);

        operation.responses(responses);
    }

    private Schema requestSchema(Class<? extends JsonRpcController> controllerClass, OpenAPI openApi) {
        var originalControllerClass = (Class<? extends JsonRpcController>) ClassUtils.getUserClass(controllerClass);
        var operationMethods = ReflectionUtils.getUniqueDeclaredMethods(
            originalControllerClass,
            method -> AnnotationUtils.findAnnotation(method, io.swagger.v3.oas.annotations.Operation.class) != null
        );

        var restPath = AnnotationUtils.findAnnotation(originalControllerClass, RequestMapping.class).value()[0];

        var requestSchema = new ComposedSchema();

        for (Method operationMethod : operationMethods) {

            var requestMappingAnnotation = AnnotatedElementUtils.getMergedAnnotation(operationMethod, RequestMapping.class);
            var restSubPath = Optional.ofNullable(requestMappingAnnotation)
                .map(RequestMapping::value)
                .filter(strings -> strings.length > 0)
                .map(strings -> strings[0])
                .map(subPath -> "/" + subPath)
                .orElse("");
            var restPathItem = openApi.getPaths().get("/" + restPath + restSubPath);

            var operationAnnotation = AnnotationUtils.findAnnotation(operationMethod, io.swagger.v3.oas.annotations.Operation.class);
            Objects.requireNonNull(operationAnnotation);

            var methodName = operationAnnotation.method().isEmpty() ? operationMethod.getName() : operationAnnotation.method();

            var parametersSchema = new ObjectSchema()
                .description("A Structured value that holds the parameter values to be used during the " +
                             "invocation of the method. This member MAY be omitted");

            if (restPathItem != null) {
                Operation restOperation = null;
                if (isJsonRpcMethod(restPathItem.getPut(), methodName)) {
                    restOperation = restPathItem.getPut();
                } else if (isJsonRpcMethod(restPathItem.getGet(), methodName)) {
                    restOperation = restPathItem.getGet();
                } else if (isJsonRpcMethod(restPathItem.getPost(), methodName)) {
                    restOperation = restPathItem.getPost();
                } else if (isJsonRpcMethod(restPathItem.getDelete(), methodName)) {
                    restOperation = restPathItem.getDelete();
                }

                if (restOperation != null) {
                    var restParameters = Optional.ofNullable(restOperation.getParameters()).orElse(Collections.emptyList());
                    var jsonRpcParameters = operationMethod.getParameters();
                    for (Parameter jsonRpcParameter : jsonRpcParameters) {

                        Operation finalRestOperation = restOperation;
                        restParameters.stream()
                            .filter(parameter -> parameter.getName().equals(jsonRpcParameter.getName()))
                            .findAny()
                            .ifPresentOrElse(
                                parameter -> parametersSchema.addProperty(jsonRpcParameter.getName(), parameter.getSchema()),
                                () -> parametersSchema.addProperty(jsonRpcParameter.getName(), finalRestOperation.getRequestBody().getContent().get(APPLICATION_JSON_VALUE).getSchema())
                            );
                    }
                }
            }

            requestSchema.addOneOfItem(new ObjectSchema()
                .title(methodName)
                .addProperty("jsonrpc", new StringSchema()
                    .type("string")
                    .description("A String specifying the version of the JSON-RPC protocol. MUST be exactly \"2.0\".")
                    .example("2.0")
                )
                .addProperty("method", new StringSchema()
                    .description(
                        "A String containing the name of the method to be invoked. " +
                        "Method names that begin with the word rpc followed by a period character (U+002E or ASCII 46) " +
                        "are reserved for rpc-internal methods and extensions and MUST NOT be used for anything else."
                    ).example(methodName))
                .addProperty("params", parametersSchema)
                .addProperty("id", new IntegerSchema()
                    .description("An identifier established by the Client that MUST contain a String, Number, or NULL " +
                                 "value if included. If it is not included it is assumed to be a notification. " +
                                 "The value SHOULD normally not be Null [1] and Numbers SHOULD NOT contain fractional parts")
                    .example(1)
                )
                .addRequiredItem("jsonrpc")
                .addRequiredItem("method")
            );

        }
        return requestSchema;
    }

    private boolean isJsonRpcMethod(Operation operation, String methodName) {
        if (operation == null) {
            return false;
        }
        return operation.getDescription().startsWith("json-rpc method: " + methodName);
    }

    private Schema responseSchema(Class<? extends JsonRpcController> controllerClass, OpenAPI openApi) {

        var originalControllerClass = (Class<? extends JsonRpcController>) ClassUtils.getUserClass(controllerClass);

        var responseSchema = new ComposedSchema();

        var operationMethods = ReflectionUtils.getUniqueDeclaredMethods(
            originalControllerClass,
            method -> AnnotationUtils.findAnnotation(method, io.swagger.v3.oas.annotations.Operation.class) != null
        );

        var restPath = AnnotationUtils.findAnnotation(originalControllerClass, RequestMapping.class).value()[0];

        for (Method operationMethod : operationMethods) {

            var requestMappingAnnotation = AnnotatedElementUtils.getMergedAnnotation(operationMethod, RequestMapping.class);
            var restSubPath = Optional.ofNullable(requestMappingAnnotation)
                .map(RequestMapping::value)
                .filter(strings -> strings.length > 0)
                .map(strings -> strings[0])
                .map(subPath -> "/" + subPath)
                .orElse("");
            var restPathItem = openApi.getPaths().get("/" + restPath + restSubPath);

            var operationAnnotation = AnnotationUtils.findAnnotation(operationMethod, io.swagger.v3.oas.annotations.Operation.class);
            Objects.requireNonNull(operationAnnotation);

            var methodName = operationAnnotation.method().isEmpty() ? operationMethod.getName() : operationAnnotation.method();

            var resultSchema = new ObjectSchema()
                .title(methodName);

            if (restPathItem != null) {
                Operation restOperation = null;
                if (isJsonRpcMethod(restPathItem.getPut(), methodName)) {
                    restOperation = restPathItem.getPut();
                } else if (isJsonRpcMethod(restPathItem.getGet(), methodName)) {
                    restOperation = restPathItem.getGet();
                } else if (isJsonRpcMethod(restPathItem.getPost(), methodName)) {
                    restOperation = restPathItem.getPost();
                } else if (isJsonRpcMethod(restPathItem.getDelete(), methodName)) {
                    restOperation = restPathItem.getDelete();
                }

                if (restOperation != null) {
                    var content = restOperation.getResponses().get("200").getContent();
                    resultSchema = Optional.ofNullable(content)
                        .map(maybeContent -> maybeContent.get(ALL_VALUE))
                        .map(MediaType::getSchema)
                        .orElse(resultSchema);
                }
            }

            var methodResultSchema = new ObjectSchema()
                .title(methodName)
                .description("""
                    This member is REQUIRED on success.
                    This member MUST NOT exist if there was an error invoking the method.
                    The value of this member is determined by the method invoked on the Server."""
                ).addProperty("jsonrpc", new StringSchema()
                    .description("A String specifying the version of the JSON-RPC protocol. MUST be exactly \"2.0\".")
                    .example("2.0")
                )
                .addProperty("result", resultSchema)
                .addProperty("error", new ObjectSchema()
                    .description("""
                        This member is REQUIRED on error.
                        This member MUST NOT exist if there was no error triggered during invocation.
                        The value for this member MUST be an Object as defined in section 5.1."""
                    )
                    .addProperty("code", new IntegerSchema()
                        .description("""
                            A Number that indicates the error type that occurred.
                            This MUST be an integer.""")
                    )
                    .addProperty("message", new StringSchema()
                        .type("string")
                        .description("""
                            A String providing a short description of the error.
                            The message SHOULD be limited to a concise single sentence."""
                        )
                    )
                    .addProperty("data", new ObjectSchema()
                        .description("""
                            A Primitive or Structured value that contains additional information about the error.
                            This may be omitted.
                            The value of this member is defined by the Server (e.g. detailed error information, nested errors etc.)."""
                        )
                    )
                )
                .addRequiredItem("jsonrpc")
                .addRequiredItem("id");

            responseSchema.addOneOfItem(methodResultSchema);
        }

        return responseSchema;
    }

}
