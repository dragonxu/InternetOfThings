package com.tyloo.util;
//package com.renhenet.util;
//
//import java.io.StringReader;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.cjk.CJKAnalyzer;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.highlight.Highlighter;
//import org.apache.lucene.search.highlight.NullFragmenter;
//import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
//
//public class HighlighterUtil {
//	private static Analyzer analyzer = new CJKAnalyzer();
//
//	public static String hignlight(String value, String keyWords) {
//		String str = null;
//
//		if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(keyWords)) {
//			try {
//				QueryParser queryParse = new QueryParser("content", analyzer);
//				queryParse.setDefaultOperator(QueryParser.AND_OPERATOR);
//
//				Query query = queryParse.parse(keyWords);
//				// ��Ҫ������ʾ���ֶθ�ʽ��������ֻ�ǼӺ�ɫ��ʾ�ͼӴ�
//				SimpleHTMLFormatter sHtmlF = new SimpleHTMLFormatter(
//						"<font class=saffron>", "</font>");
//
//				Highlighter highlighter = new Highlighter(sHtmlF,
//						new QueryScorer(query));
//				highlighter.setTextFragmenter(new NullFragmenter());
//
//				if (value != null) {
//					TokenStream tokenStream = analyzer.tokenStream("content",
//							new StringReader(value));
//
//					str = highlighter.getBestFragment(tokenStream, value);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		if (StringUtils.isEmpty(str)) {
//			str = value;
//		}
//		return str;
//	}
//
//	public static void main(String[] args) {
//		String text = "��Խ��3����ɷ�й������ѡ�  ���ν�վ��� ���ֱ��� ������Z�������������� �����ν� Ѹ�׿�Ƶ�������� ��Խ�������桷";
//
//		System.err.println(hignlight(text, "���� ���ν��"));
//
//	}
//}
