package id.ac.polban.pbo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    private SavingsAccount savings;

    @BeforeEach
    void setUp() {
        savings = new SavingsAccount("003", "Citra", 100_000, 0.05);
    }

    @Test
    void savingsAccount_adalahBankAccount() {
        assertInstanceOf(BankAccount.class, savings);
    }

    @Test
    void withdraw_melanggarSaldoMinimum_ditolak() {
        BankAccount acc = savings; // tipe deklarasi: induk
        assertThrows(InsufficientBalanceException.class, () -> acc.withdraw(60_000));
        assertEquals(100_000, acc.getBalance(), 0.001);
    }

    @Test
    void withdraw_tepatDiBatasMinimum_diterima() throws InsufficientBalanceException {
        savings.withdraw(50_000);
        assertEquals(SavingsAccount.MIN_BALANCE, savings.getBalance(), 0.001);
    }

    @Test
    void transfer_memakaiAturanWithdrawKelasAnak() {
        BankAccount target = new BankAccount("004", "Dodi", 0);
        assertThrows(InsufficientBalanceException.class,
            () -> savings.transfer(target, 70_000));
    }

    @Test
    void addInterest_menambahBungaLimaPersen() {
        savings.addInterest();
        assertEquals(105_000, savings.getBalance(), 0.001);
    }
}