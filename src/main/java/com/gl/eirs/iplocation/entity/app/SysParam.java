package com.gl.eirs.iplocation.entity.app;

import com.gl.eirs.iplocation.constants.ConfigFlag;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="sys_param")
public class SysParam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "tag")
    @Enumerated(EnumType.STRING)
    private ConfigFlag name;

    @Column(name = "value")
    String value;

    @Column(name = "feature_name")
    private String module;

}
