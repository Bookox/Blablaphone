<?php
include('functions.php'); 
$email=$_POST['email'];
$clave=$_POST['clave'];//obtiene valores con POST
//luego realiza un if ejecutando el SQL y si resulta verdadero va imprimiendo los valores en json
if ($resultset = getSQLResultSet(" SELECT Nombre,Apellido,Email,Pais,Numero,Saldo FROM users WHERE Email='$email' AND Password='$clave' AND Status='1'")) {
	
    	while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
    	echo json_encode($row);
    	//imprime la variable en json
    	}
    	
   }
   
   
?>