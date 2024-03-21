package com.gl.eirs.iplocation.repository.app;

import com.gl.eirs.iplocation.entity.app.Ipv4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface Ipv4Repository extends JpaRepository<Ipv4, Long> {

    @Query("select b from Ipv4 b where :ipNumber >= b.startIpNumber and :ipNumber <= b.endIpNumber")
    Ipv4 findByIpRange(@Param("ipNumber") long ipNumber);

}

