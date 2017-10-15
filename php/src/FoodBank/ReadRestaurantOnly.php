<?php
require "connectiontest.php";

$id="1";

$sql="SELECT name from restaurant WHERE activity like '$id'";

$result=mysqli_query($conn,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
    array_push($response,array("name"=>$row['name']));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

?>
