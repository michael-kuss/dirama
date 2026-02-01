<#include "../include/header.ftl">
<h1>Benutzer</h1>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Vorname</th>
            <th>Nachname</th>
            <th>Rolle</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <#list users.content as u>
            <tr>
                <td><strong>${u.username()}</strong></td>
                <td>${u.firstName()}</td>
                <td>${u.lastName()}</td>
                <td>${u.roles()?join(", ")}</td>
                <td>
                    <span class="status-pill ${u.active()?string('status-active', 'status-pending')}">
                        ${u.active()?string('Aktiv', 'Inaktiv')}
                    </span>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<#include "../include/footer.ftl">
