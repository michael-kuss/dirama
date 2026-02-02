<#macro sortableHeader columnName label>
<#-- Get current sort direction to toggle it -->
    <#assign currentSort = RequestParameters.sort! "">
    <#assign nextDir = (currentSort?contains(columnName + ",asc"))?string("desc", "asc")>
    <th>
        <#assign sortClass = "sort-icon">
        <#if currentSort?contains(columnName)>
            <#assign sortClass = (nextDir == "asc")?string("sort-asc", "sort-desc") >
        </#if>
        <a class="${sortClass}" href="?sort=${columnName},${nextDir}&size=${page.size}&page=${page.number}">
            <@spring.message label />
        </a>
    </th>
</#macro>

<#macro pageNav>
<#-- Configuration -->
    <#assign maxVisible = 4>
    <#assign current = page.number>
    <#assign total = page.totalPages>

<#-- Calculate dynamic range -->
    <#assign start = (current - (maxVisible / 2)?floor)>
    <#if (start < 0)><#assign start = 0></#if>

    <#assign end = start + maxVisible - 1>
    <#if (end >= total)>
        <#assign end = total - 1>
        <#assign start = end - maxVisible + 1>
        <#if (start < 0)><#assign start = 0></#if>
    </#if>

    <nav class="card-container" >
        <div style="text-align: center;">
        <#-- Helper to persist Search and Sort -->
        <#assign urlParams = "&search=${search!''}&sort=${RequestParameters.sort!''}">

        <#-- Previous Button -->
        <#if !page.first>
            <a class="btn-primary" href="?page=${current - 1}${urlParams}"><@spring.message "tables.prev" /></a>
        <#else>
            <span class="btn-outline"><@spring.message "tables.prev" /></span>
        </#if>
        <#-- Page Numbers -->
        <#list start..end as i>
            <a href="?page=${i}${urlParams}" class="${(i == current)?string('btn-primary', 'btn-outline')}">
                ${i + 1}
            </a>
        </#list>
        <#-- Next Button -->
        <#if !page.last>
            <a class="btn-primary" href="?page=${current + 1}${urlParams}"><@spring.message "tables.next" /></a>
        <#else>
            <span class="btn-outline"><@spring.message "tables.next" /></span>
        </#if>
        <div>
    </nav>
</#macro>