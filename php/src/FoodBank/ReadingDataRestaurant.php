<?php
require "connectiontest.php";

$name=$_POST["name"];
// $name="Pending";
// $name="BISTRO-C";

if($name=='Pending')
{
    $sql="SELECT * FROM restaurant where activity like '0'";
}
else
{
    $sql="SELECT * FROM restaurant where activity like '1'";
}

$result=mysqli_query($conn,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
        array_push($response,array
            (
            "name"=>$row[1],
            "town"=>$row[3],
            "street"=>$row[2],
            "phone"=>$row[5],
            "type"=>$row[4]
        ));
}

echo json_encode(array("Server_response"=>$response));


mysqli_close($conn);
 ?>
