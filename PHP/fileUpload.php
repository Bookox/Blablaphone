
<?php
$name=$_POST['name']; // recibe el nombre de la imagen
$ext = explode('.',$_FILES['image']['name']); // toma la extension de la imagen como array
$extension = $ext[1]; //toma el valor 1 del array
//ini_set('upload_max_filesize', '10M');
//ini_set('post_max_size', '10M');
//ini_set('max_input_time', 300);
//ini_set('max_execution_time', 300);


$img = "images/".$name.'.'.$extension; // es la direccion y nombre de la imagen


try {
    //lanza una excepcion si no puede subir el archivo
    if (!move_uploaded_file($_FILES['image']['tmp_name'], $img)) { // esta funcion sube el archivo a  la direccion
        throw new Exception('Could not move file');// lanza este error si no lo sube
    }

    echo "The file " . basename($_FILES['image']['name']) . // si lo hizo lanza que lo hizo
    " has been uploaded";
} catch (Exception $e) {
    die('File did not upload: ' . $e->getMessage()); //no se logro por eso lanza este error en el catch
}
?>