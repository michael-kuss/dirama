<#import "/spring.ftl" as spring/>
<#import "/include/security.ftl" as security/>

<#assign shownTitle>
    <@spring.messageText pageTitle!'' '' />
</#assign>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${shownTitle}</title>
    <link href="/css/base.css" rel="stylesheet">
    <link href="/css/material.css" rel="stylesheet">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>

<header class="main-header">
    <div class="nav-container">
        <strong>${app}</strong>
        <#assign uri="${springMacroRequestContext.requestUri}">
        <nav class="left-nav">
            <a href="/"
               class="nav-item ${(uri == "/")?string("active", "")}"><@spring.messageText "nav.dashboard" "Dashboard"/></a>
            <a href="/stations/all"
               class="nav-item ${(uri?starts_with("/stations"))?string("active", "")}"><@spring.messageText "nav.stations" "Stations"/></a>
            <a href="/triggers/all"
               class="nav-item ${(uri?starts_with("/triggers"))?string("active", "")}"><@spring.messageText "nav.triggers" "Triggers"/></a>
            <a href="/users/all"
               class="nav-item ${(uri?starts_with("/users"))?string("active", "")}"><@spring.messageText "nav.users" "Users"/></a>
        </nav>

        <div class="user-menu">
            <button class="profile-btn">
                <span class="user-name">${user.username()}</span>
                <#if user.avatarReference()?has_content>
                    <img class="avatar border" src="/avatars/${user.avatarReference()}"
                         alt="${user.username()}">
                <#else>
                    <div class="avatar">${user.abbreviation()}</div>
                </#if>
            </button>
            <div class="dropdown-content">
                <a href="/users/profile" class="dropdown-link">
                    <i class="icon-user"></i><@spring.messageText "nav.my-profile" "My Profile"/>
                </a>
                <@security.authorize role="ADMIN">
                    <a href="/swagger-ui/index.html" class="dropdown-link">
                        <i class="icon-user"></i><@spring.messageText "nav.swagger-ui" "Swagger-UI"/>
                    </a>
                </@security.authorize>
                <div class="dropdown-divider"></div>
                <a href="/logout" class="dropdown-link logout">
                    <i class="icon-logout"></i> <@spring.messageText "nav.logout" "Logout"/>
                </a>
            </div>
        </div>
    </div>
</header>
