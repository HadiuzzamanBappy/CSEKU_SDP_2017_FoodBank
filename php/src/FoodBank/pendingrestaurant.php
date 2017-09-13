<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

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

$sql2="SELECT * FROM staffdetails";

$result=mysqli_query($conn,$sql);
$result2=mysqli_query($conn,$sql2);

$response=array();

while($row=mysqli_fetch_array($result))
{
    while($row2=mysqli_fetch_array($result2)){
        if($row['admin']==$row2['id']){
        array_push($response,array
            (
            "name"=>$row[1],
            "town"=>$row[2],
            "street"=>$row[3],
            "phone"=>$row[5],
            "type"=>$row[4],
            "admin"=>$row2['name']
        ));
        }
    }
    $result2=mysqli_query($conn,$sql2);
}

echo json_encode(array("Server_response"=>$response));


mysqli_close($conn);
 ?>
