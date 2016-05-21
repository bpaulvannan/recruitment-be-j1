package de.smava.recrt.persistence.repository;

import de.smava.recrt.persistence.config.PersistenceConfig;
import de.smava.recrt.persistence.model.AppUserEntity;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
public class AppUserRepositoryTest {

    private static final String TEST_USER_NAME="paul";
    private static final String TEST_PASSWORD="bala";
    private static final String TEST_EMAIL="paul.bala@test.com";

    private static final String TEST_USER_NAME_TO_DELETE="user3";

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserRoleRepository appUserRoleRepository;

    private AppUserEntity buildTestUser(){
        AppUserEntity newUser = new AppUserEntity();
        newUser.setUsername(TEST_USER_NAME);
        newUser.setPassword(TEST_PASSWORD);
        newUser.setEmail(TEST_EMAIL);
        return newUser;
    }

    private void assertTestUser(AppUserEntity entity){
        Assert.assertNotNull(entity);
        assertEquals(TEST_USER_NAME, entity.getUsername());
        assertEquals(TEST_PASSWORD, entity.getPassword());
        assertEquals(TEST_EMAIL, entity.getEmail());
    }

    @Test
    public void testSave() throws Exception {
        AppUserEntity newUser = buildTestUser();
        appUserRepository.save(newUser);
        AppUserEntity entity = appUserRepository.findOne(TEST_USER_NAME);
        assertTestUser(entity);
    }

    @Test
    public void testFindOne() throws Exception {
        AppUserEntity entity = appUserRepository.findOne("user1");
        Assert.assertNotNull(entity);
        Assert.assertEquals("user1", entity.getUsername());
        Assert.assertEquals("1111", entity.getPassword());
        Assert.assertEquals("user1@smava.de", entity.getEmail());
    }

    @Test
    public void testDelete() throws Exception {
        AppUserEntity entity = appUserRepository.findOne(TEST_USER_NAME_TO_DELETE);
        try{
            appUserRepository.delete(TEST_USER_NAME_TO_DELETE);
            Assert.fail("DataIntegrityViolationException expected");
        }catch(DataIntegrityViolationException expected){
            //All good, ignore
        }
        appUserRoleRepository.delete(entity.getAppUserRoles());
        appUserRepository.delete(TEST_USER_NAME_TO_DELETE);
        entity = appUserRepository.findOne(TEST_USER_NAME_TO_DELETE);
        Assert.assertNull(entity);
    }

}