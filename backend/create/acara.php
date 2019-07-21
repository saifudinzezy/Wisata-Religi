<?php
include '../koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $nama = $_POST['nama'];
    $desk = $_POST['desk'];
    $pembicara = $_POST['pembicara'];
    $alamat = $_POST['alamat'];
    $duror = $_POST['duror'];
    $tanggal = $_POST['tanggal'];
    $waktu = $_POST['waktu'];

    //query untuk menambahkan data
    $query = "INSERT INTO acara VALUES (null, '$id', '$nama', '$desk', '$pembicara', '$alamat', '$duror', '$tanggal', '$waktu')";

    $exeQuery = mysqli_query($con, $query);

    echo ($exeQuery) ? json_encode(array('code' => 200, 'message' => 'Data berhasil disimpan')) : json_encode(array('code' => 400, 'message' => 'data gagal disimpan'));
} else {
    echo json_encode(array('code' => 404, 'message' => 'request tidak valid'));
}
