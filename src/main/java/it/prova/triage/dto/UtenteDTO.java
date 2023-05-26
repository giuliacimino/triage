package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import it.prova.triage.model.Ruolo;
import it.prova.triage.model.StatoUtente;
import it.prova.triage.model.Utente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteDTO {
	private Long id;
	private String username;
	private String password;
	private String nome;
	private String cognome;
	private LocalDate dataRegistrazione;
	private StatoUtente stato;
	private Long[] ruoliIds;
	
	

	public Utente buildUtenteModel(boolean includeIdRoles) {
		Utente result = Utente.builder().username(this.username).password(this.password).nome(this.nome).cognome(this.cognome).dataRegistrazione(this.dataRegistrazione).stato(this.stato).build();
		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> Ruolo.builder().id(this.id).build()).collect(Collectors.toSet()));

		return result;
	}
	

	// niente password...
	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = UtenteDTO.builder().username(utenteModel.getUsername()).password(utenteModel.getPassword()).nome(utenteModel.getNome()).cognome(utenteModel.getCognome()).dataRegistrazione(utenteModel.getDataRegistrazione()).stato(utenteModel.getStato()).build();

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}
	
	

}
