<?php //insertnew.php
 /* 
 * Following code will create a new product row
 * All product details are read from HTTP GET Request 
 */
 // array for JSON response
 $response = array();
 // check for required fields
 if (isset($_GET["MobileNumber"]))
 {
	 $MobileNumber = $_GET['MobileNumber'];	 
	 $latitude = $_GET['latitude'];	 
	 $longitude = $_GET['longitude'];
	 // include db connect class
	 require_once __DIR__ . '/connect.php';
	 // connecting to db 
	 $db = new DB_CONNECT();
	 // mysql inserting a new row 
	 $result = mysql_query("INSERT INTO GPSLocation(MobileNumber,latitude,longitude) VALUES('$MobileNumber','$latitude','$longitude')");
	 // check if row inserted or not
	 if ($result) 
	 {
		 // successfully inserted into database
		 $response["success"] = 1;
		 $response["message"] = "new Mobile Number saved...."; 
		 // echoing JSON response
		 echo json_encode($response);
	 }
	 else 
	 { 
		 // failed to insert row
		 $response["success"] = 0;
		 $response["message"] = "Oops! An error occurred.";
		 // echoing JSON response
		 echo json_encode($response); 
	 }
} 
	else 
	{
	 // required field is missing
	 $response["success"] = 0;
	 $response["message"] = "Required field(s) is missing"; 
	 // echoing JSON response
	 echo json_encode($response);
	}
?>