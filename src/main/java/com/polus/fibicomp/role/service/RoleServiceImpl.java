package com.polus.fibicomp.role.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Configuration
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

}
