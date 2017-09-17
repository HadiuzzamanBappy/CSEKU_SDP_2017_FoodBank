<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$name=$_POST["name"];
$type=$_POST["type"];

// $name="BISTRO-C";

$mysql_qry="SELECT * from restaurant where name like '$name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc())
     $id=$row['id'];

$mysql_qry2="SELECT * from staffdetails where restaurantid like '$id'";
$result2=mysqli_query($conn,$mysql_qry2);

$mysql_qry3="SELECT * from staffrole";
$result3=mysqli_query($conn,$mysql_qry3);

$response=array();

if($type=="A"){
while($row2=$result2->fetch_assoc()){
    while($row3=$result3->fetch_assoc()){
        if($row2['roletype']==$row3['id'] && $row2['activerole']=='1'){
            array_push($response,array("name"=>$row2['name'],"type"=>$row3['role'],"activerole"=>"Active"));
        }
    }
 $result3=mysqli_query($conn,$mysql_qry3);
 }

 echo json_encode(array("Server_response"=>$response));
 }
 else {
   while($row2=$result2->fetch_assoc()){
       while($row3=$result3->fetch_assoc()){
           if($row2['roletype']==$row3['id'] && $row2['activerole']=='0'){
               array_push($response,array("name"=>$row2['name'],"type"=>$row3['role'],"activerole"=>"Inactive"));
           }
       }
    $result3=mysqli_query($conn,$mysql_qry3);
    }

    echo json_encode(array("Server_response"=>$response));
 }

 mysqli_close($conn);

?>
