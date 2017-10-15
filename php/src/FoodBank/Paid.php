<?php
require "connectiontest.php";

$name="foysal";
$price="160";
$phone="01932089409";
$address="gollamari";
$orderdate="2017-04-08";

// $name=$_POST["username"];
// $price=$_POST["price"];
// $phone=$_POST["phone"];
// $address=$_POST["address"];
// $deliveryrdate=$_POST["deliveryrdate"];


$mysql_qry="SELECT * from foodorder where name  like '$name' and phonenumber like '$phone' and orderfrom like '$address' and price like '$price'";
$result=mysqli_query($conn,$mysql_qry);
$row=$result->fetch_assoc();
$id=$row['id'];

if($row['ispaid'] =='0'){
    $mysql_qry="UPDATE foodorder SET ispaid='1' where id like '$id'";
        if($result=mysqli_query($conn,$mysql_qry))
            echo "Payment Successed!!";
    }
else{
    echo "Already Paid";
    }
 ?>
