<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$type=$_POST["type"];
$res_name=$_POST["resname"];
$res_name2=$_POST["resname2"];

// $type="Merge";
// $res_name="FFF";
// $res_name2="BISTRO-C";

$mysql_qry2="SELECT * from restaurant where name like '$res_name'";
$result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc()){
        $id2=$row2['id'];
        $id3=$row2['activity'];
        $id5=$row2['admin'];

$mysql_qry3="SELECT * from restaurant where name like '$res_name2'";
$result3=mysqli_query($conn,$mysql_qry3);
        if($row3=$result3->fetch_assoc()){
            $id7=$row3['id'];
        }

if($type=="Add"){
    if($id3=="0")
    {
        $mysql_qry1="UPDATE staffdetails SET activerole='1' where id like '$id5'";
        $mysql_qry="UPDATE restaurant SET activity='1' where id like '$id2'";
        if($result=mysqli_query($conn,$mysql_qry))
            if($result1=mysqli_query($conn,$mysql_qry1))
                echo "Approvement Successed!! Re login to confirm this...";
    }
    else
    echo "Already Approved";
}
else if($type=="Delete")
{
    $mysql_qry1="DELETE FROM staffdetails WHERE id like '$id5'";
    $mysql_qry="DELETE FROM restaurant WHERE id like '$id2'";
        if($result=mysqli_query($conn,$mysql_qry1))
            if($result=mysqli_query($conn,$mysql_qry))
                echo "Successfully deleted!! Re login to confirm this...";
}
else if($type=="Merge")
{
$mysql_qry="UPDATE staffdetails SET restaurantid='$id7' where id like '$id5'";
$mysql_qry3="UPDATE staffdetails SET activerole='1' where id like '$id5'";
$mysql_qry2="DELETE FROM restaurant WHERE id like '$id2'";
if($result=mysqli_query($conn,$mysql_qry))
    if($result2=mysqli_query($conn,$mysql_qry2))
        if($result3=mysqli_query($conn,$mysql_qry3))
            echo "Successfully Merged!! Re login to confirm this...";
}
}
else {
    if($type=="Delete")
        echo "Already Deleted";
    else
        echo "Already Merged";
}
?>
