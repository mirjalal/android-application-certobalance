/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.util.Log;

public class SerialApplication{// extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = null;
	private SerialPort mSerialPort = null;
	
	public SerialApplication() {
	}

	public SerialPort getSerialPort(String path, int baudrate, int databits, int stopbits,int parity, int flowcontrol ) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
  
			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				Log.e("SerialApplication", "invalid path: " +  path);
				Log.e("SerialApplication", "invalid baudrate: " +  baudrate);
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0,databits,stopbits,parity,flowcontrol);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
