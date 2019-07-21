<?php
include '../koneksi.php';

//buat var
$code = "code";
$message = "message";
//buat var respone array untuk menampung nilai dari db
$response = array();

//cek apakah dia post
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $pass = $_POST['pass'];

    $query = "SELECT * FROM admin WHERE email ='$email' AND password ='$pass' ";
    //koneksi yuk
    $result = mysqli_query($con, $query);
    //cek apakah ada akun					

    if (mysqli_num_rows($result) === 0) {
        // data tidak ditemukan, buat pesan error
        //tadi kurang echo
        echo json_encode(
            array($code => 404, $message => 'id dan password tidak ditemukan')
        );
    } else {
        //loping data untuk mengambil nianya
        while ($data = mysqli_fetch_assoc($result)) {
            //masukan data ke array 2 dimensi
            array_push($response, array(
                "id_admin" => $data["id_admin"],
                "nama" => $data["nama"],
                "password" => $data["password"],
                "email" => $data["email"]
            ));
        }
        // $response[0]['nama'] ambil nilai dari index nol dengan assoc array key 'nama'
        echo json_encode(
            array(
                $code => 200, $message => 'selamat datang ' . $response[0]['nama'],
                'admin' => $response
            )
        );
    }
} else {
    echo json_encode(
        array($code => 101, $message => 'request tidak valid')
    );
}
