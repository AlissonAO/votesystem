package br.com.votesystem.endpoints;

import static br.com.votesystem.util.ApplicationConstants.ENDPOINT_VOTO_SESSAO_V1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.votesystem.domain.dto.VotacaoSessaoDTO;
import br.com.votesystem.services.interfaces.IEndpointService;
import br.com.votesystem.services.interfaces.IVotoSessaoService;
@RestController
@RequestMapping(ENDPOINT_VOTO_SESSAO_V1)
public class VotoSessaoEndpoint {

    private IVotoSessaoService votoSessaoService;
    private IEndpointService endpointService;
    private static final String RETRIEVE_TEMPLATE = ENDPOINT_VOTO_SESSAO_V1 + "/%s";

    /**
     * Constructor
     *
     * @param votoSessaoService
     * @param endpointService
     */
    @Autowired
    public VotoSessaoEndpoint(final IVotoSessaoService votoSessaoService,
                               final IEndpointService endpointService) {
        this.votoSessaoService = votoSessaoService;
        this.endpointService = endpointService;
    }

    /**
     * Add a poll
     *
     * @param value
     * @return poll added
     */
    @PostMapping("/")
    public ResponseEntity<String> add(@Valid @RequestBody final VotacaoSessaoDTO value, final HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(endpointService.process(RETRIEVE_TEMPLATE, votoSessaoService.add(value), request));
    }

}
