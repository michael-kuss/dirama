<#include "../include/header.ftl">
<h1><@spring.messageText "triggers.all" "Triggers"/></h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <th>#ID</th>
            <th>Station</th>
            <th>Trigger</th>
            <th>Properties</th>
            <th>Aktiv</th>
        </tr>
        </thead>
        <tbody>
        <#list triggers.content as t>
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
                    <span class="status-pill ${t.active()?string('status-active', 'status-pending')}">
                        ${t.active()?string('Aktiv', 'Inaktiv')}
                    </span>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<#include "../include/footer.ftl">
