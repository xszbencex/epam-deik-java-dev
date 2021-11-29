package com.epam.training.ticketservice.repositry.init;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.repositry.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class AccountInitializerTest {

    private static final Account ADMIN_ACCOUNT = new Account("admin", "admin", true);

    private AccountInitializer underTest;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new AccountInitializer(accountRepository, environment);
    }

    @Test
    void testInitProductsShouldSaveAdminWhenCiProfileIsActive() {
        // Given
        final AccountInitializer accountInitializer = spy(underTest);
        doReturn(true).when(accountInitializer).isProfileCiActive();

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testInitProductsShouldSaveAdminWhenCiProfileIsNotActiveButItIsNotInitializedYet() {
        // Given
        final AccountInitializer accountInitializer = spy(underTest);
        doReturn(false).when(accountInitializer).isProfileCiActive();
        given(accountRepository.findById(ADMIN_ACCOUNT.getUsername())).willReturn(Optional.empty());

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testInitProductsShouldDoNothingWhenCiProfileIsNotActiveAndAlreadyInitialized() {
        // Given
        final AccountInitializer accountInitializer = spy(underTest);
        doReturn(false).when(accountInitializer).isProfileCiActive();
        given(accountRepository.findById(ADMIN_ACCOUNT.getUsername())).willReturn(Optional.of(ADMIN_ACCOUNT));

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testIsProfileCiActiveShouldReturnTrueWhenCiIsActive() {
        // Given
        given(environment.getActiveProfiles()).willReturn(new String[]{"ci"});

        // When
        final boolean actual = underTest.isProfileCiActive();

        // Then
        assertTrue(actual);
    }

    @Test
    void testIsProfileCiActiveShouldReturnFalseWhenCiIsNotActive() {
        // Given
        given(environment.getActiveProfiles()).willReturn(new String[]{});

        // When
        final boolean actual = underTest.isProfileCiActive();

        // Then
        assertFalse(actual);
    }
}
