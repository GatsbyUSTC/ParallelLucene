package parallel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

class BlogReader {

	private int curdocnum;

	public BlogReader() {
		// TODO Auto-generated constructor stub
		curdocnum = 0;
	}

	public StringBuffer getNextBuffer() {
		int nextdocnum = getNextDocNum();
		if(nextdocnum != 0 )
			return getBlog(nextdocnum);
		return null;
	}

	public int getNextDocNum(){
		if (curdocnum < MainThread.DOCNUM) {
			curdocnum++;
			System.out.println(curdocnum);
			return curdocnum;
		}
		return 0;
	}
	
	private StringBuffer getBlog(int blogNumber) {

		String Path = "C:/Users/Gatsby/Documents/LoalaSave/blog.sina.com.cn/"
				+ blogNumber;
		File inputFile = new File(Path);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(inputFile),
							"UTF-8"));
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				stringBuffer.append(temp);
			}
			bufferedReader.close();
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
		return stringBuffer;
	}

}
