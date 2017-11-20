<?php
require "connectiontest.php";

// $name="BISTRO-C";
$name=$_POST["name"];

$mysql_qry="SELECT * from restaurant where name like '$name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }

$mysql_qry2="SELECT * from foodorder where restaurantid like '$id' and staffid not like '0'";
$result2=mysqli_query($conn,$mysql_qry2);

 $mysql_qry6="SELECT * FROM staffdetails";
 $result6=mysqli_query($conn,$mysql_qry6);

$response=array();

while($row3=$result2->fetch_assoc()){
    $staffid=$row3['staffid'];
    $chefid=$row3['chefid'];
     $mysql_qry6="SELECT * FROM staffdetails where id like '$staffid'";
     $result6=mysqli_query($conn,$mysql_qry6);
     if($row6=$result6->fetch_assoc())
         $staffrole=$row6['name'];
     else
        $staffrole="Not Set Yet";
    $mysql_qry6="SELECT * FROM staffdetails where id like '$chefid'";
     $result6=mysqli_query($conn,$mysql_qry6);
     if($row6=$result6->fetch_assoc())
         $chefrole=$row6['name'];

            if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],"orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],"orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole));
            }
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

 ?>
