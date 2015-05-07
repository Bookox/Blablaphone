<html>
<head>
<title>Actualizar contraseña</title>
</head>
<body>

<?php
include("functions.php");
$email=$_GET['email'];
//obtiene el correo mediante GET en base64 y lo decodifica 
$email=base64_decode($email);
if(isset($_POST['update']))
{
$clave = $_POST['clave'];
$clave1= $_POST['clave1'];
if ($clave==$clave1){//si ambas claves coinciden actualiza la contraseña 
ejecutarSQLCommand("UPDATE `users` SET `Password`='$clave' WHERE `Email`='$email'");
}
else { // si no lanza error
      echo "Deben coincidir ambos campos";

}
}
else // lo demas es html y form en php 
{
?>
<form method="post" action="<?php $_PHP_SELF ?>">
<table width="400" border="0" cellspacing="1" cellpadding="2">
<tr>
<td width="100">Contraseña:</td>
<td><input name="clave" type="text" id="clave"></td>
</tr>
<tr>
<td width="100">Repita la contraseña:</td>
<td><input name="clave1" type="text" id="clave1"></td>
</tr>
<tr>
<td width="100"> </td>
<td> </td>
</tr>
<tr>
<td width="100"> </td>
<td>
<input name="update" type="submit" id="update" value="Cambiar clave">
</td>
</tr>
</table>
</form>
<?php
}
?>
</body>
</html>