<?php
include "include/connect.php";
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Akshar Speech | Check Queue</title>
    


<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">

        <link rel="stylesheet" href="basic.css">


</head>
<body>






<div id="layout">
    <?php include "include/sidebar.php"; ?>

    <div id="main">
        <div class="header">
            <h2>URL to be crawled on next CRON update</h2>
        </div>

        <div class="content">
            <table class="pure-table">
    <thead>
        <tr>
            <th>#</th>
            <th>URL</th>
            <th>Addition Time</th>
	</tr>
    </thead>

    <tbody>
	<?php	pending_list(); ?>
    </tbody>
</table>
        </div>
    </div>
</div>



<?php include "footer.php"; ?>
</body>
</html>
