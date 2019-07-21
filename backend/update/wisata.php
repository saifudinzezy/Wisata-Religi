<?php
//masukan koneksi
include '../koneksi.php';
define('SITE_ROOT', realpath(dirname(__FILE__)));
//buat untuk menaruh folder
$part = "/img/";
//buat var respone array untuk menampung nilai dari db
$response = array();

//buat var
$code = "";
$message = "";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    //ambil data dari user
    $id = $_POST['id_wisata'];
    $nama = str_replace("\"", "", $_POST['nama']);
    $alamat = str_replace("\"", "", $_POST['alamat']);
    $desk = str_replace("\"", "", $_POST['desk']);
    $lat = str_replace("\"", "", $_POST['lat']);
    $long = str_replace("\"", "", $_POST['long']);

    if (isset($_FILES['image'])) {
        //
        $hapus = str_replace("\"", "", $_POST['hapus']);
        //ambil nama dari file yang kita upload
        $nama_file = str_replace(" ", "_", $_FILES['image']['name']);
        // $nama_file = $_FILES['image']['name'];
        //untuk pindahkan gambar
        $destination = SITE_ROOT . $part . $nama_file;
        //cek jika ada file maka pindahkan dan bernilai true maka eksekusi
        if (move_uploaded_file($_FILES['image']['tmp_name'], __FILE__ . '/../../img/' . $nama_file)) {
            $image = str_replace(" ", "_", $_FILES['image']['name']);
            // $cek = $_POST['cek'];
            // nol berarti sampahnya belum dibersihkan
            $cek = 0;
            //masukan querynya
            //belum selesai
            $query = "UPDATE wisata SET nama='$nama', alamat='$alamat', desk='$desk', latitude='$lat', longitude='$long',
                foto='$image' WHERE id_wisata = '$id'";

            $exeQuery = mysqli_query($con, $query);
            //buat pesan jika berhasil
            $kode = 200;
            // $pesan = "Berhasil Upload";
            $pesan = "Data berhasil diubah";
            unlink(__FILE__ . '/../../img/' . $hapus);
        } else {
            //buat pesan jika gagal
            $kode = 404;
            // $pesan = "Gagal Upload";
            $pesan = "Data gagal diubah";
        }
        //ambil nilinya dan taruh ke array untuk membuat jsonnya
        $res['code'] = $kode;
        $res['message'] = $pesan;
        //buat menjadi json
        echo json_encode($res);
    } else {
        //belum selesai			
        $query = "UPDATE wisata SET nama='$nama', alamat='$alamat', desk='$desk', latitude='$lat',
            longitude='$long' WHERE id_wisata = '$id'";
        $exeQuery = mysqli_query($con, $query);

        echo ($exeQuery) ? json_encode(array('code' => 200, 'message' => 'data berhasil ubah')) : json_encode(array('code' => 404, 'message' => 'data gagal diubah'));
    }
} else {
    echo json_encode(array('code' => 101, 'message' => 'request tidak valid'));
}
