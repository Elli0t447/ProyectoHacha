package hacha;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class HachaGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JPanel contentPane;
	
	public static JFileChooser fileChooser = new JFileChooser();
	public static JFileChooser rutaChooser = new JFileChooser();
	public static JFileChooser unionChooser = new JFileChooser();
	public static JFileChooser hashChooser = new JFileChooser();
	
	public static JLabel ErrorLabel = new JLabel("");
	public static JLabel PartirLabel = new JLabel("");
	public static JLabel CorrectLabel = new JLabel("");
	
	public static JSpinner spinner = new JSpinner();
	
	private Hacha h = new Hacha();

	/**
	 * Launch the application.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException 
	{		
		try 
		{
			//Definir el LookAndFeel del programa
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	
		} 		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedLookAndFeelException e) 
		{	
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HachaGUI frame = new HachaGUI();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	 

	/**
	 * Create the frame.
	 */
	public HachaGUI() 
	{
		setResizable(false);
		 try 
		 {
			 // Definir icono del ejecutable
		     ClassLoader cl = this.getClass().getClassLoader();
		     ImageIcon programIcon = new ImageIcon(cl.getResource("hacha.png"));
		     setIconImage(programIcon.getImage());
		  }
		  catch (Exception whoJackedMyIcon) 
		  {
		     System.out.println("Error icono no encontrado");
		  }
		
		setTitle("Hacha");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 448, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		CorrectLabel.setForeground(new Color(50, 205, 50));
		CorrectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		CorrectLabel.setBounds(23, 243, 395, 29);
		contentPane.add(CorrectLabel);
		
		ErrorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		ErrorLabel.setForeground(Color.RED);
		ErrorLabel.setBounds(32, 219, 386, 13);
		contentPane.add(ErrorLabel);
			
		JLabel ExaminarText = new JLabel("Archivo para partir:");
		ExaminarText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		ExaminarText.setBounds(23, 23, 121, 19);
		contentPane.add(ExaminarText);
		
		JLabel PartesText = new JLabel("N\u00FAmero de partes:");
		PartesText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		PartesText.setBounds(32, 95, 121, 19);
		contentPane.add(PartesText);
		spinner.setModel(new SpinnerNumberModel(2, 2, null, 1));
				
		spinner.setBounds(154, 96, 111, 20);
		contentPane.add(spinner);
			
		JLabel FileText = new JLabel("");
		FileText.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		FileText.setBounds(243, 28, 175, 13);
		contentPane.add(FileText);
			
		JLabel RutaText = new JLabel("");
		RutaText.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		RutaText.setBounds(243, 52, 175, 13);
		contentPane.add(RutaText);
				
		JButton UnirButton = new JButton("Unir");
		UnirButton.setForeground(Color.BLACK);
		UnirButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		UnirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{				
				try 
				{
					unionChooser.setCurrentDirectory(h.getRuta());
					h.select(unionChooser, false);					
					h.unirArchivo(h.getUnion());
				} 
				catch (FileNotFoundException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				} 
				catch (IOException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				} 
				catch (IllegalArgumentException e1)
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				}
				catch (ParserConfigurationException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				} 
				catch (SAXException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				} 
				catch (NoSuchAlgorithmException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				} 
				catch (NullPointerException e1) 
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
					e1.printStackTrace();
				}
				catch (Exception e1)
				{
					ErrorLabel.setText("Introduce un fichero XML válido");
				}
			}
		});
		UnirButton.setBounds(228, 174, 92, 34);
		contentPane.add(UnirButton);
		
		JButton PartirButton = new JButton("Partir");
		PartirButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		PartirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
					try 
					{
						h.separarArchivo(h.getRuta());			
					} 
					catch (NullPointerException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (FileNotFoundException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (DOMException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (NoSuchAlgorithmException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (IOException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (ParserConfigurationException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");
						e1.printStackTrace();
					} 
					catch (TransformerException e1) 
					{
						ErrorLabel.setText("Introduce una ruta primero");	
						e1.printStackTrace();
					}
			}
		});
		PartirButton.setBounds(126, 174, 92, 34);
		contentPane.add(PartirButton);
		
		JButton ExaminarButton = new JButton("Archivo");
		ExaminarButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ExaminarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{								
				try
				{
					h.select(fileChooser, false);
					if (h.getArchivo() != null && h.getRuta() != null)
					{
						PartirButton.setEnabled(true);
						UnirButton.setEnabled(true);
						ErrorLabel.setText("");
					}
					FileText.setText(h.getArchivo().getName());
				}
				catch (java.lang.NullPointerException ex)
				{
					ErrorLabel.setText("Introduce un archivo");
				}
			}
		});
		ExaminarButton.setBounds(147, 23, 85, 21);
		contentPane.add(ExaminarButton);
		
		JLabel HashLabel = new JLabel("");
		HashLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		HashLabel.setBounds(126, 127, 291, 20);
		contentPane.add(HashLabel);
		
		JLabel HashText = new JLabel("");
		HashText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		HashText.setBounds(55, 125, 69, 22);
		contentPane.add(HashText);
		
		JButton HashButton = new JButton("Hashcode");
		HashButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		HashButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					int returnVal = hashChooser.showOpenDialog(HachaGUI.contentPane);	
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File fileHash = new File(hashChooser.getSelectedFile().getAbsolutePath());					
						HashLabel.setText(h.getHashcode(fileHash));
						HashText.setText("Hashcode: ");
						System.out.println("Hashcode: " + h.getHashcode(fileHash));
						ErrorLabel.setText("");
						//JOptionPane.showMessageDialog(HashButton, h.getHashcode(h.getArchivo()), "Hashcode",JOptionPane.INFORMATION_MESSAGE);
					}
					
				} 
				catch (NullPointerException e1) 
				{
					ErrorLabel.setText("Introduce un fichero para mostrar el hashcode");
					e1.printStackTrace();
				}
				catch (FileNotFoundException e1) 
				{
					ErrorLabel.setText("Introduce un fichero para mostrar el hashcode");
					e1.printStackTrace();
				}
				catch (NoSuchAlgorithmException e1) 
				{
					ErrorLabel.setText("Introduce un fichero para mostrar el hashcode");
					e1.printStackTrace();
				} 				
				catch (IOException e1) 
				{
					ErrorLabel.setText("Introduce un fichero para mostrar el hashcode");
					e1.printStackTrace();
				}
			}
		});
		HashButton.setBounds(293, 93, 111, 25);
		contentPane.add(HashButton);		
		
		JButton Ruta = new JButton("Ruta");
		Ruta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		Ruta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					rutaChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					h.select(rutaChooser, true);
					if (h.getRuta() != null)
					{
						UnirButton.setEnabled(true);
						if (h.getArchivo() != null)
						{
							PartirButton.setEnabled(true);
						}
						
						ErrorLabel.setText("");
					}
					RutaText.setText(h.getRuta().getAbsolutePath());
				}
				catch (NullPointerException e1)
				{
					ErrorLabel.setText("Introduce una ruta");
				}

			}
		});
		Ruta.setBounds(147, 48, 85, 21);
		contentPane.add(Ruta);
		
		JLabel RutaLabel = new JLabel("Ruta de trabajo:");
		RutaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		RutaLabel.setBounds(23, 48, 121, 19);
		contentPane.add(RutaLabel);
		
		JPanel Line = new JPanel();
		Line.setBackground(new Color(204, 0, 0));
		Line.setBounds(23, 79, 395, 5);
		contentPane.add(Line);
		
		JPanel Line2 = new JPanel();
		Line2.setBackground(new Color(204, 0, 0));
		Line2.setBounds(23, 158, 395, 5);
		contentPane.add(Line2);
		
		
		PartirLabel.setForeground(new Color(50, 205, 50));
		PartirLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		PartirLabel.setBounds(23, 174, 81, 19);
		contentPane.add(PartirLabel);
	
		if (h.getArchivo() == null && h.getRuta() == null)
		{
			UnirButton.setEnabled(false);
			PartirButton.setEnabled(false);
		}
		
		fileChooser.updateUI();
		rutaChooser.updateUI();
		unionChooser.updateUI();
		hashChooser.updateUI();
		spinner.updateUI();
		
	}
}
