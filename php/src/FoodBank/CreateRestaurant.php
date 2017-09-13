<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$resname=$_POST["resname"];
$resstreet=$_POST["resstreet"];
$restown=$_POST["restown"];
$restype=$_POST["restype"];
$resphone=$_POST["resphone"];
$respass=$_POST["respass"];
$username=$_POST["username"];
$userpass=$_POST["userpass"];

// $resname="XX";
// $resstreet="ghfg";
// $restown="gvb";
// $restype="hgjh";
// $resphone="gjgj";
// $respass="jhhgh";
// $username="fahi";
// $userpass="fahi";

$mysql_qry2="SELECT * from restaurant where name like '$resname' and password like '$respass'";
$result2=mysqli_query($conn,$mysql_qry2);
if($row2=$result2->fetch_assoc())
{
    if($row2['activity']=="0")
        echo "you have already Registrate but pending";
    else
        echo "you have already Registrate and its certified";
    $id2='0';
}
else {
    $mysql_qry3="INSERT into restaurant(name,street,town,type,phone,password,activity)
            values('$resname','$resstreet','$restown','$restype','$resphone','$respass','0')";
    $result3=mysqli_query($conn,$mysql_qry3);

    $mysql_qry2="SELECT * from restaurant where name like '$resname' and password like '$respass'";
    $result2=mysqli_query($conn,$mysql_qry2);
    $row2=$result2->fetch_assoc();
        $id=$row2['id'];

    $mysql_qry4="INSERT into staffdetails(name,restaurantid,roletype,password)
            values('$username','$id','2','$userpass')";
    $result4=mysqli_query($conn,$mysql_qry4);

    $mysql_qry3="SELECT * from staffdetails where name like '$username' and password like '$userpass'";
    $result3=mysqli_query($conn,$mysql_qry3);
    if($row3=$result3->fetch_assoc())
        $id3=$row3['id'];

    $mysql_qry="UPDATE restaurant SET admin='$id3' where id like '$id'";
    if($result=mysqli_query($conn,$mysql_qry))
        echo "Your Registration Request Has Been Sent To Admin For Approval";
}
mysqli_close($conn);
?>
