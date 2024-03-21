package com.gl.eirs.iplocation.service;


import com.gl.eirs.iplocation.constants.ConfigFlag;
import com.gl.eirs.iplocation.entity.app.SysParam;
import com.gl.eirs.iplocation.repository.app.SysParamRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DbConfigService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SysParamRepository sysParamRepository;

    private Map<ConfigFlag, String> configFlagHM = new ConcurrentHashMap<>();

    @PostConstruct
    public void myInit() {
        loadAllConfig();
    }




//    @Override
    public void loadAllConfig() {
        List<SysParam> fullConfigFlag = sysParamRepository.findByModule("IP Location");
        for (SysParam configFlagElement : fullConfigFlag) {
            configFlagHM.put(configFlagElement.getName(), configFlagElement.getValue());
            logger.info("Filled Config tag:{} value:{}", configFlagElement.getName(), configFlagElement.getValue());
        }
        logger.info("Config flag data load count : {}", configFlagHM.size());
    }

    public String getValue(String tag) {
        String t = configFlagHM.get(ConfigFlag.valueOf(tag));
        return t;
    }



}
