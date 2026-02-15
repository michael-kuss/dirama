<#assign pageTitle="title.stations.all">
<#include "../include/header.ftl">
<#include "../include/table.ftl">
<#include "../include/buttons.ftl">

<h1><@spring.message"stations.all"/> <@addButton "/stations/new" /></h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <@sortableHeader "name" "stations.name" />
            <@sortableHeader "active" "stations.active" />
            <th><@spring.message"stations.action"/></th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as station>
            <tr>
                <td>
                    <div class="user-menu">
                        <div class="profile-btn">
                            <span class="user-name">${station.name()}</span>
                            <#if station.avatarReference()?has_content>
                                <img class="avatar border" src="/avatars/${station.avatarReference()}"
                                     alt="${station.name()}">
                            <#else>
                                <div class="avatar"></div>
                            </#if>
                        </div>
                    </div>
                </td>
                <td>
                    <@statusButton "/stations/update/${station.id()}/toggle/status" station.active() />
                </td>
                <td>
                    <@editButton "/stations/edit/${station.id()}" />
                    <@deleteButton "/stations/delete/${station.id()}" />
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<@pageNav />


<#include "../include/footer.ftl">
