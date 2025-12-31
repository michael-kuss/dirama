<#include "../include/header.ftl">
<h1>Create New Station</h1>
<form action="/stations/create" method="post">
    <label for="name">Data:</label>
    <input type="text" id="name" name="name">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Create</button>
</form>
<a href="/stations">Back to Home</a>
</body>
</html>