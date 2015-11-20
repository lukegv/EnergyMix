 <?php
 
 if($_GET['auth'] != "3a1b30712e4d86ba" || $_GET['key'] != "3061c95fc954aa1f")
	die("Authentification failed");
 
$handle = fopen("csvdata.txt", "r");
if ($handle) {
	$lastLine = "";
    while (($line = fgets($handle)) !== false) {
        $pos = strpos($line, "N/A");
		
		// if N/A is found, return LAST line
		if($pos != false) {
			echo $lastLine;
			break;
		}
		
		$lastLine = $line;
    }

    fclose($handle);
} else {
    // error opening the file.
} 

fclose($handle);
?> 