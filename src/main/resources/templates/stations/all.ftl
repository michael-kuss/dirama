<#include "../include/header.ftl">
<h1>My stations</h1>
<a href="/stations/new">Create New Station</a>
<div>
    <#list stations.content as station>
        <div class="station">
            ${station.name()} - ${station.stationState()}
            <a href="/stations/edit/${station.id()}">Edit</a>
            <form action="/stations/delete/${station.id()}" method="post" style="display:inline;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Delete</button>
            </form>
            <br/>
        </div>
    </#list>
</div>
</body>
</html>