<?php
require "connectiontest.php";

$type=$_POST["type"];
$name=$_POST["username"];
$res_name=$_POST["resname"];
$role=$_POST["role"];

// $type="Approve";
// $name="bappy";
// $res_name="BISTRO-C";
// $role="Admin";

$mysql_qry2="SELECT * from restaurant where name like '$res_name'";
$result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc()){
        $id2=$row2['id'];
        $id3=$row2['activity'];
        $id5=$row2['admin'];
    }

$mysql_qry3="SELECT id FROM staffrole where role like '$role'";
$result3=mysqli_query($conn,$mysql_qry3);
    if($row3=$result3->fetch_assoc())
        $id4=$row3['id'];

if($type=="Approve"){
    if($id3<>"0"){
$mysql_qry="SELECT id,activerole FROM staffdetails where name like '$name' and restaurantid like '$id2' and roletype like '$id4'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
    if($row['activerole']=="0"){
    $id=$row['id'];
    $mysql_qry="UPDATE staffdetails SET activerole='1' where id like '$id'";
        if($result=mysqli_query($conn,$mysql_qry))
            echo "Approvement Successed!!";
        }
    else
    echo "already registered";
 }
 }
 else {
     echo "This Person Restaurant is not Active";
 }
}
else if($type=="Delete")
{
    $mysql_qry="SELECT id,activerole FROM staffdetails where name like '$name' and restaurantid like '$id2' and roletype like '$id4'";
    $result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc()){
    if($row['id']==$id5)
    {
        echo "Will not Delete Because This Is Main Admin";
    }
    else {
    $mysql_qry="DELETE FROM staffdetails WHERE name like '$name' and restaurantid like '$id2' and roletype like '$id4'";
        if($result=mysqli_query($conn,$mysql_qry))
            echo "Successfully deleted!!";
    }
}
}

?>
