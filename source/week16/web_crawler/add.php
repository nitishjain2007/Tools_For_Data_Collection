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
            <h2>Add new URL and select options</h2>
        </div>

        <div class="content">
					<form class="pure-form pure-form-stacked" action="db/add_url.php" method="GET">
						<fieldset>
							<label for="email">URL</label>
							<input class="pure-input-1"  name="URL" type="text" placeholder="Enter your URL here" required>
							<div class="pure-u-1-2" style="width:49%;">
								<label for="language">Language</label>
								<select name="lang" class="pure-u-1">
								<?php language_list(); ?>
								</select>
							</div>
							<div class="pure-u-1-2" style="width:49%;">
								<label for="language">Recursive Level</label>
								<select name="re" class="pure-u-1">
<?php for($i=0;$i<11;$i++)
			echo '<option value="'.$i.'">'.$i.'</option>
			';
?>
								</select>
							</div>
							<label for="remember" class="pure-checkbox"><input name="ur" type="checkbox" value="1" style="margin-left:50px;"> Urgent
							</label>
							<button type="submit" class="pure-button pure-button-primary">Add URL</button>
						</fieldset>
					</form>
        </div>
    </div>
</div>



<?php include "footer.php"; ?>
</body>
</html>
