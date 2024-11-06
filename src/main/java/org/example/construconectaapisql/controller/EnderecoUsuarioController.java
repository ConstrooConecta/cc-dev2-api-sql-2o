package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.EnderecoUsuario;
import org.example.construconectaapisql.service.EnderecoUsuarioService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class EnderecoUsuarioController {

    private final Validator validator;
    private EnderecoUsuarioService enderecoUsuarioService;

    @Autowired
    public EnderecoUsuarioController(
            EnderecoUsuarioService enderecoUsuarioService,
            Validator validator
    ) {
        this.enderecoUsuarioService = enderecoUsuarioService;
        this.validator = validator;
    }

    @GetMapping("/addresses")
    @Operation(summary = "Show all addresses", description = "Returns a list of all available addresses")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<EnderecoUsuario> findAllAddress() {
        return this.enderecoUsuarioService.findAllAddress();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new address", description = "Creates a new address and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or address already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addAddress(@RequestBody EnderecoUsuario enderecoUsuario, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            EnderecoUsuario savedAddress = enderecoUsuarioService.saveAddress(enderecoUsuario);
            if (savedAddress != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Endereço já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar endereço: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{addresId}")
    @Operation(summary = "Delete a address", description = "Deletes the address with the specified addresId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteAddressByAddressId(@PathVariable Long addresId) {
        try {
            enderecoUsuarioService.deleteAddress(addresId);
            return ResponseEntity.ok("Endereco excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar endereço: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{addressId}")
    @Operation(summary = "Update a address", description = "Updates the address data with the specified addressId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateAddress(@Valid @PathVariable Long addressId,
                                           @RequestBody Map<String, Object> updates) {
        try {
            EnderecoUsuario enderecoUsuario = enderecoUsuarioService.findAddressById(addressId);

            List<String> validFields = Arrays.asList("cep", "uf", "cidade", "bairro", "rua", "numero", "complemento");

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "cep":
                        enderecoUsuario.setCep((String) entry.getValue());
                        break;
                    case "uf":
                        enderecoUsuario.setUf((String) entry.getValue());
                        break;
                    case "cidade":
                        enderecoUsuario.setCidade((String) entry.getValue());
                        break;
                    case "bairro":
                        enderecoUsuario.setBairro((String) entry.getValue());
                        break;
                    case "rua":
                        enderecoUsuario.setRua((String) entry.getValue());
                        break;
                    case "numero":
                        enderecoUsuario.setNumero((String) entry.getValue());
                        break;
                    case "complemento":
                        enderecoUsuario.setComplemento((String) entry.getValue());
                        break;
                    default:
                        break;
                }
            }

            DataBinder binder = new DataBinder(enderecoUsuario);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            enderecoUsuarioService.saveAddress(enderecoUsuario);
            return ResponseEntity.ok("O Endereco com id " + addressId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/address/{addressId}")
    @Operation(summary = "Find address by addressId", description = "Returns the address with the specified addressId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findAddressById(@PathVariable Long addressId) {
        return ResponseEntity.ok(enderecoUsuarioService.findAddressById(addressId));
    }

    @GetMapping("/findByCep/{cep}")
    @Operation(summary = "Search addresses by cep", description = "Returns a list of addresses with the specified cep")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByCep(@PathVariable String cep) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByCep(cep);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    @GetMapping("/findByCidade/{cidade}")
    @Operation(summary = "Search addresses by cidade", description = "Returns a list of addresses with the specified cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByCidade(@PathVariable String cidade) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByCidade(cidade);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    @GetMapping("/findByUf/{uf}")
    @Operation(summary = "Search addresses by uf", description = "Returns a list of addresses with the specified uf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUf(@PathVariable String uf) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByUf(uf);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    @GetMapping("/findByBairro/{bairro}")
    @Operation(summary = "Search addresses by bairro", description = "Returns a list of addresses with the specified bairro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByBairro(@PathVariable String bairro) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByBairro(bairro);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    @GetMapping("/findByRua/{rua}")
    @Operation(summary = "Search addresses by rua", description = "Returns a list of addresses with the specified rua")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByRua(@PathVariable String rua) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByRua(rua);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    @GetMapping("/findByUsuario/{usuario}")
    @Operation(summary = "Search addresses by usuario", description = "Returns a list of addresses with the specified usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUsuario(@PathVariable String usuario) {
        List<EnderecoUsuario> lAddress = enderecoUsuarioService.findByUserId(usuario);
        if (!lAddress.isEmpty()) {
            return ResponseEntity.ok(lAddress);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereco não encontrado.");
        }
    }

    public Map<String, String> validate(BindingResult resultado) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}