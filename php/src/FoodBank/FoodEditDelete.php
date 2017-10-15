<?php
require "connectiontest.php";

$pertype=$_POST["pertype"];
$res_name=$_POST["resname"];
$name=$_POST["name"];
$type=$_POST["type"];
$price=$_POST["price"];
$prevname=$_POST["prevname"];

// $pertype="Edit";
// $res_name="BISTRO-C";
// $name="Ice Boil";
// $type="Desert";
// $price="129";
// $prevname="Ice Cream";

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc())
        $id=$row['id'];

if($pertype=="Delete")
{
  $mysql_qry1="SELECT * from fooditems where name like '$name'";
  $result1=mysqli_query($conn,$mysql_qry1);
      if($row1=$result1->fetch_assoc())
          $id1=$row1['id'];

  $mysql_qry2="DELETE FROM  restaurantfood WHERE restaurantid like '$id' and foodid like '$id1'";
      if($result2=mysqli_query($conn,$mysql_qry2))
          echo "Successfully deleted!! Re login to confirm this...";
}
else {
  $mysql_qry3="SELECT * from fooditems where name like '$name' and type like '$type'";
  $result3=mysqli_query($conn,$mysql_qry3);
    if($row3=$result3->fetch_assoc()){
      $id3=$row3['id'];

  $mysql_qry4="SELECT * from restaurantfood where foodid like '$id3' and restaurantid like '$id'";
  $result4=mysqli_query($conn,$mysql_qry4);
    if($row4=$result4->fetch_assoc()){
      $id4=$row4['id'];
    }

  $mysql_qry5="UPDATE restaurantfood SET foodprice = '$price' WHERE id like '$id4'";
      if ($conn->query($mysql_qry5) === TRUE){
        echo "Record updated successfully->price";
      }
  }
  else
  {
  $mysql_qry6="INSERT into fooditems(name,type)
          values('$name','$type')";
  $result6=mysqli_query($conn,$mysql_qry6);

    $result7=mysqli_query($conn,$mysql_qry3);
        if($row7=$result7->fetch_assoc())
            $id7=$row7['id'];

    $mysql_qry8="SELECT * from fooditems where name like '$prevname'";
    $result8=mysqli_query($conn,$mysql_qry8);
      if($row8=$result8->fetch_assoc())
          $id8=$row8['id'];

  $mysql_qry9="SELECT * from restaurantfood where foodid like '$id8' and restaurantid like '$id'";
  $result9=mysqli_query($conn,$mysql_qry9);
        if($row9=$result9->fetch_assoc())
            $id9=$row9['id'];

  $mysql_qry10="UPDATE restaurantfood SET foodid = '$id7', foodprice = '$price' WHERE id= '$id9'";
  if ($conn->query($mysql_qry10) === TRUE)
    echo "Record updated successfully(All)";
}
}

?>
