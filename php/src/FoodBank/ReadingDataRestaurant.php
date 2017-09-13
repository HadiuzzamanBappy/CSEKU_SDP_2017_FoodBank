<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$name=$_POST["name"];
// $name="Pending";

if($name=='Pending')
{
    $sql="SELECT * FROM restaurant where activity like '0'";
}
else
{
    $sql="SELECT * FROM restaurant where activity like '1'";
}

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$result=mysqli_query($conn,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
        array_push($response,array
            (
            "name"=>$row[1],
            "town"=>$row[2],
            "street"=>$row[3],
            "phone"=>$row[5],
            "type"=>$row[4]
        ));
}

echo json_encode(array("Server_response"=>$response));


mysqli_close($conn);
 ?>
