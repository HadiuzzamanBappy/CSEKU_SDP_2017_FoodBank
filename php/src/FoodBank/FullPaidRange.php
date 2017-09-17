<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

// $name="BISTRO-C";
// $date1="2017-4-8";
// $date2="2017-4-11";

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

 $mysql_qry2="SELECT * from foodorder where restaurantid like '$id' and staffid not like '0'";
 $result2=mysqli_query($conn,$mysql_qry2);

$mysql_qry3="SELECT * FROM orderdetails WHERE DATE(deliverydate) >= '$dateid1' AND DATE(deliverydate) <= '$dateid2' AND ispaid like '1'";
$result3=mysqli_query($conn,$mysql_qry3);

$mysql_qry4="SELECT * from restaurantfood";
$result4=mysqli_query($conn,$mysql_qry4);

$mysql_qry5="SELECT * from fooditems";
$result5=mysqli_query($conn,$mysql_qry5);

$mysql_qry6="SELECT * FROM staffdetails";
$result6=mysqli_query($conn,$mysql_qry6);

$response=array();

while($row2=$result2->fetch_assoc()){
    while($row3=$result3->fetch_assoc()){
        while($row4=$result4->fetch_assoc()){
            while($row5=$result5->fetch_assoc()){
                if($row2['orderid']==$row3['id']){
                    if($row2['restaurantfoodid']==$row4['id']){
                        if($row4['foodid']==$row5['id']){
                            while($row6=$result6->fetch_assoc()){
                                if($row2['staffid']==$row6['id'])
                                {
                                    $staffrole=$row6['name'];
                                }
                            }
                            if($row3['ispaid']=="1" && $row3['ishomedelivery']=="0"){
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
                    }
                }
            }
            $result5=mysqli_query($conn,$mysql_qry5);
        }
        $result4=mysqli_query($conn,$mysql_qry4);
    }
    $result3=mysqli_query($conn,$mysql_qry3);
}

echo json_encode(array("Server_response"=>$response));

mysqli_close($conn);

?>
