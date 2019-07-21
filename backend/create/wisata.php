<?php
//masukan koneksi
include '../koneksi.php';
//buat var respone array untuk menampung nilai dari db
$response = array();

//buat var
$code = "";
$message = "";

//cek apakah dia pake post
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	//cek jika ada file yang diupload
	//imageupload adalah parameter yang harus dimasukan jika ingin mendapatkan nilainya
	if ($_FILES['image']) {
		//ambil nama dari file yang kita upload
		$nama_file = str_replace(" ", "_", $_FILES['image']['name']);
		//cek jika ada file maka pindahkan dan bernilai true maka eksekusi
		if (move_uploaded_file($_FILES['image']['tmp_name'], __FILE__ . '/../../img/' . $nama_file)) {
			//ambil data dari user
			$nama = str_replace("\"", "", $_POST['nama']);
			$alamat = str_replace("\"", "", $_POST['alamat']);
			$desk = str_replace("\"", "", $_POST['desk']);
			$lat = str_replace("\"", "", $_POST['lat']);
			$long = str_replace("\"", "", $_POST['long']);
			$foto = str_replace(" ", "_", $_FILES['image']['name']);

			// $cek = $_POST['cek'];
			// nol berarti sampahnya belum dibersihkan
			$cek = 0;
			//masukan querynya
			$query = "INSERT INTO wisata (nama, alamat, desk, latitude, longitude, foto) VALUES
					('$nama', '$alamat', '$desk', '$lat','$long', '$foto')";

			$exeQuery = mysqli_query($con, $query);
			//buat pesan jika berhasil
			$kode = 200;
			// $pesan = "Berhasil Upload";
			$pesan = "Data berhasil disimpan";
		} else {
			//buat pesan jika gagal
			$kode = 404;
			// $pesan = "Gagal Upload";
			$pesan = "Data gagal disimpan";
		}
	}
} else {
	$kode = 404;
	$pesan = "Request tidak valid";
}
//ambil nilinya dan taruh ke array untuk membuat jsonnya
$res['code'] = $kode;
$res['message'] = $pesan;
//buat menjadi json
echo json_encode($res);
