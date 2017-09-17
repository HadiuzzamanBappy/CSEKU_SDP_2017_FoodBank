<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$resname=$_POST["name"];

// $resname="BISTRO-C";

$mysql_qry2="SELECT * from restaurant where name like '$resname'";
$result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc())
        $id3=$row2['id'];

$id="0";
$id2="3";


$mysql_qry="SELECT name FROM staffdetails where activerole like '$id' and roletype like '$id2' and restaurantid like $id3";

$response=array();

$result=mysqli_query($conn,$mysql_qry);

while($row=mysqli_fetch_array($result))
{
    array_push($response,array("name"=>$row[0]));
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);
 ?>
