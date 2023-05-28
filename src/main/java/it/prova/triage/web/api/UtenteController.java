package it.prova.triage.web.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import it.prova.triage.dto.UtenteDTO;
import it.prova.triage.model.Ruolo;
import it.prova.triage.model.Utente;
import it.prova.triage.security.dto.UtenteInfoJWTResponseDTO;
import it.prova.triage.service.UtenteService;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.UtenteNotAuthorizedException;
import it.prova.triage.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

	
	@Autowired
	UtenteService utenteService;
	
	@GetMapping(value = "/userInfo")
	public ResponseEntity<UtenteInfoJWTResponseDTO> getUserInfo() {

		// se sono qui significa che sono autenticato quindi devo estrarre le info dal
		// contesto
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		List<String> ruoli = utenteLoggato.getRuoli().stream().map(item -> item.getCodice())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new UtenteInfoJWTResponseDTO(utenteLoggato.getNome(), utenteLoggato.getCognome(),
				utenteLoggato.getUsername(), ruoli));
	}
	
	@GetMapping
	public List<UtenteDTO> listAll(){
		
		// controllo che id dell'utente sia se stesso
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		
		boolean eUtente = false;

		for (Ruolo ruoloItem : utenteLoggato.getRuoli()) {
			if (ruoloItem.getDescrizione().equals("Administrator")) {
				eUtente = true;
			}
		}
		if(!eUtente)
			throw new UtenteNotAuthorizedException("Non si e' autorizzati all'operazione.");
		return UtenteDTO.createUtenteDTOListFromModelList(utenteService.listAllUtenti());
	}
	
	
	
	
	@PostMapping
	public UtenteDTO insertNew(Utente utenteInstance) {
		
		// controllo che id dell'utente sia se stesso
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		boolean eUtente = false;

		for (Ruolo ruoloItem : utenteLoggato.getRuoli()) {
			if (ruoloItem.getDescrizione().equals("Administrator")) {
				eUtente = true;
			}
		}
		if(!eUtente)
			throw new UtenteNotAuthorizedException("Non si e' autorizzati all'operazione.");
		
		if (utenteInstance.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		 Utente utenteInserito= utenteService.inserisciNuovo(utenteInstance);
		
		return UtenteDTO.buildUtenteDTOFromModel(utenteInserito);
	}
	
	
	
	@GetMapping("/{id}")
	public UtenteDTO findUtente(@PathVariable(value = "id", required = true) Long id) {
		Utente utenteCaricato = utenteService.caricaSingoloUtente(id);
		if (utenteCaricato == null) {
			throw new UtenteNotFoundException("Utente not found con id: "+id);
		}
		return UtenteDTO.buildUtenteDTOFromModel(utenteCaricato);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public void elimina (@PathVariable(value = "id", required =  true) Long id) {
		Utente utenteCaricato = utenteService.caricaSingoloUtente(id);
		if (utenteCaricato == null) {
			throw new UtenteNotFoundException("Utente not found con id: "+id);
		}
		utenteService.rimuovi(id);
	}
	
	
	@PutMapping("/aggiorna/{id}")
	public UtenteDTO update(@Valid @RequestBody UtenteDTO utenteInput, @PathVariable(value = "id",required = true) Long id) {
		Utente utenteCaricato = utenteService.caricaSingoloUtente(id);
		if (utenteCaricato == null) {
			throw new UtenteNotFoundException("Utente not found con id: "+id);
		}
		utenteInput.setId(id);
		Utente utenteAggiornato = utenteService.aggiorna(utenteInput.buildUtenteModel(false));
		return UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);
	}
	
	

}