<?php
require "connectiontest.php";

$op_type=$_POST["op_type"];
$oldname=$_POST["oldname"];
$newname=$_POST["newname"];
$res_name=$_POST["res_name"];
$role=$_POST["role"];
$oldpass=$_POST["oldpass"];
$newpass=$_POST["newpass"];

// $op_type="Delete";
// $oldname="lotifmal";
// $newname="lotifmal";
// $res_name="BISTRO-C";
// $role="Staff";
// $oldpass="baby";
// $newpass="baby";

$mysql_qry1="SELECT id from staffrole where role like '$role'";
$result1=mysqli_query($conn,$mysql_qry1);
if($row1=$result1->fetch_assoc())
    $id1=$row1['id'];

if($op_type=='Edit')
{
	if($res_name=='None')
	{
		$mysql_qry2="SELECT * FROM staffdetails where name like '$oldname' and password like '$oldpass' and roletype like '$id1'";
    	$result2=mysqli_query($conn,$mysql_qry2);
   	 	if($row2=$result2->fetch_assoc())
   	 		$id7=$row2['id'];

   	 	$mysql_qry10="UPDATE staffdetails SET name = '$newname', password = '$newpass' WHERE id= '$id7'";
  			if ($conn->query($mysql_qry10) === TRUE)
    			echo "Record updated successfully(All)";
	}
	else
	{
	$mysql_qry="SELECT * from restaurant where name like '$res_name'";
	$result=mysqli_query($conn,$mysql_qry);
	if($row=$result->fetch_assoc())
    	 $id3=$row['id'];
    $mysql_qry3="SELECT * FROM staffdetails where name like '$oldname' and password like '$oldpass' and roletype like '$id1' and restaurantid like '$id3'";
    	$result3=mysqli_query($conn,$mysql_qry3);
   	 	if($row3=$result3->fetch_assoc())
   	 		$id2=$row3['id'];

   	 	$mysql_qry10="UPDATE staffdetails SET name = '$newname', password = '$newpass' WHERE id= '$id2'";
  			if ($conn->query($mysql_qry10) === TRUE)
    			echo "Record updated successfully";    }
}
else
{
	if($res_name=='None')
	{
		$mysql_qry2="SELECT * FROM staffdetails where name like '$oldname' and password like '$oldpass' and roletype like '$id1'";
    	$result2=mysqli_query($conn,$mysql_qry2);
   	 	if($row2=$result2->fetch_assoc())
   	 		$id8=$row2['id'];

   	 	$mysql_qry10="DELETE FROM staffdetails WHERE id= '$id8'";
  			if ($conn->query($mysql_qry10) === TRUE)
    			echo "Record Deleted successfully(All)";
	}
	else
	{
		$mysql_qry="SELECT * from restaurant where name like '$res_name'";
		$result=mysqli_query($conn,$mysql_qry);
			if($row=$result->fetch_assoc())
    			 $id4=$row['id'];

    	$mysql_qry3="SELECT * FROM staffdetails where name like '$oldname' and password like '$oldpass' and roletype like '$id1' and restaurantid like '$id4'";
    	$result3=mysqli_query($conn,$mysql_qry3);
   	 	if($row3=$result3->fetch_assoc())
   	 		$id2=$row3['id'];

   	 	$mysql_qry10="DELETE FROM staffdetails WHERE id= '$id2'";
  			if ($conn->query($mysql_qry10) === TRUE)
    			echo "Record Deleted successfully";
	}
}

?>