package hacha;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Hacha 
{	
	// Archivo que sera partido
	private File archivo;
	
	// Directorio de trabajo del programa, donde se unen o parten archivos
	private File ruta;
	
	// Archivo creado tras juntar las partes del archivo original
	private File union;
	
	// Cantidad de partes en las que se partira el archivo seleccionado
	private int numPartes;
	
	// Cantidad de bytes que tiene cada parte (si sobran bytes cambiara)
	private int cantidadBytesArchivo;
	
	// Cantidad de bytes originales de la parte (si sobran bytes la ultima parte de un archivo tendra los bytes sobrantes)
	private int bytesOriginales;
	
	// Variable que sera true si quedaron bytes fuera de la separacion del archivo
	private boolean sobran;
	
	
	// Getters
	public File getArchivo() {return archivo;}
	public File getUnion() {return union;}
	public File getRuta() {return ruta;}
	public int getNumPartes() {return numPartes;}
	
	
	// Setters
	public void setArchivo(File arch) {archivo = arch;}
	public void setUnion(File uni) {union = uni;}
	public void setRuta(File rut) {ruta = rut;}
	public void setNumPartes(int numP) {numPartes = numP;}
	
	/**
	 * Metodo para definir las variables al File seleccionado por el JFileChooser
	 * @param jfc El JFileChooser para seleccionar el archivo o directorio
	 * @param isDirectory Si es true sera un directorio, si es false un fichero
	 */
	public void select(JFileChooser jfc, boolean isDirectory)
	{
		int returnVal = jfc.showOpenDialog(HachaGUI.contentPane);	
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (isDirectory)
			{
				setRuta(jfc.getSelectedFile());
			}
			else if (jfc.equals(HachaGUI.fileChooser))
			{
				setArchivo(jfc.getSelectedFile());
			}
			else
			{
				setUnion(jfc.getSelectedFile());
			}
		}
		
	}
	
	/**
	 * Metodo para separar el archivo en las partes que el usuario seleccione con JSpinner
	 * 
	 * Funcionamiento:
	 * Primero crea un buffer de bytes que sera del tamanyo que sea cada parte y hasta que ese buffer sea 0 (no tenga mas bytes que copiar) ira copiando equitativamente 
	 * a las partes que se hayan seleccionado.
	 * 
	 * Debido a que es posible que ciertos archivos no se puedan dividir sin resto (partes de igual tamanyo), 
	 * el metodo tiene un algoritmo para copiar los bytes restantes a la ultima parte creada:
	 * 
	 * Si la cantidad de bytes restantes es menor que el tamanyo de la parte, crea un nuevo archivo que se llame como la ultima
	 * parte creada y copia los bytes a ese archivo sin sustituir los que ya tenga, de modo que se queda asi:
	 * 
	 *  Archivo original: 603 bytes entre 3 partes
	 * 	parte 1: 200 bytes
	 *  parte 2: 200 bytes
	 *  parte 3: 200 bytes, no caben los ultimos 3 bytes en la ultima parte y por lo tanto crearia parte4
	 *  parte 4: 3 bytes
	 *  
	 *  Asi que, crea un nuevo parte 3 sin sustituir los que ya tiene y copia esos ultimos 3 bytes y se quedaria:
	 *  
	 *  parte1: 200 bytes
	 *  parte2: 200 bytes
	 *  parte3: 203 bytes
	 * @param ruta El directorio donde hara la separacion
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws DOMException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	public void separarArchivo(File ruta) throws FileNotFoundException, IOException, ParserConfigurationException, TransformerException, DOMException, NoSuchAlgorithmException, IllegalArgumentException, NullPointerException
	{			
		HachaGUI.CorrectLabel.setText("");
		
		numPartes = (Integer) HachaGUI.spinner.getValue();
		byte[] buffer;
		
		int tamanyoParte = (int) (archivo.length()/numPartes);
		buffer = new byte[tamanyoParte];
		
		try (FileInputStream fis = new FileInputStream(archivo);
			 BufferedInputStream bis = new BufferedInputStream(fis))
		{		
			cantidadBytesArchivo = 0;
			int nombreParteArchivo = 1;
			
			while ((cantidadBytesArchivo = bis.read(buffer)) > 0 && tamanyoParte == cantidadBytesArchivo)
			{
				bytesOriginales = cantidadBytesArchivo;
				File parte = new File(ruta + "/" + archivo.getName() + ".parte" + String.valueOf(nombreParteArchivo));
				
				FileOutputStream out = new FileOutputStream(parte);
				BufferedOutputStream bos = new BufferedOutputStream(out);
					
				bos.write(buffer, 0, cantidadBytesArchivo);
									
				System.out.println("Parte en " + parte);
				nombreParteArchivo++;	
				bos.close();
			}
			
			
			if (cantidadBytesArchivo < tamanyoParte && cantidadBytesArchivo > 0)
			{		
				tamanyoParte = cantidadBytesArchivo;
				try (FileOutputStream fos = new FileOutputStream(new File(ruta + "/" + archivo.getName() + ".parte" + String.valueOf(nombreParteArchivo-1)), true);
						BufferedOutputStream bos = new BufferedOutputStream(fos))
				{
					bos.write(buffer, 0, cantidadBytesArchivo);
					
					System.out.println("Bytes sobrantes: " + cantidadBytesArchivo);
					bos.close();
					
				}
			}	
			bis.close();
			fis.close();
			HachaGUI.PartirLabel.setText("Partición correcta!");
			
		}
				
		crearXML();
	}
	
	/**
	 * Metodo para generar un XML con los datos del fichero seleccionado con JFileChooser
	 * Define el texto de los elementos del XML con las variables del programa
	 * 
	 * El nombre del fichero XML sera: "divison_nombredelarchivo.xml"
	 * 
	 * Servira para mas adelante unir las partes del archivo partido y sacar informacion de este XML creado
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws DOMException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public void crearXML() throws ParserConfigurationException, TransformerException, DOMException, NoSuchAlgorithmException, IOException
	{
		 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder docBuilder = docFactory.newDocumentBuilder();     	      
	     Document doc = docBuilder.newDocument();
	     
	     Attr size = doc.createAttribute("bytesOriginal");
	     size.setValue(String.valueOf(archivo.length()));
	     
	     Element rootElement = doc.createElement("division");
	     rootElement.setAttributeNode(size);     
	     doc.appendChild(rootElement);
	     
	     Element nombreArch = doc.createElement("nombre");
	     nombreArch.setTextContent(String.valueOf(archivo.getName()));
	     rootElement.appendChild(nombreArch);
	     
	     Element hash = doc.createElement("hashcode");
	     rootElement.appendChild(hash);
	     hash.setTextContent(getHashcode(archivo));
	     
	     Element ext = doc.createElement("extension");
	     rootElement.appendChild(ext);
	     
	     String[] fileParts = archivo.getName().split("\\.");
	     ext.setTextContent(fileParts[1]);
	     
	     Element numPartesArch = doc.createElement("numPartes");
	     numPartesArch.setTextContent(String.valueOf(numPartes));
	     rootElement.appendChild(numPartesArch);
	     
	     Element sizeF = doc.createElement("tamañoPartes");
	     sizeF.setTextContent(String.valueOf(bytesOriginales));
	     rootElement.appendChild(sizeF);
	     
	     // Si quedaron bytes sobrantes entre la division se crea un nuevo elemento en el XML y se le asigna la cantidad de bytes sobrantes
	     if (cantidadBytesArchivo > 0)
	     {
	   	      Element sobrante = doc.createElement("bytesSobrantes");
		      sobrante.setTextContent(String.valueOf(cantidadBytesArchivo));
		      rootElement.appendChild(sobrante);
		      sobran = true;
	     }
	     
	     TransformerFactory transformerFactory = TransformerFactory.newInstance();
	     Transformer transformer = transformerFactory.newTransformer();
	     DOMSource source = new DOMSource(doc);
	     StreamResult result = new StreamResult(new File(ruta.getAbsolutePath() + "/" + "division" + "_" + fileParts[0] +".xml"));
	     transformer.transform(source, result);
	}
	
	
	/**
	 * Metodo para unir un archivo con sus partes previamente partidas
	 * 
	 * Usa un DocumentBuilderFactory para comprobar ciertos parametros del XML que se va a unir para asegurarse
	 * de que la union sea correcta
	 * 
	 * Antes de unir comprueba: si el archivo seleccionado es un XML
	 * 							si la extension del archivo final es correcta 
	 * 							el calculo de las partes del archivo es correcto entre todas las partes (si el tamanyo de bytes es correcto)
	 * 
	 * Mientras une comprueba: si las partes tienen el tamanyo correcto
	 * 
	 * Despues de unir comprueba: si la cantidad de bytes del archivo original son los mismos que el unido
	 * 							  si el hashcode del original es igual al unido						  
	 * 							  si existen bytes sobrantes comprobar si el calculo de bytes es correcto
	 * 
	 * Para seleccionar todas las partes, crea un nuevo archivo con el mismo nombre que las partes y hasta que no exista le va sumando el numero de partes al nombre, asi va iterando hasta que el archivo no exista
	 * Ejemplo: si se han creado 3 partes
	 * 				arch.parte1 existe, copiar al outputstream
	 * 				arch.parte2 existe, copiar al outputstream
	 * 				arch.parte3 existe, copiar al outputstream
	 * 				arch.parte4 no existe, acabar el bucle
	 * 
	 * 
	 * @param unionXml El archivo recogido por JFileChooser
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws NoSuchAlgorithmException
	 * @throws NullPointerException
	 */
	public void unirArchivo(File unionXml) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, NoSuchAlgorithmException, NullPointerException
	{			 				
		
			 HachaGUI.PartirLabel.setText("");
			
			 // XML
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse(unionXml);
			
			 NodeList list = doc.getElementsByTagName("division");
			 for (int temp = 0; temp < list.getLength(); temp++)
			 {
				 Node node = list.item(temp);
				 
				 if (node.getNodeType() == Node.ELEMENT_NODE)
				 {
					 Element element = (Element) node;
					 String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
					 
					 String nPartes = element.getElementsByTagName("numPartes").item(0).getTextContent();
					 String sizePartes = element.getElementsByTagName("tamañoPartes").item(0).getTextContent();
					 
					 String bytesOri = element.getAttribute("bytesOriginal");
					 String bytesSobra = null;
					 if (sobran || (Integer.parseInt(sizePartes) * Integer.parseInt(nPartes) != Integer.parseInt(bytesOri)))
					 {
						 bytesSobra = element.getElementsByTagName("bytesSobrantes").item(0).getTextContent();
					 }
					 
					 String hash = element.getElementsByTagName("hashcode").item(0).getTextContent();
					 
					 
					 
					 String exte = element.getElementsByTagName("extension").item(0).getTextContent();
				 
					 File archivoUnido = new File(ruta.getAbsolutePath() + "/" + nombre);
					 
					 String exteUnido[] = archivoUnido.getName().split("\\.");
					 String exteXml[] = unionXml.getName().split("\\.");
					 
					 try(FileOutputStream fos = new FileOutputStream(archivoUnido);
								BufferedOutputStream unir = new BufferedOutputStream(fos))	
						{
						 	// COMPROBACIONES ANTES DE UNIR
						 	if (exteUnido[1].equals(exte) 
						 			&& exteXml[1].equals("xml"))
						 	{
						 		HachaGUI.CorrectLabel.setText(HachaGUI.CorrectLabel.getText() + " XML correcto!");
						 		System.out.println("El archivo elegido es un XML!");
						 		
						 		int partCounter = 0;
							 	File f = new File(ruta.getPath() + "/" + nombre + ".parte1");
							
							 	while (f.exists())
							 	{	
							 		partCounter++;
							 		int partChecker = Integer.parseInt(nPartes) - Integer.parseInt(nPartes);
							 		f = new File(ruta.getPath() + "/" + nombre + ".parte" + partCounter);	
							 		
								 	if (f.exists() && partCounter == partChecker+partCounter)
								 	{		
								 		if (sobran || (Integer.parseInt(sizePartes) * Integer.parseInt(nPartes) != Integer.parseInt(bytesOri)))
								 		{
								 			if (f.length() == Integer.parseInt(sizePartes) || f.length() == Integer.parseInt(sizePartes) + Integer.parseInt(bytesSobra))
								 			{
								 				Files.copy(f.toPath(), unir);
								 				System.out.println("Parte " + partCounter + " correcta!");
								 			}
								 			else
								 			{
								 				System.out.println("Parte corrupta");
								 			}
								 		}
								 		else
								 		{
								 			if (f.length() == Integer.parseInt(sizePartes))
								 			{
								 				Files.copy(f.toPath(), unir);
								 				System.out.println("Parte " + partCounter + " correcta!");

								 			}
								 		}							 		
								 		
								 		partChecker++;
								 	}
								 	
							 	}
							 	HachaGUI.CorrectLabel.setText(HachaGUI.CorrectLabel.getText() + " Partes correctas!");
							 	HachaGUI.ErrorLabel.setText("");
						 	}
						 	else
						 	{
						 		HachaGUI.CorrectLabel.setText("");
						 		HachaGUI.ErrorLabel.setText("Introduce un XML válido");					 		
						 		System.out.println("XML no válido");
						 	}
						
						 	unir.close();
						 	fos.close();
						 	
						 	// COMPROBACION TRAS UNIR
						 	if (getHashcode(archivoUnido).equals(hash) 
						 			&& String.valueOf(archivoUnido.length()).equals(bytesOri))
						 	{
						 		if (sobran || (Integer.parseInt(sizePartes) * Integer.parseInt(nPartes) != Integer.parseInt(bytesOri)))
						 		{						 
						 			if (Integer.parseInt(sizePartes) * Integer.parseInt(nPartes) + Integer.parseInt(bytesSobra) == (int) archivoUnido.length())
						 			{
						 				System.out.println("El resto de bytes más las partes son de igual tamaño que el original!");
						 				HachaGUI.CorrectLabel.setText(HachaGUI.CorrectLabel.getText() + " Bytes correctos!");
						 				HachaGUI.ErrorLabel.setText("");
						 			}
						 			else
						 			{
								 		HachaGUI.ErrorLabel.setText("Error, unión no realizada correctamente");
								 		HachaGUI.CorrectLabel.setText("");
						 				System.out.println("Unión no realizada correctamente, faltan partes o el archivo esta corrupto");
						 			}
						 		}
						 		else
						 		{
						 			HachaGUI.CorrectLabel.setText(HachaGUI.CorrectLabel.getText() + " Bytes correctos!");
						 			HachaGUI.ErrorLabel.setText("");
						 			System.out.println("Bytes totales igual al original!");
						 		}
							 
						 		HachaGUI.CorrectLabel.setText(HachaGUI.CorrectLabel.getText() + " Hashcode correcto!");
						 		HachaGUI.ErrorLabel.setText("");
						 		System.out.println("Hashcode correcto!");
							 
						 	}
						 	else
						 	{
						 		HachaGUI.ErrorLabel.setText("Error, unión no realizada correctamente");
						 		HachaGUI.CorrectLabel.setText("");
						 		System.out.println("Unión no realizada correctamente, faltan partes o el archivo esta corrupto");
						 	}
						}	 
				 }	 
				 
				 // Reset de los bytes sobrantes
				 sobran = false;
		}
	}
	
	/**
	 * Metodo que devuelve el hashcode de un archivo pasado por parametro
	 * @param archHash El archivo del que se sacara el hashcode
	 * @return El Hash del archivo pasado por parametro en String
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public String getHashcode(File archHash) throws NoSuchAlgorithmException, IOException, NullPointerException
	{
		 InputStream fis =  new FileInputStream(archHash.getAbsoluteFile());

	     byte[] buffer = new byte[1024];
	     MessageDigest complete = MessageDigest.getInstance("MD5");
	     int numRead;

	     do 
	     {
	         numRead = fis.read(buffer);
	         if (numRead > 0) 
	         {
	            complete.update(buffer, 0, numRead);
	         }
	         
	     } while (numRead != -1);

	     fis.close();
	     byte[] b = complete.digest();
       
	     String result = "";

         for (int i=0; i < b.length; i++) 
         {
	         result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	     }
         
	     return result;
	}
	
}
