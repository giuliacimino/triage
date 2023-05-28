package it.prova.triage.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.service.PazienteService;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;

@RestController
@RequestMapping("api/paziente")
public class PazienteController {
	
	@Autowired
	PazienteService pazienteService;
	
	
	@GetMapping
	public List<PazienteDTO> getAll() {
		return PazienteDTO.createPazienteDTOListFromModelList(pazienteService.listAllPazienti());
	}

	
	@GetMapping("/{id}")
	public PazienteDTO caricaPaziente(@PathVariable(required = true) Long idInput) {
		return  PazienteDTO.buildPazienteDTOFromModel(pazienteService.caricaSingoloPaziente(idInput));
	}
	
	
	@PostMapping("/inserisciNuovo")
	public PazienteDTO createNewPaziente(@RequestBody Paziente pazienteInput) {
		if (pazienteInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		Paziente pazienteInserito = pazienteService.inserisciNuovo(pazienteInput);
		return PazienteDTO.buildPazienteDTOFromModel(pazienteInserito);
	}
	
	@PutMapping("/{id}")
	public PazienteDTO updatePaziente(@RequestBody Paziente pazienteInput, @PathVariable Long id) {
		Paziente pazienteToUpdate = pazienteService.caricaSingoloPaziente(id);
		pazienteToUpdate.setNome(pazienteInput.getNome());
		pazienteToUpdate.setCognome(pazienteInput.getCognome());
		pazienteToUpdate.setCodiceFiscale(pazienteInput.getCodiceFiscale());
		pazienteToUpdate.setDataRegistrazione(pazienteInput.getDataRegistrazione());
		pazienteToUpdate.setStato(pazienteInput.getStato());
		return PazienteDTO.buildPazienteDTOFromModel(pazienteService.aggiorna(pazienteToUpdate));
	}
	
	@DeleteMapping("/{id}")
	public void deletePaziente(@PathVariable(required = true) Long id) {
		pazienteService.rimuovi(pazienteService.caricaSingoloPaziente(id));
	}
	
	
	
	
	

}
