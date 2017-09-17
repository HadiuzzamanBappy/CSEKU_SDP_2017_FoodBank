<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$res_name=$_POST["name"];
// $res_name="WE HUNGRY";

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc())
     $id=$row['id'];


$mysql_qry2="SELECT * from restaurantfood where restaurantid like '$id'";
$result2=mysqli_query($conn,$mysql_qry2);


$mysql_qry3="SELECT * from fooditems";
$result3=mysqli_query($conn,$mysql_qry3);

$response=array();


while($row2=$result2->fetch_assoc()){
    while($row3=$result3->fetch_assoc()){
        if($row2['foodid']==$row3['id']){
            array_push($response,array("name"=>$row3['name'],"type"=>$row3['type'],"foodprice"=>$row2['foodprice']));
        }
    }
 $result3=mysqli_query($conn,$mysql_qry3);
 }

 echo json_encode(array("Server_response"=>$response));

 mysqli_close($conn);
?>
