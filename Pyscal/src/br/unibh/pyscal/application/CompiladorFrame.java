package br.unibh.pyscal.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class CompiladorFrame {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompiladorFrame window = new CompiladorFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CompiladorFrame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setAlwaysOnTop(true);
//		frame.setBounds(100, 100, 596, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Compilador Pyscal");
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		
		JTextArea textPys = getNewTextArea(500,500,30,30,30,30);
		JScrollPane spp = new JScrollPane(textPys);
		spp.setVisible(true);
		spp.setSize(700,700);
		textPys.setBounds(30, 30, 30, 30);
		spp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		spp.setAutoscrolls(true);
		frame.getContentPane().add(spp);
		
		JTextArea textJ = getNewTextArea(700, 700, 850, 30, 200, 900);
		JScrollPane spj = new JScrollPane(textJ);
		spj.setSize(550,550);
		spj.setBounds(850, 30, 650, 650);
		spj.setVisible(true);
		spj.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spj.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(spj);
		
		JTextArea textConsole = getNewTextArea(200, 200, 300, 300, 300, 300);
		JScrollPane spc = new JScrollPane(textConsole);
		spc.setSize(300,300);
		spc.setBounds(30, 600, 1000, 200);
		spc.setVisible(true);
		spc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(spc);
		
		JButton btnCompilar = new JButton("Compilar");
		btnCompilar.setBounds(1441, 773, 96, 35);
		frame.getContentPane().add(btnCompilar);
		
	}
	
	private JScrollPane getNewScroll(int width, int height, int b1, int b2, int b3, int b4) {
		JScrollPane sp = new JScrollPane();
		sp.setSize(width,height);
		sp.setBounds(b1, b2, b3, b4);
		sp.setVisible(true);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.setAutoscrolls(true);
		return sp;
	}
	
	private JTextArea getNewTextArea(int width, int height, int b1, int b2, int b3, int b4) {
		JTextArea textArea = new JTextArea();
		textArea.setToolTipText("Insert code here...");
		textArea.setTabSize(6);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea.setBounds(new Rectangle(10, 10, 10, 10));
		textArea.setBorder(new EmptyBorder(13, 10, 13, 10));
		textArea.setForeground(SystemColor.text);
		textArea.setBackground(Color.GRAY);
		textArea.setBounds(b1, b2, b3, b4);
		textArea.setSize(width, height);
		textArea.setPreferredSize(new Dimension(200, 200));
		textArea.setLineWrap(true);
		return textArea;
	}
}
