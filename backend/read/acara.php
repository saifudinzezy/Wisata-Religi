<?php
//koneksi
// definisikan koneksi ke database
include '../koneksi.php';

$sql = "";

//jika nip kosong

if (isset($_POST['id'])) {
    //jika post ada untuk memilih salah satu user
    $id = $_POST['id'];
    $sql = mysqli_query($con, "SELECT * FROM acara a WHERE a.id_wisata = '$id' ORDER BY a.id_acara DESC");
} else {
    $sql = mysqli_query($con, "SELECT * FROM acara a ORDER BY a.id_acara DESC");
}

//
$response = array();
$cek = mysqli_num_rows($sql);
if ($cek > 0) {
    $response["acara"] = array();
    //perulangan
    while ($row = mysqli_fetch_array($sql)) {
        $data = array();
        $data["id_acara"] = $row["id_acara"];
        $data["nama"] = $row["nama"];
        $data["desk"] = $row["desk"];
        $data["pembicara"] = $row["pembicara"];
        $data["alamat"] = $row["alamat"];
        $data["duror"] = $row["duror"];
        $data["tanggal"] = $row["tanggal"];
        $data["waktu"] = $row["waktu"];

        $response["pesan"] = "berhasil Mengambil Data";
        $response["response"] = "true";
        array_push($response["acara"], $data);
    }
    //mengubah data menjadi JSON
    echo json_encode($response);
} else {
    $response["pesan"] = "Gagal Mengambil Data";
    $response["response"] = "false";
    echo json_encode($response);
}
