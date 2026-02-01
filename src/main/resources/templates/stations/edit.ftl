<#include "../include/header.ftl">
<h1>Station editieren</h1>

<div class="card-container">

    <form action="/stations/update/${stationRequest.name()}" method="post">
        <label for="name">Data:</label>
        <input type="text" id="name" name="name" value="${stationRequest.name()}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn-primary" type="submit">Update</button>
    </form>

</div>
<#include "../include/footer.ftl">
