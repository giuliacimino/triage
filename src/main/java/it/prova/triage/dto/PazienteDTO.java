package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PazienteDTO {
	
	private Long id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private LocalDate dataRegistrazione;
	private StatoPaziente stato;
	private String codiceDottore;
	
	public Paziente buildRegistaModel() {
		return Paziente.builder().nome(this.nome).cognome(this.cognome).codiceFiscale(this.codiceFiscale).dataRegistrazione(this.dataRegistrazione).stato(this.stato).build();
	}

	public static PazienteDTO buildPazienteDTOFromModel(Paziente pazienteModel) {
		PazienteDTO result = PazienteDTO.builder().nome(pazienteModel.getNome()).cognome(pazienteModel.getCognome()).codiceFiscale(pazienteModel.getCodiceFiscale()).dataRegistrazione(pazienteModel.getDataRegistrazione()).stato(pazienteModel.getStato()).build();
		return result;
	}
	
	public static List<PazienteDTO> createPazienteDTOListFromModelList(List<Paziente> modelListInput) {
		return modelListInput.stream().map(pazienteEntity -> {
			PazienteDTO result = PazienteDTO.buildPazienteDTOFromModel(pazienteEntity);
			
			return result;
		}).collect(Collectors.toList());
	}
	
	
	
	

}
