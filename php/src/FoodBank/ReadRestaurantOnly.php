<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

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
