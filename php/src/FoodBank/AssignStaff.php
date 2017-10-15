<?php
require "connectiontest.php";

// $name="foysal";
// $price="160";
// $quantity="2";
// $phone="01932089409";
// $address="gollamari";
// $staff="fahim";

$name=$_POST["username"];
$price=$_POST["price"];
$phone=$_POST["phone"];
$address=$_POST["address"];
$staff=$_POST["staff"];

$mysql_qry3="SELECT * from staffdetails where name like '$staff' and roletype like '3'";
$result3=mysqli_query($conn,$mysql_qry3);
$row3=$result3->fetch_assoc();
$id3=$row3['id'];

$mysql_qry="SELECT * from foodorder where name  like '$name' and phonenumber like '$phone' and orderfrom like '$address' and price like '$price'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc())
	$id=$row['id'];

    $mysql_qry="UPDATE foodorder SET staffid=$id3 where id like '$id'";
        if($result=mysqli_query($conn,$mysql_qry)){
            echo "Assign Successed!!";
            }
 ?>
