<?php
include('functions.php');  // llama el script que conecta a la base de datos
$email=$_GET['email'];
$email=base64_decode($email); // obtiene la variable email con el metodo post pero luego la decodifica de encode64


if ($update = ejecutarSQLCommand("UPDATE `users` SET `Status`=1 WHERE `Email`='$email'")) { // ejecuta esta sentencia que es de functions.php
	
    	   echo "Algo ha salido mal deberias volver a clickar el link! :("; 	 // no se logro conectar si se cumple el update y lo hace 
   }
   else  {
   	
    	echo $email." Registro exitoso ya puedes disfrutar de blablaphone"; // luego de intentar el update y todo sale bien lograras estar activado
   }



   
?>
