package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void addUser() {
        User user = new User(null, "Name", "email@rambler.ru");
        userService.addUser(user);

        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isNull();
        assertThat(capturedUser.getName()).isEqualTo(user.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void addUserThrowException() {
        User user = new User(1, "Name", "email@rambler.ru");
        when(userRepository.save(user)).thenThrow(new DataAccessException("") {
        });

        assertThatThrownBy(() -> userService.addUser(user)).isInstanceOf(ConflictException.class).hasMessage("Юзер с имейлом " + user.getEmail() + " уже существует");
    }

    @Test
    void updateUser() {
        User user = new User(5, "Name", "email@rambler.ru");

        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        User updatedUser = new User(5, "NameUpdate", "email@update.ru");

        userService.updateUser(updatedUser);


        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(updatedUser.getId());
        assertThat(capturedUser.getName()).isEqualTo(updatedUser.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(updatedUser.getEmail());
    }

    @Test
    void updateUserNameNull() {
        User user = new User(5, "Name", "email@rambler.ru");

        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        User updatedUser = new User(5, null, "email@update.ru");

        userService.updateUser(updatedUser);

        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(updatedUser.getId());
        assertThat(capturedUser.getName()).isEqualTo(user.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(updatedUser.getEmail());
    }

    @Test
    void updateUserEmailNull() {
        User user = new User(5, "Name", "email@rambler.ru");

        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        User updatedUser = new User(5, "NameUpdate", null);

        userService.updateUser(updatedUser);


        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(updatedUser.getId());
        assertThat(capturedUser.getName()).isEqualTo(updatedUser.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void updateUserEmailNullAndNameNull() {
        User user = new User(5, "Name", "email@rambler.ru");

        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        User updatedUser = new User(5, null, null);

        userService.updateUser(updatedUser);


        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(updatedUser.getId());
        assertThat(capturedUser.getName()).isEqualTo(user.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void getUserById() {
        int id = 9;
        User user = new User(9, "Name", "email@rambler.ru");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThat(userService.getUserById(id)).isEqualTo(user);
    }

    @Test
    void getUserByIdThrowNotFound() {
        int id = 9;
        when(userRepository.findById(id)).thenThrow(new NotFoundException(
                "Юзера с айди " + id + "не существует"));

        assertThatThrownBy(() -> userService.getUserById(id)).isInstanceOf(NotFoundException.class).hasMessage("Юзера с айди " + id + "не существует");
    }

    @Test
    void getUsers() {
        userService.getUsers();
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser() {
        int id = 9;
        userRepository.deleteById(id);
        verify(userRepository).deleteById(id);
    }
}