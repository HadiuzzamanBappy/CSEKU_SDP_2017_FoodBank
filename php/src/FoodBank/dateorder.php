<?php
require "connectiontest.php";

$type=$_POST["type"];
$name=$_POST["username"];
$res_name=$_POST["resname"];
$role=$_POST["role"];
$date=$_POST["date"];

// $type="A";
// $name="fahim";
// $res_name="BISTRO-C";
// $role="Staff";
// $date="2017-4-9";


$sql="SELECT DATE('$date') AS odate";
$result=mysqli_query($conn,$sql);
$row=$result->fetch_assoc();
$dateid=$row['odate'];

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }

if($type=="O"){
        $mysql_qry3="SELECT * FROM foodorder where DATE(OrderDate) like '$dateid' and restaurantid like '$id'";
}
else if($type=="D"){
    $mysql_qry3="SELECT * FROM foodorder where DATE(deliverydate) like '$dateid' and restaurantid like '$id'";
}
else {
    if($role=="Admin")
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id'";
    else
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '3'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id' and staffid like '$role'";
    }
}

 $result3=mysqli_query($conn,$mysql_qry3);

 $mysql_qry6="SELECT * FROM staffdetails";
 $result6=mysqli_query($conn,$mysql_qry6);

 $result3=mysqli_query($conn,$mysql_qry3);

 $response=array();

while($row3=$result3->fetch_assoc()){
    while($row6=$result6->fetch_assoc()){
        if($row3['staffid']==$row6['id'])
        {
            $staffrole=$row6['name'];
            if($row3['ispaid']=="0" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
            }
            else if($row3['ispaid']=="0" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
            }
        }
    }
    $result6=mysqli_query($conn,$mysql_qry6);
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

?>
