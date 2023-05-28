package it.prova.triage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.model.Utente;
import it.prova.triage.repository.paziente.PazienteRepository;
import it.prova.triage.web.api.exception.PazienteNonDimessoException;
import it.prova.triage.web.api.exception.PazienteNonInVisitaException;
import it.prova.triage.web.api.exception.PazienteNotFoundException;

@Service
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

	@Override
	public void impostaCodiceDottore(String codiceFiscalePaziente, String codiceDottore) {
		Paziente result = repository.findByCodiceFiscale(codiceFiscalePaziente).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("errrore: paziente non trovato.");
		
		result.setCodiceDottore(codiceDottore);
		result.setStato(StatoPaziente.IN_VISITA);
		
		repository.save(result);

		
	}

	@Override
	public void ricovera(Long id) {
Paziente result = repository.findById(id).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		if(!result.getStato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNonInVisitaException("errore: il paziente non è in visita.");
		
		result.setStato(StatoPaziente.RICOVERATO);
		result.setCodiceDottore(null);
		
		repository.save(result);
		
	}

	@Override
	public void dimetti(Long id) {
		Paziente result = repository.findById(id).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		if(!result.getStato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNonInVisitaException("errore: il paziente non è in visita");
		
		result.setStato(StatoPaziente.DIMESSO);
		result.setCodiceDottore(null);
		
		repository.save(result);
		
	}
	
	
	
	

}
