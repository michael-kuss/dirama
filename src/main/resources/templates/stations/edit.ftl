<#include "../include/header.ftl">
<h1>Edit Station</h1>
<form action="/stations/update/${stationRequest.id()}" method="post">
    <label for="name">Data:</label>
    <input type="text" id="name" name="name" value="${stationRequest.name()}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Update</button>
</form>
</body>
</html>