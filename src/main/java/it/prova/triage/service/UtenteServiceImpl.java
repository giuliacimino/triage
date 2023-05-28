package it.prova.triage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.prova.triage.model.StatoUtente;
import it.prova.triage.model.Utente;
import it.prova.triage.repository.utente.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService{
	
	@Autowired
	UtenteRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();

	}

	@Override
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);

	}

	@Override
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);

	}

	@Override
	public Utente aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
				Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
				if (utenteReloaded == null)
					throw new RuntimeException("Elemento non trovato");
				utenteReloaded.setNome(utenteInstance.getNome());
				utenteReloaded.setCognome(utenteInstance.getCognome());
				utenteReloaded.setUsername(utenteInstance.getUsername());
				utenteReloaded.setRuoli(utenteInstance.getRuoli());
				return repository.save(utenteReloaded);
		
	}

	@Override
	public Utente inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(LocalDate.now());
		return repository.save(utenteInstance);
		
	}

	@Override
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);

		
	}

	@Override
	public List<Utente> findByExample(Utente example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);


	}

	@Override
	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);

	}

	@Override
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		
	}

	@Override
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);

	}
	
	

}
