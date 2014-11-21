<?php
include "include/connect.php";
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Akshar Speech | Remove Sentance</title>
    


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
	$sql='DELETE FROM `'.$_GET['lang'].'_sentence` WHERE id='.$_GET['id'];
	$result=mysql_query($sql);
	echo "This ID has been removed from the database.";
}
else
{
	echo "Enter unique ID of the sentance you want to remove";
}
?></h2>
        </div>
        <div class="content">
					<form class="pure-form pure-form-stacked" method="GET">
						<fieldset>
							


	<div class="pure-u-1-2" style="width:49%;">
		<label for="language">Language</label>
		<select name="lang" class="pure-u-1">
		<?php language_list(); ?>
		</select>
	</div>
	<label for="uwords">Unique ID</label>				
    <div class="pure-u-1-2" style="width:49%;">
        <input class="pure-input-1" type="text" placeholder="Unique ID" name="id" style="background: #A00;color: #FFF;">
    </div>
    <div class="pure-u-1-2">.
    </div>
							<button type="submit" class="pure-button pure-button-primary">Submit</button>
						</fieldset>
					</form>
        </div>
   
    </div>
</div>



<?php include "footer.php"; ?>
</body>
</html>
