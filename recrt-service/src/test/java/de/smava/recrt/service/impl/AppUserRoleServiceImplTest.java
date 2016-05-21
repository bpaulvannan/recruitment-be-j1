package de.smava.recrt.service.impl;

import de.smava.recrt.model.AppUserRole;
import de.smava.recrt.persistence.model.AppUserEntity;
import de.smava.recrt.persistence.model.AppUserRoleEntity;
import de.smava.recrt.persistence.repository.AppUserRoleRepository;
import de.smava.recrt.service.AppUserRoleService;
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
public class AppUserRoleServiceImplTest {
    @Autowired
    private AppUserRoleService appUserRoleService;

    @Autowired
    private AppUserRoleRepository appUserRoleRepository;

    @Test
    public void testGetByAppUser() throws Exception {
        AppUserRoleEntity roleEntity = new AppUserRoleEntity();
        List<AppUserRoleEntity> roleEntityList = new ArrayList<>();
        roleEntityList.add(roleEntity);
        expect(appUserRoleRepository.findByKeyAppUser(anyObject(AppUserEntity.class))).andReturn(roleEntityList);
        replay(appUserRoleRepository);
        List<? extends AppUserRole> result = appUserRoleService.getByAppUser(new AppUserEntity());
        assertEquals(result.size(), 1);
        assertSame(roleEntity, result.get(0));
        verify(appUserRoleRepository);
    }
}