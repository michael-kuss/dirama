<#assign pageTitle="title.users.all">
<#include "../include/header.ftl">
<#include "../include/table.ftl">
<#include "../include/buttons.ftl">

<h1><@spring.message"users.all"/> <@addButton "/users/new" /></h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <@sortableHeader "username" "users.username" />
            <@sortableHeader "firstName" "users.firstName" />
            <th>Nachname</th>
            <th>Rolle</th>
            <th>Status</th>
            <th><@spring.message "users.action"/></th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as u>
            <tr>
                <td>
                    <div class="user-menu">
                    <div class="profile-btn">
                        <span class="user-name">${u.username()}</span>
                        <#if u.avatarReference()?has_content>
                            <img class="avatar border" src="/avatars/${u.avatarReference()}"
                                 alt="${u.username()}">
                        <#else>
                            <div class="avatar">${u.abbreviation()}</div>
                        </#if>
                    </div>
                    </div>
                </td>
                <td>${u.firstName()}</td>
                <td>${u.lastName()}</td>
                <td>${u.roles()?join(", ")}</td>
                <td>
                    <#if user.username() == u.username()>
                        <@renderStatus u.active() />
                    <#else>
                        <@statusButton "/users/update/${u.id()}/toggle/status" u.active() />
                    </#if>
                </td>
                <td>
                    <@deleteButton "/users/delete/${u.id()}" />
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<@pageNav />

<#include "../include/footer.ftl">
