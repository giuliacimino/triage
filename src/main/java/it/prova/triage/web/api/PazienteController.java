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

import it.prova.triage.model.Paziente;
import it.prova.triage.service.PazienteService;

@RestController
@RequestMapping("api/paziente")
public class PazienteController {
	
	@Autowired
	PazienteService pazienteService;
	
	
	@GetMapping
	public List<Paziente> getAll() {
		return pazienteService.listAllPazienti();
	}

	
	@GetMapping("/{id}")
	public Paziente caricaPaziente(@PathVariable(required = true) Long idInput) {
		return pazienteService.caricaSingoloPaziente(idInput);
	}
	
	
	@PostMapping("/inserisciNuovo")
	public Paziente createNewPaziente(@RequestBody Paziente pazienteInput) {
		return pazienteService.inserisciNuovo(pazienteInput);
	}
	
	@PutMapping("/{id}")
	public Paziente updatePaziente(@RequestBody Paziente pazienteInput, @PathVariable Long id) {
		Paziente pazienteToUpdate = pazienteService.caricaSingoloPaziente(id);
		pazienteToUpdate.setNome(pazienteInput.getNome());
		pazienteToUpdate.setCognome(pazienteInput.getCognome());
		pazienteToUpdate.setCodiceFiscale(pazienteInput.getCodiceFiscale());
		pazienteToUpdate.setDataRegistrazione(pazienteInput.getDataRegistrazione());
		pazienteToUpdate.setStato(pazienteInput.getStato());
		return pazienteService.aggiorna(pazienteToUpdate);
	}
	
	@DeleteMapping("/{id}")
	public void deletePaziente(@PathVariable(required = true) Long id) {
		pazienteService.rimuovi(pazienteService.caricaSingoloPaziente(id));
	}
	
	
	
	
	

}
