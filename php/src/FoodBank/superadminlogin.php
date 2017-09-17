<?php
$server_name="localhost";
$user_name="csekua5_feedme";
$password="Food%^238490232";
$database_name="csekua5_feedme";

$conn=new mysqli($server_name,$user_name,$password,$database_name);

$name=$_POST["username"];
$pass=$_POST["password"];

// $name="restaurant";
// $pass="restaurant";

$mysql_qry="SELECT * FROM superadmin where name like '$name' and password like '$pass'";
$result=mysqli_query($conn,$mysql_qry);

if($result->num_rows)
    echo "true";
else
    echo "Wrong Information... Please Input correct USERNAME(exact) And PASSWORD";

mysqli_close($conn);
?>
