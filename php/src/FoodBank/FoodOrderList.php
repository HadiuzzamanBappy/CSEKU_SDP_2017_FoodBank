<?php
require "connectiontest.php";

$clientid=$_POST["clientid"];

//$clientid="9";

$mysql_qry="SELECT * from orderdetails where orderid like '$clientid'";
$result=mysqli_query($conn,$mysql_qry);

$response=array();

while($row=$result->fetch_assoc()){
    $restaurantfoodid=$row['restaurantfoodid'];
    $mysql_qry2="SELECT * from restaurantfood where id like '$restaurantfoodid'";
    $result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc())
        $foodid=$row2['foodid'];
    $mysql_qry3="SELECT * from fooditems where id like '$foodid'";
    $result3=mysqli_query($conn,$mysql_qry3);
    if($row3=$result3->fetch_assoc())
    {
        array_push($response,array("foodname"=>$row3['name'],"quantity"=>$row['quantity'],"price"=>$row['foodprice']));
    }
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

 ?>
