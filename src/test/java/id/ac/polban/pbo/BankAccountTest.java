package id.ac.polban.pbo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

@DisplayName("Pengujian kelas BankAccount")
class BankAccountTest {

    private BankAccount sender;
    private BankAccount receiver;

    @BeforeAll
    static void initAll() {
        System.out.println("@BeforeAll : sekali di awal");
    }

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach : objek baru dibuat");
        sender = new BankAccount("001", "Andi", 100_000);
        receiver = new BankAccount("002", "Budi", 0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach : test selesai");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("@AfterAll : sekali di akhir");
    }

    @Nested
    @DisplayName("Operasi transfer")
    class TransferTest {

        @Test
        @DisplayName("transfer valid memindahkan saldo ke penerima")
        void transferValid() throws InsufficientBalanceException {
            sender.transfer(receiver, 40_000);
            assertAll(
                () -> assertEquals(60_000, sender.getBalance(), 0.001),
                () -> assertEquals(40_000, receiver.getBalance(), 0.001)
            );
        }

        @Test
        @DisplayName("transfer gagal tidak mengubah saldo kedua rekening")
        void transferGagal() {
            assertThrows(InsufficientBalanceException.class,
                () -> sender.transfer(receiver, 500_000));
            assertEquals(100_000, sender.getBalance(), 0.001);
            assertEquals(0, receiver.getBalance(), 0.001);
        }
        
        @Test
        @DisplayName("transfer ke rekening null melempar IllegalArgumentException dan saldo tetap")
        void transfer_keRekeningNull_ditolak() {
            
            assertThrows(IllegalArgumentException.class, () -> 
                sender.transfer(null, 20_000));
            assertEquals(100_000, sender.getBalance(), 0.001); 
        }

        @Test
        @DisplayName("transfer ke rekening sendiri ditolak dan saldo tidak berubah")
        void transfer_keDiriSendiri_ditolak() {

            assertThrows(IllegalArgumentException.class, () -> 
                sender.transfer(sender, 20_000));
            assertEquals(100_000, sender.getBalance(), 0.001); 
        }
    }

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
        BankAccount acc = new BankAccount("001", "Andi", 100_000);
        acc.deposit(50_000);
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
        assertEquals(100_000, acc.getBalance(), 0.001);
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

    @ParameterizedTest(name = "setoran {0} ditolak")
    @ValueSource(doubles = {0, -1, -50_000})
    void deposit_tidakValid(double amount) {
        assertThrows(IllegalArgumentException.class, () -> sender.deposit(amount));
    }

    @ParameterizedTest(name = "saldo {0} - tarik {1} = {2}")
    @CsvSource({
        "100000, 10000, 90000",
        "100000, 100000, 0",
        "50000, 25000.5, 24999.5",
        "100000, 0.01, 99999.99"
    })

    void withdraw_berbagaiNilai(double awal, double tarik, double sisa)
            throws InsufficientBalanceException {
        BankAccount acc = new BankAccount("009", "Dewi", awal);
        acc.withdraw(tarik);
        assertEquals(sisa, acc.getBalance(), 0.001);
    }
}