package com.hry.dispatch.domain;

public class CalcTableElement {

	private int date;
	private String wash_elec_amout;
	private String sumary;
	private String no_wash_elec_amout;
	private String no_wash_elec_sumary;
	private String cal_rate_index_sumary;
	private String cal_rate_index;
	private String cal_rate_sumary;
	private String cal_rate;
	private String no_wash_elec_amout_2;
	private String sumary_elec_amout;
	private String sumary_cal_rate;
	private String reduce_ratio;
	private String lose_sumary;
	
	public CalcTableElement() {
		super();
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getWash_elec_amout() {
		return wash_elec_amout;
	}
	public void setWash_elec_amout(String wash_elec_amout) {
		this.wash_elec_amout = wash_elec_amout;
	}
	public String getSumary() {
		return sumary;
	}
	public void setSumary(String sumary) {
		this.sumary = sumary;
	}
	public String getNo_wash_elec_amout() {
		return no_wash_elec_amout;
	}
	public void setNo_wash_elec_amout(String no_wash_elec_amout) {
		this.no_wash_elec_amout = no_wash_elec_amout;
	}
	public String getNo_wash_elec_sumary() {
		return no_wash_elec_sumary;
	}
	public void setNo_wash_elec_sumary(String no_wash_elec_sumary) {
		this.no_wash_elec_sumary = no_wash_elec_sumary;
	}
	public String getCal_rate_index_sumary() {
		return cal_rate_index_sumary;
	}
	public void setCal_rate_index_sumary(String cal_rate_index_sumary) {
		this.cal_rate_index_sumary = cal_rate_index_sumary;
	}
	public String getCal_rate_index() {
		return cal_rate_index;
	}
	public void setCal_rate_index(String cal_rate_index) {
		this.cal_rate_index = cal_rate_index;
	}
	public String getCal_rate_sumary() {
		return cal_rate_sumary;
	}
	public void setCal_rate_sumary(String cal_rate_sumary) {
		this.cal_rate_sumary = cal_rate_sumary;
	}
	public String getCal_rate() {
		return cal_rate;
	}
	public void setCal_rate(String cal_rate) {
		this.cal_rate = cal_rate;
	}
	public String getNo_wash_elec_amout_2() {
		return no_wash_elec_amout_2;
	}
	public void setNo_wash_elec_amout_2(String no_wash_elec_amout_2) {
		this.no_wash_elec_amout_2 = no_wash_elec_amout_2;
	}
	public String getSumary_elec_amout() {
		return sumary_elec_amout;
	}
	public void setSumary_elec_amout(String sumary_elec_amout) {
		this.sumary_elec_amout = sumary_elec_amout;
	}
	public String getSumary_cal_rate() {
		return sumary_cal_rate;
	}
	public void setSumary_cal_rate(String sumary_cal_rate) {
		this.sumary_cal_rate = sumary_cal_rate;
	}
	public String getReduce_ratio() {
		return reduce_ratio;
	}
	public void setReduce_ratio(String reduce_ratio) {
		this.reduce_ratio = reduce_ratio;
	}
	public String getLose_sumary() {
		return lose_sumary;
	}
	public void setLose_sumary(String lose_sumary) {
		this.lose_sumary = lose_sumary;
	}
	@Override
	public String toString() {
		return "CalcTableElement [date=" + date + ", wash_elec_amout=" + wash_elec_amout + ", sumary=" + sumary
				+ ", no_wash_elec_amout=" + no_wash_elec_amout + ", no_wash_elec_sumary=" + no_wash_elec_sumary
				+ ", cal_rate_index_sumary=" + cal_rate_index_sumary + ", cal_rate_index=" + cal_rate_index
				+ ", cal_rate_sumary=" + cal_rate_sumary + ", cal_rate=" + cal_rate + ", no_wash_elec_amout_2="
				+ no_wash_elec_amout_2 + ", sumary_elec_amout=" + sumary_elec_amout + ", sumary_cal_rate="
				+ sumary_cal_rate + ", reduce_ratio=" + reduce_ratio + ", lose_sumary=" + lose_sumary + "]";
	}
	
}
