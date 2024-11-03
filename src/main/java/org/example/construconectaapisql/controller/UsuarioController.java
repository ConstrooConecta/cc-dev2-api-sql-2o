package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Usuario;
import org.example.construconectaapisql.service.UsuarioService;
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

import java.util.*;

@RestController
@RequestMapping("/user")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final Validator validator;

    @Autowired
    public UsuarioController(
            Validator validator,
            UsuarioService usuarioService
    ) {
        this.usuarioService = usuarioService;
        this.validator = validator;
    }

    @GetMapping("/users")
    @Operation(summary = "Show all users", description = "Returns a list of all available users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Usuario> findAllUsers() {
        return usuarioService.findAllUsers();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new user", description = "Create a new user and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or user already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addUser(@Valid @RequestBody Usuario usuario,
                                     BindingResult result
    ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            Usuario savedUsuario = usuarioService.saveUsers(usuario);
            if (savedUsuario != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar usuário: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{uid}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the specified UID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteUserByUsuarioId(@PathVariable String uid) {
        try {
            usuarioService.deleteUser(uid);
            return ResponseEntity.ok("Usuário excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar usuário: \n" + e.getMessage());
        }
    }

    @PatchMapping("/partialUpdate/{uid}")
    @Operation(summary = "Update Partially a user", description = "Updates the user data with the specified UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updatePartialUser(@Valid @PathVariable String uid,
                                               @RequestBody Map<String, Object> updates) {
        try {
            Usuario usuario = usuarioService.findUsersByUid(uid);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("cpf", "nomeCompleto", "nomeUsuario", "email", "senha", "telefone", "dataNascimento", "genero");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "cpf":
                        usuario.setCpf((String) entry.getValue());
                        break;
                    case "nomeCompleto":
                        usuario.setNomeCompleto((String) entry.getValue());
                        break;
                    case "nomeUsuario":
                        usuario.setNomeUsuario((String) entry.getValue());
                        break;
                    case "email":
                        usuario.setEmail((String) entry.getValue());
                        break;
                    case "senha":
                        usuario.setSenha((String) entry.getValue());
                        break;
                    case "telefone":
                        usuario.setTelefone((String) entry.getValue());
                        break;
                    case "dataNascimento":
                        usuario.setDataNascimento((Date) entry.getValue());
                        break;
                    case "genero":
                        usuario.setGenero((Integer) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do usuário atualizado
            DataBinder binder = new DataBinder(usuario);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            usuarioService.saveUsers(usuario);
            return ResponseEntity.ok("O usuário com uid " + uid + " foi atualizado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            // Identifica qual campo violou a restrição UNIQUE
            String message = e.getRootCause().getMessage();
            if (message.contains("telefone")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Telefone já está em uso.");
            } else if (message.contains("email")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já está em uso.");
            } else if (message.contains("nomeUsuario")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Nome de usuário já está em uso.");
            } else if (message.contains("cpf")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: CPF já está em uso.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByUid/{uid}")
    @Operation(summary = "Find user by UID", description = "Returns the user with the specified UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findUsersByUid(@PathVariable String uid) {
        try {
            Usuario usuario = usuarioService.findUsersByUid(uid);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByNomeCompleto/{nomeCompleto}")
    @Operation(summary = "Search users by full name", description = "Returns a list of users with the specified full name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeCompleto(@PathVariable String nomeCompleto) {
        List<Usuario> lUsuario = usuarioService.findByNomeCompleto(nomeCompleto);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }
    }

    @GetMapping("/findByNomeUsuario/{nomeUsuario}")
    @Operation(summary = "Search users by username", description = "Returns a list of users with the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeUsuario(@PathVariable String nomeUsuario) {
        List<Usuario> lUsuario = usuarioService.findByNomeUsuario(nomeUsuario);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }
    }

    @GetMapping("/findByCpf/{cpf}")
    @Operation(summary = "Search users by username", description = "Returns a list of users with the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByCpf(@PathVariable String cpf) {
        List<Usuario> lUsuario = usuarioService.findByCpf(cpf);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }
    }

    @GetMapping("/findByEmail/{email}")
    @Operation(summary = "Search users by email", description = "Returns a list of users with the specified email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByEmail(@PathVariable String email) {
        List<Usuario> lUsuario = usuarioService.findByEmail(email);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }
    }

    @GetMapping("/findByTelefone/{telefone}")
    @Operation(summary = "Search users by phone number", description = "Returns a list of users with the specified phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByTelefone(@PathVariable String telefone) {
        List<Usuario> lUsuario = usuarioService.findByTelefone(telefone);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }
    }

    @GetMapping("/findByGenero/{genero}")
    @Operation(summary = "Search users by gender", description = "Returns a list of users with the specified gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByGenero(@PathVariable Integer genero) {
        List<Usuario> lUsuario = usuarioService.findByGenero(genero);
        if (!lUsuario.isEmpty()) {
            return ResponseEntity.ok(lUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
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
