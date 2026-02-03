<#assign pageTitle="title.profile">
<#include "../include/header.ftl">
<#include "../include/buttons.ftl">

<div>
    <div class="card-container">
        <h1>Benutzer Profil</h1>

        <div class="table-container">
            <table class="custom-table">
                <tbody>
                <tr>
                    <td align="center">
                        <#if user.avatarReference()?has_content>
                            <img class="rounded border" height="128px" width="128px" src="/avatars/${user.avatarReference()}"
                                 alt="${user.username()}">
                        <#else>
                            <svg width="128px" height="128px" viewBox="0 0 16 16" fill="none"
                                 xmlns="http://www.w3.org/2000/svg">
                                <path d="M8 7C9.65685 7 11 5.65685 11 4C11 2.34315 9.65685 1 8 1C6.34315 1 5 2.34315 5 4C5 5.65685 6.34315 7 8 7Z"
                                      fill="#000000"/>
                                <path d="M14 12C14 10.3431 12.6569 9 11 9H5C3.34315 9 2 10.3431 2 12V15H14V12Z"
                                      fill="#000000"/>
                            </svg>
                        </#if>
                    </td>
                    <td>
                        <div class="table-container">
                            <table class="custom-table">
                                <tbody>
                                <tr>
                                    <td><b>Benutzername:</b></td>
                                    <td>${user.username()}</td>
                                </tr>
                                <tr>
                                    <td><b>Vorname:</b></td>
                                    <td>${user.firstName()}</td>
                                </tr>
                                <tr>
                                    <td><b>Nachname:</b></td>
                                    <td>${user.lastName()}</td>
                                </tr>
                                <tr>
                                    <td><b>Aktiv:</b></td>
                                    <td><@renderStatus user.active() /></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<#include "../include/footer.ftl">
