/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config;

import de.miq.dirama.common.AuthConstants;
import de.miq.dirama.common.OpenApiConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = AuthConstants.JWT_AUTHORIZATION_HEADER)
@SecurityScheme(
    name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = AuthConstants.API_KEY_AUTHORIZATION_HEADER)
@SecurityScheme(
    name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT,
    type = SecuritySchemeType.HTTP,
    scheme = "basic")
public class OpenApiConfig {}
