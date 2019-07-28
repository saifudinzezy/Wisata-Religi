-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 28, 2019 at 08:02 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `imas`
--

-- --------------------------------------------------------

--
-- Table structure for table `acara`
--

CREATE TABLE `acara` (
  `id_acara` int(11) NOT NULL,
  `id_wisata` int(11) NOT NULL DEFAULT '0',
  `nama` varchar(50) NOT NULL,
  `desk` text NOT NULL,
  `pembicara` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `duror` text NOT NULL,
  `tanggal` date NOT NULL,
  `waktu` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `acara`
--

INSERT INTO `acara` (`id_acara`, `id_wisata`, `nama`, `desk`, `pembicara`, `alamat`, `duror`, `tanggal`, `waktu`) VALUES
(1, 3, 'Pengajian Khoul', 'Khoul Makam Kayugeritan', 'ust. al jalaludin baikhaqi', 'kajen', 'almursidin', '2019-09-09', '09:09');

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `nama`, `email`, `password`) VALUES
(1, 'Bapedda', 'admin@bappeda.co.id', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `galeri`
--

CREATE TABLE `galeri` (
  `id_galeri` int(11) NOT NULL,
  `id_wisata` int(11) NOT NULL,
  `nama_foto` varchar(50) DEFAULT NULL,
  `desk_foto` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `galeri`
--

INSERT INTO `galeri` (`id_galeri`, `id_wisata`, `nama_foto`, `desk_foto`) VALUES
(3, 3, 'ggelari.jpg', 'Di depan'),
(5, 14, 'gmbahgendon.JPG', 'didalam makam');

-- --------------------------------------------------------

--
-- Table structure for table `wisata`
--

CREATE TABLE `wisata` (
  `id_wisata` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` text NOT NULL,
  `desk` text NOT NULL,
  `latitude` varchar(50) NOT NULL,
  `longitude` varchar(50) NOT NULL,
  `foto` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wisata`
--

INSERT INTO `wisata` (`id_wisata`, `nama`, `alamat`, `desk`, `latitude`, `longitude`, `foto`) VALUES
(3, 'Makam Kayugeritan', 'Pekalongan', '<p>\r\nAl Habib Abu Bakar bin Yahya berasal dari daerah gorot bagian wilayah tarim hadramaut, dibawah asuhan ayahandanya  beliau menimpa ilmu-ilmu agama dan kemudian berguru pada ulama’ lain di daerahnya,\r\n termasuk salah satu gurunya adalah Qutbul Irsyad wa Ghoutsul Bilad Al Habib Abdullah bin Alwi Al Haddad.\r\n</p>\r\n<p>\r\nMakam Al Habib Abu Bakar bin Yahya terletak di pinggir jalan penghubung antara Kecamatan Wonopringgo dan Karanganyar, \r\n</p>\r\n<P>Sebagian besar para peziarah datang pada hari kamis malam jumat, karena pada hari itu biasanya Al Habib Muhammad Luthfi bin Yahya yang merupakan cucu dari Al Habib Abu Bakar bin Yahya juga datang untuk mengunjungi makam kakeknya/</p>', '-7.010564', '109.633468', 'kayugeritan.jpg'),
(14, 'Makam Mbah Gendon', 'kesesi', '<p>\r\nMbah Gendon yang diperkirakan hidup pada sekitar tahun 1868-1960 merupakan anak satu-satunya pasangan Tarab dan Takumi. Ia lahir di Desa Kesesi Kecamatan Kesesi, Pekalongan. Sejak kecil Mohammad Arshal dikenal sebagai sosok yang pendiam, mengalah dan pemaaf.\r\n</p>\r\n<p>\r\nAdapun waktu kecilnya Mbah Gendon sehari-hari menggembala ternak milik orang lain. Sampai remaja dan dewasa pun sifatnya tidak berubah. Bahkan malah semakin menjauhi duniawi.\r\n</p>\r\n<p>\r\nSampai pada akhirnya, kedua orang tua Mbah Gendon mengenalkannya kepada seorang perempuan sebagai pendamping hidupnya. Namun tidak seperti pernikahan pada umumnya, setelah menikah Mohammad Arshal bersama rombongan pengantar malah kembali ke rumah orang tuanya.\r\n</p>\r\n<p>\r\n. “Ternyata beliau (Mbah Gendon) belum memiliki keinginan untuk berumah tangga. Namun masih ingin memperdalam ilmu agama atau mondok,” ungkap ahli waris Makam Mbah Gendon, M Arifin. Sehingga, Mbah Gendon kemudian berpamitan kepada kedua orang tua serta sanak saudara untuk berangkat mondok ke Babakan Ciwaringin Cirebon Jawa Barat. Mondok di Kiai Munir.\r\n</p>', '-7.021355', '109.494883', 'gendon.jpg'),
(15, 'Makam Siti Ambariyah', 'Pekalongan', '<p>\r\nDi Pekalongan, kita mengenal nama Siti Ambariyah yang makamnya terletak di desa Bukur, kecamatan Bojong, masyarakat sekitar menyebutnya Ibu Agung Siti Fatimah Ambariyah. Siti Ambariyah ialah seorang putri dari Ki Ageng Rogoselo, seorang wali, ulama, dan pejuang nusantara yang makamnya berada di Desa Rogoselo Kecamatan Doro. \r\n</p>\r\n<p>\r\nsebuah kisah yang berdasar dari cerita turun temurun menuturkan, bahwa Ki Penatas Angin, salah satu Pangeran dari Mataram Islam diutus oleh Sultan Agung untuk tapa brata dan belajar kepada Ki Ageng Rogoselo (Ayahanda Siti Ambariyah).\r\n</p>\r\n<p>\r\nKi Penatas Angin belajar kepada Ki Ageng Rogoselo, bertujuan untuk memperluas ilmu Agama Islam sekaligus belajar ilmu kaluragan dan strategi untuk melawan penjajah (Belanda).\r\n</p>\r\n<p>\r\nSuatu hari, Ki Penatas Angin merasa katresnan dan ingin menikahi Siti Ambariyah (putri gurunya, Ki Ageng Rogoselo), namun siapa sangka Siti Ambariyah lebih dulu memilih untuk berjuang melawan penjajah sekaligus menyebarkan syiar Islam dan pengetahuan di wilayah lain, yaitu di daerah Bojong (Pekalongan bagian barat).\r\n</p>\r\n<p>\r\n“Lebih dulu memilih berjuang demi kedaulatan nusantara, pendidikan, dan syiar agama dari pada dinikahi oleh salah seorang Pangeran”, sebuah pilihan yang sangat bijak dari seorang wanita. Siti Ambariyah pun dikenang oleh masyarakat Pekalongan sebagai wanita sholehah, penuh ketegaran, welas asih dan perjuangan. Setiap tahun diadakan peringatan haul di makamnya di desa Bukur, Bojong, Pekalongan.\r\n</p>', '-7.005027', '109.551546', 'ambariah.jpg'),
(16, 'Makam Ki Gede Penatas Angin', 'Pekalongan', '<p>\r\nKI Atas Angin adalah salah satu murid kesayangan SUNAN KALI JOGO ( WALI SONGO) . KI ATAS ANGIN selalu di musuhin oleh orang belanda, salah satunya oleh BARON SEKEBER. Baron sekeber orang belanda yang juga sakti mandra guna. Dia mencintai istri Ki Atas Angin karena kecantikannya. Pada suatu ketika Baron Sekeber mengajak Ki Atas Angin untuk melihat pertunjukan wayang di mana dalangnya adalah Sunan Kali Jogo.\r\n</p>\r\n<p>\r\nTernyata Baron Sekeber mengajak Ki Atas Angin karena ada maksud di balik semua itu, dengan diam-diam Baron Sekeber pergi meninggalkan Ki Atas Angin dan Baron Sekeber membawa pergi istri Ki Atas Angin yang di tinggal di rumah sendirian. Karena Ki Atas Angin merasa di bohongi oleh Baron Sekeber lalu beliau minta bantuan ke Sunan Kali Jogo .\r\n</p>\r\n<p>\r\nKarena Sunan Kali Jogo adalah salah satu wali yang sangat berbudi luhur Beliaupun membantu Ki Atas Angin yang meminta bantuan ke padanya dengan cara Ki Atas Angin di suruh masuk ke layar pertunjukan wayang. Dan dengan sekejap mata Ki Atas Angin sampai di rumah. Setelah sampai di rumah betapa terkejutnya Beliau mendapati istrinya sudah tidak ada. Tanpa berpikir panjang Ki Atas Angin pergi mencari Baron Sekeber yang telah membawa kabur istrinya. Diapun terbang dan tidak lama kemudian dia melihat Baron Sekeber .\r\n</p>\r\n<p>\r\nLalu Ki Atas Angin terus mengejar ngejar Baron Sekeber. Merekapun saling mengadu kekuatan ,karena mereka sama sama sakti maka pertempuranpun berlangsung sangat lama. Mereka saling serang saling tangkis sampai mereka tidak tau kalau waktu sudah gelap.\r\n</p>\r\n<p>\r\nSampai beberapa lama mereka terus melakukan perkelahian. Tapi setelah mereka saling serang Ki Atas Angin yang memenangkan pertarungan dan baron sekeber pun terus di kejar kemanapun dia pergi. Sampai di suatu tempat Baron Sekeber masuk kedalam batu dan KI Atas Angin terus menunggu sampai Bron Sekeber muncul .Tapi karena sampai beberapa hari Baron Sekeber tidak muncul juga maka Ki Atas Angin pun menunggu sampai sekarang di dalam batu yang berada di depannya sampai sekarang.\r\n</p>', '-7.072354', '109.670829', 'natasangin.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `acara`
--
ALTER TABLE `acara`
  ADD PRIMARY KEY (`id_acara`);

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `galeri`
--
ALTER TABLE `galeri`
  ADD PRIMARY KEY (`id_galeri`);

--
-- Indexes for table `wisata`
--
ALTER TABLE `wisata`
  ADD PRIMARY KEY (`id_wisata`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `acara`
--
ALTER TABLE `acara`
  MODIFY `id_acara` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `galeri`
--
ALTER TABLE `galeri`
  MODIFY `id_galeri` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `wisata`
--
ALTER TABLE `wisata`
  MODIFY `id_wisata` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
