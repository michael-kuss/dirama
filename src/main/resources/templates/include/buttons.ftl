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
            <svg fill="var(--accent-red)" height="30px" width="30px" version="1.1" id="Layer_1"
                 xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                 viewBox="0 0 300.00 300.00" xml:space="preserve">
<g>
    <g>
        <path d="M150,0C67.159,0,0.001,67.159,0.001,150c0,82.838,67.157,150.003,149.997,150.003S300.002,232.838,300.002,150
			C300.002,67.159,232.839,0,150,0z M206.584,207.171c-5.989,5.984-15.691,5.984-21.675,0l-34.132-34.132l-35.686,35.686
			c-5.986,5.984-15.689,5.984-21.672,0c-5.989-5.991-5.989-15.691,0-21.68l35.683-35.683L95.878,118.14
			c-5.984-5.991-5.984-15.691,0-21.678c5.986-5.986,15.691-5.986,21.678,0l33.222,33.222l31.671-31.673
			c5.986-5.984,15.694-5.986,21.675,0c5.989,5.991,5.989,15.697,0,21.678l-31.668,31.671l34.13,34.132
			C212.57,191.475,212.573,201.183,206.584,207.171z"/>
    </g>
</g>
</svg>
        </button>
    </form>
</#macro>

<#macro addButton url>
    <form action="${url}" method="GET" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-action">
            <svg fill="var(--accent-red)" height="30px" width="30px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg"
                 xmlns:xlink="http://www.w3.org/1999/xlink"
                 viewBox="0 0 300.003 300.003" xml:space="preserve">
<g>
    <g>
        <path d="M150,0C67.159,0,0.001,67.159,0.001,150c0,82.838,67.157,150.003,149.997,150.003S300.002,232.838,300.002,150
			C300.002,67.159,232.839,0,150,0z M213.281,166.501h-48.27v50.469c-0.003,8.463-6.863,15.323-15.328,15.323
			c-8.468,0-15.328-6.86-15.328-15.328v-50.464H87.37c-8.466-0.003-15.323-6.863-15.328-15.328c0-8.463,6.863-15.326,15.328-15.328
			l46.984,0.003V91.057c0-8.466,6.863-15.328,15.326-15.328c8.468,0,15.331,6.863,15.328,15.328l0.003,44.787l48.265,0.005
			c8.466-0.005,15.331,6.86,15.328,15.328C228.607,159.643,221.742,166.501,213.281,166.501z"/>
    </g>
</g>
</svg>
        </button>
    </form>
</#macro>
