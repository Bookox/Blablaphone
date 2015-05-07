<?php
include('functions.php'); 
include_once ('lib/swift_required.php'); //importa ambas funciones necesarias para enviar correo y conectar en la base de datos
$email=$_POST['email']; // obtiene el email por post 

if ($resultset = getSQLResultSet(" SELECT 'Nombre' FROM users WHERE Email='$email'")) { // verifica que primero el email este en la base de datos
  
      if($row = $resultset->fetch_array(MYSQLI_NUM)) { // si obtiene algun resultado de la base de datos comienza a enviar el correo
     $transport = Swift_SmtpTransport::newInstance('smtp-mail.outlook.com', 587, "tls") 
    ->setUsername('EMAIL')
    ->setPassword('CONTRASEÑA');
    $email1=base64_encode($email);
     $mailer = Swift_Mailer::newInstance($transport);
// the message itself
     $message = Swift_Message::newInstance('Recuperacion de contraseña de su cuenta en Blablaphone')
    ->setFrom(array('blablaphone@outlook.com' => 'Recuperacion de contraseña, Por favor no responda este mensaje'))
    ->setTo(array($email))
    ->setBody('<html>' .
               ' <head></head>' .
                ' <body>' .'Le enviamos para poner en orden la recuperacion de su contraseña en blablaphone haga click en el siguiente enlace:'.
                '<a href="https://blablaphone-myappsjordys.rhcloud.com/static/password.php?email='.$email1.'">Restablecer contraseña</a>'.
                '</body>'.
                '</html>',
                'text/html' );

$result = $mailer->send($message);
       $registro=true; //imprime 1 como respuesta para la aplicacion
      echo $registro;
      
      }
      else {
     $registro=0; // si en la base de datos no esta el correo envia 0 a la aplicacion
     echo $registro;
   }

}
?>
