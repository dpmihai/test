package bnrexchange;

import java.util.Date;

public class Currency {
	
	private Date date;
	private String name;
	private double value;
	private int multiplier = 1;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + multiplier;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}	
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}	
		Currency other = (Currency) obj;
		if (date == null) {
			if (other.date != null)	return false;
		} else if (!date.equals(other.date)) return false;
		if (multiplier != other.multiplier)	return false;
		if (name == null) {
			if (other.name != null)	return false;
		} else if (!name.equals(other.name)) return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))	return false;
		return true;
	}
	@Override
	public String toString() {
		return "Currency [date=" + date + ", name=" + name + ", value=" + value
				+ ", multiplier=" + multiplier + "]";		
	}
		
	public String format() {
		StringBuilder sb = new StringBuilder();
		sb.append(BnrReader.sdf.format(date));
		sb.append(String.format("%4s", multiplier));
		sb.append(String.format("%4s", name));
		sb.append(" = ");
		sb.append(String.format("%8s", value));	
		sb.append(" RON");
		return sb.toString();
	}

}
