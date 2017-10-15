<?php

require "connectiontest.php";

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
