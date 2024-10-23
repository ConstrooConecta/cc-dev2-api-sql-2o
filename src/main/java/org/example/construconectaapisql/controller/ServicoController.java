package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.model.Servico;
import org.example.construconectaapisql.model.TagServico;
import org.example.construconectaapisql.repository.ServicoRepository;
import org.example.construconectaapisql.repository.TagServicoRepository;
import org.example.construconectaapisql.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/service")
public class ServicoController {
    private final ServicoRepository servicoRepository;
    private final TagServicoRepository tagServicoRepository;

    private final ServicoService servicoService;
    private final Validator validator;

    @Autowired
    public ServicoController(
            ServicoRepository servicoRepository,
            TagServicoRepository tagServicoRepository,
            ServicoService servicoService,
            Validator validator
    ) {
        this.servicoRepository = servicoRepository;
        this.tagServicoRepository = tagServicoRepository;
        this.servicoService = servicoService;
        this.validator = validator;
    }

    @GetMapping("/services")
    @Operation(summary = "Show all services", description = "Returns a list of all available services")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Servico> findAllServices() { return servicoService.findAllServices(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new service", description = "Create a new service and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or service already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addService( @RequestBody Servico servico, BindingResult result ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n| ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            Servico savedService = servicoService.saveServices(servico);
            if (savedService != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serviço já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar serviço: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    @Operation(summary = "Delete a service", description = "Deletes the service with the specified serviceId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteServiceByServiceId ( @PathVariable Long serviceId ) {
        try {
            servicoService.deleteService(serviceId);
            return ResponseEntity.ok("Serviço excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar serviço: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{serviceId}")
    @Operation(summary = "Update a service", description = "Updates the service data with the specified serviceId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateService(@Valid @PathVariable Long serviceId, @RequestBody Map<String, Object> updates) {
        try {
            Servico servico = servicoService.findServicoById(serviceId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("nomeServico", "descricao", "preco");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "nomeServico":
                        servico.setNomeServico((String) entry.getValue());
                        break;
                    case "descricao":
                        servico.setDescricao((String) entry.getValue());
                        break;
                    case "preco":
                        servico.setPreco((BigDecimal) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do produto atualizado
            DataBinder binder = new DataBinder(servico);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            servicoService.saveServices(servico);
            return ResponseEntity.ok("O serviço com id " + serviceId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{serviceId}")
    @Operation(summary = "Find service by serviceId", description = "Returns the service with the specified serviceId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findServiceById ( @PathVariable Long serviceId ) {
        try {
            Servico servico = servicoService.findServicoById(serviceId);
            if (servico == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servico não encontrado.");
            }
            return ResponseEntity.ok(servico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByService/serviceName/{serviceName}")
    @Operation(summary = "Search services by name", description = "Returns a list of services with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByServiceName( @PathVariable String serviceName ) {
        List<Servico> lServico = servicoService.findByServiceName(serviceName);
        if(!lServico.isEmpty()) {
            return ResponseEntity.ok(lServico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servico não encontrado.");
        }
    }

    @GetMapping("/findByUser/userId/{userId}")
    @Operation(summary = "Search services by userId", description = "Returns a list of services with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUsuarioId( @PathVariable String userId ) {
        List<Servico> lServico = servicoService.findByUserId(userId);
        if(!lServico.isEmpty()) {
            return ResponseEntity.ok(lServico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servico não encontrado.");
        }
    }

    @GetMapping("/findByServiceTag/service tagName/{serviceTagName}")
    @Operation(summary = "Search services tag by service tag name", description = "Returns a list of services tag within the specified service tag name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services tag found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "404", description = "Service Tag not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<Servico>> searchByServiceTagName(@PathVariable String serviceTagName) {
        List<TagServico> tagServicos = servicoService.findByTagServiceName(serviceTagName);

        if (!tagServicos.isEmpty()) {
            List<Servico> produtos = servicoService.findByServicesTag(tagServicos);
            return ResponseEntity.ok(produtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 se a categoria não for encontrada ou estiver vazia
        }
    }

    @GetMapping("/findByServiceTag/serviceTagId/{serviceTagId}")
    @Operation(summary = "Search service by service tag id", description = "Returns a list of service within the specified service tag id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "404", description = "Service Tag not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<Servico>> searchByCategoryId(@PathVariable Long serviceTagId) {
        Optional<TagServico> tagServico = tagServicoRepository.findById(serviceTagId);

        if (tagServico.isPresent()) {
            List<Servico> servicos = servicoRepository.findByTagServicos(Set.of(tagServico.get()));
            return ResponseEntity.ok(servicos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Map<String, String> validate(BindingResult resultado ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
