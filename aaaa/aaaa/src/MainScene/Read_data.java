package MainScene;

import java.io.IOException;
import java.io.InputStream;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import chuanKouTong.Basis;
import chuanKouTong.Ecute_data;
import chuanKouTong.InputMessage;
import Connect_Sql.CreateTable;
import Connect_Sql.TakeToSql;

public class Read_data implements Runnable, SerialPortEventListener { 
	CommPortIdentifier portId = null ;
	 SerialPort serialPort = null;
	InputStream inputStream; 
    Thread readThread; 
    boolean exit=true;
    static byte[] abc=new byte[50];
    static String str = "000000"; 
    Basis basis =new Basis();
    
    public Read_data() throws InterruptedException {  
       
    	try {  
    		portId =  CommPortIdentifier.getPortIdentifier("COM5"); 
    		serialPort = (SerialPort) portId.open("Reader", 2000); 
    		inputStream = serialPort.getInputStream(); 
    		//��COM5��ȡ����
    		serialPort.addEventListener(this); 
            //��Ӽ�����
    		serialPort.notifyOnDataAvailable(true); 
    		/* ����������������,���������¼�*/ 
    		serialPort.setSerialPortParams(9600,//������
                SerialPort.DATABITS_8,//����λ��
                SerialPort.STOPBITS_1,//ֹͣλ
                SerialPort.PARITY_NONE);//У��
    		readThread = new Thread(this); 
    		readThread.start(); 
        //�����߳�
    	}catch(Exception e){e.printStackTrace();
    	}
  }  
	public void display(){
    	serialPort.close();
    	exit=false;
    	try {
			readThread.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    	public  void run() { 
         while(exit){
    		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			
		}
         }
    	} 
    public void serialEvent(SerialPortEvent event) { 
        switch(event.getEventType()) { 
        case SerialPortEvent.BI: 
        case SerialPortEvent.OE: 
        case SerialPortEvent.FE: 
        case SerialPortEvent.PE: 
        case SerialPortEvent.CD: 
        case SerialPortEvent.CTS:  
        case SerialPortEvent.DSR: 
        case SerialPortEvent.RI:  
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:break;  
        case SerialPortEvent.DATA_AVAILABLE: 
            byte[] readBuffer = new byte[200];  
            try {  
                while (inputStream.available() > 0) {  
                    int numBytes = inputStream.read(readBuffer); 
                    System.out.println("numBytes"+numBytes);
                    for (int i = 0; i < numBytes; i++) { 
                   	 abc[i]=readBuffer[i];
                        String hex = Integer.toHexString(readBuffer[i] & 0xFF); 
                        if (hex.length() == 1) { 
                          hex = '0' + hex; 
                        } 
                        hex=hex+' ';
                        System.out.print(hex.toUpperCase() ); 
                      } 
                    System.out.println();
                }  
                str = new String(readBuffer).trim();  
//                System.out.println(readBuffer); 
                //���������ַ�
            } catch (IOException e) {} 
            Ecute_data e_d =new Ecute_data();
            basis=e_d.get_Data(abc);
            new InputMessage().show(basis);
            new CreateTable();
            new TakeToSql().TakeData(basis);
            break; 
        } 
    } 
}  
