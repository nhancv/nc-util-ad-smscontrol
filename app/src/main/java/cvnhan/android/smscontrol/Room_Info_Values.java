package cvnhan.android.smscontrol;

public class Room_Info_Values {
	private int Temp;
	private int Humi;
	private String KEY;
	public Room_Info_Values(){
		Temp=0;
		Humi=0;
		KEY="0000000000";
	}
	public Room_Info_Values(String key){
		this.KEY=key;
	}
	public Room_Info_Values(int temp, int humi, String key){
		this.Temp=temp;
		this.Humi=humi;
		this.KEY=key;
		
	}
	public Room_Info_Values(Room_Info_Values info){
		this.Temp=info.getTemp();
		this.Humi=info.getHumi();
		this.KEY=info.getKey();
	}
	public int getTemp(){
		return Temp;
	}
	public int getHumi(){
		return Humi;
	}
	public String getKey(){
		return KEY;
	}
	public void setTemp(int temp){
		this.Temp=temp;
	}
	public void setHumi(int humi){
		this.Humi=humi;
	}
	public void setKey(String key){
		this.KEY=key;
	}
	
}
