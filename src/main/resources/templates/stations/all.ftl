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
                <td>${station.name()}</td>
                <td>
                    <@statusButton "/stations/update/${station.name()}/toggle/status" station.active() />
                </td>
                <td>
                    <button class="btn-outline"><a href="/stations/edit/${station.name()}">Edit</a></button>
                    <@deleteButton "/stations/delete/${station.name()}" />
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<@pageNav />


<#include "../include/footer.ftl">
