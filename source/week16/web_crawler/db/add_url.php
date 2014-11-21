<?php
//EXAMPLE QUERY
//INSERT INTO `zadmin_crawl`.`all_url` (`url`, `id`, `website`, `language`, `re`, `time`, `done`) VALUES ('http://en.wikipedia.org/wiki/New_york', NULL, '2', '3', '0', CURRENT_TIMESTAMP, '0');
include "../include/connect.php";
if(isset($_GET['URL']))
{
//Yes there was a URL in input
	$url=$_GET['URL'];
	if (strpos($url,'http://') === false && strpos($url,'https://') === false ){
		$url='http://'.$url;
	}
	if(preg_match("/\b(?:(?:https?|ftp):\/\/|www\.)[-a-z0-9+&@#\/%?=~_|!:,.;]*[-a-z0-9+&@#\/%=~_|]/i",$url))
	{
	// Yeah, that input url was actually valid too..
		$url=mysql_real_escape_string($url);			//world is full or cruel peoples, lets save us from them.
	//Time to see if I am going to be called recursively or not.. ;)
		$re=$_GET['re'];
		$ur=0;
		if($_GET['ur']==1)
			$ur=1;
		$lang=mysql_real_escape_string($_GET['lang']);
	//Construct query in PHP
		$sql="INSERT INTO `all_url` (`url`, `language`, `re`, `urgent`) VALUES ('".$url."', '".$lang."', '".$re."', '".$ur."')";
	//	echo $sql;
	//Time to execute this. It is very cruel part, hope system is fixed even after execution.
		$result=mysql_query($sql);
		if(!$result)
			header('Location: ../add.php?msg=Your URL was already in list.');	//mysql add failure
		else
			header('Location: ../add.php?msg=URL has been successfully added into the list.');	//yes finally..!
	}
	else
		header('Location: ../add.php?msg=The URL entered seems to be invalid, please try again.');	//buggy peoples.. :P
}
else
	header('Location: ../add.php?msg=Please enter your URL in the field.');
?>