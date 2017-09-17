<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

if($conn->connect_error)
{
    die('connection failed'.$conn->connect_error);
}
//$ftype="Spicy";
$mysql_qry="SELECT name,restaurantid FROM staffdetails";
$mysql_qry2="SELECT * from restaurant";
$result=mysqli_query($conn,$mysql_qry);
$result2=mysqli_query($conn,$mysql_qry2);
if($result->num_rows)
{
    while($row=$result->fetch_assoc()){
        echo '"name" :'.$row['name'].'"restaurent" :';
        while($row2=$result2->fetch_assoc())
        if($row['restaurantid']==$row2['id'])
        echo $row2['name'].'"password" :'.$row2['password']."<br>";
        $result2=mysqli_query($conn,$mysql_qry2);
    }
}
 ?>
