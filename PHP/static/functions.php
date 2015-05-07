<?php 
header( 'Content-Type: text/html;charset=utf-8' );

define( "DB_SERVER",    getenv('OPENSHIFT_MYSQL_DB_HOST') );
 
 define( "DB_USER",      getenv('OPENSHIFT_MYSQL_DB_USERNAME') ); // obtiene los valores necesarios para conectar a la base de datos
 
 define( "DB_PASSWORD",  getenv('OPENSHIFT_MYSQL_DB_PASSWORD') ); // estos valores vienen predeterminados por openshift asi que se usa el getenv que obtiene los valores
 
 define( "DB_DATABASE",  getenv('OPENSHIFT_APP_NAME') );

function ejecutarSQLCommand($commando){ // esta funcion es para ejecutar sentencias SQL directamente
 
  $mysqli = new mysqli(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE); // usa las variables obtenidas para obtener acceso a la base de datos


if ($mysqli->connect_errno) {// comprueba la conexion
    printf("fallo la conexion: %s\n", $mysqli->connect_error);
    exit();
}

if ( $mysqli->multi_query($commando)) { // si hay conexion realiza un multi query con la variable comando
     if ($resultset = $mysqli->store_result()) { 
    	while ($row = $resultset->fetch_array(MYSQLI_BOTH)) {
    		
    	}
    	$resultset->free();
     }
}

$mysqli->close();
}



function getSQLResultSet($commando){// esta funcion realiza una consulta SQL a la base de datos
 
 
  $mysqli = new mysqli(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE);

/* check connection */
if ($mysqli->connect_errno) {
    printf("Connect failed: %s\n", $mysqli->connect_error);
    exit();
}

if ( $mysqli->multi_query($commando)) {
	return $mysqli->store_result();
	  
}
$mysqli->close();
}


?>
