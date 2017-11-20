<?php
require "connectiontest.php";

$clientname=$_POST["clientname"];

//$clientname="sabbir";

$mysql_qry="SELECT * from foodorder where name like '$clientname'";
$result=mysqli_query($conn,$mysql_qry);

$response=array();

$number="1";

while($row3=$result->fetch_assoc()){
    if($row3['foodstate']=="0")
        $condition="Not Ready";
    else
        $condition="Ready";
     if($row3['ispaid']=="0" && $row3['ishomedelivery']=="0"){
                array_push($response,array("number"=>$number,"clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"condition"=>$condition));
            }
            else if($row3['ispaid']=="0" && $row3['ishomedelivery']=="1"){
                array_push($response,array("number"=>$number,"clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"condition"=>$condition));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                array_push($response,array("number"=>$number,"clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"condition"=>$condition));
            }
            else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                array_push($response,array("number"=>$number,"clientid"=>$row3['id'],"clientname"=>$row3['name'],"orderdate"=>$row3['orderdate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],"deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row3['price'],
                    "orderplace"=>$row3['orderfrom'],"condition"=>$condition));
            }
            $number=$number+1;
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

 ?>
