<#include "../include/header.ftl">
<h1>Stationen</h1>

<button class="btn-outline"><a href="/stations/new">Neue Station</a></button>

<div class="table-container">
    <table class="custom-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Status</th>
            <th>Aktion</th>
        </tr>
        </thead>
        <tbody>
        <#list stations.content as station>
            <tr>
                <td>${station.name()}</td>
                <td>
                    <span class="status-pill ${(station.stationState().name() == "ONLINE")?string('status-active', 'status-pending')}">
                        ${station.stationState()}
                    </span>
                </td>
                <td>
                    <button class="btn-outline"><a href="/stations/edit/${station.name()}">Edit</a></button>
                    <form action="/stations/delete/${station.name()}" method="post" style="display:inline;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button class="btn-primary" type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>


<#include "../include/footer.ftl">
