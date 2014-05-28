package testtask;

import java.awt.EventQueue;

import javax.swing.InputVerifier;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.LayoutStyle.ComponentPlacement;

import testtask.EncodingApplication;

import java.awt.Color;

import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Scrollbar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class AppMainWindow {

	private JFrame frmTestTaskEncoding;
	private JTextField sourceFile;
	private JTextField targetFile;
	private JTextField sourceVideoFrameWidth;
	private JTextField sourceVideoFrameHeight;
	EncodingApplication application;
	private JTextArea logArea;
	public ProgressListener listener;
	private String prevProgress;
	boolean completed = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppMainWindow window = new AppMainWindow();
					window.frmTestTaskEncoding.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTestTaskEncoding = new JFrame();
		frmTestTaskEncoding.setBackground(Color.WHITE);
		frmTestTaskEncoding.setTitle("Test task Encoding Application");
		frmTestTaskEncoding.getContentPane().setBackground(Color.WHITE);
		frmTestTaskEncoding.setResizable(false);
		frmTestTaskEncoding.setBounds(100, 100, 583, 518);
		frmTestTaskEncoding.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton presetsButton = new JButton("Encode With Different Presets");
		presetsButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		presetsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncodingApplication application = new EncodingApplication();
				listener = new ProgressListener();
				logArea.setText("");
				if (!sourceVideoFrameWidth.getText().isEmpty() & !sourceVideoFrameHeight.getText().isEmpty()){
					int widthFrame = Integer.valueOf(sourceVideoFrameWidth.getText());
					int heightFrame = Integer.valueOf(sourceVideoFrameHeight.getText());
					application.setVideoSize(widthFrame, heightFrame);
				}
				application.source = new File(sourceFile.getText());
				application.target = new File(targetFile.getText());
				NewThread logging = new NewThread(listener, logArea);
				
				logging.start();
				application.startApplication("presets", listener);
				try {
					logging.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logging.stop();	
			}
		});
		
		JButton bitratesButton = new JButton("Encode With Different Bitrates");
		bitratesButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		bitratesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				application = new EncodingApplication();
				listener = new ProgressListener();
				completed = false;
				
				logArea.setText("");
				if (!sourceVideoFrameWidth.getText().isEmpty() & !sourceVideoFrameHeight.getText().isEmpty()){
					int widthFrame = Integer.valueOf(sourceVideoFrameWidth.getText());
					int heightFrame = Integer.valueOf(sourceVideoFrameHeight.getText());
					application.setVideoSize(widthFrame, heightFrame);
				}
				application.source = new File(sourceFile.getText());
				application.target = new File(targetFile.getText());
				
			
				SwingWorker logging = new SwingWorker(){
					@Override
					protected Object doInBackground() throws Exception {
						// TODO Auto-generated method stub
						String progress;
						logArea.append("Started!\n");
						
						while(!completed){
							progress = listener.getProgress();
							if (progress != null){
			    				if (!progress.equals(prevProgress)){
			    					logArea.append(progress+"\n");
			    					logArea.setCaretPosition(logArea.getDocument().getLength());
			    					prevProgress = progress;
			    				}
			    			}
							else{
								Thread.sleep(10);
							}
						}
						logArea.append("THE END \n");
						return null;
					}
				};
				logging.execute();
				
				SwingWorker encoding = new SwingWorker(){
					@Override
					protected Object doInBackground() throws Exception {
						// TODO Auto-generated method stub
						application.startApplication("bitrates", listener);
						completed = true;
						return null;
					}
				};
				encoding.execute();
				
				/*
				NewThread logging = new NewThread(listener, logArea);
				
				logging.start();
				application.startApplication("bitrates",listener);
				try {
					logging.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logging.stop();*/				
			}
		});
		
		JButton decodeButton = new JButton("Decode");
		decodeButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		decodeButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				EncodingApplication application = new EncodingApplication();
				listener = new ProgressListener();
				logArea.setText("");
				application.source = new File(sourceFile.getText());
				application.decodedTarget = new File(targetFile.getText());
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						logArea.setText(listener.getProgress());
					}
					
				});
				
				application.startApplication("decode", listener);
				
				/*NewThread logging = new NewThread(listener, logArea);
				
				logging.start();
				application.startApplication("decode", listener);
				try {
					logging.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logging.stop();*/
				JOptionPane.showMessageDialog(null, "Decoding process is completed", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		sourceFile = new JTextField();
		sourceFile.setColumns(10);
		
		JLabel lblSourceFile = new JLabel("Source file:");
		lblSourceFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton browseSourceFileButton = new JButton("Browse");
		browseSourceFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Choose Source File");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                	sourceFile.setText(chooser.getSelectedFile().getPath());
                }
			}
		});
		
		JLabel lblTargetFile = new JLabel("Target file:");
		lblTargetFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		targetFile = new JTextField();
		targetFile.setColumns(10);
		
		JButton browseTargetFileButton = new JButton("Browse");
		browseTargetFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Choose Target File");
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if(chooser.showDialog(null, "Choose")==JFileChooser.APPROVE_OPTION){
                	targetFile.setText(chooser.getSelectedFile().getPath());
                }
			}
		});
		
		sourceVideoFrameWidth = new JTextField();
		sourceVideoFrameWidth.setColumns(10);
		
		JLabel lblWidth = new JLabel("width");
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblWidth.setHorizontalAlignment(SwingConstants.LEFT);
		
		sourceVideoFrameHeight = new JTextField();
		sourceVideoFrameHeight.setColumns(10);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblHeight.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblSpecifySourceFrame = new JLabel("Specify source frame size");
		lblSpecifySourceFrame.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSpecifySourceFrame.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblforyuvFiles = new JLabel("(for .yuv files)");
		lblforyuvFiles.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JLabel lblEncodingWithPresets = new JLabel("Encoding with presets slow,medium, fast,veryfast");
		lblEncodingWithPresets.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblEncodingWithBitrates = new JLabel("Encoding with bitrates 1.5, 2.5, 4, 6, 10 Mbps");
		lblEncodingWithBitrates.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JSeparator separator = new JSeparator();
		
		logArea = new JTextArea();
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		logArea.setBackground(Color.WHITE);
		frmTestTaskEncoding.getContentPane().add(logArea);
		
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.getHorizontalScrollBar().setAutoscrolls(true);
		scrollPane.getVerticalScrollBar().setAutoscrolls(true);	
		


/*scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
    public void adjustmentValueChanged(AdjustmentEvent e) {  
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    }
});*/


		
		JLabel lblProcessLog = new JLabel("Process log:");
		lblProcessLog.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GroupLayout groupLayout = new GroupLayout(frmTestTaskEncoding.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSourceFile)
							.addGap(4)
							.addComponent(sourceFile, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblforyuvFiles, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSpecifySourceFrame, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
							.addGap(9)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(4)
									.addComponent(lblWidth))
								.addComponent(lblHeight))
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(sourceVideoFrameWidth, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
								.addComponent(sourceVideoFrameHeight, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))))
					.addGap(6)
					.addComponent(browseSourceFileButton)
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(presetsButton, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEncodingWithPresets)))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(lblProcessLog, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 556, GroupLayout.PREFERRED_SIZE)))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(6)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 567, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(lblTargetFile)
					.addGap(4)
					.addComponent(targetFile, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(browseTargetFileButton)
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(decodeButton, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEncodingWithBitrates)
						.addComponent(bitratesButton, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))
					.addGap(19))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(lblSourceFile))
								.addComponent(sourceFile, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
							.addGap(7)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(19)
											.addComponent(lblforyuvFiles))
										.addComponent(lblSpecifySourceFrame, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(lblWidth)
									.addGap(5)
									.addComponent(lblHeight))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(sourceVideoFrameWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(2)
									.addComponent(sourceVideoFrameHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addComponent(browseSourceFileButton)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(presetsButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(lblEncodingWithPresets)))
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(lblTargetFile))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(11)
							.addComponent(targetFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(browseTargetFileButton))
						.addComponent(bitratesButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEncodingWithBitrates)
					.addGap(19)
					.addComponent(decodeButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProcessLog)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))
		);
		frmTestTaskEncoding.getContentPane().setLayout(groupLayout);
	}
}
