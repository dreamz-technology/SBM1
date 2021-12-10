package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseMatchMaking{

	@SerializedName("vashya")
	private Vashya vashya;

	@SerializedName("conclusion")
	private Conclusion conclusion;

	@SerializedName("total")
	private Total total;

	@SerializedName("gan")
	private Gan gan;

	@SerializedName("varna")
	private Varna varna;

	@SerializedName("yoni")
	private Yoni yoni;

	@SerializedName("tara")
	private Tara tara;

	@SerializedName("nadi")
	private Nadi nadi;

	@SerializedName("bhakut")
	private Bhakut bhakut;

	@SerializedName("maitri")
	private Maitri maitri;

	public Vashya getVashya(){
		return vashya;
	}

	public Conclusion getConclusion(){
		return conclusion;
	}

	public Total getTotal(){
		return total;
	}

	public Gan getGan(){
		return gan;
	}

	public Varna getVarna(){
		return varna;
	}

	public Yoni getYoni(){
		return yoni;
	}

	public Tara getTara(){
		return tara;
	}

	public Nadi getNadi(){
		return nadi;
	}

	public Bhakut getBhakut(){
		return bhakut;
	}

	public Maitri getMaitri(){
		return maitri;
	}
}