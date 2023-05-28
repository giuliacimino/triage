package it.prova.triage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DottoreResponseDTO {
	
	private String nome;
	private String cognome;
	private String codFiscalePazienteAttualmenteInVisita;
	private boolean inVisita;
	private boolean inServizio;

}
