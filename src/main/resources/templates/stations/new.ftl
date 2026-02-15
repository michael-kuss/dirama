<#assign pageTitle="title.station">
<#include "../include/header.ftl">
<#include "../include/buttons.ftl">

<div>
    <div class="card-container">
        <h1><@spring.message"stations.new"/></h1>
        <form action="/stations/create" method="post" enctype="multipart/form-data">

            <div class="table-container">
                <table class="custom-table">
                    <tbody>
                    <tr>
                        <td style="text-align: center">
                            <@avatar stationRequest.avatarReference()!"" true "edit" "radio" "edit"/>
                        </td>
                        <td>
                            <div class="table-container">
                                <table class="custom-table">
                                    <tbody>
                                    <tr>
                                        <td><b>Stationsname:</b></td>
                                        <td>
                                            <@spring.formInput "stationRequest.name"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <div style="display: flex; gap: 15px; margin-top: 20px;">
                                <button class="btn-primary" type="submit">Create</button>
                                <a class="btn-outline" href="/stations/all">Zurück</a>
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
