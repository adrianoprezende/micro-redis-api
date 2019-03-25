package com.bankslips.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankslips.entity.BankSlip;

@Repository
public interface BankslipsRepository extends JpaRepository<BankSlip, String>{

}
