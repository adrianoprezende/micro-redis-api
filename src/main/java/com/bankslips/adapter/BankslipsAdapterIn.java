package com.bankslips.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bankslips.entity.BankSlip;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.utils.DateUtils;
import com.bankslips.utils.ObjectUtils;

/**
 * The Class BankslipsAdapterIn.
 */
public class BankslipsAdapterIn implements IAdapter<BankSlip, BankSlipDTO>{

	/* (non-Javadoc)
	 * @see com.bankslips.service.adapter.IAdapter#toEntity(java.lang.Object)
	 */
	@Override
	public BankSlip toEntity(BankSlipDTO dto) {
		if (dto == null) {
			return null;
		}

		return new BankSlip.Builder()
				.customer(dto.getCustomer())
				.dueDate(DateUtils.getDateFromXMLFormat(dto.getDueDate()))
				.status(dto.getStatus())
				.totalInCents(ObjectUtils.safetyValueOf(dto.getTotalInCents()))
				.build();
	}

	/* (non-Javadoc)
	 * @see com.bankslips.service.adapter.IAdapter#toEntityList(java.util.List)
	 */
	@Override
	public List<BankSlip> toEntityList(List<BankSlipDTO> dtoList) {
		List<BankSlip> bankslips = new ArrayList<>();

		if (dtoList != null && !dtoList.isEmpty()) {
			for (BankSlipDTO dto : dtoList) {
				bankslips.add(toEntity(dto));
			}
		}

		return bankslips;
	}


}
