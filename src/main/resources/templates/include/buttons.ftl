<#macro statusButton url active>
    <form action="${url}" method="POST" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <@renderStatus active />
    </form>
</#macro>

<#macro renderStatus active>
    <#assign msgActive>
        <@spring.message "status.active" />
    </#assign>
    <#assign msgInactive>
        <@spring.message "status.inactive" />
    </#assign>

    <button type="submit"
            class="status-pill ${active?string('status-active', 'status-pending')}">
        ${active?string(msgActive, msgInactive)}
    </button>
</#macro>

<#macro deleteButton url>
    <form action="${url}" method="POST" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-action">
            <div class="material-symbols-rounded">cancel</div>
        </button>
    </form>
</#macro>

<#macro addButton url>
    <form action="${url}" method="GET" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-action">
            <div class="material-symbols-rounded">add_circle</div>
        </button>
    </form>
</#macro>

<#macro editButton url>
    <a href="${url}">
        <button class="btn-action">
            <div class="material-symbols-rounded">edit</div>
        </button>
    </a>
</#macro>

<#macro avatar reference editable editableIcon="person_edit" staticIcon="person" hoverEditIcon="edit">
    <#if editable>
        <script type="text/javascript">
            function readURL(input) {
                var url = input.value;
                var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
                if (input.files && input.files[0] && (ext == "png" || ext == "jpeg" || ext == "jpg")) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        var svg = document.getElementById('avatar');

                        if (svg) {
                            var img = document.createElement("img");
                            img.setAttribute("class", "rounded border");
                            img.setAttribute("id", "avatar");
                            img.height = 150;
                            img.width = 150;
                            img.src = e.target.result;

                            svg.parentNode.replaceChild(img, svg);
                        }
                    }
                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
    </#if>

    <#assign avatar_icon>
        <#if editable>
            ${editableIcon}
        <#else >
            ${staticIcon}
        </#if>
    </#assign>

    <label>
        <#if reference?has_content>
            <img class="rounded border" height="150px" width="150px"
                 id="avatar"
                 src="/avatars/${reference}">
            <#if editable>
            <div id="avatar" class="material-symbols rounded border icon" style="font-size: 150px;">
               ${hoverEditIcon}
            </div>
            </#if>
        <#else>
            <div id="avatar" class="material-symbols rounded border" style="font-size: 150px;">
                ${avatar_icon}
            </div>
        </#if>
        <#if editable>
            <input style="display:none" type="file" name="file" onchange="readURL(this)"/>
        </#if>
    </label>
</#macro>