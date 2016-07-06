package br.unibh.pyscal.vo;

public class EnderecoStackVO {
	private String key;
	private int index;
	
	public EnderecoStackVO() { }
	
	public EnderecoStackVO(String key, int index) {
		this.key = key;
		this.index = index;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
