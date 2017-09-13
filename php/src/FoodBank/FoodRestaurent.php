<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$food_name=$_POST["name"];
// $food_name="Chicken Grill";

$mysql_qry="SELECT * from fooditems where name like '$food_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $GLOBALS['id']=$row['id'];
 }

$mysql_qry2="SELECT * from restaurantfood where Foodid like $id";
$result2=mysqli_query($conn,$mysql_qry2);


$mysql_qry3="SELECT * from restaurant";
$result3=mysqli_query($conn,$mysql_qry3);

$response=array();

while($row3=$result3->fetch_assoc()){
    while($row2=$result2->fetch_assoc()){
        if($row2['restaurantid']==$row3['id']){
            array_push($response,array("name"=>$row3['name'],"foodprice"=>$row2['foodprice']));
        }
    }
 $result2=mysqli_query($conn,$mysql_qry2);
 }

 echo json_encode(array("Server_response"=>$response));

 mysqli_close($conn);
?>
