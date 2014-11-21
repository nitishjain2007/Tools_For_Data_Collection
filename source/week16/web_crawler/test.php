<?php
include "include/connect.php";
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Akshar Speech | Text Analysis</title>
    


<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">

        <link rel="stylesheet" href="basic.css">


</head>
<body>






<div id="layout">
    <?php include "include/sidebar.php"; 

	if(isset($_GET['msg'])&&$_GET['msg']!="")
	{
		echo '<script>alert(\'';
		echo $_GET['msg'];
		echo '\');</script>';
	}
	?>

    <div id="main">
        <div class="header">
            <h2>Add URL and see sample results</h2>
        </div>

        <div class="content">
					<form class="pure-form pure-form-stacked" action="db/test.php" method="GET">
						<fieldset>
							<label for="email">URL</label>
							<input class="pure-input-1"  name="URL" type="text" placeholder="Enter your URL here" required>
							<button type="submit" class="pure-button pure-button-primary">Test URL</button>
						</fieldset>
					</form>
        </div>
    </div>
</div>



<?php include "footer.php"; ?>
</body>
</html>
