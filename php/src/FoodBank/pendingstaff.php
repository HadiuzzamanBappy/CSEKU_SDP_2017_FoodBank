<?php
require "connectiontest.php";

$resname=$_POST["name"];

// $resname="BISTRO-C";

$mysql_qry2="SELECT * from restaurant where name like '$resname'";
$result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc())
        $id3=$row2['id'];

$id="0";
$id2="3";
$id4="4";

$mysql_qry="SELECT * FROM staffdetails where activerole like '$id' and restaurantid like $id3";

$response=array();

$result=mysqli_query($conn,$mysql_qry);

while($row=mysqli_fetch_array($result))
{
	if($row['roletype']=="3" || $row['roletype']=="4")
    array_push($response,array("name"=>$row['name'],"typerole"=>$row['roletype']));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);
 ?>
