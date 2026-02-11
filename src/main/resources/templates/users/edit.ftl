<#assign pageTitle="title.profile">
<#include "../include/header.ftl">
<#include "../include/buttons.ftl">

<div>
    <div class="card-container">
        <h1>Benutzer Profil</h1>
        <form action="/users/update/${id}" method="post" enctype="multipart/form-data">

            <div class="table-container">
                <table class="custom-table">
                    <tbody>
                    <tr>
                        <td style="text-align: center">
                           <@avatar userRequest.avatarReference()!"" true />
                            <@spring.formInput "userRequest.avatarReference" "style='display: none'"/>
                        </td>
                        <td>
                            <div class="table-container">
                                <table class="custom-table">
                                    <tbody>
                                    <tr>
                                        <td><b>Benutzername:</b></td>
                                        <td>
                                            <@spring.formInput "userRequest.username"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Vorname:</b></td>
                                        <td>
                                            <@spring.formInput "userRequest.firstName"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Nachname:</b></td>
                                        <td>
                                            <@spring.formInput "userRequest.lastName"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Role:</b></td>
                                        <td>---</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <div style="display: flex; gap: 15px; margin-top: 20px;">
                                <button class="btn-primary" type="submit">Update</button>
                                <a class="btn-outline" href="/users/all">Zurück</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form>
    </div>
</div>
<#include "../include/footer.ftl">
