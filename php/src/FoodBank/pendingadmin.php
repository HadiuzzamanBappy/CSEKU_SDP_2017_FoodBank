<?php
require "connectiontest.php";

$name=$_POST["name"];
// $name="BISTRO-C";

$mysql_qry="SELECT * from restaurant where name like '$name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc())
     $id3=$row['id'];

$id="0";
$id2="2";

$mysql_qry="SELECT name FROM staffdetails where activerole like '$id' and roletype like '$id2' and restaurantid like '$id3'";

$response=array();

$result=mysqli_query($conn,$mysql_qry);

while($row=mysqli_fetch_array($result))
{
    array_push($response,array("name"=>$row[0]));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);
 ?>
