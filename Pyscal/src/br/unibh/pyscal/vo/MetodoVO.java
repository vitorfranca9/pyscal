package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import br.unibh.pyscal.enumerador.TipoComandoEnum;
import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;

//@Data
public class MetodoVO {
	private List<LinhaVO> linhas;
	private List<VariavelVO> parametros;
	private List<VariavelVO> variaveis;
	private String nome;
	private TipoRetornoMetodoEnum tipoRetornoMetodo;
	private boolean main;
	private boolean estatico;
	private Object retorno;
	
	private boolean isComando;
	private TipoComandoEnum tipoComando;
	private VariavelVO variavelRetorno;
	private List<VariavelVO> variaveisRetorno = new ArrayList<>();
	private List<MetodoVO> subMetodos = new ArrayList<>();
	private List<MetodoVO> subMetodosFalse = new ArrayList<>();
	private MetodoVO metodoPai;
	
	public List<LinhaVO> getLinhas() {
		return linhas;
	}
	public void setLinhas(List<LinhaVO> linhas) {
		this.linhas = linhas;
	}
	public List<VariavelVO> getParametros() {
		return parametros;
	}
	public void setParametros(List<VariavelVO> parametros) {
		this.parametros = parametros;
	}
	public List<VariavelVO> getVariaveis() {
		return variaveis;
	}
	public void setVariaveis(List<VariavelVO> variaveis) {
		this.variaveis = variaveis;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public TipoRetornoMetodoEnum getTipoRetornoMetodo() {
		return tipoRetornoMetodo;
	}
	public void setTipoRetornoMetodo(TipoRetornoMetodoEnum tipoRetornoMetodo) {
		this.tipoRetornoMetodo = tipoRetornoMetodo;
	}
	public boolean isMain() {
		return main;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	public boolean isEstatico() {
		return estatico;
	}
	public void setEstatico(boolean estatico) {
		this.estatico = estatico;
	}
	public Object getRetorno() {
		return retorno;
	}
	public void setRetorno(Object retorno) {
		this.retorno = retorno;
	}
	public boolean isComando() {
		return isComando;
	}
	public void setComando(boolean isComando) {
		this.isComando = isComando;
	}
	public TipoComandoEnum getTipoComando() {
		return tipoComando;
	}
	public void setTipoComando(TipoComandoEnum tipoComando) {
		this.tipoComando = tipoComando;
	}
	public VariavelVO getVariavelRetorno() {
		return variavelRetorno;
	}
	public void setVariavelRetorno(VariavelVO variavelRetorno) {
		this.variavelRetorno = variavelRetorno;
	}
	public List<VariavelVO> getVariaveisRetorno() {
		return variaveisRetorno;
	}
	public void setVariaveisRetorno(List<VariavelVO> variaveisRetorno) {
		this.variaveisRetorno = variaveisRetorno;
	}
	public List<MetodoVO> getSubMetodos() {
		return subMetodos;
	}
	public void setSubMetodos(List<MetodoVO> subMetodos) {
		this.subMetodos = subMetodos;
	}
	public List<MetodoVO> getSubMetodosFalse() {
		return subMetodosFalse;
	}
	public void setSubMetodosFalse(List<MetodoVO> subMetodosFalse) {
		this.subMetodosFalse = subMetodosFalse;
	}
	public MetodoVO getMetodoPai() {
		return metodoPai;
	}
	public void setMetodoPai(MetodoVO metodoPai) {
		this.metodoPai = metodoPai;
	}
	
}
