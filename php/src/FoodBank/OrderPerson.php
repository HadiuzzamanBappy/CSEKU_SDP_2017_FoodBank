<?php
require "connectiontest.php";


// $name="arju";
// $phone="01765987643";
// $deliverydate="2017-10-11";
// $delivery="YES";
// $address="Gollamary,khulna";
// $restaurant="BISTRO-C";
// $price="120";
// $paid=NULL;

$name=$_POST["clientname"];
$phone=$_POST["phonenumber"];
$deliverydate=$_POST["datetime"];
$address=$_POST["address"];
$delivery=$_POST["delivery"];
$restaurant=$_POST["restaurant"];
$price=$_POST["price"];
$paid=NULL;

if($delivery=="YES")
$delivery="1";
else
$delivery="0";

$mysql_qry="SELECT id from restaurant where name like '$restaurant'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
    $id2=$row['id'];
    }
    $mysql_qry5="INSERT into foodorder(name,phonenumber,orderfrom,deliverydate,restaurantid,ishomedelivery,price)
                               values('$name','$phone','$address','$deliverydate','$id2','$delivery','$price')";
    if($result5=mysqli_query($conn,$mysql_qry5))
    {
    $mysql_qry6="SELECT id from foodorder where name like '$name' and phonenumber like '$phone' and orderfrom like '$address' and restaurantid like '$id2' and price like '$price'";
    $result6=mysqli_query($conn,$mysql_qry6);
    if($row=$result6->fetch_assoc()){
        $id=$row['id'];
        }
    echo $id;
    }
    else
        echo "false";
    
    mysqli_close($conn);
 ?>
