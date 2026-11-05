package id.ac.polban.pbo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void constructor_menyimpanDataDenganBenar() {
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        assertAll("data rekening",
            () -> assertEquals("001", acc.getAccountNumber()),
            () -> assertEquals("Andi", acc.getOwner()),
            () -> assertEquals(100_000, acc.getBalance(), 0.001)
        );
    }

    @Test
    void deposit_menambahSaldo() {
        // Arrange
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        // Act
        acc.deposit(50_000);
        // Assert
        assertEquals(150_000, acc.getBalance(), 0.001);
    }
    @Test
    void deposit_jumlahTidakValid_melemparIllegalArgument() {
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, () -> acc.deposit(-10));
        assertEquals("Jumlah setoran harus lebih dari 0", ex.getMessage());
    }

    @Test
    void withdraw_melebihiSaldo_melemparInsufficientBalance() {
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        assertThrows(InsufficientBalanceException.class, () -> 
            acc.withdraw(200_000));
        assertEquals(100_000, acc.getBalance(), 0.001); // saldo tetap
    }

    @Test
    void withdraw_jumlahValid_tidakMelempar() {
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        assertDoesNotThrow(() -> acc.withdraw(30_000));
        assertEquals(70_000, acc.getBalance(), 0.001);
    }

    @Test
    void constructor_saldoAwalNegatif_ditolak() {
        assertThrows(IllegalArgumentException.class,
            () -> new BankAccount("001", "Andi", -1));
    }
}