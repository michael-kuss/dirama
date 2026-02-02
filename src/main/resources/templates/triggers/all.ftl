<#assign pageTitle="title.triggers.all">
<#include "../include/header.ftl">
<#include "../include/table.ftl">
<#include "../include/buttons.ftl">


<h1><@spring.message"triggers.all"/></h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <@sortableHeader "id" "tables.id" />
            <@sortableHeader "station" "triggers.station" />
            <th><@spring.message "triggers.trigger"/></th>
            <th><@spring.message "triggers.properties"/></th>
            <@sortableHeader "active" "triggers.active" />
            <th><@spring.message "tables.action"/></th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as t>
            <tr>
                <td>#${t.id()}</td>
                <td>${t.station()}</td>
                <td>${t.trigger().name()}</td>
                <td>
                    <ul>
                        <#list t.properties() as key, value>
                            <li>${key} = ${value}</li>
                        </#list>
                    </ul>
                </td>
                <td>
                    <@statusButton "/triggers/update/${t.id()}/toggle/status" t.active() />
                </td>
                <td>
                    <@deleteButton "/triggers/delete/${t.id()}" />
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<@pageNav />


<#include "../include/footer.ftl">
