package commen;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

class IndexFiles {

	public static final String IndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	private static final int DocumentNum = 10000;

	public static void main(String[] args) {
		IndexFiles indexFiles = new IndexFiles();
		long startTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index begin @" +startTime+ "!!!!!!!!!!");
		indexFiles.index();
		long endTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index complete @" +endTime+ "!!!!!!!!!!!");
		System.out.println("total time is :" +(endTime-startTime)+ "miliseconds");
	}

	public void index() {
		try {
			Directory dir = FSDirectory.open(Paths.get(IndexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			BlogDealer blogDealer = new BlogDealer();

			for (int i = 1; i < DocumentNum; i++) {
				Blog blog = blogDealer.getBlog(i);
				if (blog != null) {
					Document document = new Document();
					TextField titleField = new TextField("title",
							blog.getTitle(), Store.NO);
					document.add(titleField);
					TextField contentField = new TextField("content",
							blog.getContent(), Store.NO);
					document.add(contentField);
					//System.out.println("indexing " + i + " blog");
					indexWriter.addDocument(document);
				}
			}
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
