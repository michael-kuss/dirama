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
    <a href="${url}"><button class="btn-action"><div class="material-symbols-rounded">edit</div></button></a>
</#macro>
