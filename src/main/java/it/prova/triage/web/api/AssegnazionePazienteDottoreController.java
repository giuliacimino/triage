package it.prova.triage.web.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottorePazienteDTO;
import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.service.PazienteService;
import it.prova.triage.web.api.exception.DottoreNonDisponibileException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/assegnaPaziente")
public class AssegnazionePazienteDottoreController {
	private static final Logger LOGGER = LogManager.getLogger(AssegnazionePazienteDottoreController.class);

	@Autowired
	private WebClient webClient;
	@Autowired
	private PazienteService pazienteService;

	
	@GetMapping("/{codicedottore}")
	public DottoreResponseDTO verificaDisponibilitaDottore(@PathVariable(required = true) String codicedottore) {

		LOGGER.info(".........invocazione servizio esterno............");

		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verificaDisponibilitaDottore/" + codicedottore).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> {
					throw new DottoreNonDisponibileException("errore: il dottore non è disponibile.");
				}).bodyToMono(DottoreResponseDTO.class).block();

		LOGGER.info(".........invocazione servizio esterno completata............");

		return dottoreResponseDTO;
	}
	
	
	
	@PostMapping("/impostaVisita")
	public DottorePazienteDTO impostaVisita(@RequestBody DottorePazienteDTO dottoreInstance) {
		pazienteService.impostaCodiceDottore(dottoreInstance.getCodFiscalePazienteAttualmenteInVisita(),
				dottoreInstance.getCodiceDottore());
		LOGGER.info(".........invocazione servizio esterno............");

		ResponseEntity<DottorePazienteDTO> response = webClient.post().uri("/impostaVisita")
				.body(Mono.just(new DottorePazienteDTO(dottoreInstance.getCodiceDottore(),
						dottoreInstance.getCodFiscalePazienteAttualmenteInVisita())), DottorePazienteDTO.class)
				.retrieve().toEntity(DottorePazienteDTO.class).block();

		LOGGER.info(".........invocazione servizio esterno completata............");

		return new DottorePazienteDTO(response.getBody().getCodiceDottore(),
				response.getBody().getCodFiscalePazienteAttualmenteInVisita());
	}
	
	
	
	
	
	
	


}
