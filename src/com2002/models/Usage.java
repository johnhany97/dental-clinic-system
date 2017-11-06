package com2002.models;

;
import java.sql.SQLException;
import java.time.LocalDate;

import com2002.utils.Database;

public class Usage {
	
	private int checkUpUsed;
	private int hygieneUsed;
	private int repairWorkUsed;
	private LocalDate startDate;
	
	public LocalDate getDate() {
		return startDate;
	}
	
	
	public int getCheckUpUsed() {
		return checkUpUsed;
	}
	public int getHygieneUsed() {
		return hygieneUsed;
	}
	public int getRepairWorkUsed() {
		return repairWorkUsed;
	}
	
	
	
	
	
	

}
