package ru.netology.HW.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.HW.data.DataHelper;
import ru.netology.HW.page.DashboardPage;
import ru.netology.HW.page.LoginPage;
import ru.netology.HW.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.HW.data.DataHelper.getFirstCardNumber;
import static ru.netology.HW.data.DataHelper.getSecondCardNumber;

public class MoneyTransferTest {
    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstCardToSecondCard() {
        int amount = 3_000;
        val dashboardPage = new DashboardPage();
        val transferPage= new TransferPage();
        val firstCardBalanceBeforeTransfer = dashboardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashboardPage.getSecondCardBalance();
        dashboardPage.pushSecondCardButton();
        transferPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalance = firstCardBalanceBeforeTransfer - amount;
        val secondCardBalance = secondCardBalanceBeforeTransfer + amount;

        assertEquals(firstCardBalance, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalance, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldTransferMoneyFromSecondCardToFirstCard() {
        int amount = 6_000;
        val dashboardPage = new DashboardPage();
        val transferPage= new TransferPage();
        val firstCardBalanceBeforeTransfer = dashboardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashboardPage.getSecondCardBalance();
        dashboardPage.pushFirstCardButton();
        transferPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalance = firstCardBalanceBeforeTransfer + amount;
        val secondCardBalance = secondCardBalanceBeforeTransfer - amount;

        assertEquals(firstCardBalance, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalance, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldHaveNotGoodBalance() {
        int amount = 11_000;
        val dashboardPage = new DashboardPage();
        val transferPage= new TransferPage();
        val firstCardBalanceBeforeTransfer = dashboardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashboardPage.getSecondCardBalance();
        dashboardPage.pushFirstCardButton();
        transferPage.transferMoney(amount, getSecondCardNumber());
        transferPage.getErrorLimit();
    }
}
