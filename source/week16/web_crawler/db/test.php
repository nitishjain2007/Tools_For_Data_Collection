<?php
ini_set('display_errors',1);
ini_set('display_startup_errors',1);
error_reporting(E_ALL | E_STRICT);
ini_set('html_errors', 1);
ini_set('max_execution_time', 0);
set_time_limit(0);
require "../include/crawl.php";
require "../include/connect.php";
function startsWith($haystack, $needle)
{
    return $needle === "" || strpos($haystack, $needle) === 0;
}
function endsWith($haystack, $needle)
{
    return $needle === "" || substr($haystack, -strlen($needle)) === $needle;
}
function add_words_into_dic($s,$l)
{
	$sen=mysql_real_escape_string($s);
	foreach(get_num_of_words($s) as $word)
	{
		mysql_query("INSERT INTO `".$l."_words` (`word`) VALUES ('".$word."')");
		mysql_query("UPDATE `".$l."_words` SET `occur`=`occur`+1 WHERE `word` LIKE '".$word."'");
	}
}
function add_sentence($s,$l)
{
	$sen=mysql_real_escape_string($s);
	//INSERT INTO `all_sentence`(`sen`, `length`) VALUES ("I am shivam",10)
	$sql='INSERT INTO `'.$l.'_sentence`(`sen`, `length`,`words`) VALUES ("'.$s.'",'.strlen($s).','.count(get_num_of_words($s)).')';
	mysql_query($sql);
}
function add_url($url,$data)
{
	$url=mysql_real_escape_string($url);
	$sql="INSERT INTO `all_url` (`url`, `language`, `re`, `urgent`) VALUES ('".$url."', '".$data['language']."', '".($data['re']-1)."', '".$data['urgent']."')";
	mysql_query($sql);
}
function find_website_patterns($url)
{
	$query = "SELECT * FROM `website` ORDER BY `id` DESC";
	$result = mysql_query($query);
	while($row = mysql_fetch_array($result)){
		if($row['id']==2)
			return explode('|',$row['path']);
		if(strpos($url,$row['search']) !== false) {
			return explode('|',$row['path']);
		}
	}
}
function get_num_of_words($string) {
    $string = preg_replace('/\s+/', ' ', trim($string));
    $words = explode(" ", $string);
    return $words;
}

function crawl($data_main)
{
	$patterns=find_website_patterns($data_main['url']);
	$html=file_get_html($data_main['url']);
	var_dump($patterns);
	$count=0;				// COUNT OF THE SENTANCES WE GOT. NO USE BUT WROTE FOR DEBUGGING PURPOSE.
	echo "<h2>SENTENCES FOUND</h2><br>";
	foreach($patterns as $pattern)
	{
		$data=$html->find($pattern);
		foreach($data as $i)
		{
			$para=strip_tags($i->plaintext);
			$no_bracket=preg_replace("/\([^)]+\)/",'',$para);
			$no_bracket=preg_replace("/\[[^)]+\]/",'',$no_bracket);
			$re = '/# Split sentences on whitespace between them.
				(?<=                # Begin positive lookbehind.
				  [.!?]             # Either an end of sentence punct,
				| [.!?][\'"]        # or end of sentence punct and quote.
				)                   # End positive lookbehind.
				(?<!                # Begin negative lookbehind.
				  Mr\.              # Skip either "Mr."
				| Mrs\.             # or "Mrs.",
				| Ms\.              # or "Ms.",
				| Jr\.              # or "Jr.",
				| Dr\.              # or "Dr.",
				| Prof\.            # or "Prof.",
				| Sr\.              # or "Sr.",
				| T\.V\.A\.         # or "T.V.A.",
				| St\.         # or "T.V.A.",
				| \s[A-Z]\.              # or initials ex: "George W. Bush",
									# or... (you get the idea).
				)                   # End negative lookbehind.
				\s+                 # Split on whitespace between sentences.
				/ix';
			$sentences = preg_split($re, $no_bracket, -1, PREG_SPLIT_NO_EMPTY);
			foreach($sentences as $sentence)
			{
				if(strlen($sentence)>10 && count(get_num_of_words($sentence))>2)
				{
				//	add_words_into_dic($sentence,$data_main['language']);
				//	add_sentence($sentence,$data_main['language']);
					preg_replace("/\s+/", " ", $sentence);
					$sentence=rtrim($sentence, "<br>");
					$sentence=rtrim($sentence, "<br/>");
					var_dump($sentence);
					echo "<br/>";
					//echo $sentence."<br/>";
					$count++;
				}
			}
		}
	}
	echo "<br/><h2>LINKS FOUND</h2><br>";
	$p=parse_url($data_main['url']);
	$data=$html->find('a');
	$count2=0;
	foreach($data as $got_url)
	{
		if($got_url->href!="")
		{
			if (strpos($got_url->href,$p["host"]) === false && strpos($got_url->href,'http://') === false && strpos($got_url->href,'https://') === false ){
				$got_url->href='http://'.$p["host"].$got_url->href;
			}
			//add_url($got_url,$data_main);
			echo $got_url->href."<br/>
			";
			$count2++;
		}
	}
}

function crawled($id)
{
	$sql='UPDATE `all_url` SET done=1 WHERE id='.$id;
	mysql_query($sql);
}
function add_language_if_not($i)
{
	mysql_query("CREATE TABLE IF NOT EXISTS `".$i."_sentence` (
	  `id` int(11) NOT NULL AUTO_INCREMENT,
	  `sen` varchar(1000) NOT NULL,
	  `done` int(11) NOT NULL DEFAULT '0',
	  `length` int(11) NOT NULL,
	  `words` int(11) NOT NULL,
	  PRIMARY KEY (`id`)
	)");
	mysql_query("CREATE TABLE IF NOT EXISTS `".$i."_words` (
	  `word` varchar(50) NOT NULL,
	  `download` int(10) unsigned NOT NULL DEFAULT '0',
	  `occur` int(10) unsigned NOT NULL DEFAULT '0',
	  PRIMARY KEY (`word`)
	)");
}
$query = "SELECT * FROM `all_url` WHERE done=0 ORDER BY `urgent` DESC,`id` ASC LIMIT 0,1";
$result = mysql_query($query);
while($row = mysql_fetch_array($result)){
	/*
	Yes this has been crawled now.
	Left on top i.e. even before crawling is actually done because :
	1. We can't effort breaking script for any particular URL
	2. This insure the buggy URLs even get passed.
	3. Rest is secret.
	*/
//	crawled($row['id']);
	//Check if tables exist, else make it quickly..!
//	add_language_if_not($row['language']);
	//lets crawl now as everything is set.. :D
	//var_dump($row);
	$testtt = array();
	$testtt["url"]=$_GET['URL'];
	crawl($testtt);					//Crawl and do anything you wish with this URL.. :P
	//lets go to sleep enough work for now.
	// Reason : we cant effort to bug any webserve rby crawling again and again.
	sleep(0.3);
}
?>