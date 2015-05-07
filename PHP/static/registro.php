<?php include ('functions.php');
include_once ('lib/swift_required.php');// llama a las funciones necesarias para conectar y enviar correos
$nombre=$_POST['nombre']; // obtiene todos estos parametros por POST 
$apellido=$_POST['apellido'];
$email=$_POST['email'];
$pais=$_POST['pais'];
$contra=$_POST['contra'];
$tel=$_POST['tel'];



if ($resultset = getSQLResultSet(" SELECT * FROM users WHERE Email='$email' OR Numero='$tel'")) { // si el correo o el numero estan registrados ya, envia 0 a la app
  
      if($row = $resultset->fetch_array(MYSQLI_NUM)) {
      $registro=0;
      echo $registro;// imprime 0 para la app por que ya el correo o el numero esta repetido
      
      }
else {

// ejecuta directamente el SQL si el correo o el numero no estan registrados
 ejecutarSQLCommand("INSERT INTO  `users` (
`Nombre`,
`Apellido`,
`Email`,
`Pais`,
`Password`,
`Numero`
)
VALUES (
'$nombre' ,
'$apellido' ,
'$email' ,
'$pais',
'$contra',
'$tel'
) 
;");  
    $registro=true;
    echo $registro;// imprime 1 y procede a enviar el correo 
   $transport = Swift_SmtpTransport::newInstance('smtp-mail.outlook.com', 587, "tls") 
    ->setUsername('CORREO') // se asignan los valores necesarios para que swift envie el correo
    ->setPassword('CONTRASEÑA');
$email1=base64_encode($email);
$mailer = Swift_Mailer::newInstance($transport);
// the message itself
$message = Swift_Message::newInstance('Activacion de su cuenta en Blablaphone')
    ->setFrom(array('blablaphone@outlook.com' => 'Activacion de su cuenta en Blablaphone, Por favor no responda este mensaje'))
    ->setTo(array($email))
    ->setBody('<html>' .
               ' <head></head>' .
                ' <body>' .'Le enviamos para verificar la activacion de su cuenta en blablaphone por favor para activar su cuenta haga click en el siguiente enlace:'.
                '<a href="https://blablaphone-myappsjordys.rhcloud.com/static/activacion.php?email='.$email1.'">Activar tu cuenta en blablaphone</a>'.
                '</body>'.
                '</html>',
                'text/html' );

$result = $mailer->send($message); // envia el correo electronico

        

    }

}
      
   
 ?>