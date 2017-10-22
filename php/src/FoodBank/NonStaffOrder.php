<?php
require "connectiontest.php";

// $name="BISTRO-C";
// $person_type="chef";

$name=$_POST["name"];
$person_type=$_POST["type"];

$mysql_qry="SELECT * from restaurant where name like '$name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }

if($person_type=="staff")
$mysql_qry2="SELECT * from foodorder where restaurantid like '$id' and staffid like '0' and chefid not like '0'";
else
$mysql_qry2="SELECT * from foodorder where restaurantid like '$id' and staffid like '0' and chefid like '0'";

$result2=mysqli_query($conn,$mysql_qry2);

$response=array();

while($row3=$result2->fetch_assoc()){
    if($person_type=="staff"){
        $chefid=$row3['chefid'];
        $mysql_qry4="SELECT * from staffdetails where id like $chefid";
        $result4=mysqli_query($conn,$mysql_qry4);
        $row4=$result4->fetch_assoc();
        $chefname=$row4['name'];
    }
    else
    {
        $chefname="Not Set Yet";
    }
            if($row3['ispaid']=="0" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"name"=>$chefname));
            }
            else if($row3['ispaid']=="0" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"name"=>$chefname));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"name"=>$chefname));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                array_push($response,array("clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"name"=>$chefname));
            }
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

 ?>
