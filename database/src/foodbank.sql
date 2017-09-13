-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 17, 2017 at 08:30 AM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foodbank`
--

-- --------------------------------------------------------

--
-- Table structure for table `fooditems`
--

CREATE TABLE `fooditems` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Dumping data for table `fooditems`
--

INSERT INTO `fooditems` (`id`, `name`, `type`) VALUES
(1, 'Chicken Pizza', 'Spicy'),
(2, 'Ham Burger', 'spicy'),
(3, 'Lacchi', 'Desert'),
(5, 'Black Coffee', 'Drinks'),
(6, 'Ice Cream', 'Desert'),
(7, 'Chicken Swarma', 'Spicy'),
(8, 'Beef Burger', 'Spicy'),
(9, 'Biriyani', 'Meals'),
(10, 'Coke', 'Soft Drinks'),
(11, 'Chicken Fry', 'Spicy'),
(12, 'French Fry', 'Spicy'),
(13, 'Chicken Grill', 'Spicy'),
(14, 'Chawmin', 'Spicy'),
(15, 'Paratha', 'General'),
(17, 'Shik Kabab', 'Spicy'),
(18, 'Moglay', 'Spicy'),
(19, 'Beef(per piece)', 'Spicy'),
(20, 'Chicken(per piece)', 'Spicy'),
(21, 'Mutton(per piece)', 'Spicy'),
(22, 'Rice(Per Plate)', 'General');

-- --------------------------------------------------------

--
-- Table structure for table `foodorder`
--

CREATE TABLE `foodorder` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `restaurantid` int(11) NOT NULL,
  `restaurantfoodid` int(11) DEFAULT NULL,
  `orderid` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` varchar(20) NOT NULL,
  `staffid` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `foodorder`
--

INSERT INTO `foodorder` (`id`, `name`, `restaurantid`, `restaurantfoodid`, `orderid`, `quantity`, `price`, `staffid`) VALUES
(28, 'baji', 1, 2, 42, 2, '160', 10),
(29, 'riaz', 1, 6, 43, 3, '360', 10),
(30, 'lotif', 1, 6, 44, 3, '360', 10),
(31, 'arju', 1, 3, 45, 3, '180', 10),
(32, 'rakib', 1, 2, 46, 3, '240', 10),
(33, 'foysal', 1, 6, 47, 2, '240', 10);

-- --------------------------------------------------------

--
-- Table structure for table `orderdetails`
--

CREATE TABLE `orderdetails` (
  `id` int(11) NOT NULL,
  `OrderDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `ispaid` int(10) DEFAULT NULL,
  `phonenumber` varchar(20) DEFAULT NULL,
  `deliverydate` varchar(255) DEFAULT NULL,
  `ishomedelivery` tinyint(1) DEFAULT NULL,
  `orderfrom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orderdetails`
--

INSERT INTO `orderdetails` (`id`, `OrderDate`, `ispaid`, `phonenumber`, `deliverydate`, `ishomedelivery`, `orderfrom`) VALUES
(42, '2017-04-08 12:35:16', 1, '01985363836', '2017-04-08', 1, 'boira'),
(43, '2017-04-08 22:09:12', 1, '01975836645', '2017-04-10', 1, 'khora bosti, khulna'),
(44, '2017-04-10 19:38:53', 0, '01987564576', '2017-04-11', 1, 'khaba holer pisone'),
(45, '2017-04-10 23:10:45', 0, '01943567434', '2017-04-12', 1, 'gollamari,green road'),
(46, '2017-04-16 11:23:06', 1, '01973852628', '2017-04-17', 1, 'boira'),
(47, '2017-04-16 12:44:33', 1, '01963528352', '2017-04-18', 1, 'gollamari');

-- --------------------------------------------------------

--
-- Table structure for table `restaurant`
--

CREATE TABLE `restaurant` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `password` varchar(25) DEFAULT NULL,
  `activity` int(11) NOT NULL,
  `admin` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `restaurant`
--

INSERT INTO `restaurant` (`id`, `name`, `street`, `town`, `type`, `phone`, `password`, `activity`, `admin`) VALUES
(1, 'BISTRO-C', 'Royel Mor', 'Khulna', 'Cafe & Party', '+8801943567453', 'bistro', 1, 4),
(2, 'WE HUNGRY', 'Royel Mor', 'Khulna', 'Chinese & FastFood', '+8801674985357', 'hungry', 1, 5),
(3, 'MEJBAN BARI', 'Zero-Point,In Front of KU', 'Khulna', 'Regional Special Restaurant', '+8801765354767', 'mejban', 1, 6),
(4, 'hskb', 'bnb', 'gjb', 'gjbg', 'ghh', 'gjv', 0, 11);

-- --------------------------------------------------------

--
-- Table structure for table `restaurantfood`
--

CREATE TABLE `restaurantfood` (
  `id` int(11) NOT NULL,
  `restaurantid` int(11) DEFAULT NULL,
  `foodid` int(11) DEFAULT NULL,
  `foodprice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `restaurantfood`
--

INSERT INTO `restaurantfood` (`id`, `restaurantid`, `foodid`, `foodprice`) VALUES
(1, 1, 1, 250),
(2, 1, 2, 80),
(3, 1, 5, 60),
(4, 1, 6, 120),
(5, 1, 7, 105),
(6, 1, 13, 120),
(7, 1, 11, 90),
(8, 1, 12, 110),
(9, 2, 13, 90),
(10, 2, 17, 60),
(11, 2, 18, 40),
(12, 3, 19, 70),
(13, 3, 20, 65),
(14, 3, 21, 90),
(15, 3, 22, 10);

-- --------------------------------------------------------

--
-- Table structure for table `staffdetails`
--

CREATE TABLE `staffdetails` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `restaurantid` int(11) DEFAULT NULL,
  `activerole` int(10) NOT NULL,
  `roletype` int(11) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `staffdetails`
--

INSERT INTO `staffdetails` (`id`, `name`, `restaurantid`, `activerole`, `roletype`, `password`) VALUES
(4, 'bappy', 1, 1, 2, 'bappy'),
(5, 'aakash', 2, 1, 2, 'aakash'),
(6, 'siam', 3, 1, 2, 'siam'),
(10, 'fahim', 1, 1, 3, 'fahim'),
(11, 'hjg', 4, 0, 2, 'g');

-- --------------------------------------------------------

--
-- Table structure for table `staffrole`
--

CREATE TABLE `staffrole` (
  `id` int(11) NOT NULL,
  `role` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `staffrole`
--

INSERT INTO `staffrole` (`id`, `role`) VALUES
(2, 'Admin'),
(3, 'Staff');

-- --------------------------------------------------------

--
-- Table structure for table `superadmin`
--

CREATE TABLE `superadmin` (
  `id` int(11) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `superadmin`
--

INSERT INTO `superadmin` (`id`, `name`, `password`) VALUES
(1, 'restaurant', 'restaurant');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `fooditems`
--
ALTER TABLE `fooditems`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `foodorder`
--
ALTER TABLE `foodorder`
  ADD PRIMARY KEY (`id`),
  ADD KEY `restaurantfoodid` (`restaurantfoodid`),
  ADD KEY `orderid` (`orderid`);

--
-- Indexes for table `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `restaurant`
--
ALTER TABLE `restaurant`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `restaurantfood`
--
ALTER TABLE `restaurantfood`
  ADD PRIMARY KEY (`id`),
  ADD KEY `restaurantfood_ibfk_1` (`restaurantid`),
  ADD KEY `restaurantfood_ibfk_2` (`foodid`);

--
-- Indexes for table `staffdetails`
--
ALTER TABLE `staffdetails`
  ADD PRIMARY KEY (`id`),
  ADD KEY `restaurantid` (`restaurantid`),
  ADD KEY `roletype` (`roletype`);

--
-- Indexes for table `staffrole`
--
ALTER TABLE `staffrole`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `superadmin`
--
ALTER TABLE `superadmin`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `fooditems`
--
ALTER TABLE `fooditems`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
--
-- AUTO_INCREMENT for table `foodorder`
--
ALTER TABLE `foodorder`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;
--
-- AUTO_INCREMENT for table `orderdetails`
--
ALTER TABLE `orderdetails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;
--
-- AUTO_INCREMENT for table `restaurant`
--
ALTER TABLE `restaurant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `restaurantfood`
--
ALTER TABLE `restaurantfood`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `staffdetails`
--
ALTER TABLE `staffdetails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `staffrole`
--
ALTER TABLE `staffrole`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `superadmin`
--
ALTER TABLE `superadmin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `foodorder`
--
ALTER TABLE `foodorder`
  ADD CONSTRAINT `foodorder_ibfk_1` FOREIGN KEY (`restaurantfoodid`) REFERENCES `restaurantfood` (`id`),
  ADD CONSTRAINT `foodorder_ibfk_2` FOREIGN KEY (`orderid`) REFERENCES `orderdetails` (`id`);

--
-- Constraints for table `restaurantfood`
--
ALTER TABLE `restaurantfood`
  ADD CONSTRAINT `restaurantfood_ibfk_1` FOREIGN KEY (`restaurantid`) REFERENCES `restaurant` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `restaurantfood_ibfk_2` FOREIGN KEY (`foodid`) REFERENCES `fooditems` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `staffdetails`
--
ALTER TABLE `staffdetails`
  ADD CONSTRAINT `staffdetails_ibfk_1` FOREIGN KEY (`restaurantid`) REFERENCES `restaurant` (`id`),
  ADD CONSTRAINT `staffdetails_ibfk_2` FOREIGN KEY (`roletype`) REFERENCES `staffrole` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
