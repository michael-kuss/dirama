<#assign pageTitle="title.dashboard">
<#include "./include/header.ftl">
<h1><@spring.messageText pageTitle!'' '' /></h1>

<div class="card-container">
    <#list page.content as title>
        <table class="custom-table">
            <tbody>
            <tr>
                <td style="text-align: center">
                    <img class="rounded border" height="120px" width="160px"
                         id="avatar"
                         src="https://pure-fm.de/wp-content/uploads/neutral-dab.jpg">
                </td>
                <td>
                    <div class="table-container">
                        <table class="custom-table">
                            <tbody>
                            <tr>
                                <td><b>Station:</b></td>
                                <td>${title.station()}</td>
                            </tr>
                            <tr>
                                <td><b>Title:</b></td>
                                <td>${title.title()}</td>
                            </tr>
                            <tr>
                                <td><b>Artist:</b></td>
                                <td>${title.artist()}</td>
                            </tr>
                            <tr>
                                <td><b>Date:</b></td>
                                <td>${title.titleDate().format('yyyy-MM-dd HH:mm:ss')}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </#list>
</div>



<div class="card-container">
    <h1>Station</h1>
    <p>Stations go here</p>

    <div style="display: flex; gap: 15px; margin-top: 20px;">
        <button class="btn-primary">Execute Action</button>
        <button class="btn-outline">More Info</button>
    </div>
</div>

<#include "./include/footer.ftl">