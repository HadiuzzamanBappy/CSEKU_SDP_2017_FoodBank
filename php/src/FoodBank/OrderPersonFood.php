<?php
require "connectiontest.php";

// $restaurantname="BISTRO-C";
// $clientid="3";
// $foodname="Ham Burger";
// $quantity="1";

$restaurantname=$_POST["restaurantname"];
$clientid=$_POST["clientid"];
$foodname=$_POST["foodname"];
$quantity=$_POST["quantity"];

$mysql_qry="SELECT id from restaurant where name like '$restaurantname'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
    $id=$row['id'];
    }

$mysql_qry2="SELECT id from fooditems where name like '$foodname'";
$result2=mysqli_query($conn,$mysql_qry2);
if($row2=$result2->fetch_assoc()){
    $id2=$row2['id'];
    }

$mysql_qry3="SELECT * from restaurantfood where restaurantid like '$id' and foodid like '$id2'";
$result3=mysqli_query($conn,$mysql_qry3);
if($row3=$result3->fetch_assoc()){
    $id3=$row3['id'];
    $price=$row3['foodprice'];
    }

$mysql_qry4="INSERT into orderdetails(orderid,restaurantfoodid,foodprice,quantity)
                                    values('$clientid','$id3','$price','$quantity')";
if($result4=mysqli_query($conn,$mysql_qry4))
    echo "true";
else
    echo "false";

mysqli_close($conn);
 ?>
