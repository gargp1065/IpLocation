package com.gl.eirs.iplocation.repository.app;

import com.gl.eirs.iplocation.entity.app.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Integer>, JpaSpecificationExecutor<SysParam> {

//    @Query("select u.value from SysParam u where u.tag= :tag")
//    String getValueFromTag(@Param("tag") String tag);

    List<SysParam> findByModule(String module);

}
