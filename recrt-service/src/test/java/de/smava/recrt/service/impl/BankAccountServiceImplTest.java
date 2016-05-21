package de.smava.recrt.service.impl;

import de.smava.recrt.model.BankAccount;
import de.smava.recrt.persistence.model.AppUserEntity;
import de.smava.recrt.persistence.model.BankAccountEntity;
import de.smava.recrt.persistence.repository.AppUserRepository;
import de.smava.recrt.persistence.repository.BankAccountRepository;
import de.smava.recrt.service.BankAccountService;
import de.smava.recrt.service.config.CachingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServiceConfig.class,CachingConfig.class})
public class BankAccountServiceImplTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService bankAccountService;

    private static final String TEST_USERNAME="paul";
    private static final String TEST_IBAN="IBAN";
    private static final String TEST_BIC="BIC";
    private BankAccountEntity buildTestBankAccount(){
        AppUserEntity appUser = new AppUserEntity(TEST_USERNAME);
        return new BankAccountEntity(TEST_IBAN,TEST_BIC, appUser);
    }

    @Test
    public void testGetByAppUser() throws Exception {
        reset(appUserRepository,bankAccountRepository);

        //Build test data
        List<BankAccountEntity> bankAccountList = new ArrayList<>();
        BankAccountEntity testBankAccount = buildTestBankAccount();
        bankAccountList.add(testBankAccount);
        AppUserEntity testUser = testBankAccount.getAppUser() ;

        //Record appRepository behavior
        expect(appUserRepository.findOne(anyString())).andReturn(testUser).once();
        replay(appUserRepository);

        //Record bankAccountRepository behavior
        expect(bankAccountRepository.findByAppUser(anyObject(AppUserEntity.class))).andReturn(bankAccountList).once();
        replay(bankAccountRepository);

        //Assert results
        List<? extends BankAccount> result = bankAccountService.getByAppUser(testUser.getUsername());
        assertEquals(result.size(), 1);
        BankAccount account = result.get(0);
        assertSame(testBankAccount, account);
        assertSame(testUser, account.getAppUser());
        assertEquals(TEST_USERNAME,account.getAppUser().getUsername());
        assertEquals(TEST_IBAN,account.getIban());
        assertEquals(TEST_BIC,account.getBic());

        verify(appUserRepository);
        verify(bankAccountRepository);
    }

    @Test
    public void testCreate() throws Exception {
        reset(appUserRepository,bankAccountRepository);

        //Build test data
        BankAccountEntity testBankAccount = buildTestBankAccount();
        AppUserEntity testUser = testBankAccount.getAppUser() ;

        //Record behavior
        expect(appUserRepository.findOne(anyString())).andReturn(testUser).once();
        replay(appUserRepository);
        expect(bankAccountRepository.save(anyObject(BankAccountEntity.class))).andReturn(testBankAccount).once();
        replay(bankAccountRepository);

        //Assert
        BankAccount bankAccount = bankAccountService.create(testBankAccount);
        assertNotNull(bankAccount);

        verify(appUserRepository);
        verify(bankAccountRepository);
    }
}