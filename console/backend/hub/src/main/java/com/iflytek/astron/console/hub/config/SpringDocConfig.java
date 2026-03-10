package com.iflytek.astron.console.hub.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Iterator;
import java.util.Set;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Astron Agent Console Server", version = "1.0", description = "Astron Agent Console Server API Document"))
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Define security scheme
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Please enter a valid JWT Token (format: Bearer <token>)")))
                // Globally add security requirements (all interfaces require authentication by default)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    @Primary // Add @Primary annotation to ensure this Bean is used preferentially
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
        properties.setPersistAuthorization(true);
        // Other custom configurations...
        return properties;
    }

    /**
     * Intercept fastjson2 JSONObject/JSONArray to prevent Kotlin reflection errors during springdoc
     * schema generation.
     */
    @Bean
    public ModelConverter fastjson2ModelConverter() {
        return new ModelConverter() {
            private static final Set<String> FASTJSON_TYPES = Set.of(
                    JSONObject.class.getName(),
                    JSONArray.class.getName());

            @Override
            public Schema<?> resolve(AnnotatedType type, ModelConverterContext context,
                    Iterator<ModelConverter> chain) {
                String typeName = null;
                if (type.getType() instanceof JavaType javaType) {
                    typeName = javaType.getRawClass().getName();
                } else if (type.getType() instanceof Class<?> clazz) {
                    typeName = clazz.getName();
                }
                if (typeName != null && FASTJSON_TYPES.contains(typeName)) {
                    if (typeName.equals(JSONArray.class.getName())) {
                        return new ArraySchema().items(new ObjectSchema());
                    }
                    return new ObjectSchema();
                }
                return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
            }
        };
    }
}
