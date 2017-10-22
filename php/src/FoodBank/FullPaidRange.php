<?php
require "connectiontest.php";

// $name="BISTRO-C";
// $date1="2017-10-8";
// $date2="2017-10-18";

$date1=$_POST["date1"];
$date2=$_POST["date2"];
$name=$_POST["name"];

$sql="SELECT DATE('$date1') AS odate";
$result=mysqli_query($conn,$sql);
$row=$result->fetch_assoc();
$dateid1=$row['odate'];

$sql="SELECT DATE('$date2') AS odate";
$result=mysqli_query($conn,$sql);
$row=$result->fetch_assoc();
$dateid2=$row['odate'];

$mysql_qry="SELECT * from restaurant where name like '$name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }

$mysql_qry3="SELECT * FROM foodorder WHERE DATE(deliverydate) >= '$dateid1' AND DATE(deliverydate) <= '$dateid2' AND ispaid like '1' and restaurantid like '$id'";
$result3=mysqli_query($conn,$mysql_qry3);

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
