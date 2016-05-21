package de.smava.recrt.service.impl;

import de.smava.recrt.model.AppUser;
import de.smava.recrt.persistence.model.AppUserEntity;
import de.smava.recrt.persistence.repository.AppUserRepository;
import de.smava.recrt.service.AppUserService;
import de.smava.recrt.service.config.CachingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServiceConfig.class,CachingConfig.class})
public class AppUserServiceImplTest {

    private static final String TEST_USER_NAME="paul";

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testGetAll() throws Exception {
        reset(appUserRepository);
        AppUserEntity testUser = new AppUserEntity(TEST_USER_NAME);
        List<AppUserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(testUser);
        expect(appUserRepository.findAll()).andReturn(userEntityList).times(1);
        replay(appUserRepository);
        List<? extends AppUser> result = appUserService.getAll();
        assertEquals(result.size(), 1);
        assertSame(testUser, result.get(0));
        verify(appUserRepository);
    }

    @Test
    public void testGet() throws Exception {
        reset(appUserRepository);
        AppUserEntity testUser = new AppUserEntity(TEST_USER_NAME);
        expect(appUserRepository.findOne(anyString())).andReturn(testUser).times(1);
        replay(appUserRepository);
        AppUser result = appUserService.get(TEST_USER_NAME);
        assertEquals(result.getUsername(), TEST_USER_NAME);
        verify(appUserRepository);
    }
}