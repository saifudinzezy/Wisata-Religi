<?php
//koneksi
// definisikan koneksi ke database
include '../koneksi.php';

$sql = "";

//jika nip kosong

if (isset($_POST['id'])) {
    //jika post ada untuk memilih salah satu user
    $id = $_POST['id'];
    $sql = mysqli_query($con, "SELECT * FROM galeri a WHERE a.id_wisata = '$id' ORDER BY a.id_galeri DESC");
} else {
    $sql = mysqli_query($con, "SELECT * FROM galeri a ORDER BY a.id_galeri DESC");
}

//
$response = array();
$responses = array();
$cek = mysqli_num_rows($sql);
if ($cek > 0) {
    $response["galeri"] = array();
    //perulangan
    while ($row = mysqli_fetch_array($sql)) {
        $data = array();
        $data["id_galeri"] = $row["id_galeri"];
        $data["nama_foto"] = $row["nama_foto"];
        $data["desk_foto"] = $row["desk_foto"];        

        $response["pesan"] = "berhasil Mengambil Data";
        $response["response"] = "true";
        array_push($response["galeri"], $data);
    }
    //mengubah data menjadi JSON
    echo json_encode($response);
} else {
    $response["pesan"] = "Gagal Mengambil Data";
    $response["response"] = "false";
    echo json_encode($response);
}
