package it.prova.triage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.model.Utente;
import it.prova.triage.repository.paziente.PazienteRepository;
import it.prova.triage.web.api.exception.PazienteNonDimessoException;

public class PazienteServiceImpl implements PazienteService{
	
	@Autowired
	PazienteRepository repository;

	@Override
	public List<Paziente> listAllPazienti() {
		return (List<Paziente>) repository.findAll();

	}

	@Override
	public Paziente caricaSingoloPaziente(Long id) {
		return repository.findById(id).orElse(null);

	}


	@Override
	public Paziente aggiorna(Paziente pazienteInstance) {
		return repository.save(pazienteInstance);

		
	}

	@Override
	public Paziente inserisciNuovo(Paziente pazienteInstance) {
		pazienteInstance.setStato(StatoPaziente.IN_ATTESA_VISITA);
		return repository.save(pazienteInstance);


		
	}

	@Override
	public void rimuovi(Paziente pazienteInstance) {
		if (!pazienteInstance.getStato().equals(StatoPaziente.DIMESSO)) {
			throw new PazienteNonDimessoException("impossibile rimuovere un paziente non dimesso");
		}
		repository.delete(pazienteInstance);	
	}
	
	
	
	

}
