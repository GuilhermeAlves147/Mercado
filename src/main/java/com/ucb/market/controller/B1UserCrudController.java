package com.ucb.market.controller;

import com.ucb.market.dto.UserResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.User;
import com.ucb.market.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** CRUD administrativo acadêmico: usar autenticação/autorização antes de publicar na Internet. */
@RestController
@RequestMapping("/b1/usuarios")
@RequiredArgsConstructor
public class B1UserCrudController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    public record NewUser(@NotBlank String username,@Email @NotBlank String email,@NotBlank String password) {}
    public record EditUser(@NotBlank String username,@Email @NotBlank String email) {}

    @GetMapping
    public List<UserResponse> list() {return users.findAll().stream().map(UserResponse::from).toList();}
    @GetMapping("/{id}")
    public UserResponse one(@PathVariable Integer id) {return UserResponse.from(get(id));}
    private User get(Integer id){return users.findById(id).orElseThrow(()->new ResourceNotFoundException("Usuário não encontrado: "+id));}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserResponse create(@Valid @RequestBody NewUser req){
        if(users.existsByEmail(req.email())||users.existsByUsername(req.username()))throw new BusinessException("E-mail ou nome de usuário já cadastrado.");
        User user=new User();user.setEmail(req.email());user.setUsername(req.username());user.setPassword(encoder.encode(req.password()));return UserResponse.from(users.save(user));
    }
    @PutMapping("/{id}") @Transactional
    public UserResponse update(@PathVariable Integer id,@Valid @RequestBody EditUser req){
        User u=get(id);
        users.findByEmail(req.email()).filter(other->!other.getId().equals(id)).ifPresent(other->{throw new BusinessException("Este e-mail já está cadastrado.");});
        if(users.existsByUsername(req.username())&&!u.getUsername().equals(req.username()))throw new BusinessException("Nome de usuário já cadastrado.");
        u.setEmail(req.email());u.setUsername(req.username());return UserResponse.from(u);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional
    public void delete(@PathVariable Integer id){users.delete(get(id));users.flush();}
}
