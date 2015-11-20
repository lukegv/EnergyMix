 <?php
 
 if($_GET['auth'] != "3a1b30712e4d86ba" || $_GET['key'] != "3061c95fc954aa1f")
	die("Authentification failed");
 
$myfile = fopen("csvdata.txt", "r") or die("Unable to open file!");
echo fread($myfile,filesize("csvdata.txt"));
fclose($myfile);
?> 