package MainScene;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
	public  String getTime() { 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String d=df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
//		Date d =new Date();
		return d;
	}
}