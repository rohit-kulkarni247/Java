import java.io.Serializable;


class Sample implements Serializable{
	String name;	
	String email;
	String password;
	Integer Id;
	
	public Sample(String name,String email,String password, Integer Id){
		this.name=name;
		this.email=email;
		this.password=password;
		this.Id=Id;
	}
}
