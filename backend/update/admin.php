<?php
include '../koneksi.php';
$query = "";
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $pass = $_POST['pass'];

    $query = "UPDATE admin SET password='$pass' WHERE id_admin='$id'";

    $exeQuery = mysqli_query($con, $query);

    echo ($exeQuery) ? json_encode(array('code' => 200, 'message' => 'Data berhasil ubah')) : json_encode(array('code' => 400, 'message' => 'data gagal diubah'));
} else {
    echo json_encode(array('code' => 404, 'message' => 'request tidak valid'));
}
