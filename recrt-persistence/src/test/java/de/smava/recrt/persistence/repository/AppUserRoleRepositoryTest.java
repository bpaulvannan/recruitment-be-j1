package de.smava.recrt.persistence.repository;

import de.smava.recrt.model.UserRole;
import de.smava.recrt.persistence.config.PersistenceConfig;
import de.smava.recrt.persistence.model.AppUserEntity;
import de.smava.recrt.persistence.model.AppUserRoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
public class AppUserRoleRepositoryTest {

    private static final String TEST_USER_NAME="user2";

    @Autowired
    private AppUserRoleRepository appUserRoleRepository;

    @Test
    public void testFindByKeyAppUser() throws Exception {
        AppUserEntity appUserEntity = new AppUserEntity(TEST_USER_NAME);
        List<AppUserRoleEntity> roleEntityList = appUserRoleRepository.findByKeyAppUser(appUserEntity);
        assertEquals(1, roleEntityList.size());
        assertEquals(UserRole.ROLE_USER, roleEntityList.get(0).getRole());
    }
}