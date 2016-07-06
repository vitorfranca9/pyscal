package br.unibh.pyscal.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.util.PyscalConstantUtil;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.CompiladorVO;

public class CompiladorFrame {

	private JFrame frame;
	private CompiladorVO compilador;
	private JButton btnCompilar;
	private JTextArea textConsole;
	private JTextArea textJ;
	private JScrollPane spp;
	private JTextArea textPys;

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
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setAlwaysOnTop(true);
//		frame.setBounds(100, 100, 596, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Compilador Pyscal");
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
//		frame.setMinimumSize(new Dimension(400, 400));
//		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		
		textPys = getNewTextArea(700,500,30,30,30,30);
//		textPys.setText("class Comandos10:"+"\n" +
//					"def void funcao(integer x):" +"\n"+
//						"writeln(x);" +"\n"+
//					"end;" +"\n"+
//					"defstatic void main(String[] args):" +"\n"+
//						"funcao(5);" +"\n"+
//					"end;" +"\n" +
//				"end.\n" +"\n");
		spp = new JScrollPane(textPys);
		spp.setVisible(true);
		spp.setSize(700,500);
//		textPys.setBounds(30, 30, 30, 30);
		spp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		spp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		spp.setAutoscrolls(true);
		frame.getContentPane().add(spp);
		
		textJ = getNewTextArea(700, 500, 400, 30, 200, 600);
		JScrollPane spj = new JScrollPane(textJ);
		spj.setSize(700,500);
		spj.setBounds(700, 0, 662, 650);
		spj.setVisible(true);
		spj.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		spj.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(spj);
		
		textConsole = getNewTextArea(200, 200, 300, 300, 300, 300);
		JScrollPane spc = new JScrollPane(textConsole);
		spc.setSize(700,700);
		spc.setBounds(0, 500, 700, 212);
		spc.setVisible(true);
		spc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		spc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(spc);
		
		btnCompilar = new JButton("Compilar");
		btnCompilar.setBounds(702, 652, 660, 60);
		frame.getContentPane().add(btnCompilar);
		
		ArquivoVO arquivo;
		try {
			arquivo = FileUtil.montarArquivo(PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS10);
			textPys.setText(arquivo.getConteudoOriginal());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(frame, "Nenhum arquivo encontrado");
			e1.printStackTrace();
			textPys.setText("");
		}
		
		btnCompilar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					textJ.setText("");
					textConsole.setText("");
					String text = textPys.getText();
					FileUtil.writePysFile(PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS10, text);
					compilador = new CompiladorVO(PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS10);
					compilador.analisar();
					compilador.compilar();
					compilador.rodar();
					
					String resultadoJ = compilador.getResultadoJ();
					String resultadoJErro = compilador.getResultadoJErro();
					String resultadoClass = compilador.getResultadoClass();
					String resultadoClassErro = compilador.getResultadoClassErro();
					
					textJ.setText(compilador.getJCode());
					textJ.repaint();
					
					if (resultadoJ != null && !resultadoJ.isEmpty()) {
//							JOptionPane.showMessageDialog(frame, resultadoJ);
						textConsole.setText(resultadoJ);
					}
					if (resultadoJErro != null && !resultadoJErro.isEmpty()) {
						JOptionPane.showMessageDialog(frame, resultadoJErro);
						textConsole.setText(textConsole.getText() +"\n"+resultadoClass);
					}
					if (resultadoClass != null && !resultadoClass.isEmpty()) {
						textConsole.setText(textConsole.getText() +"\n"+resultadoClass);
					}
					if (resultadoClassErro != null && !resultadoClassErro.isEmpty()) {
						JOptionPane.showMessageDialog(frame, resultadoClassErro);
						textConsole.setText(textConsole.getText() +"\n"+resultadoClass);
					}
					
				} catch (Exception ex) {
					String message = "";
					if (ex instanceof AnaliseLexicaException) {
						message = "Erro léxico: "+ex.getMessage();
						System.out.println("Erro léxico: "+ex.getMessage());
					} else if (ex instanceof AnaliseSintaticaException) {
						System.out.println("Erro sintático: "+ex.getMessage());
						message = "Erro léxico: "+ex.getMessage();
					} else if (ex instanceof AnaliseSemanticaException){
						System.out.println("Erro semântico: "+ex.getMessage());
						message = "Erro léxico: "+ex.getMessage();
					} else {
						message = "Erro léxico: "+ex.getMessage();
						System.out.println("Erro: "+ex.getMessage());
					}
					JOptionPane.showMessageDialog(frame, message);
					ex.printStackTrace();
				}
			}
		});
		
	}
	
//	private JScrollPane getNewScroll(int width, int height, int b1, int b2, int b3, int b4) {
//		JScrollPane sp = new JScrollPane();
//		sp.setSize(width,height);
//		sp.setBounds(b1, b2, b3, b4);
//		sp.setVisible(true);
//		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		sp.setAutoscrolls(true);
//		return sp;
//	}
	
	private JTextArea getNewTextArea(int width, int height, int b1, int b2, int b3, int b4) {
		JTextArea textArea = new JTextArea();
		textArea.setToolTipText("Insert code here...");
		textArea.setTabSize(6);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea.setBounds(new Rectangle(10, 10, 10, 10));
		textArea.setBorder(new EmptyBorder(13, 10, 13, 10));
		textArea.setForeground(SystemColor.BLACK);
		textArea.setBackground(Color.GRAY);
		textArea.setBounds(b1, b2, b3, b4);
		textArea.setSize(width, height);
//		textArea.setPreferredSize(new Dimension(200, 200));
		textArea.setLineWrap(true);
		return textArea;
	}
}
