package com.gl.eirs.iplocation.repository.app;

import com.gl.eirs.iplocation.entity.app.Ipv6;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface Ipv6Repository extends JpaRepository<Ipv6, Long> {

    @Query("select b from Ipv6 b where :ipNumber >= b.startIpNumber and :ipNumber <= b.endIpNumber")
    Ipv6 findByIpRange(@Param("ipNumber") BigInteger ipNumber);

}
