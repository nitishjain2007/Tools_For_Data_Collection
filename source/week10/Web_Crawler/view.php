<?php
include "include/connect.php";
?>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Akshar Speech | Pending URLs</title>
<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
<link rel="stylesheet" href="basic.css">
</head>
<body>
<div id="layout">
    <?php include "include/sidebar.php"; ?>


    <div id="main">
        <div class="header">
            <h2><?php

if(isset($_GET['id']))
{
	$sql='SELECT * FROM `all_sentence` WHERE id='.$_GET['id'];
	$result=mysql_query($sql);
	$row=mysql_fetch_array($result);
	if($row)
	echo $row['sen'];
	else
	echo "This ID doesn't exist.";
}
else
{
	echo "Enter unique ID of the sentance you want to view";
}
?></h2>
        </div>
        <div class="content">
					<form class="pure-form pure-form-stacked" method="GET">
						<fieldset>
							


	<label for="uwords">Unique ID</label>				
    <div class="pure-u-1-2" style="width:49%;">
        <input class="pure-input-1" type="text" placeholder="Unique ID" name="id">
    </div>
    <div class="pure-u-1-2">.
    </div>
							<button type="submit" class="pure-button pure-button-primary">Submit</button>
						</fieldset>
					</form>
        </div>
   
    </div>
</div>



</body>
</html>
