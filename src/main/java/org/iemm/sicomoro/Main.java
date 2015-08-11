package org.iemm.sicomoro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.iemm.sicomoro.db.dto.MovementDTO;
import org.iemm.sicomoro.service.ConfigService;
import org.iemm.sicomoro.service.ParseFileService;
import org.iemm.sicomoro.service.ReportService;

public class Main {
	private JFrame mainFrame = new JFrame("Sicomoro");
    private JFileChooser fileChooser;
    private JTextField field = new JTextField();
	
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Main main = new Main();
                main.createAndShowGUI();
            }
        });
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
    	
    	fileChooser = new JFileChooser();
    	fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo de excel", "xlsx", "xls", "csv"));
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBounds(0, 0, 400, 150);
        mainFrame.setJMenuBar(createMenu());
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setLocationRelativeTo(null);
        
        
        

       
 
        //Add the ubiquitous "Hello World" label.
        final JButton loadBtn = new JButton("Cargar Archivo");
        loadBtn.addActionListener(new LoadFileAction());
        
        final JButton processBtn = new JButton("Procesar");
        processBtn.addActionListener(new ProcessAction());

        
        field.setText(fileChooser.getCurrentDirectory().toString());
        field.setSize(100, 10);
        field.setEditable(false);
        mainFrame.getContentPane().add(field);
        mainFrame.getContentPane().add(loadBtn);
        mainFrame.getContentPane().add(processBtn);
        
        

        
 
        //Display the window.
       // frame.pack();
        mainFrame.setVisible(true);
    }
    
    private class LoadFileAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnValue = fileChooser.showOpenDialog(mainFrame);
			if(returnValue == JFileChooser.APPROVE_OPTION) {
				field.setText(fileChooser.getSelectedFile().toString());
			} 
		} 	
    }
    
    private class ProcessAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final ParseFileService parseFileService = new ParseFileService();
			final List<MovementDTO> listofMovement = parseFileService.parse(new File(field.getText()));
			final ReportService reportService = new ReportService();
			reportService.getReports(ConfigService.getConfig().getBigDecimal("last.total"), listofMovement);
		}
    }
    
    private JMenuBar createMenu() {
    	 // Create the menu
        final JMenuBar menuBar = new JMenuBar();
        
        final JMenu herramientas = new JMenu("Herramientas");
        herramientas.setMnemonic('t');
        
        final JMenu ayuda = new JMenu("Ayuda");
        ayuda.setMnemonic('a');
        
        final JMenuItem acercaDe = new JMenuItem("Acerca de");
        acercaDe.setMnemonic('a');
        
        final JMenuItem opciones = new JMenuItem("Opciones");
        opciones.setMnemonic('O');
        
        ayuda.add(acercaDe);
        herramientas.add(opciones);
        
        menuBar.add(herramientas);
        menuBar.add(ayuda);
        return menuBar;
    }
}
