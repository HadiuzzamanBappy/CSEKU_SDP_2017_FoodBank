<?php
require "connectiontest.php";

$name=$_POST["username"];
$pass=$_POST["password"];

// $name="restaurant";
// $pass="restaurant";

$mysql_qry="SELECT * FROM superadmin where name like '$name' and password like '$pass'";
$result=mysqli_query($conn,$mysql_qry);

if($row=$result->fetch_assoc())
    echo "true";
else
    echo "Wrong Information... Please Input correct USERNAME(exact) And PASSWORD";

mysqli_close($conn);
?>
