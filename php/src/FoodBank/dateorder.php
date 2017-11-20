<?php
require "connectiontest.php";

$type=$_POST["type"];
$name=$_POST["username"];
$res_name=$_POST["resname"];
$role=$_POST["role"];
$date=$_POST["date"];

// $type="A";
// $name="lotif";
// $res_name="BISTRO-C";
// $role="Chef";
// $date="2017-10-11";


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
        if($role=="Admin")
        $mysql_qry3="SELECT * FROM foodorder where DATE(OrderDate) like '$dateid' and restaurantid like '$id'";
    else if($role=="Staff")
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '3'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where DATE(OrderDate) like '$dateid' and restaurantid like '$id' and staffid like '$role'";
    }
    else
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '4'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where DATE(OrderDate) like '$dateid' and restaurantid like '$id' and chefid like '$role'";
    }
}
else if($type=="D"){
    if($role=="Admin")
        $mysql_qry3="SELECT * FROM foodorder where DATE(deliverydate) like '$dateid' and restaurantid like '$id'";
    else if($role=="Staff")
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '3'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where DATE(deliverydate) like '$dateid' and restaurantid like '$id' and staffid like '$role'";
    }
    else
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '4'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where DATE(deliverydate) like '$dateid' and restaurantid like '$id' and chefid like '$role'";
    }
}
else if($type=="DN")
{
    $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '4'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id' and chefid like '$role' and foodstate like '1'";
}
else if($type=="UNDN")
{
    $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '4'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id' and chefid like '$role' and foodstate like '0'";
}
else {
    if($role=="Admin")
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id'";
    else if($role=="Staff")
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '3'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id' and staffid like '$role'";
    }
    else
    {
        $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '4'";
        $result6=mysqli_query($conn,$mysql_qry6);
        if($row6=$result6->fetch_assoc())
            $role=$row6['id'];
        $mysql_qry3="SELECT * FROM foodorder where restaurantid like '$id' and chefid like '$role'";
    }
}

 $result3=mysqli_query($conn,$mysql_qry3);

 $result3=mysqli_query($conn,$mysql_qry3);

 $response=array();

while($row3=$result3->fetch_assoc()){
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
     else
        $chefrole="Not Set Yet";

     if($row3['foodstate']=="0")
        $foodstate="Not Ready";
    else
        $foodstate="Ready";
            if($row3['ispaid']=="0" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole,"foodstate"=>$foodstate));
            }
            else if($row3['ispaid']=="0" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole,"foodstate"=>$foodstate));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole,"foodstate"=>$foodstate));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole,"chefname"=>$chefrole,"foodstate"=>$foodstate));
            }
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

?>
