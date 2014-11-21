<?php
    $conn=mysql_connect("127.0.0.1","crawl","crawl")or die("Error id #1. MysQl connection failed."); ;
    $db_found = mysql_select_db("zadmin_crawl")or die("Error id #2. DB connection failed."); ;
	
	function language_list()
	{
		$query = "SELECT * FROM `language`";
		$result = mysql_query($query);
		//<option>Language</option>
		while($row = mysql_fetch_array($result)){
			echo "<option value=".$row['id'].">".$row['name']."</option>";
		}
	}
	function website_url()
	{
		$query = "SELECT * FROM `website`";
		$result = mysql_query($query);
		//<option>Language</option>
		while($row = mysql_fetch_array($result)){
			echo "<option value=".$row['id'].">".$row['name']."</option>";
		}
	}
	function pending_list()
	{
		$query = "SELECT * FROM `all_url` WHERE done=0 AND re>-1 ORDER BY `urgent` DESC,`id` ASC LIMIT 0,20";
		$result = mysql_query($query);
		$i=1;
		while($row = mysql_fetch_array($result)){
		echo '	<tr';
		if($i%2==0)
			echo ' class="pure-table-odd"';
		echo '>
				<td>'.$row['id']."_".$row['website']."_".$row['language'].'</td>
				<td>'.$row['url'].'</td>
				<td>'.$row['time'].'</td>
			</tr>
			';
		$i++;
		}
	}
?>