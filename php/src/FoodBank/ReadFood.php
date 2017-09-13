<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$sql="SELECT * from fooditems WHERE EXISTS(SELECT DISTINCT foodid from restaurantfood where fooditems.id=restaurantfood.foodid)";


$result=mysqli_query($conn,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
    array_push($response,array("name"=>$row[1],"type"=>$row[2]));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);
 ?>
