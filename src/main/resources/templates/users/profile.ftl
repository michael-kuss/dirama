<#include "../include/header.ftl">
<div>
    <div class="card-container">
        <h1>Benutzer Profil</h1>

        <div class="table-container">
            <table class="custom-table">
                <tbody>
                <tr>
                    <td><b>Benutzername:</b></td><td>${user.username()}</td>
                </tr>
                <tr>
                    <td><b>Vorname:</b></td><td>${user.firstName()}</td>
                </tr>
                <tr>
                    <td><b>Nachname:</b></td><td>${user.lastName()}</td>
                </tr>
                <tr>
                    <td><b>Aktiv:</b></td><td>${user.active()?c}</td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
</div>
<#include "../include/footer.ftl">
