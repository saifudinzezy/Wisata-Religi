<?php 
	include '../koneksi.php';

	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		$tabel = $_POST['tabel'];
		$cari = $_POST['cari'];
		$id = $_POST['id_data'];

			$query = "DELETE FROM $tabel WHERE $cari = '$id' ";
			$exeQuery = mysqli_query($con, $query);

			echo ($exeQuery) ? json_encode(array('code' => 200, 'message' => 'Data berhasil dihapus')) : json_encode(array('code' => 400, 'message' => 'data gagal dihapus'));
	} else{
		echo json_encode(array('code' => 404, 'message' => 'request tidak valid'));
	}
 ?>