<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

// $name="baji";
// $price="180";
// $quantity="2";
// $phone="01985363836";
// $address="boira";
// $orderdate="2017-04-08";

$name=$_POST["username"];
$price=$_POST["price"];
$quantity=$_POST["quantity"];
$phone=$_POST["phone"];
$address=$_POST["address"];
$orderdate=$_POST["orderdate"];

$mysql_qry2="SELECT * from orderdetails where phonenumber like '$phone' and orderfrom like '$address'";
$result2=mysqli_query($conn,$mysql_qry2);
$row2=$result2->fetch_assoc();
$id2=$row2['id'];

$mysql_qry="SELECT * from foodorder where name  like '$name' and quantity like '$quantity' and quantity like $quantity";
$result=mysqli_query($conn,$mysql_qry);
$row=$result->fetch_assoc();
$id=$row['orderid'];

if($id==$id2){
$mysql_qry2="SELECT * from orderdetails where id like '$id'";
$result2=mysqli_query($conn,$mysql_qry2);
$row2=$result2->fetch_assoc();
if($row2['ispaid'] =='0'){
    $mysql_qry="UPDATE orderdetails SET ispaid='1' where id like '$id'";
        if($result=mysqli_query($conn,$mysql_qry)){
            echo "Payment Successed!!";
            }
        }
else{
    echo "Already Paid";
    }
}
 ?>
