<?php
//koneksi
// definisikan koneksi ke database
include '../koneksi.php';

$sql = "";

//jika nip kosong

if (isset($_POST['id'])) {
    //jika post ada untuk memilih salah satu user
    $id = $_POST['id'];
    $sql = mysqli_query($con, "SELECT * FROM wisata a WHERE a.id_wisata = '$id' ORDER BY a.id_wisata DESC");
} else {
    $sql = mysqli_query($con, "SELECT * FROM wisata a ORDER BY a.id_wisata DESC");
}

//query untuk menampilkan semua data ditable
// $sql=mysqli_query($con,"SELECT * FROM jadwal_tranfusi ORDER BY id_tranfusi desc");
//untuk menampung isi data
$response = array();
$responses = array();
$cek = mysqli_num_rows($sql);
if ($cek > 0) {
    $response["wisata"] = array();
    //perulangan
    while ($row = mysqli_fetch_array($sql)) {
        $data = array();
        $data["id_wisata"] = $row["id_wisata"];
        $data["nama"] = $row["nama"];
        $data["alamat"] = $row["alamat"];
        $data["desk"] = utf8_encode($row["desk"]);;
        $data["latitude"] = $row["latitude"];
        $data["longitude"] = $row["longitude"];
        $data["foto"] = utf8_encode($row["foto"]);

        //galeri
        $cari = $row["id_wisata"];
        $foto = array();
        $galeri = mysqli_query($con, "SELECT * FROM galeri WHERE id_wisata = '$cari' ORDER BY id_galeri DESC");
        while ($row = mysqli_fetch_array($galeri)) {
            $foto["nama_foto"] = $row["nama_foto"];
            $foto["desk_foto"] = $row["desk_foto"];

            //push data untuk menambahkan array ke array
            array_push($responses, $foto);
            //masukan data ke array menjadi array array
            $data["galeri"] = $responses;
        }

        $response["pesan"] = "berhasil Mengambil Data";
        $response["response"] = "true";
        array_push($response["wisata"], $data);
    }
    //mengubah data menjadi JSON
    echo json_encode($response);
} else {
    $response["pesan"] = "Gagal Mengambil Data";
    $response["response"] = "false";
    echo json_encode($response);
}
