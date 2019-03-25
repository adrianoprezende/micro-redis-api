package com.bankslips.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bankslips.entity.BankSlip;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.utils.DateUtils;
import com.bankslips.utils.ObjectUtils;

public class BankslipsAdapterOut implements IAdapter<BankSlipDTO, BankSlip> {

	@Override
	public BankSlipDTO toEntity(BankSlip entity) {
		if (entity == null) {
			return null;
		}

		return new BankSlipDTO.Builder()
				.id(entity.getId())
				.customer(entity.getCustomer())
				.dueDate(DateUtils.getDateToXMLFormat(entity.getDueDate()))
				.status(entity.getStatus())
				.totalInCents(ObjectUtils.safetyValueOf(entity.getTotalInCents()))
				.fine(ObjectUtils.safetyValueOf(entity.getFine()))
				.build();
	}

	@Override
	public List<BankSlipDTO> toEntityList(List<BankSlip> entityList) {
		List<BankSlipDTO> bankslips = new ArrayList<>();

		if (entityList != null && !entityList.isEmpty()) {
			for (BankSlip dto : entityList) {
				bankslips.add(toEntity(dto));
			}
		}

		return bankslips;
	}

}
