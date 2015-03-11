package parallel;


class Blog {
	private String Title;
	private String Description;
	private String Content;
	
	public Blog(String Title, String Descrption, String Content){
		this.Title = Title;
		this.Description = Descrption;
		this.Content = Content;
	}
	
	public String getTitle(){
		return Title;
	}
	
	public String getDescription(){
		return Description;
	}
	
	public String getContent(){
		return Content;
	}
}
