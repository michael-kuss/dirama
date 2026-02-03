<#assign pageTitle="title.profile">
<#include "../include/header.ftl">
<#include "../include/buttons.ftl">
<script type="text/javascript">
    function readURL(input) {
        var url = input.value;
        var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
        if (input.files && input.files[0]&& (ext == "png" || ext == "jpeg" || ext == "jpg")) {
            var reader = new FileReader();

            reader.onload = function (e) {
                var svg = document.getElementById('avatar');

                if (svg) {
                        var img = document.createElement("img");
                        img.setAttribute("class","rounded border");
                        img.setAttribute("id", "avatar");
                        img.height= 128;
                        img.width= 128;
                        img.src = e.target.result;

                        svg.parentNode.replaceChild(img, svg);
                }
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
<div>
    <div class="card-container">
        <h1>Benutzer Profil</h1>
        <form action="/users/create" method="post" enctype="multipart/form-data">

            <div class="table-container">
                <table class="custom-table">
                    <tbody>
                    <tr>
                        <td align="center">
                            <#if userCreateRequest.avatarReference()?has_content>
                                <img class="rounded border" height="128px" width="128px"
                                     src="/avatars/${user.avatarReference()}"
                                     alt="${userCreateRequest.username()}">
                            <#else>
                                <svg id="avatar" width="128px" height="128px" viewBox="0 0 16 16" fill="none"
                                     xmlns="http://www.w3.org/2000/svg">
                                    <path d="M8 7C9.65685 7 11 5.65685 11 4C11 2.34315 9.65685 1 8 1C6.34315 1 5 2.34315 5 4C5 5.65685 6.34315 7 8 7Z"
                                          fill="#000000"/>
                                    <path d="M14 12C14 10.3431 12.6569 9 11 9H5C3.34315 9 2 10.3431 2 12V15H14V12Z"
                                          fill="#000000"/>
                                </svg>
                            </#if>
                            <label class="btn-primary" style="display: block;">
                                <input style="display:none" type="file" name="file" onchange="readURL(this)"/>
                                Image
                            </label>
                        </td>
                        <td>
                            <div class="table-container">
                                <table class="custom-table">
                                    <tbody>
                                    <tr>
                                        <td><b>Benutzername:</b></td>
                                        <td>
                                            <@spring.formInput "userCreateRequest.username"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Vorname:</b></td>
                                        <td>
                                            <@spring.formInput "userCreateRequest.firstName"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Nachname:</b></td>
                                        <td>
                                            <@spring.formInput "userCreateRequest.lastName"/>
                                            <@spring.showErrors "<br>" "error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><b>Password:</b></td>
                                        <td>
                                            <@spring.formPasswordInput "userCreateRequest.password"/>
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
                                <button class="btn-primary" type="submit">Create</button>
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
