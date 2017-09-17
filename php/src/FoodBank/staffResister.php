<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$name=$_POST["username"];
$res_name=$_POST["restaurent"];
$respass=$_POST["respass"];
$pass=$_POST["pass"];
$role=$_POST["role"];

// $name="f";
// $res_name="No";
// $respass="No";
// $pass="f";
// $role="User";

if($role=='User')
{
    $mysql_qry3="INSERT into staffdetails(name,activerole,roletype,password)
                        values('$name','1','1','$pass')";
     if($result=mysqli_query($conn,$mysql_qry3))
        echo 'Your Registration Request As User Approved..plz Re-login';
}
else
{
$mysql_qry2="SELECT * from restaurant where name like '$res_name' and password like '$respass'";
$result2=mysqli_query($conn,$mysql_qry2);

if($role=="Admin")
            $id4="2";
            else
            $id4="3";

$mysql_qry4="SELECT * from staffdetails where name like '$name' and password like '$pass'";
$result4=mysqli_query($conn,$mysql_qry4);
if($row=$result4->fetch_assoc()){
    if($row2=$result2->fetch_assoc()){
        if($row['restaurantid']==$row2['id'] && $row['roletype']==$id4){
            if($row['activerole']=="0")
                echo "you have already Registrate but pending";
            else
                echo "you have already Registrate and its certified";
        }
        else
            {
            $result2=mysqli_query($conn,$mysql_qry2);
            if($row2=$result2->fetch_assoc()){
                $id3=$row2['id'];
                $mysql_qry3="INSERT into staffdetails(name,restaurantid,activerole,roletype,password)
                        values('$name','$id3','0','$id4','$pass')";
                if($result=mysqli_query($conn,$mysql_qry3))
                    echo "Your Registration Request Has Been Sent To Admin For Approval";
                }
            }
        }
    else
        echo "Wrong Information";
}
else
{
$result2=mysqli_query($conn,$mysql_qry2);
if($row2=$result2->fetch_assoc()){
    $id3=$row2['id'];
    if($role=="Admin")
    $id4="2";
    else
    $id4="3";
    $mysql_qry3="INSERT into staffdetails(name,restaurantid,activerole,roletype,password)
            values('$name','$id3','0','$id4','$pass')";
    if($result=mysqli_query($conn,$mysql_qry3))
        echo "Your Registration Request Has Been Sent To Admin For Approval";
    }
}
}

mysqli_close($conn);
?>
