/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android_serialport_api;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class SerialService  {

	public final Object mLock = new Object();//Object des Threads fuer die syncronized-Bloecke
	
    //private Handler handler = new Handler();
    private ReadSerialThread readSerialThread = null;
    private List<MessageReceivedListener> messageReceivedListeners = new ArrayList<MessageReceivedListener>();
    
	protected SerialApplication mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private Boolean isDirectForwarding = false;
	
	private String stringTermination = "\n";

	private String mDeviceName;

	private int mBaudrate = 9600;
	private int mDatabits;
	private int mStopbits;
	private int mParity;
	private int mFlowControl;


	public int getBaudrate() {return mBaudrate;}
	public void setBaudrate(int mBaudrate) {this.mBaudrate = mBaudrate;}


	public int getmDatabits() {return mDatabits;}
	public void setmDatabits(int mDatabits) {this.mDatabits = mDatabits;}



	public int getmStopbits() {return mStopbits;}
	public void setmStopbits(int mStopbits) {this.mStopbits = mStopbits;}



	public int getmParity() {return mParity;}
	public void setmParity(int mParity) {this.mParity = mParity;}



	public int getmFlowControl() {return mFlowControl;}
	public void setmFlowControl(int mFlowControl) {this.mFlowControl = mFlowControl;}



    public Boolean getIsDirectForwarding() {
		return isDirectForwarding;
	}

    //if set to true, then the received message will be forwareded directly, whithout detection of terminator string
	public void setIsDirectForwarding(Boolean isDirectForwarding) {
		this.isDirectForwarding = isDirectForwarding;
	}

	public void setOnMessageReceivedListener(MessageReceivedListener listener){
    	messageReceivedListeners.add(listener);
    }
    
    public void removeOnMessageReceivedListener(MessageReceivedListener listener){
    	messageReceivedListeners.remove(listener);
    }
    
    
    

	public SerialService(String deviceName, int baudrate,int databits,int stopbits,int parity, int flowcontrol) {
		mApplication = new SerialApplication();
		try {

			this.mDeviceName = deviceName;
			this.mBaudrate = baudrate;
			this.mDatabits=databits;
			this.mParity=parity;
			this.mStopbits=stopbits;
			this.mFlowControl=flowcontrol;


			this.mSerialPort = mApplication.getSerialPort(deviceName,baudrate,databits,stopbits,parity,flowcontrol);
			this.mOutputStream = mSerialPort.getOutputStream();
			this.mInputStream = mSerialPort.getInputStream();

		} catch (Exception e){
			Log.e("SerialService", e.toString());
		}
	}


    
   public void startReadSerialThread(){
    	readSerialThread = new ReadSerialThread();
    	readSerialThread.start();
    }
   
   public void sendMessage(String message){
	   try {
		if(mOutputStream != null){
			mOutputStream.write(message.getBytes());
			Log.e("SerialService", "sent to autoclave lafomed as string: " + message);
		}else{
			resetConnection();
			Log.e("SerialService", "OUTputsteram == 0!");
			
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
  
   public void resetConnection() {
	   try {

	   
		   if(mSerialPort != null){
			   mSerialPort.close();
		   }
		   if(mOutputStream != null){
				mOutputStream.close();
			}
		   if(mInputStream != null){
			   mInputStream.close();
		   }
		   if(mApplication != null){
			   mApplication.closeSerialPort();
		   }
			this.mSerialPort = mApplication.getSerialPort(mDeviceName,mBaudrate,mDatabits,mStopbits,mParity,mFlowControl);
			this.mOutputStream = mSerialPort.getOutputStream();
			this.mInputStream = mSerialPort.getInputStream();
	} catch (Exception e) {
		Log.e("SerialService", e.toString());
	}

		
	
}


   public void write(byte oneByte ){
	   try {
		if(mOutputStream != null){
			mOutputStream.write(oneByte);
			Log.e("SerialService", "sent byte: " + oneByte);
		}else{
			Log.e("SerialService", "OUTputsteram == 0!");
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   

public void sendBytes(byte[] bytes ){
	   try {
		if(mOutputStream != null){
			mOutputStream.write(bytes);
			Log.e("SerialService", "sent to autoclave lafomed: " + new String(bytes));
		}else{
			Log.e("SerialService", "OUTputsteram == 0!");
			
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
        
 
   
 	/**
	 * Receive data from the Serial Interface
	 * 
	 * @param buffer
	 *            Buffer to write to (the entire array will be filled)
	 * @return Received packet
	 */
	public int read(byte[] buffer) {
		return this.read(buffer, 0, buffer.length);
	}

	/**
	 * Receive data from the Serial Interface
	 * 
	 * @param buffer
	 *            Buffer to write to
	 * @param offset
	 *            offset to start reading
	 * @param length
	 *            Length to read
	 * @return Received packet
	 */
	public int read(byte[] buffer, int offset, int length) {
		

    if (mInputStream != null){
    	synchronized (this.mInputStream) {	
			try {
					int counter =0;
					
					//wait a view milliseconds if there is a byte to read. If not then return error flag
					while (true)
				    {
				        int available = mInputStream.available();
				        if (available > 0) { break; }
				        Thread.sleep(10);
				        counter++;
				        if(counter>100){
				        	return -1;
				        }
				    }
					
					
					return mInputStream.read(buffer, 0, 1);
					
				
			} catch (Exception e) {
				Log.e("exception during read: !! ", e.toString() + e.getMessage());
				return -1;
			}
    	}
    }
			return -1;
	
		

	}
	
   
  public void clearRxBuffer(){
	  try {
	
	  if (mInputStream != null){
		  int availableBytes;
	
		availableBytes = mInputStream.available();

		if(availableBytes>0){
			mInputStream.skip(availableBytes);
	  	}
	  }
	  } catch (Exception e) {
	  }	  
	  
  }

	public String getStringTerminatin() {
	return stringTermination;
}



public void setStringTerminatin(String stringTerminatin) {
	this.stringTermination = stringTerminatin;
}

	private class ReadSerialThread extends Thread {

		private boolean isRunning = true;
		private String message = "";
		


		@Override
		public void run() {
			super.run();
			while(isRunning) {
				int size;
				try {
					byte[] buffer = new byte[1];
					if (mInputStream != null){
						size = mInputStream.read(buffer);
						if (size > 0) {
							message = message + new String(buffer);
							//Log.e("SerialService", "received: " + new String(buffer));
							if(isDirectForwarding){
								for(MessageReceivedListener listener : messageReceivedListeners){
									listener.onMessageReceived(message);
									message = "";
								}
							}else{
								if(message.endsWith(stringTermination)){
									for(MessageReceivedListener listener : messageReceivedListeners){
										listener.onMessageReceived(message);
										message = "";
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
			

     	
	      


    }
