<#assign pageTitle="title.users.all">
<#include "../include/header.ftl">
<#include "../include/table.ftl">
<#include "../include/buttons.ftl">

<h1><@spring.message"users.all"/></h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <@sortableHeader "username" "users.username" />
            <@sortableHeader "firstName" "users.firstName" />
            <th>Nachname</th>
            <th>Rolle</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as u>
            <tr>
                <td><strong>${u.username()}</strong></td>
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
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<@pageNav />

<#include "../include/footer.ftl">
