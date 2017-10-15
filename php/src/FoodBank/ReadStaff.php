<?php
require "connectiontest.php";

$res_name=$_POST["res"];
// $res_name="BISTRO-C";

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }

$sql="SELECT name from staffdetails WHERE roletype like '3' and restaurantid like '$id' and activerole like '1'";


$result=mysqli_query($conn,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
    array_push($response,array("name"=>$row[0]));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);
 ?>
