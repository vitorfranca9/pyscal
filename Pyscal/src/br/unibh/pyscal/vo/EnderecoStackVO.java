package br.unibh.pyscal.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of="key")
@ToString
public class EnderecoStackVO {
	@Getter @Setter private String key;
	@Getter @Setter private int index;
}
