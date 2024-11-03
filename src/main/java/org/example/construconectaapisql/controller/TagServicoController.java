package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.TagServico;
import org.example.construconectaapisql.repository.TagServicoRepository;
import org.example.construconectaapisql.service.ServicoService;
import org.example.construconectaapisql.service.TagServicoService;
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
@RequestMapping("/serviceTag")
public class TagServicoController {
    private final TagServicoRepository tagServicoRepository;
    private final Validator validator;
    private final ServicoService servicoService;
    private final TagServicoService tagServicoService;

    @Autowired
    public TagServicoController(
            TagServicoRepository tagServicoRepository,
            Validator validator,
            ServicoService servicoService, TagServicoService tagServicoService) {
        this.tagServicoRepository = tagServicoRepository;
        this.validator = validator;
        this.servicoService = servicoService;
        this.tagServicoService = tagServicoService;
    }

    @GetMapping("/servicesTag")
    @Operation(summary = "Show all services tag", description = "Returns a list of all available service tags")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<TagServico> findAllServicesTag() { return tagServicoService.findAllTags(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new services tag", description = "Create a new service tags and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ServiceTag created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "201", description = "Services Tag created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or services tag already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addServiceTag(@Valid @RequestBody TagServico tagServico, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            if (tagServicoService.existsByNameIgnoreCase(tagServico.getNome())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tag de Servico já existe.");
            }

            // Salvar a nova tagServico
            TagServico savedTagServico = tagServicoService.saveTags(tagServico);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTagServico);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar Tag de Servico: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{serviceTagId}")
    @Operation(summary = "Delete a service tag", description = "Deletes the services tag with the specified serviceTagId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service Tag deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service Tag not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteServiceTagByServiceTagId ( @PathVariable Long serviceTagId ) {
        try {
            tagServicoService.deleteTag(serviceTagId);
            return ResponseEntity.ok("Tag de Serviço excluída com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar Tag de Serviço: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{serviceTagId}")
    @Operation(summary = "Update a service tag", description = "Updates the service tag data with the specified serviceTagId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service tag updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service tag not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateServiceTag(@Valid @PathVariable Long serviceTagId,
                                              @RequestBody Map<String, Object> updates) {
        try {
            TagServico tagServico = tagServicoService.findTagsById(serviceTagId);

            List<String> validFields = Arrays.asList("nome", "precoMedio");

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "nome":
                        tagServico.setNome((String) entry.getValue());
                        break;
                    case "precoMedio":
                        tagServico.setPrecoMedio((BigDecimal) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação dos dados atualizados
            DataBinder binder = new DataBinder(tagServico);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            // Salvando a tag de serviço atualizada
            tagServicoService.saveTags(tagServico);
            return ResponseEntity.ok("A Tag de Serviço com ID " + serviceTagId + " foi atualizada com sucesso.");

        } catch (DataIntegrityViolationException e) {
            String message = e.getRootCause().getMessage();
            if (message.contains("nome")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Já existe uma Tag de Serviço com este nome.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a Tag de Serviço: \n" + e.getMessage());
        }
    }

    @GetMapping("/findById/{serviceTagId}")
    @Operation(summary = "Find service tag by serviceTagId", description = "Returns the service tag with the specified serviceTagId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ServiceTag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "404", description = "ServiceTag not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findServicesTagById (@PathVariable Long serviceTagId) {
        try {
            TagServico tagServico = tagServicoService.findTagsById(serviceTagId);
            if (tagServico == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TagServico não encontrada.");
            }
            return ResponseEntity.ok(tagServico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByNome/{nome}")
    @Operation(summary = "Search services tag by name", description = "Returns a list of service tags with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ServiceTag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagServico.class))),
            @ApiResponse(responseCode = "404", description = "ServiceTag not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeCompleto ( @PathVariable String nome ) {
        List<TagServico> lTagServico = tagServicoService.findByNome(nome);
        if(!lTagServico.isEmpty()) {
            return ResponseEntity.ok(lTagServico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TagServico não encontrada.");
        }
    }

    public Map<String, String> validate( BindingResult resultado ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
