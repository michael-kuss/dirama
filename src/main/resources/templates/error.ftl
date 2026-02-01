<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error ${status!"500"} - System Message</title>
    <link href="/css/base.css" rel="stylesheet">
</head>
<body>

<div class="background-overlay"></div>

<div class="error-container">
    <h1 class="error-code">${status!"500"}</h1>
    <div class="error-message">${error!"Unexpected Error"}</div>

    <div class="divider"></div>

    <div class="details">
        <p><strong>Path:</strong> ${path!"Unknown"}</p>
        <p>${message!"The server encountered an issue and could not complete your request."}</p>
    </div>

    <a href="/" class="btn-back">Return to Dashboard</a>
</div>

</body>
</html>