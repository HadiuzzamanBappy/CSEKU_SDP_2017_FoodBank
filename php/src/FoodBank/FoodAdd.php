<?php
require "connectiontest.php";

$res_name=$_POST["restaurent"];
$food_name=$_POST["food"];
$price=$_POST["price"];
$type=$_POST["foodtype"];

// $res_name="BISTRO-C";
// $food_name="Chicken Meat";
// $price="85";
// $type="Spicy";

$mysql_qry="SELECT * from restaurant where name like '$res_name'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc())
     $id=$row['id'];

$mysql_qry2="SELECT * from restaurantfood where restaurantid like '$id'";
$result2=mysqli_query($conn,$mysql_qry2);

$mysql_qry3="SELECT * from fooditems";
$result3=mysqli_query($conn,$mysql_qry3);

$id2="0";

while($row2=$result2->fetch_assoc()){
    while($row3=$result3->fetch_assoc()){
        if($row2['foodid']==$row3['id'])
        {
            if(strcasecmp($row3['name'],$food_name)==0)
            $id2="1";
        }
    }
    $result3=mysqli_query($conn,$mysql_qry3);
}

if($id2=="1")
    echo "Already Existed in Your Restaurant";
else
    {
        $mysql_qry4="INSERT INTO fooditems(name,type)
                        values('$food_name','$type')";
        $result4=mysqli_query($conn,$mysql_qry4);

        $mysql_qry5="SELECT id from fooditems where name like '$food_name'";
        $result5=mysqli_query($conn,$mysql_qry5);
        if($row5=$result5->fetch_assoc())
        $id3=$row5['id'];

        $mysql_qry6="INSERT INTO restaurantfood(restaurantid,foodid,foodprice)
                        values('$id','$id3','$price')";
        if($result6=mysqli_query($conn,$mysql_qry6))
            echo "FoodItem Addition Successful";
    }

?>
