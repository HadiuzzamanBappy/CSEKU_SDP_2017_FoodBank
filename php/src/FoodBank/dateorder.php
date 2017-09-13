<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

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
if($type=="O"){
$mysql_qry3="SELECT * FROM orderdetails where DATE(OrderDate) like '$dateid'";
}
else if($type=="D"){
$mysql_qry3="SELECT * FROM orderdetails where DATE(deliverydate) like '$dateid'";
}
else {
    $mysql_qry3="SELECT * FROM orderdetails";
}
$result3=mysqli_query($conn,$mysql_qry3);

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
     $id=$row['id'];
 }
if($role=="Admin"){
 $mysql_qry2="SELECT * from foodorder where restaurantid like '$id'";
 $result2=mysqli_query($conn,$mysql_qry2);
 }
 else {
     $mysql_qry6="SELECT id FROM staffdetails where name like '$name' and roletype like '3'";
     $result6=mysqli_query($conn,$mysql_qry6);
     if($row6=$result6->fetch_assoc())
     $role=$row6['id'];
     $mysql_qry2="SELECT * from foodorder where restaurantid like '$id' and staffid like '$role'";
     $result2=mysqli_query($conn,$mysql_qry2);
 }

 $mysql_qry4="SELECT * from restaurantfood";
 $result4=mysqli_query($conn,$mysql_qry4);

 $mysql_qry5="SELECT * from fooditems";
 $result5=mysqli_query($conn,$mysql_qry5);

 $mysql_qry6="SELECT * FROM staffdetails";
 $result6=mysqli_query($conn,$mysql_qry6);

 $response=array();

while($row2=$result2->fetch_assoc()){
    while($row3=$result3->fetch_assoc()){
        if($row2['orderid']==$row3['id']){
            while($row4=$result4->fetch_assoc()){
                while($row5=$result5->fetch_assoc()){
                    if($row2['restaurantfoodid']==$row4['id']){
                        if($row2['staffid']<>"0")
                        {
                            while($row6=$result6->fetch_assoc()){
                                if($row2['staffid']==$row6['id'])
                                {
                                    $staffrole=$row6['name'];
                                }
                            }
                        if($row4['foodid']==$row5['id']){
                            if($row3['ispaid']=="0" && $row3['ishomedelivery']=="0"){
                            array_push($response,array("clientname"=>$row2['name'],"foodname"=>$row5['name'],"quantity"=>$row2['quantity'],
                            "orderdate"=>$row3['OrderDate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],
                            "deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row4['foodprice'],
                            "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
                        }
                        else if($row3['ispaid']=="0" && $row3['ishomedelivery']=="1"){
                            array_push($response,array("clientname"=>$row2['name'],"foodname"=>$row5['name'],"quantity"=>$row2['quantity'],
                            "orderdate"=>$row3['OrderDate'],"ispaid"=>"Not Paid","phonenumber"=>$row3['phonenumber'],
                            "deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row4['foodprice'],
                            "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
                        }
                        else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
                            array_push($response,array("clientname"=>$row2['name'],"foodname"=>$row5['name'],"quantity"=>$row2['quantity'],
                            "orderdate"=>$row3['OrderDate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],
                            "deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Just Cook","price"=>$row4['foodprice'],
                            "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
                        }
                        else if($row3['ispaid']=="1" && $row3['ishomedelivery']=="1"){
                            array_push($response,array("clientname"=>$row2['name'],"foodname"=>$row5['name'],"quantity"=>$row2['quantity'],
                            "orderdate"=>$row3['OrderDate'],"ispaid"=>"Paid","phonenumber"=>$row3['phonenumber'],
                            "deliverydate"=>$row3['deliverydate'],"isdelivery"=>"Home Delivery","price"=>$row4['foodprice'],
                            "orderplace"=>$row3['orderfrom'],"staffrole"=>$staffrole));
                        }
                        }
                        $result6=mysqli_query($conn,$mysql_qry6);
                        }
                    }
                }
                $result5=mysqli_query($conn,$mysql_qry5);
            }
            $result4=mysqli_query($conn,$mysql_qry4);
        }
    }
    $result3=mysqli_query($conn,$mysql_qry3);
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

?>
