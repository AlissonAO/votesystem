package br.com.votesystem.services.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.votesystem.enuns.CpfState;
import br.com.votesystem.services.interfaces.ICpfValidatorService;
import br.com.votesystem.util.StringUtil;

@Service
public class CpfValidatorServiceImpl implements ICpfValidatorService {
    @Value("${cpf.validator.endpoint:https://user-info.herokuapp.com/users}")
    private String endpoint;

    private final RestTemplate restTemplate;
    private final Logger logger;

    /**
     * Constructor
     */
    public CpfValidatorServiceImpl(final Logger logger) {
        this.restTemplate = new RestTemplate();
        this.logger = logger;
    }


    @Override
    public CpfState checkCpf(final String identification) {
        return request(identification != null ? StringUtil.clean(identification) : "");
    }


    private CpfState request(final String identification) {
        ResponseEntity<String> response = null;
        CpfState state = null;

        try {
            response = restTemplate.exchange(endpoint + "/" + identification, HttpMethod.GET, null, String.class);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                if (response.getBody().contains("UNABLE")) {
                    state = CpfState.UNABLE;
                } else if (response.getBody().contains("ABLE")) {
                    state = CpfState.ABLE;
                }
            }

            logger.info("Requeisicao CPF validacao  {} resposta: {}", identification, state);
        } catch (Exception e) {
            if (e.getMessage().contains("404")) {
                state = CpfState.NOT_FOUND;
                logger.info("Requeisicao CPF validacao  {} resposta: {}", identification, state);
            } else {
                logger.error("ERROR na requisicao do CPF ", e);
            }
        }

        return state;
    }
}
