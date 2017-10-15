<?php
require "connectiontest.php";

$type=$_POST["type"];
$name=$_POST["username"];
$res_name=$_POST["resname"];
$role=$_POST["role"];

// $type="Delete";
// $name="fahi";
// $res_name="BISTRO-C";
// $role="Staff";

$mysql_qry2="SELECT * from restaurant where name like '$res_name'";
$result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc()){
        $id2=$row2['id'];
    }

$mysql_qry3="SELECT id FROM staffrole where role like '$role'";
$result3=mysqli_query($conn,$mysql_qry3);
    if($row3=$result3->fetch_assoc())
        $id4=$row3['id'];
        
if($type=="Approve"){
$mysql_qry="SELECT * FROM staffdetails where name like '$name' and restaurantid like '$id2'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
    if($row['activerole']=="0"){
    $id=$row['id'];
    $mysql_qry="UPDATE staffdetails SET activerole='1' where id like '$id'";
        if($result=mysqli_query($conn,$mysql_qry))
            echo "Approvement Successed!! Re login to confirm this...";
        }
    else
        echo "already registered";
 }
}
else if($type=="Delete")
{
    $mysql_qry="SELECT id FROM staffdetails where name like '$name' and restaurantid like '$id2'";
    $result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc()){
    $mysql_qry="DELETE FROM staffdetails WHERE name like '$name' and restaurantid like '$id2'";
        if($result=mysqli_query($conn,$mysql_qry))
            echo "Successfully deleted!! Re login to confirm this...";
    }
}

?>
