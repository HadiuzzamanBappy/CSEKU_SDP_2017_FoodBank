<?php
$server_name="localhost";
$user_name="root";
$password="";
$database_name="foodbank";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$name=$_POST["username"];
$res_name=$_POST["restaurent"];
$role=$_POST["role"];
$pass=$_POST["password"];

// $name="bappy";
// $res_name="BISTRO-C";
// $role="Admin";
// $pass="bappy";

$mysql_qry1="SELECT id from staffrole where role like '$role'";
$result=mysqli_query($conn,$mysql_qry1);
if($row=$result->fetch_assoc())
    $id=$row['id'];
if($res_name=='None')
{
    $mysql_qry2="SELECT * FROM staffdetails where name like '$name' and password like '$pass' and roletype like '$id'";
    $result2=mysqli_query($conn,$mysql_qry2);
    if($row2=$result2->fetch_assoc()){
            echo "true";
        }
    else{
        echo "no";
    }
}
else
{
    $mysql_qry4="SELECT activerole FROM staffdetails where name like '$name'";
    $result4=mysqli_query($conn,$mysql_qry4);
    if($row4=$result4->fetch_assoc())
        if($row4['activerole']=="0")
            echo "You Are Not Allowed Staff";
        else
        {
            $mysql_qry3="SELECT * from restaurant where name like '$res_name'";
            $result3=mysqli_query($conn,$mysql_qry3);
            if($row3=$result3->fetch_assoc())
                $id2=$row3['id'];
            $mysql_qry5="SELECT * FROM staffdetails where name like '$name' and password like '$pass' and roletype like '$id' and restaurantid like '$id2'";
            $result5=mysqli_query($conn,$mysql_qry5);
            if($row5=$result5->fetch_assoc())
                echo 'true';
            else
                echo 'false';
        }
    else
        echo 'false';
}

?>