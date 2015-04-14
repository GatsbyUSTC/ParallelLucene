package parallel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


class BlogDealer {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BlogDealer documentDealer = new BlogDealer();
		documentDealer.getBlog(8846);
	}
	
	public Blog getBlog(int blogNumber){
		
		String Path = "C:/Users/Gatsby/Documents/LoalaSave/blog.sina.com.cn/" + blogNumber;
		File inputFile = new File(Path);
		if(!inputFile.exists())
			return null;
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(  
			        new FileInputStream(inputFile),"UTF-8"));
			String temp;
			StringBuffer stringBuffer = new StringBuffer();
			while((temp = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(temp);
			}
			int start = stringBuffer.indexOf("<!-- 正文开始 -->");
			if(start == -1){
				bufferedReader.close();
				return null;
			}
			int end = stringBuffer.indexOf("<!-- 正文结束 -->", start);
			if(end == -1){
				bufferedReader.close();
				return null;
			}
			String content = PassHtmlUtils.filterHtml(stringBuffer.substring(start, end));
			content = content.replaceAll(" ", "");
			content = content.replaceAll("	", "");
			content = content.replaceAll("&nbsp;", "");
			if((start = stringBuffer.indexOf("<title>")) == -1){
				bufferedReader.close();
				return null;
			}
			if((end = stringBuffer.indexOf("</title>", start)) == -1){
				bufferedReader.close();
				return null;
			}
			String title = stringBuffer.substring(start+7, end);
			bufferedReader.close();
			return new Blog(title, null, content);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
