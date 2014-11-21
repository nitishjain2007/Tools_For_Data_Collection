<?php
	
include "include/connect.php";

$what = "SELECT * FROM `".$_POST['lang']."_sentence` ";
$query = "WHERE 1 ";
if($_POST['minl']!="")
$query.=" AND  words>".$_POST['minl'];
if($_POST['maxl']!="")
$query.=" AND  words<".$_POST['maxl'];
//var_dump($_POST['words']);
if($_POST['words']!="")
{
	$words=explode(',',$_POST['words']);
	foreach($words as $word)
		$query.=" AND sen NOT LIKE '% ".trim($word)." %'";
}
if($_POST['sent']!="")
{
	$sens=explode(',',$_POST['sent']);
	foreach($sens as $sen)
	{
		preg_replace("/[^A-Za-z0-9 ]/", '', $sen);
		$words=explode(' ',$sen);
		foreach($words as $word)
		$query.=" AND sen NOT LIKE '% ".trim($word)." %'";
	}
}
$query.=" ORDER BY done ASC LIMIT 0,50";
$condi=$query;
$query=$what.$query;
//echo $query;
$result = mysql_query($query);
$qry2="UPDATE `".$_POST['lang']."_sentence` SET done=done+1 ";
$qry2.=$condi;
if(isset($_POST['dn_please']))
{
	//mysql_query($qry2);
	header('Content-disposition: attachment; filename=akshar_dn.txt');
	header('Content-type: text/plain');
	while($row = mysql_fetch_array($result)){
	mysql_query('UPDATE '.$_POST['lang'].'_sentence SET done=done+1 WHERE id='.$row['id']);
	echo $row['id'].'__'.$_POST['lang'].')'.trim($row['sen'])."\n";
	}
	exit;
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Akshar Speech | View Sentances</title>
<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
<link rel="stylesheet" href="basic.css">
</head>
<body>




<?php if(!(isset($_POST['search']))) {?>

<div id="layout">
    <?php include "include/sidebar.php"; ?>

    <div id="main">
        <div class="header">
            <h2>Please select options and download list of sentances accordingly.</h2>
        </div>
        <div class="content">
					<form class="pure-form pure-form-stacked" method="post">
						<fieldset>
    <div class="pure-u-1-2" style="width:49%;">
        							<label for="language">Language</label>
							<select name="lang" class="pure-u-1">
								<?php language_list(); ?>
							</select>
    </div>
	<label for="uwords">Count of words (Empty to ignore a field)</label>				
    <div class="pure-u-1-2" style="width:49%;">
        <input class="pure-input-1" type="text" placeholder="Minimum words" name="minl">
    </div>
    <div class="pure-u-1-2">
        <input class="pure-input-1" type="text" placeholder="Maximum words" name="maxl">
    </div>
	<label for="uwords">Exclude words</label>	
	<textarea rows="4" cols="125" placeholder="Comma Seperated words which you wish to exclude..." name="words"></textarea>
	<label for="uwords">Exclude words of sentances</label>	
	<textarea rows="4" cols="125" placeholder="Comma Seperated sentances whose words you wish to exclude..."></textarea>
		<input type="text" name="search" hidden value="1">
							<button type="submit" class="pure-button pure-button-primary">Submit</button>
						</fieldset>
					</form>
        </div>
   
    </div>
</div>

<?php } else { ?>

<div id="layout">
    <?php include "include/sidebar.php"; ?>

    <div id="main">
        <div class="header">
            <h2>Search Results</h2>
        </div>

        <div class="content">
            <table class="pure-table">
    <thead>
        <tr>
            <th>#</th>
            <th>Sentence</th>
	</tr>
    </thead>

    <tbody>
	<?php
	$i=1;
	echo $query;
	while($row = mysql_fetch_array($result)){
	echo '	<tr';
	if($i%2==0)
		echo ' class="pure-table-odd"';
	echo '>
			<td>'.$row['id'].'__'.$_POST['lang'].'</td>
			<td>'.($row['sen']).'</td>
		</tr>
		';
	$i++;
	}
	
	?>
    </tbody>
</table>
        </div>
    </div>
</div>

<div style="position:fixed;bottom:10px;right:10px;">
<form method="POST">
<?php
foreach ($_POST as $a => $b) {
	echo "<input type='hidden' name='".htmlentities($a)."' value='".htmlentities($b)."'>";
}
?>
<input type="submit" value="DOWNLOAD" name="dn_please"/>
</form>
</div>

<?php } ?>
<?php include "footer.php"; ?>
</body>
</html>
