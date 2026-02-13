<#macro authorize role>
    <#local authorized = false>
    <#if user??>
        <#if user.roles()??>
            <#list user.roles() as auth>
                <#if auth == "ROLE_" + role || auth == role>
                    <#local authorized = true>
                    <#break>
                </#if>
            </#list>
        </#if>
    </#if>

    <#if authorized>
        <#nested>
    </#if>
</#macro>
