package com.pressing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Pressing API",
            version = "v1",
            description = "OpenAPI documentation for the Pressing application.",
            contact = @Contact(name = "Pressing", email = "pressing+contact@kdmarc.xyz")),
    servers = {@Server(url = "/", description = "Default Server")},
    security = {@SecurityRequirement(name = "oauth2")})
@SecurityScheme(
    name = "oauth2",
    type = SecuritySchemeType.OAUTH2,
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(
                    authorizationUrl =
                        "http://localhost:8180/realms/master/protocol/openid-connect/auth",
                    tokenUrl = "http://localhost:8180/realms/master/protocol/openid-connect/token",
                    scopes = {
                      @OAuthScope(name = "openid", description = "OpenID scope"),
                      @OAuthScope(name = "profile", description = "User profile"),
                      @OAuthScope(name = "email", description = "User email")
                    })))
@Configuration
public class OpenApiConfig {}
