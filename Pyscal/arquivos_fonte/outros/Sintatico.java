package exemplosemantico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author moises
 */
public class Sintatico {

    private final Lexico lexico;
    private Token token;
    private int endereco = 0;
    private int rotulo = 0;
    
    private String nomeArquivo;
    private BufferedWriter arquivoCodigo;
    private final HashMap<Integer,String[]> tabelaComandos;

    public Sintatico(Lexico lexico) {

        this.lexico = lexico;
        this.tabelaComandos = new HashMap<>();
        this.tabelaComandos.put(No.TIPO_INT, new String[]{"I","i"});
        this.tabelaComandos.put(No.TIPO_STRING, new String[]{"Ljava/lang/String;", "a"});
        this.tabelaComandos.put(No.TIPO_BOOLEAN, new String[]{"I","i"});
        this.tabelaComandos.put(Token.MAIOR, new String[]{"cmple",""});
        this.tabelaComandos.put(Token.MAIOR_IGUAL, new String[]{"cmplt",""});
    }

    // Fecha os arquivos de entrada e de tokens.
    public void fechaArquivos() {

        lexico.fechaArquivo();
    }

    // Imprime ocorrencia de erro sintatico.
    public void erroSintatico(String mensagem) {

        System.out.println("Erro sintatico na linha " + token.getLinha() + " e coluna " + token.getColuna() + ":");
        System.out.println(mensagem + "\n");
    }
    
    // Imprime ocorrencia de erro semantico.
    public void erroSemantico(String mensagem, Token token) {

        System.out.println("Erro semantico na linha " + token.getLinha() + " e coluna " + token.getColuna() + ":");
        System.out.println(mensagem + "\n");
    }
    
    // Verifica se o token esperado = token atual. Se sim, desloca para o proximo.
    public boolean casaToken(int numeroToken) {

        while (token.getClasse() == Lexico.ERRO) {
            token = lexico.proxToken();
        }
        if (token.getClasse() == numeroToken) {
            token = lexico.proxToken();
            return true;
        } 
        return false;
    }
    
    // Verifica se o token esperado = token atual
    public boolean isToken(int numeroToken) {

        return token.getClasse() == numeroToken;
    }
    
    // Gera a quantidade de espacos necessarios para identacao em um escopo.
    public String geraIdentacao(int espaco) {
        
        String identacao = "";
        for (int i = 0; i < espaco; i++) {
            identacao += "    ";
        }
        return identacao;
    }
    
    // Gera um novo rotulo (label) de desvios no Assembly JVM.
    public String novoRotulo() {
         return "L" + this.rotulo++;
    }
    
    // Imprime a saida do prompt/terminal do SO para sucesso ou fracasso de comandos.
    public void imprimeSaidaComando(InputStream tipoSaida) throws IOException {
        String linha;
        BufferedReader input = new BufferedReader(new InputStreamReader(tipoSaida));
        while ((linha = input.readLine()) != null) {
            System.out.println(linha);
        }
    }
    
    // Cria o arquivo .j com o código assembly gerado na analise semantica.
    public void criaArquivoAssembly(String codigo) {
        
        try {
            arquivoCodigo = new BufferedWriter(new FileWriter(nomeArquivo + ".j"));
            arquivoCodigo.write(codigo);
            arquivoCodigo.flush();
            arquivoCodigo.close();
            
            Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + nomeArquivo + ".j");
            imprimeSaidaComando(cmd.getInputStream());
            imprimeSaidaComando(cmd.getErrorStream());
            
            cmd = Runtime.getRuntime().exec("java " + nomeArquivo);
            imprimeSaidaComando(cmd.getInputStream());
            imprimeSaidaComando(cmd.getErrorStream());
        }
        catch (IOException erroArquivo) {
            System.out.println("Erro de criacao do arquivo " + nomeArquivo + ".j");
            System.out.println(erroArquivo.getMessage());
            System.exit(7);
        }
    }

    // Programa --> Classe EOF
    public void Programa() {

        token = lexico.proxToken(); // Leitura inicial obrigatoria do primeiro

        No noClasse = Classe(1);
        if (!casaToken(Token.EOF)) {
            erroSintatico("Esperado final de arquivo, encontrado " + token.getLexema());
        }
        
        System.out.println();
        if (noClasse.tipo == No.TIPO_VAZIO) {
            noClasse.imprimeConteudo();
            System.out.println("\nCompilação de Programa Realizada!\n");
            criaArquivoAssembly(noClasse.code);
        }
    }

    // Classe --> "public" "class" ID ListaDeclaraVar ListaCmd "end"
    public No Classe(int espaco) {

        if (!casaToken(Token.PUBLIC)) {
            erroSintatico("Esperado public, encontrado " + token.getLexema());
        }
        if (!casaToken(Token.CLASS)) {
            erroSintatico("Esperado class, encontrado " + token.getLexema());
        }
        
        No noClasse = new No(null);
        Token t = this.token;
        
        if (!casaToken(Token.ID)) {
            erroSintatico("Esperado um identificador, encontrado " + token.getLexema());
        } else {
            this.nomeArquivo = t.getLexema();
            
            No noListaVar = ListaDeclaraVar();
            No noListaCmd = ListaCmd(++espaco);
            
            if (!casaToken(Token.END)) {
                erroSintatico("Esperado end, encontrado " + token.getLexema());
            }
            
            if (noListaVar.tipo != No.TIPO_ERRO & noListaCmd.tipo == No.TIPO_VAZIO) {
                noClasse.tipo = No.TIPO_VAZIO;
            } else {
                noClasse.tipo = No.TIPO_ERRO;
            }
            
            // Apenas para reverter a ordem dos filhos do padrão recursivo.
            Collections.reverse(noListaCmd.getFilhos());
            for(No filho : noListaCmd.getFilhos()) {
                noListaCmd.code += filho.code;
            }
            
            String identa = geraIdentacao(espaco);
            noClasse.code = ".class public " + t.getLexema() + "\n" +
                            ".super java/lang/Object\n" +
                            identa + ".method public static main([Ljava/lang/String;)V\n" +
                            identa + ".limit stack 50\n" +
                            identa + ".limit locals 50\n" + 
                            noListaCmd.code + 
                            identa + "    return\n" +
                            identa + ".end method";
            
            noClasse.setPai(t);
            noClasse.addFilho(noListaCmd);
        }
        
        return noClasse;
    }
    
    // ListaDeclaraVar --> TipoPrimitivo ID ";" ListaDeclaraVar | λ
    public No ListaDeclaraVar() {
        
        No noVar = new No(null);
        
        if (isToken(Token.NUM_INT) || isToken(Token.STRING)) {
            No noTipo = TipoPrimitivo();
            Token t = this.token;
            
            if (!casaToken(Token.ID)) {
                erroSintatico("Esperado identificador, encontrado " + t.getLexema());
            } else {
                Token id = Lexico.tabela.get(t.getLexema());
                if (id != null) {
                    noVar.tipo = No.TIPO_ERRO;
                    erroSemantico("Declaracao duplicada da variavel " + t.getLexema(), t);
                }
                else {
                    noVar.tipo = noTipo.tipo;
                    t.setTipo(noTipo.tipo);
                    t.setEndereco(endereco++);
                    Lexico.tabela.put(t.getLexema(), t);
                }
                
                if (!casaToken(Token.PONTO_VIRGULA)) {
                    erroSintatico("Esperado ;, encontrado " + token.getLexema());
                }
                No noListaVar = ListaDeclaraVar();
                
                noVar.setPai(t);
                Collections.reverse(noListaVar.getFilhos()); // Apenas para reverter a ordem dos filhos do padrão recursivo.
                noVar.addFilho(noListaVar);
            }
            
        }
        
        return noVar;
    }
    
    // TipoPrimitivo --> numInt | boolean | String
    public No TipoPrimitivo() {
        
        No noTipo = new No(null);
        Token t = this.token;
        
        if (casaToken(Token.NUM_INT)) {
            noTipo.setPai(t);
            noTipo.tipo = No.TIPO_INT;
        } else if (casaToken(Token.BOOLEAN)) {
            noTipo.setPai(t);
            noTipo.tipo = No.TIPO_BOOLEAN;
        } else if (casaToken(Token.STRING)) {
            noTipo.setPai(t);
            noTipo.tipo = No.TIPO_STRING;
        }
        
        return noTipo;
    }
    
    // ListaCmd --> CmdDispln ListaCmd | λ
    public No ListaCmd(int espaco) {
        
        No noListaCmd = new No(null);
        
        // Usando o isToken para decidir sobre o First de CmdDispln ou de CmdAtrib
        if (isToken(Token.SYSOUTDISPLN)) {
            No noCmdDispln = CmdDispln(espaco);
            noListaCmd = ListaCmd(espaco);
            
            // Simplesmente redireciona o tipo da expressao para o comando atual
            // se a lista de comandos anteriores está correta
            if (noListaCmd.tipo == No.TIPO_VAZIO) {
                noListaCmd.tipo = noCmdDispln.tipo;
            }
            
            noListaCmd.addFilho(noCmdDispln);
        } else if (isToken(Token.ID)) {
            No noCmdAtrib = CmdAtrib(espaco);
            noListaCmd = ListaCmd(espaco);
            
            // Simplesmente redireciona o tipo da expressao para o comando atual
            // se a lista de comandos anteriores está correta
            if (noListaCmd.tipo == No.TIPO_VAZIO) {
                noListaCmd.tipo = noCmdAtrib.tipo;
            }
            
            noListaCmd.addFilho(noCmdAtrib);
        }
        
        return noListaCmd;
    }

    // CmdDispln --> "SystemOutDispln" "(" Expressao ")" ";"
    public No CmdDispln(int espaco) {

        No noCmdDispln = new No(null);
        Token t = this.token;
        String tipoAssembly = null;
        
        if (!casaToken(Token.SYSOUTDISPLN)) {
            erroSintatico("Esperado SystemOutDispln, encontrado " + token.getLexema());
        } else {
            if (!casaToken(Token.ABRE_PAREN)) {
                erroSintatico("Esperado (, encontrado " + token.getLexema());
            } else {
                No noExp = Expressao(++espaco);
                
                if (!casaToken(Token.FECHA_PAREN)) {
                    erroSintatico("Esperado ), encontrado " + token.getLexema());
                }
                if (!casaToken(Token.PONTO_VIRGULA)) {
                    erroSintatico("Esperado ;, encontrado " + token.getLexema());
                }
                
                // Simplesmente redireciona o tipo da expressao para o comando
                if (noExp.tipo == No.TIPO_ERRO) {
                    noCmdDispln.tipo = No.TIPO_ERRO;
                } else {
                    noCmdDispln.tipo = No.TIPO_VAZIO;
                    tipoAssembly = tabelaComandos.get(noExp.tipo)[0];
                }
                
                String identa = geraIdentacao(espaco);
                noCmdDispln.code = identa + "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                                   "    " + noExp.code +
                                   identa +"invokevirtual java/io/PrintStream/println(" + tipoAssembly + ")V\n";
                
                noCmdDispln.setPai(t);
                noCmdDispln.addFilho(noExp);
            }
        }
        
        return noCmdDispln;
    }
    
    public No CmdAtrib(int espaco) {
        
        No noCmdAtrib = new No(null);
        Token t1 = this.token;
        String tipoAssembly;
        
        if (!casaToken(Token.ID)) {
            erroSintatico("Esperado identificador, encontrado " + token.getLexema());
        } else {
            Token t2 = this.token;
            if (!casaToken(Token.ATRIBUICAO)) {
                erroSintatico("Esperado =, encontrado " + token.getLexema());
            } else {
                No noExp = Expressao(++espaco);
                No noID = new No(null);
                
                if (!casaToken(Token.PONTO_VIRGULA)) {
                    erroSintatico("Esperado ;, encontrado " + token.getLexema());
                }
                
                Token id = Lexico.tabela.get(t1.getLexema());
                if (id == null) {
                    noCmdAtrib.tipo = No.TIPO_ERRO;
                    erroSemantico("Variavel " + t1.getLexema() + " nao declarada", t1);
                } else if (id.getTipo() == noExp.tipo) {
                    noID.setPai(id);
                    noID.tipo = id.getTipo();
                    noCmdAtrib.tipo = No.TIPO_VAZIO;
                    tipoAssembly = tabelaComandos.get(noExp.tipo)[1];
                    
                    String identa = geraIdentacao(espaco);
                    noCmdAtrib.code = noExp.code + 
                                      identa + tipoAssembly + "store " + id.getEndereco() + "\n";
                } else {
                    noCmdAtrib.tipo = No.TIPO_ERRO;
                    erroSemantico("Atribuicao de valor em variavel de tipo incompativel", t2);
                }
                
                noCmdAtrib.setPai(t2);
                noCmdAtrib.addFilho(noID);
                noCmdAtrib.addFilho(noExp);
            }
        }
        
        return noCmdAtrib;
    }

    // Expressao --> Expressao1 Expressao'
    public No Expressao(int espaco) {
        
        No noExp1 = Expressao1(espaco);
        No noExpLinha = ExpressaoLinha(++espaco);
        
        if (noExpLinha.getPai() != null) {
            if (noExpLinha.tipo == noExp1.tipo & noExp1.tipo == No.TIPO_INT) {
                noExpLinha.tipo = No.TIPO_BOOLEAN;
            } else {
                noExpLinha.tipo = No.TIPO_ERRO;
                erroSemantico("Comparacao entre tipos incompativeis", noExp1.getPai());
            }
            
            noExpLinha.code = noExp1.code + noExpLinha.code;
            
            noExpLinha.addFilho(noExp1);
            Collections.reverse(noExpLinha.getFilhos()); // Apenas para reverter a ordem dos filhos do padrão recursivo.
            return noExpLinha;
        }
        
        return noExp1;
    }
    
    // Expressao' --> ">" Expressao1 Expressao'  |  ">=" Expressao1 Expressao'  |  λ
    public No ExpressaoLinha(int espaco) {
        
        No noExpLinhaPai = new No(null);
        Token t = this.token;
        String tipoAssembly = null;
        String tipoOperador;
        
        if (casaToken(Token.MAIOR) || casaToken(Token.MAIOR_IGUAL)) {
            No noExp1 = Expressao1(espaco);
            No noExpLinhaFilho = ExpressaoLinha(espaco);
            
            String identa = geraIdentacao(espaco);
            String continuacaoCodigo = "";
            
            if (noExpLinhaFilho.tipo == No.TIPO_VAZIO & noExp1.tipo == No.TIPO_INT) {
                noExpLinhaPai.tipo = noExp1.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
            } else if (noExpLinhaFilho.tipo == noExp1.tipo & noExp1.tipo == No.TIPO_INT) {
                noExpLinhaPai.tipo = noExp1.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
                continuacaoCodigo = noExp1.code + 
                                    noExpLinhaFilho.code +
                                    identa + "iand\n";
            } else {
                noExpLinhaPai.tipo = No.TIPO_ERRO;
            }
            
            tipoOperador = tabelaComandos.get(t.getClasse())[0];
            noExpLinhaPai.next = novoRotulo();
            noExpLinhaPai.fail = novoRotulo();
            
            noExpLinhaPai.code = noExp1.code + 
                                 identa + "if_" + tipoAssembly + tipoOperador + " " + noExpLinhaPai.fail + "\n" +
                                 identa + "ldc 1\n" +
                                 identa + "goto " + noExpLinhaPai.next + "\n" +
                                 identa + noExpLinhaPai.fail + ": \n" + 
                                 identa + "ldc 0\n" +
                                 identa + noExpLinhaPai.next + ":\n" +
                                 continuacaoCodigo;
            
            noExpLinhaPai.setPai(t);
            noExpLinhaFilho.addFilho(noExp1);
            noExpLinhaPai.addFilho(noExpLinhaFilho);
        }
        
        return noExpLinhaPai;
    }
    
    // Expressao1 --> Expressao2 Expressao1'
    public No Expressao1(int espaco) {
        
        No noExp2 = Expressao2(espaco);
        No noExp1Linha = Expressao1Linha(++espaco);
        
        if (noExp1Linha.getPai() != null) {
            if (noExp1Linha.tipo == noExp2.tipo & noExp2.tipo == No.TIPO_INT) {
                noExp1Linha.tipo = No.TIPO_INT;
            } else {
                noExp1Linha.tipo = No.TIPO_ERRO;
                erroSemantico("Adicao entre tipos incompativeis", noExp2.getPai());
            }
            
            noExp1Linha.code = noExp2.code + noExp1Linha.code;
            
            noExp1Linha.addFilho(noExp2);
            Collections.reverse(noExp1Linha.getFilhos()); // Apenas para reverter a ordem dos filhos do padrão recursivo.
            return noExp1Linha;
        }
        
        return noExp2;
    }
    
    // Expressao1' --> "+" Expressao2 Expressao1'  |  λ
    public No Expressao1Linha(int espaco) {
        
        No noExp1LinhaPai = new No(null);
        Token t = this.token;
        String tipoAssembly = null;
        
        if (casaToken(Token.MAIS)) {
            No noExp2 = Expressao2(espaco);
            No noExp1LinhaFilho = Expressao1Linha(espaco);

            if (noExp1LinhaFilho.tipo == No.TIPO_VAZIO & noExp2.tipo == No.TIPO_INT) {
                noExp1LinhaPai.tipo = noExp2.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
            } else if (noExp1LinhaFilho.tipo == noExp2.tipo & noExp2.tipo == No.TIPO_INT) {
                noExp1LinhaPai.tipo = noExp2.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
            } else {
                noExp1LinhaPai.tipo = No.TIPO_ERRO;
            }
            
            String identa = geraIdentacao(espaco);
            noExp1LinhaPai.code = noExp2.code + 
                                  noExp1LinhaFilho.code + 
                                  identa + tipoAssembly + "add\n";
            
            noExp1LinhaPai.setPai(t);
            noExp1LinhaFilho.addFilho(noExp2);
            noExp1LinhaPai.addFilho(noExp1LinhaFilho);
        }

        return noExp1LinhaPai;
    }
    
    // Expressao2 --> Expressao3 Expressao2'
    public No Expressao2(int espaco) {
        
        No noExp3 = Expressao3(espaco);
        No noExp2Linha = Expressao2Linha(espaco);
        
        if (noExp2Linha.getPai() != null) {
            if (noExp2Linha.tipo == noExp3.tipo & noExp3.tipo == No.TIPO_INT) {
                noExp2Linha.tipo = No.TIPO_INT;
            } else {
                noExp2Linha.tipo = No.TIPO_ERRO;
                erroSemantico("Multiplicacao entre tipos incompativeis", noExp3.getPai());
            }
            
            noExp2Linha.code = noExp3.code + noExp2Linha.code;
            
            noExp2Linha.addFilho(noExp3);
            Collections.reverse(noExp2Linha.getFilhos()); // Apenas para reverter a ordem dos filhos do padrão recursivo.
            return noExp2Linha;
        }
        
        return noExp3;
    }
    
    // Expressao2' --> "*" Expressao3 Expressao2'  |  λ
    public No Expressao2Linha(int espaco) {
        
        No noExp2LinhaPai = new No(null);
        Token t = this.token;
        String tipoAssembly = null;
        
        if (casaToken(Token.VEZES)) {
            No noExp3 = Expressao3(espaco);
            No noExp2LinhaFilho = Expressao2Linha(espaco);
            
            if (noExp2LinhaFilho.tipo == No.TIPO_VAZIO & noExp3.tipo == No.TIPO_INT) {
                noExp2LinhaPai.tipo = noExp3.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
            } else if (noExp2LinhaFilho.tipo == noExp3.tipo & noExp3.tipo == No.TIPO_INT) {
                noExp2LinhaPai.tipo = noExp3.tipo;
                tipoAssembly = tabelaComandos.get(No.TIPO_INT)[1];
            } else {
                noExp2LinhaPai.tipo = No.TIPO_ERRO;
            }
            
            String identa = geraIdentacao(espaco);
            noExp2LinhaPai.code = noExp3.code + 
                                  noExp2LinhaFilho.code + 
                                  identa + tipoAssembly + "mul\n";
            
            noExp2LinhaPai.setPai(t);
            noExp2LinhaFilho.addFilho(noExp3);
            noExp2LinhaPai.addFilho(noExp2LinhaFilho);
        }
        
        return noExp2LinhaPai;
    }
    
    // Expressao3 --> ConstInt  |  ConstString  |  ID
    public No Expressao3(int espaco) {
        
        No noExp3 = new No(null);
        Token t = this.token;
        String tipoAssembly;
        
        String identa = geraIdentacao(espaco);
        if (casaToken(Token.CONST_INT)) {
            noExp3.setPai(t);
            noExp3.tipo = No.TIPO_INT;
            noExp3.code = identa + "ldc " + t.getLexema() + "\n";
        } else if (casaToken(Token.CONST_STRING)) {
            noExp3.setPai(t);
            noExp3.tipo = No.TIPO_STRING;
            noExp3.code = identa + "ldc \"" + t.getLexema() + "\"\n";
        } else if (casaToken(Token.ID)) {
            Token id = Lexico.tabela.get(t.getLexema());
            if (id == null) {
                noExp3.tipo = No.TIPO_ERRO;
                erroSemantico("Variavel " + t.getLexema() + " nao declarada", t);
            } else {
                noExp3.tipo = id.getTipo();
                tipoAssembly = tabelaComandos.get(id.getTipo())[1];
                noExp3.code = identa + tipoAssembly + "load " + id.getEndereco() + "\n";
            }
            noExp3.setPai(t);
        } else {
            noExp3.tipo = No.TIPO_ERRO;
            erroSintatico("Esperado numero inteiro, string ou identificador, encontrado " + token.getLexema());
        }
        
        return noExp3;
    }
}
