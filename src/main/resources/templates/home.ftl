<#include "./include/header.ftl">
<h1>This is public home page</h1>
<div class="card-container">
    <h1>${pageTitle!"Welcome"}</h1>
    <p>${contentBody!"Enter your text here..."}</p>

    <div style="display: flex; gap: 15px; margin-top: 20px;">
        <button class="btn-primary">Execute Action</button>
        <button class="btn-outline">More Info</button>
    </div>
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