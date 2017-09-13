<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

// $name="baji";
// $price="160";
// $quantity="2";
// $phone="01985363836";
// $address="boira";
// $staff="fahim";
$name=$_POST["username"];
$price=$_POST["price"];
$quantity=$_POST["quantity"];
$phone=$_POST["phone"];
$address=$_POST["address"];
$staff=$_POST["staff"];

$mysql_qry3="SELECT * from staffdetails where name like '$staff' and roletype like '3'";
$result3=mysqli_query($conn,$mysql_qry3);
$row3=$result3->fetch_assoc();
$id3=$row3['id'];

$mysql_qry2="SELECT * from orderdetails where phonenumber like '$phone' and orderfrom like '$address'";
$result2=mysqli_query($conn,$mysql_qry2);
$row2=$result2->fetch_assoc();
$id2=$row2['id'];

$mysql_qry="SELECT * from foodorder where name  like '$name' and quantity like '$quantity' and quantity like $quantity";
$result=mysqli_query($conn,$mysql_qry);
$row=$result->fetch_assoc();
$id=$row['orderid'];

if($id==$id2){
$mysql_qry4="SELECT * from foodorder where orderid like '$id'";
$result4=mysqli_query($conn,$mysql_qry4);
$row4=$result4->fetch_assoc();
// if($row4['staffid'] =='0'){
    $mysql_qry="UPDATE foodorder SET staffid=$id3 where orderid like '$id'";
        if($result=mysqli_query($conn,$mysql_qry)){
            echo "Assign Successed!!";
            }
//         }
// else{
//     echo "Already Assigned";
//     }
}
 ?>
