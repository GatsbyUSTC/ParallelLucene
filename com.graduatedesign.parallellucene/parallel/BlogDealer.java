package parallel;

public class BlogDealer {
	public static Blog dealblog(StringBuffer stringBuffer){
		int start = stringBuffer.indexOf("<!-- 正文开始 -->");
		if (start == -1)
			return null;
		int end = stringBuffer.indexOf("<!-- 正文结束 -->", start);
		if (end == -1)
			return null;
		String content = PassHtmlUtils.filterHtml(stringBuffer.substring(
				start, end));
		content = content.replaceAll(" ", "");
		content = content.replaceAll("	", "");
		content = content.replaceAll("&nbsp;", "");
		if ((start = stringBuffer.indexOf("<title>")) == -1)
			return null;
		if ((end = stringBuffer.indexOf("</title>", start)) == -1) 
			return null;
		String title = stringBuffer.substring(start + 7, end);
		return new Blog(title, null, content);
	}
}
