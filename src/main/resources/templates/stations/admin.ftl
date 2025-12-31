<#include "../include/header.ftl">
<h1>All stations</h1>
<div>
    <#list stations.content as station>
        <div class="station">
            ${station.name()} - ${station.stationState()}
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