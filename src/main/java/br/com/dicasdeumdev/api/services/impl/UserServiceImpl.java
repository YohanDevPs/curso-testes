package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.exeptions.ObjectNotFoundException;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.UserService;
import br.com.dicasdeumdev.api.services.exeptions.DataIntegratyViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User findById(Integer id) {
        validationId(id);
        return repository.findById(id).get();
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User create(UserDTO userDTO) {
        validationUser(userDTO);
        return repository.save(modelMapper.map(userDTO, User.class));
    }

    @Override
    public User update(UserDTO userDTO) {
        validationUser(userDTO);
        return repository.save(modelMapper.map(userDTO, User.class));
    }

    @Override
    public void delete(Integer id) {
        validationId(id);
        repository.deleteById(id);
    }

    public void validationUser(UserDTO userDTO) {
        Optional<User> user = repository.findByEmail(userDTO.getEmail());

        if(user.isPresent() && !user.get().getId().equals(userDTO.getId())) {
            throw new DataIntegratyViolationException("E-mail já cadastrado no sistema");
        }
    }

    public void validationId(Integer userId) {
        repository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }
}
