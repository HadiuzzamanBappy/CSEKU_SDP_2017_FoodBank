<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);


// $name="arju";
// $phone="01765987643";
// $date="20-10-17";
// $quantity="2";
// $delivery="YES";
// $address="Gollamary,khulna";
// $restaurant="BISTRO-C";
// $price="120";
// $food="Chicken Fry";
// $paid=NULL;
$name=$_POST["clientname"];
$phone=$_POST["phonenumber"];
$date=$_POST["datetime"];
$quantity=$_POST["quantity"];
$address=$_POST["address"];
$delivery=$_POST["delivery"];
$restaurant=$_POST["restaurant"];
$price=$_POST["price"];
$food=$_POST["food"];
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

$mysql_qry2="SELECT id from fooditems where name like '$food'";
$result2=mysqli_query($conn,$mysql_qry2);
if($row=$result2->fetch_assoc()){
    $id3=$row['id'];
    }
$mysql_qry3="SELECT id from restaurantfood where foodid like '$id3' and restaurantid like '$id2'";
$result3=mysqli_query($conn,$mysql_qry3);
if($row=$result3->fetch_assoc()){
    $id4=$row['id'];
    }

$id5="0";
$mysql_qry4="SELECT * from foodorder where name like '$name'";
$result4=mysqli_query($conn,$mysql_qry4);
while($row=$result4->fetch_assoc()){
    if($row['restaurantid']==$id2 && $row['restaurantfoodid']==$id4){
        $id5="1";
        $quan=$row['quantity'];
    }
}
    if($id5=="1"){
        echo "you have already ordered this and the quantity was ".$quan." .";
    }
    else
    {
        $mysql_qry5="INSERT into orderdetails(ispaid,phonenumber,deliverydate,ishomedelivery,orderfrom)
                values('$paid','$phone','$date','$delivery','$address')";
        $result5=mysqli_query($conn,$mysql_qry5);

        $mysql_qry6="SELECT id from orderdetails where phonenumber like '$phone' and orderfrom like '$address'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row=$result6->fetch_assoc()){
            $id=$row['id'];
            }
        $mysql_qry7="INSERT into foodorder(name,restaurantid,restaurantfoodid,orderid,quantity,price)
                values('$name','$id2','$id4','$id','$quantity','$price')";
        if($result=mysqli_query($conn,$mysql_qry7)){
            echo "true";
            }
        else {
            echo "there is something problem..Plz try again.....later";
        }
    }
    mysqli_close($conn);
 ?>
