

IMPORTANTE!! 

para poner en marcha el servidor(de momento) seguid los siguientes pasos:

1. crear una variable de environment para s_socket.java, loginView.java y RegisterView.java en eclipse
	- desplegar el boton verde de run, y elegir "run configurations"
	- En el panel izquierdo,en la seccion java aplication elegir el .java y seleccionar environment en el panel derecho
	- en environment añadir variable con name "FIREBASE_WEB_API_KEY" con value "AIzaSyABdvQlNQVNptzkwphI8th_Q2q9aqUMPSI" y click en save
	- repetir con el resto de .java


2- se debe copiar el private key(json) de nuestra carpeta en drive y pegarlo dentro del paquete adminsdk
	nombre de la carpeta en nuestro drive: /reto1/firebase-sdk 

	
	
PRUEBA SERVIDOR

Una vez hecho los dos pasos. Lanzar S_socket.java y luego LoginView.java o RegisterView.java

usuario a probar: anna.krol@example.com passworD: ana1234 o registrar un usuario y probad ese mismo