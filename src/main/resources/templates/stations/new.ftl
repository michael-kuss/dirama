<#include "../include/header.ftl">
<h1><@spring.message"stations.new"/></h1>
<div class="card-container">

    <form action="/stations/create" method="post">
        <label for="name">Daten:</label>
        <@spring.formInput "stationRequest.name"/>
        <@spring.showErrors "<br>"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div style="display: flex; gap: 15px; margin-top: 20px;">
            <button class="btn-primary" type="submit">Create</button>
            <a class="btn-outline" href="/stations/all">Zurück</a>
        </div>
    </form>
</div>
<#include "../include/footer.ftl">
